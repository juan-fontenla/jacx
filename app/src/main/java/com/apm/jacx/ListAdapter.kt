package com.apm.jacx

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

//private val context: Context, private val arrayList: java.util.ArrayList<MyData>
class ListAdapter(private val context: DetailTripMusic,
                  var image: Array<Int>, var tituloCancion: Array<String>, var autorCancion: Array<String>): BaseAdapter() {



    override fun getCount(): Int {
        return tituloCancion.size
    }

    override fun getItem(position: Int): Any {
        return position;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view: View? = convertView
        val viewHolder: ViewHolder

        if(view==null) {
            viewHolder = ViewHolder()
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.listview_detail_trip_music, null, true)


            viewHolder.tituloCancion = view.findViewById(R.id.tV_titulo_elemento_list_trip_music)
            viewHolder.autorCancion = view.findViewById(R.id.tV_autor_elemento_list_trip_music)
            viewHolder.image = view.findViewById(R.id.iV_portada)

            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.tituloCancion?.text = tituloCancion[position]
        viewHolder.autorCancion?.text = autorCancion[position]
        viewHolder.image?.setImageResource(image[position])

        return view
    }


    // Añadimos los diferentes argumentos necesarios para el listview
    private inner class ViewHolder {
        var tituloCancion: TextView? = null
        var image: ImageView? = null
        var autorCancion: TextView? = null
    }
}