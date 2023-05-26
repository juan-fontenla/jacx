package com.apm.jacx

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.adapter.ItemPhotoAdapter
import com.apm.jacx.client.ApiClient
import com.apm.jacx.data.Datasource
import com.apm.jacx.internalStorage.AppPreferences
import com.apm.jacx.trip.DataSourceTrip
import com.apm.jacx.trip.Friend
import com.apm.jacx.trip.ItemFriendAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

private const val ARG_PARAM1 = "routeName"

class TripFriendFragment : Fragment() {

    private var routeName: String? = "Proba"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            routeName = it.getString(ARG_PARAM1)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_trip_friend, container, false)

        getFriendsFromRoute(routeName);

        val username = view.findViewById<TextInputEditText>(R.id.username_friend)
        val newUser = view.findViewById<Button>(R.id.button_new_friend)

        newUser.setOnClickListener {
            if (username != null){
                createNewFriend(username.text, routeName)
            }
            else {
                Toast.makeText(context, "Debe introducir el nombre de un amigo", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun loadFriendFragmentData(jsonArray: JsonArray) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.list_friends)

        val myDataset = DataSourceTrip().getFriends(jsonArray)

        recyclerView?.adapter = context?.let { ItemFriendAdapter(it, myDataset, routeName!!) }
        Toast.makeText(context, "Datos de amigos cargados", Toast.LENGTH_SHORT).show();
    }

    private fun createNewFriend(username: CharSequence?, rutaName: String?) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val spinner = requireView().findViewById<ProgressBar>(R.id.progressBar)
                spinner.visibility = View.VISIBLE

                val jsonBody = JSONObject().apply {
                    put("username", username)
                    put("routeName", rutaName)
                }.toString()

                ApiClient.post("/route/user", jsonBody)

                getFriendsFromRoute(rutaName)

                spinner.visibility = View.INVISIBLE
            } catch (e: IOException) {
                Toast.makeText(context, "There was a problem adding the friend", Toast.LENGTH_LONG)
                    .show()
                val spinner = requireView().findViewById<ProgressBar>(R.id.progressBar)
                spinner.visibility = View.INVISIBLE
            } catch (e: Exception) {
                Toast.makeText(context, "There was a problem adding the friend", Toast.LENGTH_LONG)
                    .show()
                val spinner = requireView().findViewById<ProgressBar>(R.id.progressBar)
                spinner.visibility = View.INVISIBLE
            }
        }
    }

    private fun getFriendsFromRoute(rutaName: String?){
        // Petición al backend.
        // Se debe utilizar las corrutinas de esta forma. No mediante GlobalScope.
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val spinner = requireView().findViewById<ProgressBar>(R.id.progressBar)
                spinner.visibility = View.VISIBLE
                val responseGet = ApiClient.get("/route/$rutaName")
                if (!responseGet.isNullOrBlank()){
                    val jsonObject: JsonObject = Gson().fromJson(responseGet, JsonObject::class.java)

                    val users = jsonObject.get("users").asJsonArray
                    if (users.isEmpty) {
                        Toast.makeText(context, "The are not users in the route yet", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        loadFriendFragmentData(users)
                    }
                } else {
                    Toast.makeText(context, "The route does not exist", Toast.LENGTH_SHORT).show()
                }

                spinner.visibility = View.INVISIBLE

            } catch (e: IOException) {
                // Manejar errores de red aquí
                Toast.makeText(context, "There was a problem loading the friend", Toast.LENGTH_LONG)
                    .show()
                val spinner = requireView().findViewById<ProgressBar>(R.id.progressBar)
                spinner.visibility = View.INVISIBLE
            } catch (e: Exception) {
                // Manejar otros errores aquí
                Toast.makeText(context, "There was a problem loading the friends", Toast.LENGTH_LONG)
                    .show()
                val spinner = requireView().findViewById<ProgressBar>(R.id.progressBar)
                spinner.visibility = View.INVISIBLE
            }
        }
    }
}