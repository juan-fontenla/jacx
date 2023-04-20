package com.apm.jacx

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

class LoginActivity : AppCompatActivity() {
    // Request code will be used to verify if result comes from the login activity. Can be set to any integer.
    private val REQUEST_CODE = 45897640
    private val CLIENT_ID = "84d6e78f634c4bf593e20545c8768c47"
    private val REDIRECT_URI = "jacx://authcallback"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginBtn: Button = findViewById(R.id.button_login)
        loginBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val signUpBtn: Button = findViewById(R.id.button_signup)
        signUpBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val resetPasswordLink: TextView = findViewById(R.id.Reset_password)
        resetPasswordLink.setOnClickListener {
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }

        val googleBtn: Button = findViewById(R.id.connect_google)
        googleBtn.setOnClickListener {
            Toast.makeText(applicationContext, "Conectando con Google", Toast.LENGTH_SHORT).show();
        }

        val spotifyBtn: Button = findViewById(R.id.connect_spotify)
        spotifyBtn.setOnClickListener {
            // Inicializamos login spotify.
            val builder = AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);
            builder.setShowDialog(false)
            // Se añaden los siguientes scopes según las funcinalidades que queremos realizar:
            // https://developer.spotify.com/documentation/web-api/reference/get-list-users-playlists
            builder.setScopes(arrayOf(
                "streaming",
                "playlist-read-private",
                "playlist-read-collaborative")
            )
            val request = builder.build()
            AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    // Sesión iniciada de forma correcta!
                    val token = response.accessToken

                    // TODO: Aquí debemos guardadr el token en la base datos.
                    // Toast.makeText(applicationContext, token, Toast.LENGTH_LONG).show();
                    // Almacenamos el token
                    MainApplication.TOKEN = token

                    // Redirigimos a la actividad principal.
                    val intentMain = Intent(this, MainActivity::class.java)
                    startActivity(intentMain)
                }
                AuthorizationResponse.Type.ERROR -> {
                    println(response.error)
                    Toast.makeText(applicationContext, "Error: " + response.error, Toast.LENGTH_LONG).show();
                }
                else -> {
                    Toast.makeText(applicationContext, "Error desconocido: " + response.error, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}