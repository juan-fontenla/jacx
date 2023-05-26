package com.apm.jacx

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.client.ApiClient
import com.apm.jacx.spotify.MusicViewModel
import com.apm.jacx.spotify.domain.PlayList
import com.apm.jacx.spotify.domain.TrackItem
import com.apm.jacx.trip.ItemMusicTripAdapter
import com.apm.jacx.trip.RoutePlaylist
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException


private const val ARG_PARAM1 = "routeName"

class TripMusicFragment : Fragment() {

    private var routeName: String? = "rutaXaime"
    private val viewModel: MusicViewModel by viewModels()

    private lateinit var firstSpinner: Spinner
    private lateinit var secondSpinner: Spinner

    private lateinit var dialogView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            routeName = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trip_music, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadMusicTrip(routeName)
        createListenerMusicButton(view)

        viewModel.initPlaylistMutableData()

        dialogView = layoutInflater.inflate(R.layout.custom_dialog, null)
        firstSpinner = dialogView.findViewById(R.id.firstSpinner)
        secondSpinner = dialogView.findViewById(R.id.secondSpinner)

        // Observador para el primer desplegable
        viewModel.playList.observe(requireActivity(), Observer { firstOptions ->
            // Actualizar el adaptador del primer desplegable con los nuevos valores
            val firstAdapter =
                PlayListArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    firstOptions
                )
            firstAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            firstSpinner.adapter = firstAdapter
        })

        // Observador para el segundo desplegable
        viewModel.tracks.observe(requireActivity(), Observer { secondOptions ->
            // Actualizar el adaptador del segundo desplegable con los nuevos valores
            val secondAdapter =
                TrackArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    secondOptions
                )
            secondAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            secondSpinner.adapter = secondAdapter
        })
    }

    private fun loadMusicTrip(rutaName: String?) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val responseGet = ApiClient.get("/route/$rutaName")
                val route = Gson().fromJson(responseGet, RoutePlaylist::class.java)

                viewModel.initTracksByArrayIds(route.playlists, rutaName!!)
                viewModel.listTracksTrip.observe(viewLifecycleOwner) {
                    val recyclerView = view?.findViewById<RecyclerView>(R.id.list_music_trip)
                    recyclerView?.adapter = context?.let {
                        viewModel.listTracksTrip.value?.let { value ->
                            ItemMusicTripAdapter(
                                it,
                                value
                            )
                        }
                    }
                    recyclerView?.setHasFixedSize(true)
                }
            } catch (e: IOException) {
                // Manejar errores de red aquí
                Log.d("Error de red", e.toString())
                Toast.makeText(requireContext(), "Error de red", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                // Manejar otros errores aquí
                Log.d("Error en la peticion", e.toString())
                Toast.makeText(requireContext(), "Error en la peticion", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            TripMusicFragment()
                .apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }

    private fun createListenerMusicButton(viewFragment: View) {
        val button: FloatingActionButton =
            viewFragment.findViewById(R.id.btn_add_new_post_detail_trip_album)
        button.setOnClickListener {
            showCustomDialog(viewModel.playList, viewModel.tracks)
        }
    }

    private fun showCustomDialog(
        firstOptionsLiveData: MutableLiveData<List<PlayList>>,
        secondOptionsLiveData: MutableLiveData<List<TrackItem>>
    ) {

        var trackIdSelected: String? = null

        // Inicializar el adaptador del primer desplegable con una lista vacía
        val firstAdapter = PlayListArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListOf()
        )

        // Observar cambios en la MutableLiveData<List<PlayList>> y actualizar el adaptador
        firstOptionsLiveData.observe(viewLifecycleOwner, Observer { playLists ->
            firstAdapter.clear()
            firstAdapter.addAll(playLists)
            firstAdapter.notifyDataSetChanged()
        })
        firstAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        firstSpinner.adapter = firstAdapter

        // Inicializar el adaptador del segundo desplegable con una lista vacía
        val secondAdapter = TrackArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListOf()
        )
        // Observar cambios en la MutableLiveData<List<PlayList>> y actualizar el adaptador
        secondOptionsLiveData.observe(viewLifecycleOwner, Observer { trackItems ->
            secondAdapter.clear()
            secondAdapter.addAll(trackItems)
            secondAdapter.notifyDataSetChanged()
        })
        secondAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        secondSpinner.adapter = secondAdapter

        // Observar cambios en el primer desplegable y actualizar los valores del segundo desplegable
        firstSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedOption = firstAdapter.getItem(position)
                selectedOption?.toPlaylistItem()?.let { viewModel.initTracksMutableData(it.id) }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Observar cambios en el segundo desplegable y realizar acciones adicionales si es necesario
        secondSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                trackIdSelected = secondAdapter.getItem(position)?.id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        val dialogParent = dialogView.parent as? ViewGroup
        dialogParent?.removeView(dialogView) // Remover el padre si existe

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Seleccione una canción")
            .setView(dialogView)
            .setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
                // Realiza acciones adicionales si es necesario al hacer clic en Aceptar
                routeName?.let { trackIdSelected?.let { it1 -> addSongToRoute(it1, it) } }
            })
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }
    private fun addSongToRoute(spotifyId: String, routeName: String ) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val jsonBody = JSONObject().apply {
                    put("spotifyID", spotifyId)
                    put("routeName", routeName )
                }.toString()
                ApiClient.post("/route/playlist", jsonBody)

                // Creamos unha instacia do fragmento
                val currentFragment = this@TripMusicFragment // Obtener el fragmento actual
                val newFragment = TripMusicFragment.newInstance(routeName) // Crear una nueva instancia del fragmento
                val fragmentTransaction = parentFragmentManager.beginTransaction()
                fragmentTransaction.replace(currentFragment.id, newFragment)
                fragmentTransaction.commit()

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

}

private class PlayListArrayAdapter(context: Context, resource: Int, objects: List<PlayList>) :
    ArrayAdapter<PlayList>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val playList = getItem(position)
        val name = playList?.name
        (view as? TextView)?.text = name
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val playList = getItem(position)
        val name = playList?.name
        (view as? TextView)?.text = name
        return view
    }
}

private class TrackArrayAdapter(context: Context, resource: Int, objects: List<TrackItem>) :
    ArrayAdapter<TrackItem>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val track = getItem(position)
        val name = track?.name
        (view as? TextView)?.text = name
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val track = getItem(position)
        val name = track?.name
        (view as? TextView)?.text = name
        return view
    }
}
