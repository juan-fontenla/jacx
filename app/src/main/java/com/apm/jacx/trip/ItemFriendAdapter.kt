package com.apm.jacx.trip

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.R
import com.apm.jacx.client.ApiClient
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

        holder.userNameView.text = item.username
        holder.emailView.text = item.email

        holder.userNameView.setOnClickListener{
            showDialogDeleteFriend(item.username)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    /* Mostramos un dialogo para confirmar si se quiere o no eliminar el amigo */
    private fun showDialogDeleteFriend(username: String) {

        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder.setTitle("Delete travel friend")
        dialogBuilder.setMessage("Do you want to remove this user from the trip?")
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                /* Eliminamos el amigo del viaje */
                removeFriendFromTheTrip(username)
            }
            // negative button text and action
            .setNegativeButton("No", null)

        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun removeFriendFromTheTrip(username: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val jsonBody = JSONObject().apply {
                    put("username", username)
                }.toString()

                val responsePost = ApiClient.delete("/friend", jsonBody)
                Log.d("DELETE FRIENDS", responsePost)


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