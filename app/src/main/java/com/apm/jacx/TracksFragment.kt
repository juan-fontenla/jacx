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
import com.apm.jacx.adapter.ItemPlayListAdapter
import com.apm.jacx.adapter.TrackAdapter
import com.apm.jacx.spotify.MusicViewModel
import com.apm.jacx.spotify.PlayList


class TracksFragment : Fragment() {

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
        val viewFragment = inflater.inflate(R.layout.fragment_tracks, container, false)
        loadTracksFragmentData(viewFragment)

        // Inflate the layout for this fragment
        return viewFragment
    }

    private fun loadTracksFragmentData(viewFragment: View) {
        playlist?.let { Log.d("Playlist: ", it.id) }

        // EStá mostrando todas las canciones en ambas playList pero se muestran
        viewModel.tracks.observe(viewLifecycleOwner) {
            val recyclerView = viewFragment.findViewById<RecyclerView>(R.id.list_tracks)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            recyclerView?.adapter = context?.let { viewModel.tracks.value?.let { it1 ->
                TrackAdapter(it,
                    it1
                )
            } }
            recyclerView.setHasFixedSize(true)
         }
    }
}