package com.apm.jacx

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import java.util.ArrayList

class ListAdapterDetailMusic(private val context: DetailTripMusic,
                             private val arrayList: ArrayList<DetailTripMusic.MyDataMusic>) : BaseAdapter() {

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

        val convertView = LayoutInflater.from(context).inflate(R.layout.listview_detail_trip_music, parent, false)

        val tituloCancion = convertView.findViewById<TextView>(R.id.tV_titulo_elemento_list_trip_music)
        tituloCancion.text = arrayList[position].tituloCancion

        val autorCancion = convertView.findViewById<TextView>(R.id.tV_autor_elemento_list_trip_music)
        autorCancion.text = arrayList[position].autorCancion

        val portada = convertView.findViewById<ImageView>(R.id.iV_portada)
        portada.setImageResource(arrayList[position].portada)

        return convertView
    }
}