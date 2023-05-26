package com.apm.jacx.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.DetailRouteActivity
import com.apm.jacx.R
import com.apm.jacx.model.Trip
import com.squareup.picasso.Picasso

class ItemTripAdapter(
    private val context: Context,
    private val dataset: List<Trip>
) : RecyclerView.Adapter<ItemTripAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val cardTrip: CardView  = view.findViewById(R.id.card_trip)
        val textTrip: TextView = view.findViewById(R.id.title_trip)
        val startDate: TextView = view.findViewById(R.id.startDate)
        val endDate: TextView = view.findViewById(R.id.endDate)
        val image: ImageView = view.findViewById(R.id.image_of_trip)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_trip_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.textTrip.text = item.name
        holder.startDate.text = item.startDate.toString()
        holder.endDate.text = item.endDate.toString()
        Picasso.get().load(item.finish.url).into(holder.image)
        holder.cardTrip.setOnClickListener {
            val intent = Intent(context, DetailRouteActivity::class.java)
            intent.putExtra("routeId", item.id)
            intent.putExtra("routeName", item.name)
            context.startActivity(intent)
        }
    }
}