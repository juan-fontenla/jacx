package com.apm.jacx


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.apm.jacx.client.ApiClient
import com.apm.jacx.internalStorage.AppPreferences
import com.apm.jacx.spotify.MusicViewModel
import com.apm.jacx.util.AppVariables
import com.apm.jacx.util.AppVariables.CLIENT_ID_SPOTIFY
import com.apm.jacx.util.AppVariables.REDIRECT_URI_SPOTIFY
import com.apm.jacx.util.AppVariables.REQUEST_CODE_GOOGLE
import com.apm.jacx.util.AppVariables.REQUEST_CODE_SPOTIFY
import com.apm.jacx.validations.ValidationUtils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException


class LoginActivity : AppCompatActivity() {
    private val viewModel: MusicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializamos el singleton que gestiona el almacenamiento interno:
        AppPreferences.setup(applicationContext)

        setContentView(R.layout.activity_login)

        // Si ya existe un token no es necesario volver a iniciar sesion
        if(AppPreferences.TOKEN_BD != null && AppPreferences.TOKEN_BD != ""){
            val intentMain = Intent(this, MainActivity::class.java)
            startActivity(intentMain)
        }

        val userName = findViewById<TextInputEditText>(R.id.user_name)
        val password = findViewById<TextInputEditText>(R.id.user_password)

        val loginBtn: Button = findViewById(R.id.button_login)
        loginBtn.setOnClickListener {
            if (ValidationUtils.validateName(userName) && ValidationUtils.validatePassword(password)) {
                login(userName.text?.trim(), password.text?.trim())
            }
        }

        val signUpBtn: Button = findViewById(R.id.button_signup)
        signUpBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val resetPasswordLink: TextView = findViewById(R.id.Reset_password)
        resetPasswordLink.setOnClickListener {
            val intent = Intent(this, PasswordEmailActivity::class.java)
            startActivity(intent)
        }

