package com.apm.jacx.util

import com.apm.jacx.model.Waypoint
import com.google.android.libraries.places.api.model.Place
import org.json.JSONObject
import java.security.MessageDigest

class Util {

    companion object {
        fun placeToWaypoint(place: Place): Waypoint {
            val json = JSONObject()
            json.put("name", place.name)
            json.put("point", "${place.latLng!!.latitude},${place.latLng!!.longitude}")
            json.put("url", place.iconUrl)
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
    }
}