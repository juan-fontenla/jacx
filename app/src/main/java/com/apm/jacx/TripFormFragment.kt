package com.apm.jacx

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.apm.jacx.client.ApiClient
import com.apm.jacx.model.Trip
import com.apm.jacx.model.Waypoint
import com.apm.jacx.util.Util
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import java.time.LocalDate

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TripFormFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TripFormFragment : Fragment() {

    private lateinit var origin : Waypoint
    private lateinit var destination : Waypoint

    private lateinit var tappedInput : EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mainActivity : MainActivity = activity as MainActivity
        mainActivity.showUpButton()
        // Init Places SDK
        Places.initialize(requireContext(), getString(R.string.key))
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createButton = getView()?.findViewById<MaterialButton>(R.id.createButton)
        createButton!!.setOnClickListener{ onCreateButtonClick() }
        // Autocomplete fields
        val fields = listOf(Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ICON_BACKGROUND_COLOR, Place.Field.PHOTO_METADATAS)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(activity)
        val setOriginInput = getView()?.findViewById<EditText>(R.id.startInput)
        setOriginInput!!.setOnClickListener {
            tappedInput = setOriginInput
            startAutocomplete.launch(intent)
        }
        val setDestinationInput = getView()?.findViewById<EditText>(R.id.endInput)
        setDestinationInput!!.setOnClickListener {
            tappedInput = setDestinationInput
            endAutocomplete.launch(intent)
        }
        val startDateInput = view.findViewById<EditText>(R.id.startDateInput)
        startDateInput!!.setOnClickListener {
            showDatePickerDialog(startDateInput, LocalDate.now())
        }
        val endDateInput = view.findViewById<EditText>(R.id.endDateInput)
        endDateInput!!.setOnClickListener {
            showDatePickerDialog(endDateInput, LocalDate.now().plusDays(1))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val mainActivity : MainActivity = activity as MainActivity
        mainActivity.hideUpButton()
    }

    private fun onCreateButtonClick() = runBlocking {

        val name = view?.findViewById<EditText>(R.id.tripNameInput)!!.text
        val startDate = view?.findViewById<EditText>(R.id.startDateInput)!!.text
        val endDate = view?.findViewById<EditText>(R.id.endDateInput)!!.text

        if (name.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || !::origin.isInitialized || !::destination.isInitialized) {
            Toast.makeText(context, R.string.trip_form_errors, Toast.LENGTH_LONG).show()
            return@runBlocking
        }

        //Build object
        val startDateArray = startDate.split("/").map { it.toInt() }.toTypedArray()
        val endDateArray = endDate.split("/").map { it.toInt() }.toTypedArray()
        val newTrip = JSONObject()
        newTrip.put("name", name)
        newTrip.put("startDate", JSONArray(startDateArray))
        newTrip.put("endDate", JSONArray(endDateArray))
        newTrip.put("begin", origin.toJSONObject())
        newTrip.put("finish", destination.toJSONObject())

        // Send request
        try {
            val responseBody = JSONObject(ApiClient.post("/route", newTrip.toString()))
            val trip = Trip(responseBody)
            // Start new activity
            val intent = Intent(activity, DetailRouteActivity::class.java)
            intent.putExtra("routeId", trip.id)
            startActivity(intent)
        } catch (e: IOException) {
            Log.e("TripFormFragment", "Error: " + e.message)
            Toast.makeText(context, R.string.network_err, Toast.LENGTH_LONG).show()
        }
    }

    private fun setPlace(result: ActivityResult): Place? {
        if (result.resultCode == Activity.RESULT_OK) {
            // Process selected waypoint
            val intent = result.data
            if (intent != null) {
                val place = Autocomplete.getPlaceFromIntent(intent)
                tappedInput.setText(place.name)
                return place
            }
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            // The user canceled the operation.
            Log.i("TripFormFragment", "User canceled autocomplete")
        }
        return null
    }

    private val startAutocomplete =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val place = setPlace(result)
            if (place != null) {
                origin = Util.placeToWaypoint(place, requireContext())
            }
        }

    private val endAutocomplete =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val place = setPlace(result)
            if (place != null) {
                destination = Util.placeToWaypoint(place, requireContext())
            }
        }

    private fun showDatePickerDialog(editTextDate: EditText, initDate: LocalDate) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                // Se selecciona una fecha, puedes realizar acciones aquí
                val selectedDate = "$year/${month + 1}/$dayOfMonth"
                editTextDate.setText(selectedDate)
            },
            // Valores iniciales de la fecha mostrada en el diálogo
            initDate.year,
            initDate.monthValue - 1,
            initDate.dayOfMonth
        )
        datePickerDialog.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TripFormFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TripFormFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}