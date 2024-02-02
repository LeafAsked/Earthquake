package com.example.earthquake
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Feature(
    val properties: Properties,
    val geometry: Geometry
) : Parcelable, Comparable<Feature> {
    override fun compareTo(other: Feature): Int {
        return (other.properties.mag * 100).toInt() - (this.properties.mag * 100).toInt()
    }
}
