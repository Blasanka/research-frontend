package com.samadi.uber_car_animation_android.utils

import android.content.Context
import android.graphics.*
import com.example.floral.R
import com.google.android.gms.maps.model.LatLng

import kotlin.math.abs
import kotlin.math.atan

object MapUtils {
    fun getListOfLocations(): ArrayList<LatLng> {
        val locationList = ArrayList<LatLng>()
        locationList.add(LatLng(6.9907, 79.8932
        ))
        locationList.add(LatLng(7.1247, 79.8750
        ))
        locationList.add(LatLng(7.1725, 79.8853
        ))
        locationList.add(LatLng(7.2008, 79.8737
        ))
        locationList.add(LatLng(7.2657, 79.8591
        ))
        locationList.add(LatLng(7.2838, 79.8578
        ))
        locationList.add(LatLng(7.3493, 79.8353
        ))
        locationList.add(LatLng(7.4239, 79.8353
        ))
        locationList.add(LatLng(7.4573, 79.8254
        ))
        locationList.add(LatLng(7.5620, 79.8017
        ))



        return locationList
    }
    fun getOriginDestinationMarkerBitmap(): Bitmap {
        val height = 80
        val width = 80
        val bitmap = Bitmap.createBitmap(height, width, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), paint)
        return bitmap
    }
    fun getCarBitmap(context: Context): Bitmap {
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_car)
        return Bitmap.createScaledBitmap(bitmap, 200, 400, false)
    }

    fun getRotation(start: LatLng, end: LatLng): Float {
        val latDifference: Double = abs(start.latitude - end.latitude)
        val lngDifference: Double = abs(start.longitude - end.longitude)
        var rotation = -1F
        when {
            start.latitude < end.latitude && start.longitude < end.longitude -> {
                rotation = Math.toDegrees(atan(lngDifference / latDifference)).toFloat()
            }
            start.latitude >= end.latitude && start.longitude < end.longitude -> {
                rotation = (90 - Math.toDegrees(atan(lngDifference / latDifference)) + 90).toFloat()
            }
            start.latitude >= end.latitude && start.longitude >= end.longitude -> {
                rotation = (Math.toDegrees(atan(lngDifference / latDifference)) + 180).toFloat()
            }
            start.latitude < end.latitude && start.longitude >= end.longitude -> {
                rotation =
                    (90 - Math.toDegrees(atan(lngDifference / latDifference)) + 270).toFloat()
            }
        }
        return rotation
    }
}