package com.apm.jacx

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.adapter.TrackAdapter
import com.apm.jacx.spotify.MediaServiceLifecycle
import com.apm.jacx.spotify.MusicViewModel
import com.apm.jacx.spotify.domain.PlayList


class TracksFragment : Fragment() {

    private val mediaServiceLifecycle: MediaServiceLifecycle by lazy {
        // Inicializacion MediaServiceLifecycle
        MediaServiceLifecycle()
    }

    private var playlist: PlayList? = null;
    private val viewModel: MusicViewModel by viewModels()

    fun loadPlaylist(playlistLoad: PlayList) {
        playlist = playlistLoad;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tracks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadTracksFragmentData(view)
    }


    private fun loadTracksFragmentData(viewFragment: View) {
        playlist?.id?.let { viewModel.initTracksMutableData(it) }
        // EStá mostrando todas las canciones en ambas playList pero se muestran
        viewModel.tracks.observe(viewLifecycleOwner) {
            Log.d("Tracks", it.toString());
            val recyclerView = viewFragment.findViewById<RecyclerView>(R.id.list_tracks)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            recyclerView?.adapter = context?.let { viewModel.tracks.value?.let { it1 ->
                TrackAdapter(it,
                    it1,
                    mediaServiceLifecycle
                )
            } }
            recyclerView.setHasFixedSize(true)
         }
    }
}