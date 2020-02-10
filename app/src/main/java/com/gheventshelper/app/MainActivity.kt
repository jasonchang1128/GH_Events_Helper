package com.gheventshelper.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button;
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val city_button = findViewById(R.id.CityButton) as Button
        city_button.setOnClickListener {
            OpenCityEvents();
        }

        val road_button = findViewById(R.id.RoadButton) as Button
        road_button.setOnClickListener {
            OpenRoadEvents();
        }
    }

    fun OpenCityEvents() {
        val intent = Intent(this, CityEvents::class.java);
        startActivity(intent);
    }

    fun OpenRoadEvents() {
        val intent = Intent(this, RoadEvents::class.java);
        startActivity(intent);
    }
}
