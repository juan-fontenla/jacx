package com.apm.jacx

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.trip.DataSourceTrip
import com.apm.jacx.trip.ItemAlbumTripAdapter
import com.apm.jacx.trip.ItemFriendAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TripAlbumFragment : Fragment() {
    // TODO: Rename and change types of parameters
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
        val viewFragment = inflater.inflate(R.layout.fragment_trip_album, container, false);
        loadAlbumTripFragmentData(viewFragment)
        createListenerAlbumTripButton(viewFragment)
        createListenerMenuButton(viewFragment)

        return viewFragment
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TripAlbumFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun loadAlbumTripFragmentData(viewFragment: View) {
        // Initialize data.
        val myDataset = DataSourceTrip().loadAlbumTrip()

        Log.d("Album trip dataset loaded", myDataset.toString())

        val recyclerView = viewFragment.findViewById<RecyclerView>(R.id.list_album_trip)
        recyclerView?.adapter = context?.let { ItemAlbumTripAdapter(it, myDataset) }

        Toast.makeText(context, "Datos de album del viaje cargados", Toast.LENGTH_SHORT).show();

    }

    private fun createListenerAlbumTripButton(viewFragment: View) {
        val button : FloatingActionButton = viewFragment.findViewById(R.id.btn_add_new_post_detail_trip_album)
        button.setOnClickListener {
            Toast.makeText(context, "Añadir un nuevo post", Toast.LENGTH_SHORT).show();
        }
    }

    private fun createListenerMenuButton(viewFragment: View) {
        val btnMenu : ImageButton = viewFragment.findViewById(R.id.iB_menu_burger_trip_album)
        btnMenu.setOnClickListener {
            Toast.makeText(context, "Menu", Toast.LENGTH_SHORT).show();
        }
    }
}