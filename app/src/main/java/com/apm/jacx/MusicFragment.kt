package com.apm.jacx

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.adapter.ItemPlayListAdapter
import com.apm.jacx.spotify.MusicViewModel
import com.apm.jacx.util.AppVariables.CLIENT_ID_SPOTIFY
import com.apm.jacx.util.AppVariables.REDIRECT_URI_SPOTIFY
import com.apm.jacx.util.AppVariables.REQUEST_CODE_SPOTIFY
import com.apm.jacx.util.RecyclerViewSizeChangeListener
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MusicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MusicFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val CLIENT_ID = "84d6e78f634c4bf593e20545c8768c47"
    private val REDIRECT_URI = "jacx://authcallback"
    private var spotifyAppRemote: SpotifyAppRemote? = null

    private val viewModel: MusicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // Spotify
        val connectionParams = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
            }
        })
        //
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music, container, false)
    }

    // Ciclo de vida: Aquí se deben realizar los cambios iniciales de la interfaz
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadMusicFragmentData(view)
        createListenerSpotifyButton(view)

        val recyclerView = view.findViewById<RecyclerView>(R.id.list_playlist)
        val containerNoMusic = view.findViewById<ConstraintLayout>(R.id.no_playlist)
        val sizeChangeListener = RecyclerViewSizeChangeListener {
            // Lógica a ejecutar cuando cambie el tamaño del RecyclerView
            val itemCount = recyclerView.adapter?.itemCount ?: 0
            if(itemCount <= 0) {
                recyclerView.visibility = View.INVISIBLE
                containerNoMusic.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                containerNoMusic.visibility = View.INVISIBLE
            }
        }
        recyclerView.addOnScrollListener(sizeChangeListener)
    }

    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MusicFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MusicFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun loadMusicFragmentData(viewFragment: View) {
        viewModel.initPlaylistMutableData()
        viewModel.playList.observe(viewLifecycleOwner) {
            val recyclerView = viewFragment.findViewById<RecyclerView>(R.id.list_playlist)
            Log.d("Playlist disponibles en Spotify", it.toString())
            recyclerView?.adapter = context?.let {
                viewModel.playList.value?.let { it1 ->
                    ItemPlayListAdapter(
                        it,
                        it1
                    )
                }
            }
            recyclerView.setHasFixedSize(true)
        }
    }

    private fun createListenerSpotifyButton(viewFragment: View) {
        val button: Button = viewFragment.findViewById(R.id.spotify_button)
        button.setOnClickListener {
            // Inicializamos login spotify.
            val builder = AuthorizationRequest.Builder(
                CLIENT_ID_SPOTIFY,
                AuthorizationResponse.Type.TOKEN,
                REDIRECT_URI_SPOTIFY
            );
            builder.setShowDialog(false)
            // Se añaden los siguientes scopes según las funcinalidades que queremos realizar
            builder.setScopes(
                arrayOf(
                    "streaming",
                    "playlist-read-private",
                    "playlist-read-collaborative",
                    "user-read-private",
                    "user-read-email"
                )
            )
            val request = builder.build()
            AuthorizationClient.openLoginActivity(requireActivity(), REQUEST_CODE_SPOTIFY, request)
        }
    }
}