package com.apm.jacx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.apm.jacx.validations.ValidationUtils
import com.google.android.material.textfield.TextInputEditText

class ResetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val newPassword = findViewById<TextInputEditText>(R.id.new_password_text)
        val confirmPassword = findViewById<TextInputEditText>(R.id.confirm_password_text)

        val resetPasswordBtn: Button = findViewById(R.id.confirm)
        resetPasswordBtn.setOnClickListener {
            if (ValidationUtils.validatePassword(newPassword) && ValidationUtils.validatePassword(confirmPassword)) {
                Toast.makeText(applicationContext, newPassword.text, Toast.LENGTH_SHORT).show();
                Toast.makeText(applicationContext, confirmPassword.text, Toast.LENGTH_SHORT).show();

                //TODO: Añadir peticion back
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}