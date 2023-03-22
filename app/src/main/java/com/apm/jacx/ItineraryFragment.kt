package com.apm.jacx

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
        createNewRoute(viewFragment)
        deleteStopRoute(viewFragment)
        addNewRoute(viewFragment)
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

    private fun addNewRoute(viewFragment: View) {

        val btnAddRoute : FloatingActionButton = viewFragment.findViewById(R.id.btn_add_route_trip_friend)
        btnAddRoute.setOnClickListener {
            Toast.makeText(activity, "Añadir ruta", Toast.LENGTH_SHORT).show();
        }

    }

    private fun saveRoute(viewFragment: View) {

        val btnSaveRoute : FloatingActionButton = viewFragment.findViewById(R.id.btn_save_route_trip_friend)
        btnSaveRoute.setOnClickListener {
            Toast.makeText(activity, "Guardar ruta", Toast.LENGTH_SHORT).show();
        }

    }







}