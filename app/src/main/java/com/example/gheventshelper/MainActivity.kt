package com.example.gheventshelper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button;
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val city_button = findViewById(R.id.CityButton) as Button
        city_button.setOnClickListener {
            OpenCityEvents();
        }
    }

    fun OpenCityEvents() {
        val intent = Intent(this, CityEvents::class.java);
        startActivity(intent);
    }
}
