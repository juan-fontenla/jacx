package com.apm.jacx.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.AlbumFragment
import com.apm.jacx.R
import com.apm.jacx.client.ApiClient
import com.apm.jacx.model.Photo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

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

        holder.photoView.setImageBitmap(base64ToBitmap(item.base64))

        holder.photoView.setOnClickListener {
            showDeleteConfirmationDialog(item.id)
        }
    }

    fun base64ToBitmap(base64String: String): Bitmap {
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    private fun deleteImage(id: Int){
        // Petición al backend.
        // Se debe utilizar las corrutinas de esta forma. No mediante GlobalScope.
        CoroutineScope(Dispatchers.Main).launch {
            try {

                val jsonBody = JSONObject().apply {
                    put("id", id)
                }.toString()

                ApiClient.delete("/image", jsonBody)

                val fragmentToLoad = AlbumFragment()
                val activity = context as AppCompatActivity
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.main_view_container, fragmentToLoad)
                    .addToBackStack(null)
                    .commit()

            } catch (e: IOException) {
                Toast.makeText(context, "There was a problem deleting the image", Toast.LENGTH_LONG)
                    .show()
            } catch (e: Exception) {
                Toast.makeText(context, "There was a problem deleting the image", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun showDeleteConfirmationDialog(id: Int) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Delete Image")
        alertDialogBuilder.setMessage("¿Are you sure you want to delete the image?")
        alertDialogBuilder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            // Acción a realizar si se confirma la eliminación
            deleteImage(id)
        }
        alertDialogBuilder.setNegativeButton("No", null)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}