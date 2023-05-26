package com.apm.jacx.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.R
import com.apm.jacx.client.ApiClient
import com.apm.jacx.model.Waypoint
import com.squareup.picasso.Picasso
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.util.*

class ItemWaypointAdapter(
    private val context: Context,
    private val dataset: MutableList<Waypoint>
) : RecyclerView.Adapter<ItemWaypointAdapter.ItemViewHolder>(), ItemTouchHelperAdapter {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view), ItemTouchHelperViewHolder {
        val cardWaypoint: CardView  = view.findViewById(R.id.card_waypoint)
        val textWaypoint: TextView = view.findViewById(R.id.waypoint_name)
        val image: ImageView = view.findViewById(R.id.waypoint_image)

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(Color.WHITE)
        }
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
        Picasso.get().load(item.url).into(holder.image)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(dataset, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemSwiped(position: Int) = runBlocking {
        val id = dataset[position].id
        dataset.removeAt(position)
        val body = JSONObject()
        body.put("id", id)
        ApiClient.delete("/waypoint", body.toString())
        notifyItemRemoved(position)
    }
}