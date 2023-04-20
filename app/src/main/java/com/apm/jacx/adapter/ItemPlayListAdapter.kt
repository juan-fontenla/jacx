package com.apm.jacx.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.R
import com.apm.jacx.spotify.PlayList
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import com.squareup.picasso.Picasso
import java.net.URL


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

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.titleSongText.text = item.name

        println("---- desde aqui es el contenido de la playlist:")
        println(item)

        // TODO: falta mostrar la imagen del album
        Picasso.get().load(item.images[0].url).into(holder.image_song);

        // A traves de este enlace reproducimos la playlist
        holder.songButton.setOnClickListener {
            Toast.makeText(context, "${item.name}", Toast.LENGTH_SHORT).show();
        }
    }
}