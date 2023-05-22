package com.apm.jacx.validations

import android.util.Patterns
import androidx.core.content.res.TypedArrayUtils.getText
import com.apm.jacx.R
import com.apm.jacx.R.*
import com.google.android.material.textfield.TextInputEditText

internal object ValidationUtils {

    fun validatePassword(password: TextInputEditText): Boolean {
        return if (password.text.isNullOrBlank()) {
            password.error = "Es necesario introducir una contraseña"
            password.requestFocus();
            false
        } else {
            password.error = null
            true
        }
    }

    fun validateUsername(userName: TextInputEditText): Boolean {
        return if (userName.text.isNullOrBlank()) {
            userName.error = "Es necesario introducir un nombre de usuario"
            userName.requestFocus();
            false
        } else {
            userName.error = null
            true
        }
    }

    fun validateName(userName: TextInputEditText): Boolean {
        return if (userName.text.isNullOrBlank()) {
            userName.error = "Es necesario introducir un nombre"
            userName.requestFocus();
            false
        } else {
            userName.error = null
            true
        }
    }

    fun validateLastname(userName: TextInputEditText): Boolean {
        return if (userName.text.isNullOrBlank()) {
            userName.error = "Es necesario introducir los apellidos"
            userName.requestFocus();
            false
        } else {
            userName.error = null
            true
        }
    }

    fun validateEmail(email: TextInputEditText): Boolean {
        return if (email.text.isNullOrBlank()) {
            email.error = "Es necesario introducir un correo electrónico"
            email.requestFocus();
            false
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email.text).matches()) {
            email.error = "El formato es incorrecto. Ej: a@mail.com"
            email.requestFocus();
            false
        } else {
            email.error = null
            true
        }
    }

    fun validateBirthdate(birthdate: TextInputEditText): Boolean {
        return if (birthdate.text.isNullOrBlank()) {
            birthdate.error = "Es necesario introducir una fecha de nacimiento"
            birthdate.requestFocus();
            false
        } else {
            birthdate.error = null
            true
        }
    }
}