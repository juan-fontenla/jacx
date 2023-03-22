package com.apm.jacx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
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
            Toast.makeText(applicationContext, "Conectando con Spotify", Toast.LENGTH_SHORT).show();
        }
    }
}