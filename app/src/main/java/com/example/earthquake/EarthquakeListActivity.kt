package com.example.earthquake

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.earthquake.databinding.ActivityEarthquakeListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EarthquakeListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEarthquakeListBinding

    companion object {
        const val TAG = "EarthquakesListActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEarthquakeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val inputStream = resources.openRawResource(R.raw.earthquakes)
        val jsonString = inputStream.bufferedReader().use {
            it.readText()
        }
        Log.d(TAG, "OnCreate: jsonString $jsonString")
        val gson = Gson()
        val type = object: TypeToken<FeatureCollection>() {
        }.type
        val earthquakes = gson.fromJson<FeatureCollection>(jsonString, type)

        Log.d(TAG,"LoadEarthquakes $earthquakes")
    }
}