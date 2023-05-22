package com.apm.jacx.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.R
import com.apm.jacx.spotify.MediaServiceLifecycle
import com.apm.jacx.spotify.TrackItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track

class TrackAdapter (
    private val context: Context,
    private val dataset: List<TrackItem>,
    private val mediaService: MediaServiceLifecycle
) : RecyclerView.Adapter<TrackAdapter.ItemViewHolder>() {

    private val CLIENT_ID = "84d6e78f634c4bf593e20545c8768c47"
    private val REDIRECT_URI = "jacx://authcallback"
    private var spotifyAppRemote: SpotifyAppRemote? = null
    private val mediaServiceLifecycle: MediaServiceLifecycle? = null


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
        val artist: TextView = view.findViewById(R.id.artist_spotify_song)
        val previewUrl: FloatingActionButton = view.findViewById(R.id.previewUrl)
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

        /* Solo algunas canciones tienen previewUrl para poder escuchar fuera de Spotify */
        holder.previewUrl.setOnClickListener {
            if (item.previewUrl == null) {
                mediaService.stop()
                Toast.makeText(context, "Esta canción no está disponible", Toast.LENGTH_SHORT).show();
            } else {
                mediaService.play(item.previewUrl, position)
            }
        }

    }
}