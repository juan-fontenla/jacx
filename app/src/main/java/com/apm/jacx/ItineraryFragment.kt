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
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ItineraryFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

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

        createNewRoute(viewFragment)
        deleteStopRoute(viewFragment)
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

    private fun createNewRoute(viewFragment: View) {

        val btnStartRoute : Button = viewFragment.findViewById(R.id.btn_start_route_trip_friend)
        btnStartRoute.setOnClickListener {
            Toast.makeText(activity, "Iniciar ruta", Toast.LENGTH_SHORT).show();
        }

        val btnFinishRoute : Button = viewFragment.findViewById(R.id.btn_finish_route_trip_friend2)
        btnFinishRoute.setOnClickListener {
            Toast.makeText(activity, "Finalizar ruta", Toast.LENGTH_SHORT).show();
        }

    }

    private fun deleteStopRoute(viewFragment: View) {

        val btnDeleteStop : ImageButton = viewFragment.findViewById(R.id.iB_delete_stop_present)
        btnDeleteStop.setOnClickListener {
            Toast.makeText(activity, "Eliminar parada", Toast.LENGTH_SHORT).show();
        }

    }

    private fun addWaypoint(viewFragment: View) {

        val btnAddRoute : FloatingActionButton = viewFragment.findViewById(R.id.btn_add_route_trip_friend)
        btnAddRoute.setOnClickListener {
            val fields = listOf(Place.Field.LAT_LNG, Place.Field.NAME)
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
                    sendWaypoint(place.latLng)
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i("ItineraryFragment", "User canceled autocomplete")
            }
        }

    private fun sendWaypoint(latLng: LatLng) = runBlocking {
        Log.d("ItineraryFragment", "SEND WAYPOINT!")
        val pointString = "${latLng.latitude},${latLng.longitude}"
        // TODO send point to server (maybe include name)

        // TODO add to list if successful
        activity?.runOnUiThread {
            Toast.makeText(activity, "Send $pointString waypoint to server", Toast.LENGTH_LONG).show()
        }
    }


    private fun saveRoute(viewFragment: View) {

        val btnSaveRoute : FloatingActionButton = viewFragment.findViewById(R.id.btn_save_route_trip_friend)
        btnSaveRoute.setOnClickListener {
            Toast.makeText(activity, "Guardar ruta", Toast.LENGTH_SHORT).show();
        }

    }







}