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
import com.apm.jacx.trip.ItemFriendAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TripFriendFragment : Fragment() {

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
        val viewFragment = inflater.inflate(R.layout.fragment_trip_friend, container, false)
        loadFriendFragmentData(viewFragment)
        createListenerFriendButton(viewFragment)

        return viewFragment
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TripFriendFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    private fun loadFriendFragmentData(viewFragment: View) {
        // Initialize data.
        val myDataset = DataSourceTrip().loadFriendsTrip()

        Log.d("Friends dataset loaded", myDataset.toString())

        val recyclerView = viewFragment.findViewById<RecyclerView>(R.id.list_friends)
        recyclerView?.adapter = context?.let { ItemFriendAdapter(it, myDataset) }

        Toast.makeText(context, "Datos de amigos cargados", Toast.LENGTH_SHORT).show();

    }

    private fun createListenerFriendButton(viewFragment: View) {
        val button : FloatingActionButton = viewFragment.findViewById(R.id.btn_add_new_friend_trip_album)
        button.setOnClickListener {
            Toast.makeText(context, "Añadir amigos", Toast.LENGTH_SHORT).show();
        }
    }

}