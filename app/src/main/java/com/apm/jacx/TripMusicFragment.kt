package com.apm.jacx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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

        val rootView = inflater.inflate(R.layout.fragment_trip_music, container, false)

        val btnAddMusic: FloatingActionButton = rootView.findViewById(R.id.btn_add_new_post_detail_trip_album)
        btnAddMusic.setOnClickListener {
            Toast.makeText(activity, "Añadir canción", Toast.LENGTH_SHORT).show()
        }

        // Llamamos a la activity DetailTripMusic del ListView desde el fragment
        // TODO: Descomentar para poder ver la Activity con el Adapter de la ListView
        // val intent: Intent = Intent(activity, DetailTripMusic::class.java)
        // startActivity(intent)

        // Inflate the layout for this fragment
        return rootView;
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

}