package com.apm.jacx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.R
import com.apm.jacx.model.Photo

class ItemPhotoAdapter(
    private val context: Context,
    private val dataset: List<Photo>
) : RecyclerView.Adapter<ItemPhotoAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val photoView: ImageView = view.findViewById(R.id.photo_in_album)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_photo_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.photoView.setOnClickListener {
            Toast.makeText(context, "Abriendo photo ${item.id}", Toast.LENGTH_SHORT).show();
        }
    }
}