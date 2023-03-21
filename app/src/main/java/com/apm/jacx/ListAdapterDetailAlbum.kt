package com.apm.jacx

import android.widget.BaseAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import java.util.ArrayList;


class ListAdapterDetailAlbum (private val context: DetailTripAlbum,
                              private val arrayList: ArrayList<DetailTripAlbum.MyData>) : BaseAdapter() {

    override fun getCount(): Int {
        return arrayList.size;
    }

    override fun getItem(position: Int): Any {
        return position;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val convertView = LayoutInflater.from(context).inflate(R.layout.listview_detail_trip_album, parent, false)

        val userName = convertView.findViewById<TextView>(R.id.tV_username_trip_album)
        userName.text = arrayList[position].userName

        val horaPublicacion = convertView.findViewById<TextView>(R.id.tV_horaPublicacion_trip_album)
        horaPublicacion.text = "Hace " + arrayList[position].horaPublicacion + " horas"

        val logoPerfil = convertView.findViewById<ImageView>(R.id.iV_logo_perfil_album)
        logoPerfil.setImageResource(arrayList[position].logoPerfil)

        val publicacionPerfil = convertView.findViewById<ImageView>(R.id.iV_publicacion_album)
        publicacionPerfil.setImageResource(arrayList[position].publicacionPerfil)

        return convertView
    }


}
