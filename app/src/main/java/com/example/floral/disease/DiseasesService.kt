package com.example.floral.disease

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Multipart
import retrofit2.http.Part

interface DiseasesService {
    @Multipart
    @POST("/api/v1.0/detect-disease")
    fun detectDisease(@Part part: MultipartBody.Part) : Call<ResultResponse>


}