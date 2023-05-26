package com.apm.jacx

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.adapter.ItemTouchHelperCallback
import com.apm.jacx.adapter.ItemTripAdapter
import com.apm.jacx.adapter.ItemWaypointAdapter
import com.apm.jacx.client.ApiClient
import com.apm.jacx.model.Waypoint
import com.apm.jacx.util.Util
import com.apm.jacx.view_model.ActivityViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ItineraryFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var myViewModel: ActivityViewModel
    private lateinit var waypoints: MutableList<Waypoint>
    private lateinit var adapter: ItemWaypointAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val viewFragment = inflater.inflate(R.layout.fragment_itinerary, container, false)

        // Init Places SDK
        Places.initialize(requireContext(), getString(R.string.key))

        myViewModel = ViewModelProvider(requireActivity())[ActivityViewModel::class.java]
        waypoints = myViewModel.apiResult.value?.waypoints!!
        waypoints.sortBy { it.orderPosition }
        adapter = ItemWaypointAdapter(requireContext(), waypoints)
        recyclerView = viewFragment.findViewById<RecyclerView>(R.id.list_trip_waypoints)
        recyclerView!!.adapter = adapter
        val itemTouchHelperCallback = ItemTouchHelperCallback(adapter)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)


        addWaypoint(viewFragment)
        saveRoute(viewFragment)

        return viewFragment
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ItineraryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun addWaypoint(viewFragment: View) {

        val btnAddRoute : FloatingActionButton = viewFragment.findViewById(R.id.btn_add_route_trip_friend)
        btnAddRoute.setOnClickListener {
            val fields = listOf(Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ICON_BACKGROUND_COLOR, Place.Field.PHOTO_METADATAS)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(activity)
            startAutocomplete.launch(intent)

        }

    }

    private val startAutocomplete =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Process selected waypoint
                val intent = result.data
                if (intent != null) {
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    sendWaypoint(place)
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i("ItineraryFragment", "User canceled autocomplete")
            }
        }

    private fun sendWaypoint(place: Place) = runBlocking {
        // Create way point
        var waypoint = Util.placeToWaypoint(place, requireContext())
        waypoint.orderPosition = waypoints.size + 1
        var trip = myViewModel.apiResult.value
        val json = JSONObject(ApiClient.post("/waypoint", waypoint.toJSONObject().toString()))
        waypoint = Waypoint(json)
        // Assign way point to trip
        val assignBody = JSONObject()
        assignBody.put("routeId", trip!!.id)
        assignBody.put("waypointId", waypoint.id)
        ApiClient.post("/route/waypoint", assignBody.toString())
        // Update view
        waypoints.add(waypoint)
        adapter.notifyDataSetChanged()
    }


    private fun saveRoute(viewFragment: View) {

        val btnSaveRoute : FloatingActionButton = viewFragment.findViewById(R.id.btn_save_route_trip_friend)
        btnSaveRoute.setOnClickListener {
            Toast.makeText(activity, "Guardar ruta", Toast.LENGTH_SHORT).show();
        }

    }







}