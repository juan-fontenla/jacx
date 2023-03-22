package com.apm.jacx.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.R
import com.apm.jacx.model.Song
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ItemSongAdapter(
    private val context: Context,
    private val dataset: List<Song>
) : RecyclerView.Adapter<ItemSongAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val titleSongText: TextView = view.findViewById(R.id.title_song)
        val artistSongText: TextView = view.findViewById(R.id.artist_song)
        val songButton: FloatingActionButton = view.findViewById(R.id.play_song)
        // val image_song: ImageView = view.findViewById(R.id.image_song)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_song_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.titleSongText.text = item.title
        holder.artistSongText.text = item.artist
        holder.songButton.setOnClickListener {
            Toast.makeText(context, "Reproduciendo ${item.title} :)", Toast.LENGTH_SHORT).show();
        }
    }
}