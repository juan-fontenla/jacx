package com.apm.jacx

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.adapter.ItemPhotoAdapter
import com.apm.jacx.adapter.ItemTripAdapter
import com.apm.jacx.data.Datasource

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExploreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExploreFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewFragment = inflater.inflate(R.layout.fragment_explore, container, false)
        loadTripsFragmentData(viewFragment)
        return viewFragment
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExploreFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExploreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun loadTripsFragmentData(viewFragment: View ) {
        // Initialize data.
        val myDataset = Datasource().loadTrips()

        Log.d("Trips dataset loaded", myDataset.toString())

        val recyclerView = viewFragment.findViewById<RecyclerView>(R.id.list_trips_explore)
        recyclerView?.adapter = context?.let { ItemTripAdapter(it, myDataset) }

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //recyclerView.setHasFixedSize(true)

        Toast.makeText(context, "Datos de viajes cargados", Toast.LENGTH_SHORT).show();
    }
}