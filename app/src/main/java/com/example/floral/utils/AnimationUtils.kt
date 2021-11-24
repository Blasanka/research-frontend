package com.samadi.uber_car_animation_android.utils

import android.animation.ValueAnimator


import android.view.animation.LinearInterpolator

import com.google.android.gms.maps.model.Polyline



object AnimationUtils {

    fun polylineAnimator(): ValueAnimator {
        val valueAnimator = ValueAnimator.ofInt(0, 100)
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = 4000
        return valueAnimator
    }
    fun carAnimator(): ValueAnimator {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = 3000
        valueAnimator.interpolator = LinearInterpolator()
        return valueAnimator
    }


    }
