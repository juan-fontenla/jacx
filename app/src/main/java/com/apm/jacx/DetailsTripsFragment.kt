package com.apm.jacx

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction


class DetailsTripsFragment : Fragment() {

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
        val mapFragment : Fragment = MapFragment()
        val transaction : FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.include, mapFragment).commit()
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
}