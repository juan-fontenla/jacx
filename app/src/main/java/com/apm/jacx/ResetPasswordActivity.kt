package com.apm.jacx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
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

class ResetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val newPassword = findViewById<TextInputEditText>(R.id.new_password_text)
        val confirmPassword = findViewById<TextInputEditText>(R.id.confirm_password_text)

        val resetPasswordBtn: Button = findViewById(R.id.confirm)
        resetPasswordBtn.setOnClickListener {
            if (ValidationUtils.validatePassword(newPassword) && ValidationUtils.validatePassword(
                    confirmPassword
                )
            ) {
                if (confirmPassword.text?.trim().toString() == newPassword.text?.trim().toString()) {
                    changePassword(newPassword.text?.trim().toString())
                } else {
                    newPassword.setText("")
                    confirmPassword.setText("")
                    newPassword.requestFocus()
                    Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.cancel() // Cancelar todas las corutinas cuando se destruye la actividad
    }

    private fun changePassword(password: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val btn = findViewById<Button>(R.id.confirm)
                btn.visibility = View.INVISIBLE
                val spinner = findViewById<ProgressBar>(R.id.change_password_spinner)
                spinner.visibility = View.VISIBLE

                val jsonBody = JSONObject().apply {
                    put("password", password)
                }.toString()
                val responsePost = ApiClient.post("/user/password", jsonBody)
                Gson().fromJson(responsePost, JsonObject::class.java)

                val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                startActivity(intent)
            } catch (e: IOException) {
                // Manejar errores de red aquí
                Log.d("Error de red", e.toString())
                Toast.makeText(this@ResetPasswordActivity, "Error de red", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                // Manejar otros errores aquí
                Log.d("Error en la peticion", e.toString())
                Toast.makeText(this@ResetPasswordActivity, "Error en la peticion", Toast.LENGTH_LONG).show()
            } finally {
                val btn = findViewById<Button>(R.id.confirm)
                btn.visibility = View.VISIBLE
                val spinner = findViewById<ProgressBar>(R.id.change_password_spinner)
                spinner.visibility = View.INVISIBLE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}