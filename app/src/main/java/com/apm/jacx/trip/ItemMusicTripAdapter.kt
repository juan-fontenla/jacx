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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.LoginActivity
import com.apm.jacx.R
import com.apm.jacx.TracksFragment
import com.apm.jacx.TripMusicFragment
import com.apm.jacx.client.ApiClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

class ItemMusicTripAdapter(
    private val context: Context,
    private val dataset: List<MusicTrip>
) : RecyclerView.Adapter<ItemMusicTripAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val tituloCancionView: TextView = view.findViewById(R.id.title_spotify_song_trip)
        val autorCancionView: TextView = view.findViewById(R.id.artist_spotify_song_trip)
        val btn: FloatingActionButton = view.findViewById(R.id.buttonSongTrip)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.listview_detail_trip_music, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.tituloCancionView.text = item.tituloCancion
        holder.autorCancionView.text = item.autorCancion
        holder.btn.setOnClickListener {
            deleteSongTrip(item.spotifyId, item.rutaName, holder)
        }
    }

    private fun deleteSongTrip(spotifyId: String, rutaName: String, holder: ItemViewHolder) {
        Toast.makeText(context, "deleting $spotifyId from $rutaName", Toast.LENGTH_LONG).show()
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val jsonBody = JSONObject().apply {
                    put("spotifyID", spotifyId)
                    put("routeName", rutaName)
                }.toString()
                ApiClient.delete("/route/playlist", jsonBody)

                // Creamos unha instacia do fragmento
                val framentToLoad = TripMusicFragment()
                val activity = holder.itemView.context as AppCompatActivity
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.main_view_container_trip, framentToLoad)
                    .addToBackStack(null)
                    .commit()

            } catch (e: IOException) {
                // Manejar errores de red aquí
                Log.d("Error de red", e.toString())
                Toast.makeText(context, "Error de red", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                // Manejar otros errores aquí
                Log.d("Error en la peticion", e.toString())
                Toast.makeText(context, "Error en la peticion", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}