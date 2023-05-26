package com.apm.jacx

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.apm.jacx.client.ApiClient
import com.apm.jacx.internalStorage.AppPreferences
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initCalendarSelector()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val password = findViewById<TextInputEditText>(R.id.signup_register_password)
        val username = findViewById<TextInputEditText>(R.id.signup_register_username)
        val mail = findViewById<TextInputEditText>(R.id.signup_register_email)
        val birthdate = findViewById<TextInputEditText>(R.id.signup_register_birthdate)
        val name = findViewById<TextInputEditText>(R.id.signup_register_name)
        val lastname = findViewById<TextInputEditText>(R.id.signup_register_lastname)

        val signUpBtn: Button = findViewById(R.id.signup_button_form)
        signUpBtn.setOnClickListener {
            if (ValidationUtils.validateUsername(username) && ValidationUtils.validatePassword(
                    password
                )
                && ValidationUtils.validateEmail(mail) && ValidationUtils.validateBirthdate(
                    birthdate
                ) && ValidationUtils.validateName(name) && ValidationUtils.validateLastname(lastname)
            ) {
                registerUser(
                    username.text?.trim().toString(),
                    password.text?.trim().toString(),
                    mail.text?.trim().toString(),
                    birthdate.text?.trim().toString(),
                    name.text?.trim().toString(),
                    lastname.text?.trim().toString()
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.cancel() // Cancelar todas las corutinas cuando se destruye la actividad
    }

    private fun initCalendarSelector() {
        val datePicker = findViewById<Button>(R.id.calendar_button)
        val selectedDate = findViewById<TextInputEditText>(R.id.signup_register_birthdate)
        val calendar: Calendar = Calendar.getInstance()

        // Formato de fecha deseado
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val dateSetListener = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            // Actualizar el calendario con la fecha seleccionada
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            // Obtener la fecha formateada
            val dateText = dateFormat.format(calendar.time)

            // Mostrar la fecha seleccionada en el EditText
            selectedDate.setText(dateText)
        }

        datePicker.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun registerUser(
        username: String,
        password: String,
        mail: String,
        birthdate: String,
        name: String,
        lastname: String
    ) {
        // Petición al backend.
        // Se debe utilizar las corrutinas de esta forma. No mediante GlobalScope.
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val btn = findViewById<Button>(R.id.signup_button_form)
                btn.visibility = View.INVISIBLE
                val spinner = findViewById<ProgressBar>(R.id.signup_button_form_spinner)
                spinner.visibility = View.VISIBLE

                val jsonBody = JSONObject().apply {
                    put("username", username)
                    put("password", password)
                    put("firstName", name)
                    put("lastName", lastname)
                    put("birthday", birthdate)
                    put("email", mail)
                }.toString()
                val responsePost = ApiClient.post("/user", jsonBody)
                Gson().fromJson(responsePost, JsonObject::class.java)

                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)

                Toast.makeText(this@RegisterActivity, "Usuario $username creado!", Toast.LENGTH_LONG).show()

            } catch (e: IOException) {
                // Manejar errores de red aquí
                Log.d("Error de red", e.toString())
                Toast.makeText(this@RegisterActivity, "Error de red", Toast.LENGTH_LONG).show()

                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                // Manejar otros errores aquí
                Log.d("Error en la peticion", e.toString())
                Toast.makeText(this@RegisterActivity, "Error en la peticion", Toast.LENGTH_LONG).show()
            } finally {
                val btn = findViewById<Button>(R.id.signup_button_form)
                btn.visibility = View.VISIBLE
                val spinner = findViewById<ProgressBar>(R.id.signup_button_form_spinner)
                spinner.visibility = View.INVISIBLE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}