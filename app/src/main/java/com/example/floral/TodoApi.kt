package com.example.testfloral

import com.example.floral.utils.Cluster
import com.example.floral.utils.PriceRange
import retrofit2.Response
import retrofit2.http.GET

interface TodoApi {

    @GET("/api/v1.0/demand/3")
    suspend fun getTodos(): Response<List<Todo>>

    @GET("/cluster/")
    suspend fun getCluster(): Response<Cluster>

    @GET("/price_range/")
    suspend fun getPriceRange(): Response<PriceRange>

    //3,4,7
    @GET("/api/v1.0/recommendations/3")
    suspend fun getRec(): Response<List<RacAlgorithm>>

    @GET("/api/v1.0/fav/3")
    suspend fun getFav(): Response<List<FavList>>

}