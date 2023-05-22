package com.apm.jacx

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.apm.jacx.client.ApiClient
import com.apm.jacx.internalStorage.AppPreferences
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.spotify.sdk.android.auth.AuthorizationClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import java.time.format.DateTimeFormatter
import java.util.Locale


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializamos lo informacion del usuario
        val userInformation: JsonObject =
            Gson().fromJson(AppPreferences.USER_INFORMATION, JsonObject::class.java)

        val firstname = userInformation.get("firstName").asString
        val lastname = userInformation.get("lastName").asString
        // Obtén el idioma actual de la aplicación
        val currentLocale: Locale = Locale.getDefault()
        // Crea un objeto DateTimeFormatter con el formato largo en el idioma actual
        val dateFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", currentLocale)
        // Formatea la fecha en un String
        val birthday : String = userInformation.get("birthday").asString.format(dateFormatter)
        val email = userInformation.get("email").asString

        val firstnameText = view.findViewById<TextView>(R.id.signup_name)
        firstnameText.text = firstname
        val lastnameText =view.findViewById<TextView>(R.id.signup_lastname)
        lastnameText.text = lastname
        val emailText =view.findViewById<TextView>(R.id.profile_email)
        emailText.text = email
        val birthdayText =view.findViewById<TextView>(R.id.profile_birthday)
        birthdayText.text = birthday

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }


        val logoutBtn = getView()?.findViewById<Button>(R.id.profile_logout)
        logoutBtn!!.setOnClickListener {
            deleteToken()
        }

        val logoutGoogle = getView()?.findViewById<Button>(R.id.logout_google)
        logoutGoogle?.setOnClickListener {
            activity?.let { it1 ->
                mGoogleSignInClient?.signOut()?.addOnCompleteListener(it1, object : OnCompleteListener<Void?> {
                    override fun onComplete(p0: Task<Void?>) {
                        //TODO: ELIMINAR TOKEN DE BASE DE DATOS
                        AuthorizationClient.clearCookies(context)
                    }
                })
            }
        }

        val logoutSpotify = getView()?.findViewById<Button>(R.id.profile_logout_spotify)
        logoutSpotify?.setOnClickListener {
            // TODO: Se debe eliminar el token de la base de datos
            AuthorizationClient.clearCookies(context)
        }

        val resetPasswordBtn = getView()?.findViewById<TextView>(R.id.profile_change_password)
        resetPasswordBtn!!.setOnClickListener {
            val intent = Intent(activity, ResetPasswordActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.cancel() // Cancelar todas las corutinas cuando se destruye la actividad
    }

    private fun deleteToken() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val jsonBody = JSONObject().apply {}.toString()
                val responsePost = ApiClient.post("/logout", jsonBody)
                Gson().fromJson(responsePost, JsonObject::class.java)

                // Eliminamos el token del almacenamiento interno y sus datos asociados
                AppPreferences.USER_INFORMATION = null
                AppPreferences.TOKEN_BD = null

                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            } catch (e: IOException) {
                // Manejar errores de red aquí
                Log.d("Error de red", e.toString())
                Toast.makeText(context, "Error de red", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                // Manejar otros errores aquí
                Log.d("Error en la peticion", e.toString())
                Toast.makeText(context, "Error en la peticion", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}