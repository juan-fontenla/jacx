package com.apm.jacx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.R
import com.apm.jacx.model.Waypoint
import com.squareup.picasso.Picasso

class ItemWaypointAdapter(
    private val context: Context,
    private val dataset: List<Waypoint>
) : RecyclerView.Adapter<ItemWaypointAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val cardWaypoint: CardView  = view.findViewById(R.id.card_waypoint)
        val textWaypoint: TextView = view.findViewById(R.id.waypoint_name)
        val waypointOrder: TextView = view.findViewById(R.id.waypoint_order)
        val image: ImageView = view.findViewById(R.id.waypoint_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_waypoint_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.textWaypoint.text = item.name
        holder.waypointOrder.text = item.orderPosition.toString()
        Picasso.get().load(item.url).into(holder.image)
        holder.cardWaypoint.setOnClickListener {
            // TODO ?
        }
    }
}