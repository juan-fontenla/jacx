package com.apm.jacx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class DetailRouteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_route)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation_trip)
        bottomNav.setOnItemSelectedListener(navListener)

        supportFragmentManager.beginTransaction().replace(R.id.main_view_container_trip, DetailsTripsFragment()).commit()
    }

    private val navListener = NavigationBarView.OnItemSelectedListener {
        lateinit var selectedFragment: Fragment
        when (it.itemId) {
            R.id.friend_trip -> {
                selectedFragment = TripFriendFragment()
            }
            R.id.music_trip -> {
                selectedFragment = TripMusicFragment()
            }
            R.id.album_trip -> {
                selectedFragment = TripAlbumFragment()
            }

        }
        supportFragmentManager.beginTransaction().replace(R.id.main_view_container_trip, selectedFragment).commit()
        true
    }
}