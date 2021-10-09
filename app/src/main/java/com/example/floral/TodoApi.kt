package com.example.floral

import com.example.testfloral.FavList
import com.example.testfloral.RacAlgorithm
import com.example.testfloral.Todo
import retrofit2.Response
import retrofit2.http.GET

interface TodoApi {

    @GET("/api/v1.0/demand/3")
    suspend fun getTodos(): Response<List<Todo>>

    //3,4,7
    @GET("/api/v1.0/recommendations/7")
    suspend fun getRec(): Response<List<RacAlgorithm>>

    @GET("/api/v1.0/fav/3")
    suspend fun getFav(): Response<List<FavList>>

    @GET("/api/v1.0/writer/7::2081::7::978298504")
    suspend fun getwri(): Response<List<FavList>>

}