package com.apm.jacx

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.adapter.ItemPhotoAdapter
import com.apm.jacx.data.Datasource
import com.google.android.material.floatingactionbutton.FloatingActionButton


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AlbumFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlbumFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val viewFragment = inflater.inflate(R.layout.fragment_album, container, false)
        loadAlbumFragmentData(viewFragment)
        createListenerAddButton(viewFragment)
        return viewFragment
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AlbumFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AlbumFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun loadAlbumFragmentData(viewFragment: View) {
        // Initialize data.
        val myDataset = Datasource().loadPhotos()


        Log.d("Photos dataset loaded", myDataset.toString())

        val recyclerView = viewFragment.findViewById<RecyclerView>(R.id.list_photos)
        val numberOfColumns = 3
        recyclerView?.layoutManager = GridLayoutManager(context, numberOfColumns)
        recyclerView?.adapter = context?.let { ItemPhotoAdapter(it, myDataset) }

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //recyclerView.setHasFixedSize(true)

        Toast.makeText(context, "Datos de fotos cargados", Toast.LENGTH_SHORT).show();
    }

    private fun createListenerAddButton(viewFragment: View) {
        val button : FloatingActionButton = viewFragment.findViewById(R.id.add_photo_button)
        button.setOnClickListener {
            Toast.makeText(context, "Añadir foto", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search_input, menu);
        return super.onCreateOptionsMenu(menu, inflater)
    }
}