package com.apm.jacx

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class DetailsTripsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var navigationButton: FloatingActionButton
    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var origin: String
    private lateinit var waypoints: String
    private lateinit var destination: String
    private lateinit var route: PolylineOptions
    private var navigation = false
    private var userMarker: Marker? = null
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
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

        // Send query to Google Maps API to get route polyline to print in map
        initialDataFetch()

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_selected_route, menu);
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isRotateGesturesEnabled = true
        printRoute() // Print route if already exists
    }

    @SuppressLint("MissingPermission")
    fun updateLocation(location: Location) {
        val position = LatLng(location.latitude, location.longitude)
        if(::mMap.isInitialized) {
            mMap.isMyLocationEnabled = true
            // TODO move camera if position is centered and navigation enabled
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
        }
    }

    private fun onNavigationButtonClick() {
        if (navigation) {
            (activity as DetailRouteActivity).disableNavigation()
            navigation = false
            navigationButton.setImageResource(R.drawable.baseline_play_arrow_24)
        } else {
            (activity as DetailRouteActivity).enableNavigation()
            navigation = true
            navigationButton.setImageResource(R.drawable.baseline_pause_24)
        }
    }

    private fun initialDataFetch() = runBlocking {
        // Get key points and send query to Google Maps API
        withContext(Dispatchers.IO) {
            getKeyPoints()
        }
        requestRoute()
    }

    private fun getKeyPoints() {
        // TODO get points from server
        origin = "43.27,-8.54"
        waypoints = "42.03,-8.7|41.53,-8.65"
        destination = "41.16,-8.7"
    }

    private fun requestRoute() = runBlocking {
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
                    val polyline = jsonRoute.getJSONObject("overview_polyline").getString("points")
                    val points = PolyUtil.decode(polyline)
                    route = PolylineOptions().addAll(points).width(8f).color(Color.BLUE).geodesic(true)
                    printRoute()
                }
            })
        }
    }

    private fun printRoute() {
        if (this::mMap.isInitialized && this::route.isInitialized) {
            activity?.runOnUiThread {
                mMap.addPolyline(route)
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