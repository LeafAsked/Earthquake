package com.example.earthquake

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import java.util.Date

class EarthquakeAdapter(private var earthquakeList: FeatureCollection) : RecyclerView.Adapter<EarthquakeAdapter.ViewHolder>() {
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textViewMagnitude: TextView
            val textViewLocation: TextView
            val textViewTime: TextView
            val layout: ConstraintLayout

            init {
                // Define click listener for the ViewHolder's View
                textViewMagnitude = view.findViewById(R.id.textView_earthquakeItem_magnitude)
                textViewLocation = view.findViewById(R.id.textView_earthquakeItem_location)
                textViewTime = view.findViewById(R.id.textView_itemEarthquake_time)
                layout = view.findViewById(R.id.layout_item_earthquake)

            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EarthquakeAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_earthquake, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: EarthquakeAdapter.ViewHolder, position: Int) {
        val earthquake = earthquakeList.features[position]
        val context = holder.layout.context
        holder.textViewMagnitude.text = String.format("%.2f", earthquake.properties.mag)
        holder.textViewLocation.text = earthquake.properties.place
        holder.textViewTime.text = Date(earthquake.properties.time).toString()

        holder.layout.setOnClickListener {
            val registrationIntent = Intent(context, EarthquakeMapActivity::class.java)
            // 2. Optionally add information to send with the intent
            registrationIntent.putExtra(EarthquakeMapActivity.EXTRA_EARTHQUAKE, earthquake)
            // 3. launch the new activity using the intent
            context.startActivity(registrationIntent)
            Toast.makeText(context, "You clicked this ${earthquake.properties.title}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = earthquakeList.features.size
}