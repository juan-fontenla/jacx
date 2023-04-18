package com.apm.jacx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapsSdkInitializedCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class DetailRouteActivity : AppCompatActivity(), OnMapsSdkInitializedCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_route)

        MapsInitializer.initialize(applicationContext, MapsInitializer.Renderer.LATEST, this)

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

    override fun onMapsSdkInitialized(renderer: MapsInitializer.Renderer) {
        when (renderer) {
            MapsInitializer.Renderer.LATEST -> Log.d("MapsDemo", "The latest version of the renderer is used.")
            MapsInitializer.Renderer.LEGACY -> Log.d("MapsDemo", "The legacy version of the renderer is used.")
        }
    }
}