        // GOOGLE:
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        val googleBtn: Button = findViewById(R.id.connect_google)
        googleBtn.setOnClickListener {
            val account = GoogleSignIn.getLastSignedInAccount(this)
            if (account == null) {
                val signInIntent: Intent = mGoogleSignInClient.signInIntent
                startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE)
            } else {
                login("${account.displayName}-google", "O6EEy732JL3oPuBg")
                val intentMain = Intent(this, MainActivity::class.java)
                startActivity(intentMain)
            }
        }
        // END GOOGLE

        // SPOTIFY:
        val spotifyBtn: Button = findViewById(R.id.connect_spotify)
        spotifyBtn.setOnClickListener {
            // Inicializamos login spotify.
            val builder = AuthorizationRequest.Builder(
                CLIENT_ID_SPOTIFY,
                AuthorizationResponse.Type.TOKEN,
                REDIRECT_URI_SPOTIFY
            );
            builder.setShowDialog(false)
            // Se añaden los siguientes scopes según las funcinalidades que queremos realizar
            builder.setScopes(
                arrayOf(
                    "streaming",
                    "playlist-read-private",
                    "playlist-read-collaborative",
                    "user-read-private",
                    "user-read-email"
                )
            )
            val request = builder.build()
            AuthorizationClient.openLoginActivity(this, REQUEST_CODE_SPOTIFY, request)
        }
        // END SPOTIFY
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.cancel() // Cancelar todas las corutinas cuando se destruye la actividad
    }

    private fun login(username: CharSequence?, password: CharSequence?) {
        // Petición al backend.
        // Se debe utilizar las corrutinas de esta forma. No mediante GlobalScope.
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val loginBtn = findViewById<Button>(R.id.button_login)
                loginBtn.visibility = View.INVISIBLE
                val spinner = findViewById<ProgressBar>(R.id.login_spinner)
                spinner.visibility = View.VISIBLE

                val jsonBody = JSONObject().apply {
                    put("username", username)
                    put("password", password)
                }.toString()
                val responsePost = ApiClient.post("/login", jsonBody)
                val jsonObject: JsonObject = Gson().fromJson(responsePost, JsonObject::class.java)

                AppPreferences.TOKEN_BD = jsonObject.get("token").asString
                loadUserInformation(username.toString())

            } catch (e: IOException) {
                // Manejar errores de red aquí
                Log.d("Error de red", e.toString())
                Toast.makeText(this@LoginActivity, "Datos de acceso incorrectos", Toast.LENGTH_LONG)
                    .show()
                val loginBtn = findViewById<Button>(R.id.button_login)
                loginBtn.visibility = View.VISIBLE
                val spinner = findViewById<ProgressBar>(R.id.login_spinner)
                spinner.visibility = View.INVISIBLE
                resetInputs()
            } catch (e: Exception) {
                // Manejar otros errores aquí
                Log.d("Error en la peticion", e.toString())
                Toast.makeText(this@LoginActivity, "Datos de acceso incorrectos", Toast.LENGTH_LONG)
                    .show()
                val loginBtn = findViewById<Button>(R.id.button_login)
                loginBtn.visibility = View.VISIBLE
                val spinner = findViewById<ProgressBar>(R.id.login_spinner)
                spinner.visibility = View.INVISIBLE
                resetInputs()
            }
        }
    }

    // Carga los datos del usuario y los almacena en almacenamiento interno
    fun loadUserInformation(username: String) {
        // Petición al backend.
        // Se debe utilizar las corrutinas de esta forma. No mediante GlobalScope.
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val responseGet = ApiClient.get("/user/$username")
                AppPreferences.USER_INFORMATION = responseGet

                Log.d("Informacion de usuario", AppPreferences.USER_INFORMATION.toString())

                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            } catch (e: IOException) {
                // Manejar errores de red aquí
                Log.d("Error de red", e.toString())
                Toast.makeText(this@LoginActivity, "Datos de acceso incorrectos", Toast.LENGTH_LONG)
                    .show()
            } catch (e: Exception) {
                // Manejar otros errores aquí
                Log.d("Error en la peticion", e.toString())
                Toast.makeText(this@LoginActivity, "Datos de acceso incorrectos", Toast.LENGTH_LONG)
                    .show()

            } finally {
                val loginBtn = findViewById<Button>(R.id.button_login)
                loginBtn.visibility = View.VISIBLE
                val spinner = findViewById<ProgressBar>(R.id.login_spinner)
                spinner.visibility = View.INVISIBLE
                resetInputs()
            }
        }
    }

    private fun checkAccountAndLoginSpotify() {
        viewModel.getSpotifyUserInformation()
        viewModel.me.observe(this) {
            val email = it.email
            val displayName = it.display_name
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    // Comprobamos si existe el usuario de spotify
                    val responseGet = ApiClient.get("/user/$displayName-spotify")
                    if(responseGet.isBlank()) {
                        // Si no existe el usuario se crea
                        val jsonBody = JSONObject().apply {
                            put("username", "$displayName-spotify")
                            put("password", "O6EEy732JL3oPuBg")
                            put("firstName", displayName)
                            put("email", email)
                        }.toString()
                        ApiClient.post("/user", jsonBody)
                        Toast.makeText(this@LoginActivity, "Usuario $displayName-spotify creado!", Toast.LENGTH_LONG).show()
                    }

                    login("$displayName-spotify", "O6EEy732JL3oPuBg" )

                } catch (e: IOException) {
                    // Manejar errores de red aquí
                    Log.d("Error de red", e.toString())
                    Toast.makeText(this@LoginActivity, "Error de red", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    // Manejar otros errores aquí
                    Log.d("Error en la peticion", e.toString())
                    Toast.makeText(this@LoginActivity, "Error en la peticion", Toast.LENGTH_LONG).show()
                }
            }
        }


    }

    private fun resetInputs() {
        val userName = findViewById<TextInputEditText>(R.id.user_name)
        userName.setText("")
        val password = findViewById<TextInputEditText>(R.id.user_password)
        password.setText("")
        userName.requestFocus()
    }

    private fun checkAccountAndLoginGoogle() {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        val email = account?.email
        val displayName = account?.givenName
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Comprobamos si existe el usuario de spotify
                val responseGet = ApiClient.get("/user/$displayName-google")
                if (responseGet.isBlank()) {
                    // Si no existe el usuario se crea
                    val jsonBody = JSONObject().apply {
                        put("username", "$displayName-google")
                        put("password", "O6EEy732JL3oPuBg")
                        put("firstName", displayName)
                        put("email", email)
                    }.toString()
                    ApiClient.post("/user", jsonBody)
                    Toast.makeText(
                        this@LoginActivity,
                        "Usuario $displayName-google creado!",
                        Toast.LENGTH_LONG
                    ).show()
                }

                login("$displayName-google", "O6EEy732JL3oPuBg")

            } catch (e: IOException) {
                // Manejar errores de red aquí
                Log.d("Error de red", e.toString())
                Toast.makeText(this@LoginActivity, "Error de red", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                // Manejar otros errores aquí
                Log.d("Error en la peticion", e.toString())
                Toast.makeText(this@LoginActivity, "Error en la peticion", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == AppVariables.REQUEST_CODE_SPOTIFY) {
            val response = AuthorizationClient.getResponse(resultCode, intent)
            handleSignInResultSpotify(response)
        }

        if (requestCode == AppVariables.REQUEST_CODE_GOOGLE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)
            handleSignInResultGoogle(task)
        }
    }

    private fun handleSignInResultSpotify(response: AuthorizationResponse) {
        when (response.type) {
            AuthorizationResponse.Type.TOKEN -> {

                // Almacenamos el token en el almacenamiento interno
                Log.d("token spotify:", response.accessToken)
                AppPreferences.TOKEN_SPOTIFY = response.accessToken

                // Creamos la cuenta de usuario si no existe y hacemos login.
                checkAccountAndLoginSpotify()

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
                ).show()
            }

            else -> {
                Toast.makeText(
                    applicationContext,
                    "Error desconocido: " + response.error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun handleSignInResultGoogle(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Almacenamos el token en el almacenamiento interno
            AppPreferences.TOKEN_GOOGLE = account.idToken

            checkAccountAndLoginGoogle()

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
            ).show()
        }
    }
}

