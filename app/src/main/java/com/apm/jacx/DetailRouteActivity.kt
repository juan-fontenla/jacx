package com.apm.jacx

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapsSdkInitializedCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"
private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

class DetailRouteActivity : AppCompatActivity(), OnMapsSdkInitializedCallback {

    private var userPositionServiceBound = false
    private var userPositionService: UserPositionService? = null
    private lateinit var userPositionBroadcastReceiver: UserPositionBroadcastReceiver
    private lateinit var sharedPreferences: SharedPreferences
    private val userPositionServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as UserPositionService.LocalBinder
            userPositionService = binder.service
            userPositionServiceBound = true
            if (foregroundPermissionApproved()) {
                userPositionService?.subscribeToLocationUpdates() ?: Log.d(TAG, "Service Not Bound")
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            userPositionService = null
            userPositionServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_route)

        MapsInitializer.initialize(applicationContext, MapsInitializer.Renderer.LATEST, this)

        userPositionBroadcastReceiver = UserPositionBroadcastReceiver()

        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation_trip)
        bottomNav.setOnItemSelectedListener(navListener)

        supportFragmentManager.beginTransaction().replace(R.id.main_view_container_trip, DetailsTripsFragment()).commit()

        if (!foregroundPermissionApproved()) {
            requestForegroundPermissions()
        }
    }

    override fun onStart() {
        super.onStart()

        val serviceIntent = Intent(this, UserPositionService::class.java)
        bindService(serviceIntent, userPositionServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            userPositionBroadcastReceiver,
            IntentFilter(
                UserPositionService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
            userPositionBroadcastReceiver
        )
        super.onPause()
    }

    override fun onStop() {
        if (userPositionServiceBound) {
            unbindService(userPositionServiceConnection)
            userPositionServiceBound = false
        }

        super.onStop()
    }

    /*
    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult ?: return
                    for (location in locationResult.locations) {
                        Log.d("LOCATION", location.toString())
                        val lat = location.latitude
                        val lng = location.longitude
                        val detailsTripsFragment = supportFragmentManager.findFragmentById(R.id.main_view_container_trip) as DetailsTripsFragment
                        detailsTripsFragment.updateLocation(lat, lng)
                    }
                }
            },
            null
        )
    }
    */


    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            ACCESS_FINE_LOCATION
        )
    }

    private fun requestForegroundPermissions() {
        val provideRationale = foregroundPermissionApproved()

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
            Snackbar.make(
                findViewById(R.id.relativeLayout),
                R.string.permission_rationale,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.ok) {
                    // Request permission
                    ActivityCompat.requestPermissions(
                        this@DetailRouteActivity,
                        arrayOf(ACCESS_FINE_LOCATION),
                        REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
                    )
                }
                .show()
        } else {
            Log.d(TAG, "Request foreground only permission")
            ActivityCompat.requestPermissions(
                this@DetailRouteActivity,
                arrayOf(ACCESS_FINE_LOCATION),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionResult")

        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    // If user interaction was interrupted, the permission request
                    // is cancelled and you receive empty arrays.
                    Log.d(TAG, "User interaction was cancelled.")

                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    // Permission was granted.
                    userPositionService?.subscribeToLocationUpdates()

                else -> {
                    // Permission denied.

                    Snackbar.make(
                        findViewById(R.id.relativeLayout),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.settings) {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                BuildConfig.APPLICATION_ID,
                                null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        .show()
                }
            }
        }
    }

    private val navListener = NavigationBarView.OnItemSelectedListener {
        lateinit var selectedFragment: Fragment
        when (it.itemId) {
            R.id.map_view -> {
                selectedFragment = MapFragment()
            }
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

    private inner class UserPositionBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val location = intent.getParcelableExtra<Location>(
                UserPositionService.EXTRA_LOCATION
            )

            if (location != null) {
                Log.d("LOCATION", location.toString())
                val lat = location.latitude
                val lng = location.longitude
                val detailsTripsFragment = supportFragmentManager.findFragmentById(R.id.main_view_container_trip) as DetailsTripsFragment
                detailsTripsFragment.updateLocation(lat, lng)
            }
        }
    }
}