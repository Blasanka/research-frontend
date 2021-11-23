package com.example.floral.disease

import android.Manifest

object Constants {
    const val TAG = "camerax"
    const val FILE_NAME_FORMAT = "yy-MM-dd-HH-mm-ss-SSS"
    const val REQUEST_CODE_PERMISSION = 123
    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    const val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE: Int = 1888
    const val GALLERY_IMAGE_ACTIVITY_REQUEST_CODE: Int = 1000

    const val BASE_URL = "https://flower-disease-cnn.herokuapp.com/"
    const val LOCATION_BASE_URL = "https://geo-spatial-data-analysis.herokuapp.com/"
}