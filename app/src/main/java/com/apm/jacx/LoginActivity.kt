package com.apm.jacx


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.apm.jacx.internalStorage.AppPreferences
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.spotify.sdk.android.auth.AccountsQueryParameters.CLIENT_ID
import com.spotify.sdk.android.auth.AccountsQueryParameters.REDIRECT_URI
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE


class LoginActivity : AppCompatActivity() {
    private val REQUEST_CODE_GOOGLE = 45897640

    private val REQUEST_CODE_SPOTIFY = 65290045
    private val CLIENT_ID_SPOTIFY = "84d6e78f634c4bf593e20545c8768c47"
    private val REDIRECT_URI_SPOTIFY = "jacx://authcallback"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializamos el singleton que gestiona el almacenamiento interno:
        AppPreferences.setup(applicationContext)

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

        // GOOGLE:
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        val googleBtn: Button = findViewById(R.id.connect_google)
        googleBtn.setOnClickListener {
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE)
        }
        // END GOOGLE

        // SPOTIFY:
        val spotifyBtn: Button = findViewById(R.id.connect_spotify)
        spotifyBtn.setOnClickListener {
            // Inicializamos login spotify.
            val builder = AuthorizationRequest.Builder(CLIENT_ID_SPOTIFY, AuthorizationResponse.Type.TOKEN, REDIRECT_URI_SPOTIFY);
            builder.setShowDialog(false)
            // Se añaden los siguientes scopes según las funcinalidades que queremos realizar:
            // https://developer.spotify.com/documentation/web-api/reference/get-list-users-playlists
            builder.setScopes(arrayOf(
                "streaming",
                "playlist-read-private",
                "playlist-read-collaborative")
            )
            val request = builder.build()
            AuthorizationClient.openLoginActivity(this, REQUEST_CODE_SPOTIFY, request)
        }
        // END SPOTIFY
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == REQUEST_CODE_SPOTIFY) {
            val response = AuthorizationClient.getResponse(resultCode, intent)
            handleSignInResultSpotify(response)
        }

        if (requestCode == REQUEST_CODE_GOOGLE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)
            handleSignInResultGoogle(task);
        }
    }

    private fun handleSignInResultSpotify(response: AuthorizationResponse) {
        when (response.type) {
            AuthorizationResponse.Type.TOKEN -> {

                // Almacenamos el token en el almacenamiento interno
                Log.d( "token spotify:", response.accessToken);
                AppPreferences.TOKEN_SPOTIFY = response.accessToken;

                // Redirigimos a la actividad principal.
                val intentMain = Intent(this, MainActivity::class.java)
                startActivity(intentMain)
            }
            AuthorizationResponse.Type.ERROR -> {
                println(response.error)
                Toast.makeText(
                    applicationContext,
                    "Error: " + response.error,
                    Toast.LENGTH_LONG
                ).show();
            }
            else -> {
                Toast.makeText(
                    applicationContext,
                    "Error desconocido: " + response.error,
                    Toast.LENGTH_LONG
                ).show();
            }
        }
    }
    private fun handleSignInResultGoogle(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Toast.makeText(
                applicationContext,
                "Sesion iniciada: " + account,
                Toast.LENGTH_LONG
            ).show();

            // Almacenamos el token en el almacenamiento interno
            AppPreferences.TOKEN_GOOGLE = account.idToken;

            // Signed in successfully, show authenticated UI.
            val intentMain = Intent(this, MainActivity::class.java)
            startActivity(intentMain)

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(
                applicationContext,
                "Error desconocido: " + e.message,
                Toast.LENGTH_LONG
            ).show();
        }
    }
}

