package com.apm.jacx

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.apm.jacx.model.Trip
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.time.Duration
import java.time.LocalTime


class DetailsTripsFragment : Fragment(), DetailRouteFragment, OnMapReadyCallback {

    private val COUNTDOWN_TIME = 5000L
    private lateinit var navigationButton: FloatingActionButton
    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var origin: String
    private lateinit var waypoints: String
    private lateinit var destination: String
    private lateinit var route: PolylineOptions
    private lateinit var printedRoute: Polyline
    private lateinit var userPosition: LatLng
    private var isTimerRunning = false
    private var lastTimeOutOfRoute : LocalTime? = null
    private var navigation = false
    private var userMarker: Marker? = null
    private val client = OkHttpClient()

    override fun setTripData(trip: Trip) {
        // Send query to Google Maps API to get route polyline to print in map
        initialDataFetch(trip)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setHasOptionsMenu(true);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_trips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationButton = requireView().findViewById<FloatingActionButton>(R.id.navigation)
        navigationButton!!.setOnClickListener{ onNavigationButtonClick() }

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    // Acceso a crear una nueva ruta del viaje
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_ubicacion -> {
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_view_container_trip, ItineraryFragment())?.commit()
                true
            }
            else -> super.onOptionsItemSelected(item);
        }

    }

/*    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_selected_route, menu);
        return super.onCreateOptionsMenu(menu, inflater)
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        turnOffNavigation()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isRotateGesturesEnabled = true
        printRoute() // Print route if already exists
    }

    @SuppressLint("MissingPermission")
    fun updateLocation(location: Location) {
        userPosition = LatLng(location.latitude, location.longitude)
        if(::mMap.isInitialized) {
            mMap.isMyLocationEnabled = true
            // TODO move camera if position is centered and navigation enabled
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPosition, 15f))
        }
        // Check user follows route
        if (PolyUtil.isLocationOnPath(userPosition, route.points, true, 10.0)) {
            lastTimeOutOfRoute = null
            isTimerRunning = false
        } else {
            if (!isTimerRunning) {
                lastTimeOutOfRoute = LocalTime.now()
                isTimerRunning = true
            } else {
                if (Duration.between(lastTimeOutOfRoute, LocalTime.now()).toMillis() > COUNTDOWN_TIME) {
                    origin = "${userPosition.latitude},${userPosition.longitude}"
                    isTimerRunning = false
                    lastTimeOutOfRoute = null
                    requestRoute()
                }
            }
        }
    }

    private fun onNavigationButtonClick() {
        if (navigation) {
            turnOffNavigation()
        } else {
            (activity as DetailRouteActivity).enableNavigation()
            Log.d("DetailsTripsFragment", "Navigation enabled")
            navigation = true
            navigationButton.setImageResource(R.drawable.baseline_pause_24)
        }
    }

    private fun turnOffNavigation() {
        (activity as DetailRouteActivity).disableNavigation()
        navigation = false
        navigationButton.setImageResource(R.drawable.baseline_play_arrow_24)
        lastTimeOutOfRoute = null
        isTimerRunning = false
    }

    private fun initialDataFetch(trip: Trip) = runBlocking {
        // Get key points and send query to Google Maps API
        getKeyPoints(trip)
        requestRoute()
    }

    private fun getKeyPoints(trip: Trip) {
        origin = trip.begin.point!!
        val waypointArray = arrayListOf<String>()
        trip.waypoints.forEach { waypoint ->
            waypoint.point?.let { waypointArray.add(it) }
        }
        waypoints = waypointArray.joinToString("|")
        destination = trip.finish.point!!
    }

    private fun requestRoute() = runBlocking {
        // Clear route if exists
        if (::printedRoute.isInitialized) {
            printedRoute.remove()
        }
        // Build url
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("maps.googleapis.com")
            .addPathSegment("maps")
            .addPathSegment("api")
            .addPathSegment("directions")
            .addPathSegment("json")
            .addQueryParameter("origin", origin)
            .addQueryParameter("waypoints", waypoints)
            .addQueryParameter("destination", destination)
            .addQueryParameter("key", getString(R.string.key))
            .build()

        // Perform request
        val request = Request.Builder().url(url).build()
        withContext(Dispatchers.IO) {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Toast.makeText(activity, "Error on route calculation", Toast.LENGTH_LONG)
                }

                override fun onResponse(call: Call, response: Response) {
                    // Get JSON response
                    val json = response.body!!.string()
                    val jsonObject = JSONObject(json)
                    // Check status code
                    if (jsonObject.getString("status") != "OK") {
                        Log.e("ROUTE", "Error on response: " + jsonObject.getString("status"))
                        return
                    }
                    // Get and print polyline calculated
                    val jsonRoute = jsonObject.getJSONArray("routes").getJSONObject(0)
                    val jsonLegs = jsonRoute.getJSONArray("legs")
                    val points = mutableListOf<LatLng>()
                    for (i in 0 until jsonLegs.length()) {
                        val leg = jsonLegs.getJSONObject(i)
                        val steps = leg.getJSONArray("steps")
                        for (i in 0 until steps.length()) {
                            val step = steps.getJSONObject(i)
                            val polyline = step.getJSONObject("polyline").getString("points")
                            points.addAll(PolyUtil.decode(polyline))
                        }
                    }
                    route = PolylineOptions().addAll(points).width(8f).color(Color.BLUE).geodesic(true)
                    printRoute()
                }
            })
        }
    }

    private fun printRoute() {
        if (this::mMap.isInitialized && this::route.isInitialized) {
            activity?.runOnUiThread {
                printedRoute = mMap.addPolyline(route)
                // Update camera to match route
                val builder = LatLngBounds.builder()
                for (point in route.points) {
                    builder.include(point)
                }
                val bounds = builder.build()
                val padding = 100
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                mMap.moveCamera(cameraUpdate)
            }
        } else {
            Log.w("DetailsTripFragment", "Map or route not initialized!")
        }
    }


}