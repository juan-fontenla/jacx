package com.apm.jacx

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ItineraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_itinerary)

        val btnStartRoute : Button = findViewById(R.id.btn_start_route_trip_friend)
        btnStartRoute.setOnClickListener {
            Toast.makeText(this, "Iniciar ruta", Toast.LENGTH_SHORT).show();
        }

        val btnFinishRoute : Button = findViewById(R.id.btn_finish_route_trip_friend2)
        btnFinishRoute.setOnClickListener {
            Toast.makeText(this, "Finalizar ruta", Toast.LENGTH_SHORT).show();
        }

        val btnDeleteStop : ImageButton = findViewById(R.id.iB_delete_stop_present)
        btnDeleteStop.setOnClickListener {
            Toast.makeText(this, "Eliminar parada", Toast.LENGTH_SHORT).show();
        }

        val btnAddRoute : FloatingActionButton = findViewById(R.id.btn_add_route_trip_friend)
        btnAddRoute.setOnClickListener {
            Toast.makeText(this, "Añadir ruta", Toast.LENGTH_SHORT).show();
        }

        val btnSaveRoute : FloatingActionButton = findViewById(R.id.btn_save_route_trip_friend)
        btnSaveRoute.setOnClickListener {
            Toast.makeText(this, "Guardar ruta", Toast.LENGTH_SHORT).show();
        }

    }
}