package com.apm.jacx

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


class DetailTripMusic : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_trip_music)

        val titulosCanciones = arrayOf(
            "Caminar", "Una Noche Sin Pensar", "Calm Down", "Quien Diría"
        )

        val imagenesPortada = arrayOf(
            R.drawable.baseline_photo_album_24, R.drawable.baseline_photo_album_24,
            R.drawable.baseline_photo_album_24, R.drawable.baseline_photo_album_24)

        val autorCancion = arrayOf(
            "Dani Martin", "Sebastian Yatra", "Rema, Selena Gomez", "Quevedo"
        )

        val mLeadsList = findViewById<ListView?>(R.id.listView_trip_music);

        mLeadsList.adapter = ListAdapter(this, imagenesPortada, titulosCanciones, autorCancion)
    }
}