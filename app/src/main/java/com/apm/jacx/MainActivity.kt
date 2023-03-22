package com.apm.jacx

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView


class MainActivity : AppCompatActivity() {

    private var actionBar: ActionBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionBar = supportActionBar

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener(navListener)

        supportFragmentManager.beginTransaction().replace(R.id.main_view_container, TripsFragment()).commit()
    }

    fun showUpButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun hideUpButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                Log.d("BACK_STACK", supportFragmentManager.backStackEntryCount.toString())
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
            R.id.explore -> {
                selectedFragment = ExploreFragment()
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
}