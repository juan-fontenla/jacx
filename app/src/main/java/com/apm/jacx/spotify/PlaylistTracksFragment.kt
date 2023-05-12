package com.apm.jacx.spotify

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.R
import com.apm.jacx.spotify.domain.PlaylistTracksViewModel
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AccountsQueryParameters.CLIENT_ID
import com.spotify.sdk.android.auth.AccountsQueryParameters.REDIRECT_URI

class PlaylistTracksFragment : Fragment() {

    lateinit var mediaServiceLifecycle: MediaServiceLifecycle
    private var spotifyAppRemote: SpotifyAppRemote? = null

    private val viewModel: PlaylistTracksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);

        // Spotify
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d("SONGS!", "Connected! Yay!")
                // Now you can start interacting with App Remote
                // El metodo connected nos permite reproducir musica aleatoria.
                // connected()
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("SONG", throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })
        //
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycle.addObserver(mediaServiceLifecycle)

//        val trackAdapter = TrackAdapter(mediaServiceLifecycle)
        loadSongOfPlayList(view)

//        viewModel.tracks.observe(viewLifecycleOwner) {
//            trackAdapter.submitList(it)
//            footerAdapter.submitList(
//                listOf(
//                    generateFooter(
//                        it.size,
//                        it.sumOf { track -> track.duration }.toLong()
//                    )
//                )
//            )
//        }
    }

    private fun loadSongOfPlayList(viewFragment: View) {

        viewModel.tracks.observe(viewLifecycleOwner) {

            val recyclerView = viewFragment.findViewById<RecyclerView>(R.id.list_songs)
            Log.d("Playlist disponibles en Spotify", it.toString())
//            recyclerView?.adapter = context?.let { viewModel.tracks.value?.let { it1 ->
//                TrackAdapter(it,
//                   listOf( it1.size,
//                       it1.sumOf { track -> track.duration })
//                )
//            } }
            recyclerView.setHasFixedSize(true)
        }
    }
}