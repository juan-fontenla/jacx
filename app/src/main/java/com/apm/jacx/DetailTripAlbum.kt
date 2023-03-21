package com.apm.jacx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView

class DetailTripAlbum : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_trip_album)

        val listViewDetailAlbum = findViewById<ListView?>(R.id.listView_trip_album);
        val arrayList: ArrayList<MyData> = ArrayList();
        var adapter: ListAdapterDetailAlbum? = null

        arrayList.add(MyData("Xaime Pardal", 3,  R.mipmap.ic_launcher_round, R.drawable.baseline_photo_album_24))
        arrayList.add(MyData("Juan Fontenla", 12,  R.mipmap.ic_launcher_round, R.drawable.baseline_photo_album_24))
        arrayList.add(MyData("Clara Juncal", 3,  R.mipmap.ic_launcher_round, R.drawable.baseline_photo_album_24))
        arrayList.add(MyData("Ana Maria Lema", 5,  R.mipmap.ic_launcher_round, R.drawable.baseline_photo_album_24))

        listViewDetailAlbum.adapter = ListAdapterDetailAlbum(this,arrayList)
    }

    // Creamos un array temporal para mostrar los datos:
    class MyData(val userName: String, val horaPublicacion: Int, val logoPerfil: Int, val publicacionPerfil: Int);
}