package com.example.earthquake

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.earthquake.databinding.ActivityEarthquakeListBinding

class EarthquakeListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEarthquakeListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEarthquakeListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}