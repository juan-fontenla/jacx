package com.apm.jacx

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.apm.jacx.client.ApiClient
import com.apm.jacx.validations.ValidationUtils
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

class PasswordEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_email)

        val btnSend = findViewById<Button>(R.id.button_mail)
        val username = findViewById<TextInputEditText>(R.id.password_email_register_username)

        btnSend.setOnClickListener {
            if (ValidationUtils.validateName(username)) {
                sendMail(username.text?.trim().toString())
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.cancel() // Cancelar todas las corutinas cuando se destruye la actividad
    }

    private fun sendMail(username: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val btn = findViewById<Button>(R.id.button_mail)
                btn.visibility = View.INVISIBLE
                val spinner = findViewById<ProgressBar>(R.id.send_mail_spinner)
                spinner.visibility = View.VISIBLE

                val jsonBody = JSONObject().apply {}.toString()
                val responsePost = ApiClient.post("/resetPassword/$username", jsonBody)
                Gson().fromJson(responsePost, JsonObject::class.java)


                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            } catch (e: IOException) {
                // Manejar errores de red aquí
                Log.d("Error de red", e.toString())
                Toast.makeText(this@PasswordEmailActivity, "Error de red", Toast.LENGTH_LONG).show()

                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                // Manejar otros errores aquí
                Log.d("Error en la peticion", e.toString())
                Toast.makeText(this@PasswordEmailActivity, "Error en la peticion", Toast.LENGTH_LONG).show()
            } finally {
                val btn = findViewById<Button>(R.id.button_mail)
                btn.visibility = View.VISIBLE
                val spinner = findViewById<ProgressBar>(R.id.send_mail_spinner)
                spinner.visibility = View.INVISIBLE
                findViewById<TextInputEditText>(R.id.password_email_register_username).setText("")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}