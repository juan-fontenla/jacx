package com.apm.jacx

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apm.jacx.adapter.ItemPhotoAdapter
import com.apm.jacx.client.ApiClient
import com.apm.jacx.data.Datasource
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.JsonArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import android.Manifest

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TripAlbumFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val REQUEST_IMAGE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TripAlbumFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trip_album, container, false)

        getAlbumImages()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Solicitar permisos si no están concedidos
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_IMAGE
            )

        } else {
            Toast.makeText(activity, "Permission denied", Toast.LENGTH_LONG)
        }

        val button = requireView().findViewById<FloatingActionButton>(R.id.btn_add_new_post_detail_trip_album)
        button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            val bitmap = BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(imageUri!!))

            val imageToBase64 = bitmapToBase64(bitmap)
            uploadImage(imageToBase64)
        }
    }

    private fun loadAlbumFragmentData(jsonArray: JsonArray) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.list_album_trip)
        val numberOfColumns: Int;

        // Initialize data.
        val myDataset = Datasource().loadPhotos(jsonArray)

        if (myDataset.size == 1){
            numberOfColumns = 1
        } else {
            numberOfColumns = 2
        }

        recyclerView?.layoutManager = GridLayoutManager(context, numberOfColumns)
        recyclerView?.adapter = context?.let { ItemPhotoAdapter(it, myDataset) }

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView?.setHasFixedSize(true)
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun uploadImage(imageInput: String) {
        // Petición al backend.
        // Se debe utilizar las corrutinas de esta forma. No mediante GlobalScope.
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val spinner = requireView().findViewById<ProgressBar>(R.id.progressBar)
                spinner.visibility = View.VISIBLE

                val jsonBody = JSONObject().apply {
                    put("base64", imageInput)
                }.toString()

                ApiClient.post("/image", jsonBody)
                getAlbumImages()

                spinner.visibility = View.INVISIBLE

            } catch (e: IOException) {
                // Manejar errores de red aquí
                Toast.makeText(context, "There was a problem uploading the image", Toast.LENGTH_LONG)
                    .show()
                val spinner = requireView().findViewById<ProgressBar>(R.id.progressBar)
                spinner.visibility = View.INVISIBLE
            } catch (e: Exception) {
                // Manejar otros errores aquí
                Toast.makeText(context, "There was a problem uploading the image", Toast.LENGTH_LONG)
                    .show()
                val button = requireView().findViewById<FloatingActionButton>(R.id.add_photo_button)
                button.visibility = View.VISIBLE
                val spinner = requireView().findViewById<ProgressBar>(R.id.progressBar)
                spinner.visibility = View.INVISIBLE
            }
        }
    }

    fun getAlbumImages(){
        // Petición al backend.
        // Se debe utilizar las corrutinas de esta forma. No mediante GlobalScope.
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val spinner = requireView().findViewById<ProgressBar>(R.id.progressBar)
                spinner.visibility = View.VISIBLE
                val responseGet = ApiClient.get("/images")

                val jsonArray: JsonArray = Gson().fromJson(responseGet, JsonArray::class.java)
                if (jsonArray.size() == 0) {
                    Toast.makeText(context, "There are no images", Toast.LENGTH_SHORT).show()
                }
                else {
                    loadAlbumFragmentData(jsonArray)
                }

                spinner.visibility = View.INVISIBLE

            } catch (e: IOException) {
                // Manejar errores de red aquí
                Toast.makeText(context, "There was a problem loading the images", Toast.LENGTH_LONG)
                    .show()
                val spinner = requireView().findViewById<ProgressBar>(R.id.progressBar)
                spinner.visibility = View.INVISIBLE
            } catch (e: Exception) {
                // Manejar otros errores aquí
                Toast.makeText(context, "There was a problem loading the image", Toast.LENGTH_LONG)
                    .show()
                val spinner = requireView().findViewById<ProgressBar>(R.id.progressBar)
                spinner.visibility = View.INVISIBLE
            }
        }
    }
}