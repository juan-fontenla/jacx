package com.apm.jacx

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.apm.jacx.internalStorage.AppPreferences
import com.apm.jacx.util.AppVariables
import com.apm.jacx.util.AppVariables.REQUEST_CODE_GOOGLE
import com.apm.jacx.util.AppVariables.REQUEST_CODE_SPOTIFY
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationResponse


class MainActivity : AppCompatActivity() {

    private var actionBar: ActionBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionBar = supportActionBar

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener(navListener)

        supportFragmentManager.beginTransaction().replace(R.id.main_view_container, TripsFragment()).commit()    }

    fun showUpButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun hideUpButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                return supportFragmentManager.popBackStackImmediate()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val navListener = NavigationBarView.OnItemSelectedListener {
        lateinit var selectedFragment: Fragment
        when (it.itemId) {
            R.id.search -> {
                selectedFragment = TripsFragment()
            }
            R.id.album -> {
                selectedFragment = AlbumFragment()
            }
            R.id.music -> {
                selectedFragment = MusicFragment()
            }
            R.id.profile -> {
                selectedFragment = ProfileFragment()
            }
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_view_container, selectedFragment)
        transaction.commit()
        true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == REQUEST_CODE_SPOTIFY) {
            val response = AuthorizationClient.getResponse(resultCode, intent)
            handleSignInResultSpotify(response)
        }
    }
    private fun handleSignInResultSpotify(response: AuthorizationResponse) {
        when (response.type) {
            AuthorizationResponse.Type.TOKEN -> {
                // Almacenamos el token en el almacenamiento interno
                AppPreferences.TOKEN_SPOTIFY = response.accessToken
                supportFragmentManager.beginTransaction().replace(R.id.main_view_container, MusicFragment()).commit()

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

}