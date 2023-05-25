package com.apm.jacx

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.client.ApiClient
import com.apm.jacx.internalStorage.AppPreferences
import com.apm.jacx.trip.DataSourceTrip
import com.apm.jacx.trip.Friend
import com.apm.jacx.trip.ItemFriendAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.spotify.sdk.android.auth.AuthorizationClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

/* Permite asociar un amigo al viaje */
class NewFriendFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_friend, container, false)
    }

    // Modificase a vista aquí:
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usernameFriend = getView()?.findViewById<TextView>(R.id.username_friend)

        /* Asociar por userName a un nuevo amigo en el viaje */
        val buttonNewFriend = getView()?.findViewById<Button>(R.id.button_new_friend)
        buttonNewFriend!!.setOnClickListener {
            createNewFriend(usernameFriend!!.text?.trim())
            backToFragment()
//            Toast.makeText(context, "Nuevo amigo", Toast.LENGTH_SHORT).show();
        }

        /* Volver a la pantalla anterior */
        val buttonBack = getView()?.findViewById<Button>(R.id.button_back)
        buttonBack!!.setOnClickListener {
           backToFragment()
        }

    }

    /* Volvemos al fragment para mostrar la lista de amigos */
    private fun backToFragment() {
        val fragmentToLoad = TripFriendFragment()
        val activity = context as AppCompatActivity
        activity.supportFragmentManager.beginTransaction()
            .replace(R.id.main_view_container, fragmentToLoad)
            .addToBackStack(null)
            .commit()

        /* Actualizamos la lista con el valor añadido */
        /* TODO: No recarga la lista de amigos */
//        fragmentToLoad.loadFriendFragmentData(viewFragment)
    }

    /* Asociamos un nuevo amigo al viaje */
    private fun createNewFriend(username: CharSequence?) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val jsonBody = JSONObject().apply {
                    put("username", username)
                }.toString()

                val responsePost = ApiClient.post("/friend", jsonBody)
                Log.d("POST FRIENDS", responsePost)

            } catch (e: IOException) {
                // Manejar errores de red aquí
                Log.d("Error de red", e.toString())
            } catch (e: Exception) {
                // Manejar otros errores aquí
                Log.d("Error en la peticion", e.toString())
            }
        }
    }
}