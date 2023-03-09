package com.apm.jacx

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener(navListener)

        supportFragmentManager.beginTransaction().replace(R.id.main_view_container, TripsFragment()).commit()
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
        supportFragmentManager.beginTransaction().replace(R.id.main_view_container, selectedFragment).commit()
        true
    }
}