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

class ItemMusicTripAdapter(
    private val context: Context,
    private val dataset: List<MusicTrip>
) : RecyclerView.Adapter<ItemMusicTripAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val portadaView: ImageView = view.findViewById(R.id.icon_spotify_song)
        val tituloCancionView: TextView = view.findViewById(R.id.title_spotify_song)
        val autorCancionView: TextView = view.findViewById(R.id.artist_spotify_song)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemMusicTripAdapter.ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.listview_detail_trip_music, parent, false)
        return ItemMusicTripAdapter.ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemMusicTripAdapter.ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.tituloCancionView.setOnClickListener {
            Toast.makeText(context, "Este titulo se corresponde con ${item.id}", Toast.LENGTH_SHORT).show();
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}