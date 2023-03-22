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

class ItemAlbumTripAdapter(private val context: Context,
                           private val dataset: List<AlbumTrip>) :
    RecyclerView.Adapter<ItemAlbumTripAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        val logoPerfilView: ImageView = view.findViewById(R.id.iV_logo_perfil_album)
        val userNameView: TextView = view.findViewById(R.id.tV_username_trip_album)
        val horaPublicacionView: TextView = view.findViewById(R.id.tV_horaPublicacion_trip_album)
        val publicacionPerfilView: ImageView = view.findViewById(R.id.iV_publicacion_album)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.listview_detail_trip_album, parent, false)
        return ItemAlbumTripAdapter.ItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.userNameView.setOnClickListener {
            Toast.makeText(context, "Este userName se corresponde con ${item.id}", Toast.LENGTH_SHORT).show();
        }
    }

}