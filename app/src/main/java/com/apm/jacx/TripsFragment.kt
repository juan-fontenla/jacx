package com.apm.jacx

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.adapter.ItemTripAdapter
import com.apm.jacx.client.ApiClient
import com.apm.jacx.data.Datasource
import com.apm.jacx.model.Trip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException

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
    private var tripList: MutableList<Trip> = mutableListOf<Trip>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
        fetchTripsData(viewFragment)
        return viewFragment
    }

    private fun fetchTripsData(viewFragment: View) = runBlocking{
        try {
            val responseBody = ApiClient.get("/route/owner")
            val jsonTripList = JSONArray(responseBody)
            if (jsonTripList.length() == 0) {
                Toast.makeText(context, "There are no trips yet", Toast.LENGTH_SHORT).show()
            }
            for (i in 0 until jsonTripList.length()) {
                val jsonTrip = jsonTripList.getJSONObject(i)
                tripList.add(Trip(jsonTrip))
            }
            val recyclerView = viewFragment.findViewById<RecyclerView>(R.id.list_own_trips)
            recyclerView?.adapter = context?.let { ItemTripAdapter(it, tripList) }
        } catch (e: Exception) {
            Log.e("TripsFragment", "Error fetching trips: " + e.message)
        }
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

}