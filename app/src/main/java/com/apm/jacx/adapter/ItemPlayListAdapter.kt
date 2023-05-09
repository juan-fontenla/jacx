package com.apm.jacx.adapter

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
import com.apm.jacx.*
import com.apm.jacx.spotify.PlayList
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import com.squareup.picasso.Picasso



class ItemPlayListAdapter(
    private val context: Context,
    private val dataset: List<PlayList>
) : RecyclerView.Adapter<ItemPlayListAdapter.ItemViewHolder>() {

    private val CLIENT_ID = "84d6e78f634c4bf593e20545c8768c47"
    private val REDIRECT_URI = "jacx://authcallback"
    private var spotifyAppRemote: SpotifyAppRemote? = null

    private fun connected() {

        spotifyAppRemote?.let {
            // Play a playlist
            val playlistURI = "spotify:playlist:37i9dQZF1DX2sUQwD7tbmL"
            it.playerApi.play(playlistURI)
            // Subscribe to PlayerState
            it.playerApi.subscribeToPlayerState().setEventCallback {
                val track: Track = it.track
                Log.d("MainActivity", track.name + " by " + track.artist.name)
            }
        }

    }

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val titleSongText: TextView = view.findViewById(R.id.title_song)
        val image_song: ImageView = view.findViewById(R.id.image_song)
        val totalCanciones: TextView = view.findViewById(R.id.totalCanciones)
        val songButton: FloatingActionButton = view.findViewById(R.id.play_song)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_song_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    // Este método nos permite recuperar el contenido de una playlist para mostrar posteriormente en el recyclerview
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]

        holder.titleSongText.text = item.name
        holder.totalCanciones.text = item.tracks.total.toString() + " canciones"

        // Comprobamos si la playlist creada en Spotify tiene asociada una imagne o todavía no
        if (item.images != null && item.images.isNotEmpty()) {
            Picasso.get().load(item.images[0].url).into(holder.image_song)
        } else {
            // Cargar una imagen por defecto cuando la playlist no existan canciones
            Picasso.get().load(R.drawable.image_default).into(holder.image_song)
        }

        // TODO: Falta conseguir mostrar la nueva pantalla para mostrar las canciones
        val searchByPinButton = holder.songButton
        searchByPinButton!!.setOnClickListener{
            val intent = Intent(context, DetailRouteActivity::class.java)
            context.startActivity(intent)
        }

    }
}