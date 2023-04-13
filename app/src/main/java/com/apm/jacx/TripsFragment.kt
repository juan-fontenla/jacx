package com.apm.jacx

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.adapter.ItemTripAdapter
import com.apm.jacx.data.Datasource
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TripsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TripsFragment : Fragment() {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val joinButton = getView()?.findViewById<FloatingActionButton>(R.id.joinButton)
        joinButton!!.setOnClickListener{ onJoinButtonClick() }

        val createButton = getView()?.findViewById<FloatingActionButton>(R.id.newButton)
        createButton!!.setOnClickListener{ onCreateButtonClick() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val viewFragment = inflater.inflate(R.layout.fragment_trips, container, false)
        loadTripsFragmentData(viewFragment)
        return viewFragment
    }

    private fun onJoinButtonClick() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.addToBackStack(null)
        transaction?.replace(R.id.main_view_container, JoinFragment())
        transaction?.commit()
    }

    private fun onCreateButtonClick() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.addToBackStack(null)
        transaction?.replace(R.id.main_view_container, TripFormFragment())
        transaction?.commit()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TripsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TripsFragment().apply {
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

        val recyclerView = viewFragment.findViewById<RecyclerView>(R.id.list_own_trips)
        recyclerView?.adapter = context?.let { ItemTripAdapter(it, myDataset) }

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // recyclerView.setHasFixedSize(true)

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search_input, menu);
        return super.onCreateOptionsMenu(menu, inflater)
    }

}