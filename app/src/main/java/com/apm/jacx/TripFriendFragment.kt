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
import com.apm.jacx.client.ApiClient
import com.apm.jacx.internalStorage.AppPreferences
import com.apm.jacx.trip.DataSourceTrip
import com.apm.jacx.trip.ItemFriendAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

class TripFriendFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_friend, container, false)
    }

    // Recordar utilizar esto no ciclo de vida ;)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFriendFragmentData(view)
        createListenerFriendButton(view)
    }


    // Tengo que hacer algo similar al loginactivity para poder coger los datos
    // Sip
    private fun loadFriendFragmentData(viewFragment: View) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val myDataset = DataSourceTrip().getFriends()
                val recyclerView = viewFragment.findViewById<RecyclerView>(R.id.list_friends)
                recyclerView?.adapter = context?.let { ItemFriendAdapter(it, myDataset) }

                Toast.makeText(context, "Datos de amigos cargados", Toast.LENGTH_SHORT).show();
            } catch (e: IOException) {
                // Manejar errores de red aquí
                Log.d("Error de red", e.toString())
            } catch (e: Exception) {
                // Manejar otros errores aquí
                Log.d("Error en la peticion", e.toString())
            }
        }

    }

    private fun createListenerFriendButton(viewFragment: View) {
        val button : FloatingActionButton = viewFragment.findViewById(R.id.btn_add_new_friend_trip_album)
        button.setOnClickListener {

            // Creamos unha instacia do fragmento
            val fragmentToLoad = NewFriendFragment()
            val activity = context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction()
                // TODO ÑAPA para que corra sen que casque e programar máis rapido
                // .replace(R.id.main_view_container_trip, fragmentToLoad)
                .replace(R.id.main_view_container, fragmentToLoad)
                .addToBackStack(null)
                .commit()
            Toast.makeText(context, "Añadir amigos", Toast.LENGTH_SHORT).show();
        }
    }

}