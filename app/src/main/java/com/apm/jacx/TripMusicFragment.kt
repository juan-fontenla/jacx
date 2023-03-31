package com.apm.jacx

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.trip.DataSourceTrip
import com.apm.jacx.trip.ItemMusicTripAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TripMusicFragment : Fragment() {

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

        val viewFragment = inflater.inflate(R.layout.fragment_trip_music, container, false)

        loadMusicTripFragmentData(viewFragment)
        createListenerMusicButton(viewFragment)

        return viewFragment;
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TripMusicFragment()
                .apply {

                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun loadMusicTripFragmentData(viewFragment: View) {
        // Initialize data.
        val myDataset = DataSourceTrip().loadMusicTrip()

        Log.d("Music of trip dataset loaded", myDataset.toString())

        val recyclerView = viewFragment.findViewById<RecyclerView>(R.id.list_music_trip)
        recyclerView?.adapter = context?.let { ItemMusicTripAdapter(it, myDataset) }

        Toast.makeText(context, "Datos de musica cargados", Toast.LENGTH_SHORT).show();

    }

    private fun createListenerMusicButton(viewFragment: View) {
        val button : FloatingActionButton = viewFragment.findViewById(R.id.btn_add_new_post_detail_trip_album)
        button.setOnClickListener {
            Toast.makeText(context, "Añadir nueva canción", Toast.LENGTH_SHORT).show();
        }
    }


}