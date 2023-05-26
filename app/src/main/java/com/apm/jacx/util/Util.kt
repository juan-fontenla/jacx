package com.apm.jacx.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.apm.jacx.model.Waypoint
import com.google.android.libraries.places.api.model.Place
import org.json.JSONObject
import java.security.MessageDigest
import android.content.res.Resources
import android.util.Log
import com.apm.jacx.R
import java.net.URLEncoder

class Util {

    companion object {
        fun placeToWaypoint(place: Place, context: Context): Waypoint {
            val json = JSONObject()
            json.put("name", place.name)
            json.put("point", "${place.latLng!!.latitude},${place.latLng!!.longitude}")
            json.put("url", getPhotoUrl(place, context))
            json.put("color", place.iconBackgroundColor)
            return Waypoint(json)
        }

        // Genera un PIN de 8 digitos a partir de una cadena de texto.
        fun generatePIN(input: String): String {
            val digest = MessageDigest.getInstance("SHA-256")
            val encodedHash = digest.digest(input.toByteArray())
            val truncatedHash =
                encodedHash.copyOfRange(0, 8) // Truncate to 8 bytes (16 hexadecimal digits)

            val stringBuilder = StringBuilder()
            for (byte in truncatedHash) {
                val num = byte.toInt() and 0xFF // Convert byte to unsigned integer
                val digit = num % 10 // Keep only the last digit
                stringBuilder.append(digit)
            }
            return stringBuilder.toString()
        }

        // Cerrar teclado
        fun hideKeyboard(activity: Activity) {
            val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val currentFocusView = activity.currentFocus
            if (currentFocusView != null) {
                inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
            }
        }

        private fun getPhotoUrl(place: Place, context: Context): String? {
            if (place.photoMetadatas == null || place.photoMetadatas.size == 0) {
                return null
            }
            val key = context.resources.getString(R.string.key)
            val photoMetadata = place.photoMetadatas.get(0)
            val photoReference = photoMetadata.toString().split("photoReference=", "}")[1]
            return "https://maps.googleapis.com/maps/api/place/photo" +
                    "?photoreference=${photoReference}" +
                    "&maxwidth=350" +
                    "&maxheight=210" +
                    "&key=${key}" +
                    "&attributions=${URLEncoder.encode(photoMetadata.attributions, "UTF-8")}"
        }
    }
}