package com.apm.jacx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.R
import com.apm.jacx.spotify.TrackItem
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import com.squareup.picasso.Picasso

class TrackAdapter (
    private val context: Context,
    private val dataset: List<TrackItem>
) : RecyclerView.Adapter<TrackAdapter.ItemViewHolder>() {

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
            }
        }

    }

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val titleSongText: TextView = view.findViewById(R.id.title_spotify_song)
        val image_song: ImageView = view.findViewById(R.id.icon_spotify_song)
        val artist: TextView = view.findViewById(R.id.artist_spotify_song)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.listview_track_playlist, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.titleSongText.text = item.name
        holder.artist.text = item.artists

        // Comprobamos si la playlist creada en Spotify tiene asociada una imagne o todavía no
        if (item.uri != null && item.uri.isNotEmpty()) {
            Picasso.get().load(item.uri[0].toString()).into(holder.image_song)
        } else {
            // Cargar una imagen por defecto cuando la playlist no existan canciones
            Picasso.get().load(R.drawable.image_default).into(holder.image_song)
        }


    }
}