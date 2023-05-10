package com.apm.jacx

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

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

    private lateinit var origin : String
    private lateinit var destination : String

    private lateinit var tappedInput : EditText
    private var selectedPoint : LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        val fields = listOf(Place.Field.LAT_LNG, Place.Field.NAME)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(activity)
        val setOriginInput = getView()?.findViewById<EditText>(R.id.startInput)
        setOriginInput!!.setOnClickListener {
            tappedInput = setOriginInput
            startAutocomplete.launch(intent)
            if (selectedPoint != null) {
                origin = "${selectedPoint!!.latitude},${selectedPoint!!.longitude}"
            }
            selectedPoint = null
        }
        val setDestinationInput = getView()?.findViewById<EditText>(R.id.endInput)
        setDestinationInput!!.setOnClickListener {
            tappedInput = setDestinationInput
            startAutocomplete.launch(intent)
            if (selectedPoint != null) {
                destination = "${selectedPoint!!.latitude},${selectedPoint!!.longitude}"
            }
            selectedPoint = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val mainActivity : MainActivity = activity as MainActivity
        mainActivity.hideUpButton()
    }

    private fun onCreateButtonClick() {
        val intent = Intent(activity, DetailRouteActivity::class.java)
        startActivity(intent)
    }

    private fun onInputClick(editText: EditText) {

    }

    private val startAutocomplete =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Process selected waypoint
                val intent = result.data
                if (intent != null) {
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    selectedPoint = place.latLng
                    tappedInput.setText(place.name)
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i("TripFormFragment", "User canceled autocomplete")
            }
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