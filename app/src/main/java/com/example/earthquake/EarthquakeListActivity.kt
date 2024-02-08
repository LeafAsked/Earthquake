package com.example.earthquake

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.earthquake.RetrofitHelper.getInstance
import com.example.earthquake.databinding.ActivityEarthquakeListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EarthquakeListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEarthquakeListBinding
    private lateinit var earthquakes : FeatureCollection

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
        earthquakes = gson.fromJson(jsonString, type)

        Log.d(TAG,"LoadEarthquakes $earthquakes")


        val retrofit = getInstance()
        val earthquakeService = retrofit.create(EarthquakeService::class.java)
        val earthquakeCall = earthquakeService.getEarthquakeDataPastDay()
        earthquakeCall.enqueue(object : Callback<FeatureCollection> {
            override fun onResponse(
                call: Call<FeatureCollection>,
                response: Response<FeatureCollection>
            ) {
                // this is where you get your data
                // this is where you will set up your adapter for recyclerview
                // don't forget a null check before trying to use the data
                // response.body() contains the object in the <> after Response
                earthquakes = response.body()!!
                earthquakes.features = earthquakes.features
                    .sortedBy { -it.properties.time }
                    .filter{it.properties.mag >= 1.0}
                refreshList()
                Log.d(TAG, "onResponse: ${earthquakes.features}")
            }

            override fun onFailure(call: Call<FeatureCollection>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun sortByName() {
        earthquakes.features = earthquakes.features.sortedBy { -it.properties.mag }
    }

    fun sortByRanking() {
        earthquakes.features = earthquakes.features.sortedBy { -it.properties.time }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_game -> {
                sortByName()
                refreshList()
                true
            }
            R.id.help -> {
                sortByRanking()
                refreshList()
                true
            }
            R.id.alert -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Earthquake Data")
                builder.setMessage("Purple: Significant (>6.5)\nRed: Large (4.5-6.5)\nOrange: " +
                        "Moderate (2.5-4.5)\nBlue: Small (1.0-2.5)\n\nThe number represents " +
                        "the magnitude of the earthquake.")
                builder.setPositiveButton("Stay safe out there") { dialog, which ->
                    Toast.makeText(applicationContext,
                        android.R.string.ok, Toast.LENGTH_SHORT).show()
                }
                builder.show()
                refreshList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun refreshList() {
        val adapter = EarthquakeAdapter(earthquakes)


        binding.EarthquakeListEarthquake.layoutManager = LinearLayoutManager(this)
        binding.EarthquakeListEarthquake.adapter = adapter
    }
}