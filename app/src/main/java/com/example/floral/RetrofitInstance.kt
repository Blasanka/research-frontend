package com.example.testfloral

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: TodoApi by lazy {

        Retrofit.Builder()
            .baseUrl("http://192.168.1.5:8001")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TodoApi::class.java)
    }
}