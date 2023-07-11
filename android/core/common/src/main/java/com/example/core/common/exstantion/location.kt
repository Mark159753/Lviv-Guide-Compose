package com.example.core.common.exstantion

import android.content.Context
import android.location.Location
import com.example.core.common.R
import kotlin.math.roundToInt

/**
 * Returns distance to in meters
 * */
fun Location.distanceTo(lat:Double, lon:Double):Float{
    val locationTo = Location("locationTo").also {
        it.latitude = lat
        it.longitude = lon
    }
    return distanceTo(locationTo)
}

fun Location.distanceToFormatted(lat:Double, lon:Double, context: Context, maxInKm:Int = 999):String{
    val distance = distanceTo(lat, lon).roundToInt()
    val distanceInKm = (distance / 1000.0)
    return when{
        distance < 1000 -> context.getString(R.string.location_meters_value, distance.toString())
        distanceInKm < maxInKm -> context.getString(R.string.location_kilometers_value, String.format("%.1f", distanceInKm))
        else -> context.getString(R.string.location_unspecified_value)
    }
}

fun Location?.safeDistanceToFormatted(lat:Double, lon:Double, context: Context, maxInKm:Int = 999):String{
    return this?.distanceToFormatted(lat, lon, context, maxInKm)
        ?: context.getString(R.string.location_unspecified_value)
}