package com.apm.jacx

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.internalStorage.AppPreferences
import com.apm.jacx.trip.DataSourceTrip
import com.apm.jacx.trip.ItemFriendAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TripFriendFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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


    // Tengo que hacer algo similar al loginactivity para poder coger los datos
    private fun loadFriendFragmentData(viewFragment: View) {
        // Initialize data.
        // TODO: Aqui se le tiene que pasar un atributo
        val myDataset = DataSourceTrip().loadFriendsTrip()
        println(myDataset.size)

        Log.d("Friends dataset loaded", myDataset.toString())

        val recyclerView = viewFragment.findViewById<RecyclerView>(R.id.list_friends)
        recyclerView?.adapter = context?.let { ItemFriendAdapter(it, myDataset) }

        Toast.makeText(context, "Datos de amigos cargados", Toast.LENGTH_SHORT).show();

    }

    private fun createListenerFriendButton(viewFragment: View) {
        val button : FloatingActionButton = viewFragment.findViewById(R.id.btn_add_new_friend_trip_album)
        button.setOnClickListener {

            // Creamos unha instacia do fragmento
            val framentToLoad = NewFriendFragment()
            val activity = context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.main_view_container_trip, framentToLoad)
                .addToBackStack(null)
                .commit()
            Toast.makeText(context, "Añadir amigos", Toast.LENGTH_SHORT).show();
        }
    }

}