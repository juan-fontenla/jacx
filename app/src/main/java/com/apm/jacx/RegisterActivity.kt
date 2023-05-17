package com.apm.jacx

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.apm.jacx.validations.ValidationUtils
import com.google.android.material.textfield.TextInputEditText

class RegisterActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val password = findViewById<TextInputEditText>(R.id.register_password)
        val userName = findViewById<TextInputEditText>(R.id.register_name)
        val mail = findViewById<TextInputEditText>(R.id.register_mail)
        val birthdate = findViewById<TextInputEditText>(R.id.register_birthdate)

        val signUpBtn: Button = findViewById(R.id.button_signup3)
        signUpBtn.setOnClickListener {
            if (ValidationUtils.validateName(userName) && ValidationUtils.validatePassword(password)
                && ValidationUtils.validateEmail(mail) && ValidationUtils.validateBirthdate(birthdate))
            {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    /** Called when the user taps the Send button */
    fun sendMessage(view: View) {
        // Do something in response to button
    }
}