package com.apm.jacx.trip

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.MainActivity
import com.apm.jacx.R
import com.apm.jacx.client.ApiClient
import com.apm.jacx.internalStorage.AppPreferences
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

class ItemFriendAdapter(
    private val context: Context,
    private val dataset: List<Friend>
) : RecyclerView.Adapter<ItemFriendAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val photoView: ImageView = view.findViewById(R.id.iV_perfil_trip_friend)
        val userNameView: TextView = view.findViewById(R.id.tV_userName_trip_friend)
        val emailView: TextView = view.findViewById(R.id.tV_email_userName_trip_friend)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.listview_detail_trip_friend, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]

        getFriends("ana")
        holder.userNameView.setOnClickListener {
            Toast.makeText(context, "Este email se corresponde con ${item.id}", Toast.LENGTH_SHORT).show();
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    private fun getFriends(username: CharSequence?) {
        // Petición al backend.
        // Se debe utilizar las corrutinas de esta forma. No mediante GlobalScope.
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val jsonBody = JSONObject().apply {
                    put("username", username)
                }.toString()
                val responsePost = ApiClient.post("/friends", jsonBody)
                val jsonObject: JsonObject = Gson().fromJson(responsePost, JsonObject::class.java)
                println(jsonObject)


                



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