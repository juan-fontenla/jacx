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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.spotify.sdk.android.auth.AuthorizationClient
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

        val firstnameText = view.findViewById<TextView>(R.id.text_name)
        firstnameText.text = firstname
        val lastnameText =view.findViewById<TextView>(R.id.text_login)
        lastnameText.text = lastname
        val emailText =view.findViewById<TextView>(R.id.text_mail)
        emailText.text = email
        val birthdayText =view.findViewById<TextView>(R.id.text_date)
        birthdayText.text = birthday

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }


        val logoutBtn = getView()?.findViewById<Button>(R.id.logout)
        logoutBtn!!.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        val logoutGoogle = getView()?.findViewById<Button>(R.id.logout_google)
        logoutGoogle?.setOnClickListener {
            getActivity()?.let { it1 ->
                mGoogleSignInClient?.signOut()?.addOnCompleteListener(it1, object : OnCompleteListener<Void?> {
                    override fun onComplete(p0: Task<Void?>) {
                        //TODO: ELIMINAR TOKEN DE BASE DE DATOS
                        AuthorizationClient.clearCookies(context)
                    }
                })
            }
        }

        val logoutSpotify = getView()?.findViewById<Button>(R.id.logout_spotify)
        logoutSpotify?.setOnClickListener {
            // TODO: Se debe eliminar el token de la base de datos
            AuthorizationClient.clearCookies(context)
        }

        val resetPasswordBtn = getView()?.findViewById<TextView>(R.id.change_password)
        resetPasswordBtn!!.setOnClickListener {
            val intent = Intent(activity, ResetPasswordActivity::class.java)
            startActivity(intent)
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