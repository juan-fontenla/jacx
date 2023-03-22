package com.apm.jacx.trip

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.R

class ItemFriendAdapter(
    private val context: Context,
    private val dataset: List<Friend>
) : RecyclerView.Adapter<ItemFriendAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val photoView: ImageView = view.findViewById(R.id.iV_perfil_trip_friend)
        val userNameView: TextView = view.findViewById(R.id.tV_userName_trip_friend)
        val emailView: TextView = view.findViewById(R.id.tV_email_userName_trip_friend)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.listview_detail_trip_friend, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.userNameView.setOnClickListener {
            Toast.makeText(context, "Este email se corresponde con ${item.id}", Toast.LENGTH_SHORT).show();
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}