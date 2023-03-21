package com.apm.jacx

import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class DetailTripMusic : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_trip_music)

        val listViewDetailMusic = findViewById<ListView?>(R.id.listView_trip_music);
        val arrayList: ArrayList<MyDataMusic> = ArrayList();
        var adapter: ListAdapterDetailMusic? = null

        arrayList.add(MyDataMusic("Calm Down", "Rema, Selena Gomez",  R.drawable.baseline_photo_album_24))
        arrayList.add(MyDataMusic("Caminar", "Dani Martin",  R.drawable.baseline_photo_album_24))
        arrayList.add(MyDataMusic("Una Noche Sin Pensar", "Sebastian Yatra",  R.drawable.baseline_photo_album_24))
        arrayList.add(MyDataMusic("Quien Diría", "Quevedo", R.drawable.baseline_photo_album_24))
        arrayList.add(MyDataMusic("Flowers", "Miley Cyrus", R.drawable.baseline_photo_album_24))

        listViewDetailMusic.adapter = ListAdapterDetailMusic(this, arrayList)
    }

    // Creamos un array temporal para mostrar los datos:
    class MyDataMusic(val tituloCancion: String, val autorCancion: String, val portada: Int);
}