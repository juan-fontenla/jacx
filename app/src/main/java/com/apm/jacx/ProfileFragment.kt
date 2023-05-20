package com.apm.jacx

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.apm.jacx.internalStorage.AppPreferences
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.spotify.sdk.android.auth.AuthorizationClient
import java.time.format.DateTimeFormatter
import java.util.Locale

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    // En esta etapa del ciclo de vida de deben cargar los datos en pantalla.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Inicializamos lo informacion del usuario
        val userInformation: JsonObject =
            Gson().fromJson(AppPreferences.USER_INFORMATION, JsonObject::class.java)

        val firstname = userInformation.get("firstName").toString()
        val lastname = userInformation.get("lastName").toString()
        // Obtén el idioma actual de la aplicación
        val currentLocale: Locale = Locale.getDefault()
        // Crea un objeto DateTimeFormatter con el formato largo en el idioma actual
        val dateFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", currentLocale)
        // Formatea la fecha en un String
        val birthday : String = userInformation.get("birthday").toString().format(dateFormatter)
        val email = userInformation.get("email").toString()

        val textView1 = view.findViewById<TextView>(R.id.firstname_profile)
        textView1.text = firstname
        val textView2 =view.findViewById<TextView>(R.id.lastname_profile)
        textView2.text = lastname
        val textView3 =view.findViewById<TextView>(R.id.email)
        textView3.text = email
        val textView4 =view.findViewById<TextView>(R.id.birthday_profile)
        textView4.text = birthday

        val logoutBtn = view.findViewById<Button>(R.id.logout)
        logoutBtn!!.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        val logoutSpotify = view.findViewById<Button>(R.id.logout_spotify)
        logoutSpotify?.setOnClickListener {
            // TODO: Se debe eliminar el token de la base de datos
            AuthorizationClient.clearCookies(context)
        }

        val resetPasswordBtn = view.findViewById<TextView>(R.id.change_password)
        resetPasswordBtn!!.setOnClickListener {
            val intent = Intent(activity, ResetPasswordActivity::class.java)
            startActivity(intent)
        }

    }
}