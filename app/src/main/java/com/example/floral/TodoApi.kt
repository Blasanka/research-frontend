package com.example.floral

import com.example.floral.utils.Cluster
import com.example.floral.utils.PriceRange
import com.example.testfloral.FavList
import com.example.testfloral.RacAlgorithm
import com.example.testfloral.Todo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TodoApi {

    @GET("/api/v1.0/demand/3")
    suspend fun getTodos(): Response<List<Todo>>

    @GET("/cluster/")
    suspend fun getCluster(): Response<Cluster>

    @GET("/price_range/")
    suspend fun getPriceRange(): Response<PriceRange>

    //3,4,7
    @GET("/api/v1.0/recommendations/{Id}")
    suspend fun getRec(@Path("Id") employeeId: String): Response<List<RacAlgorithm>>

    @GET("/api/v1.0/fav/{Id}")
    suspend fun getFav(): Response<List<FavList>>

    @GET("/api/v1.0/writer/{Id}")
    suspend fun getwri(@Path("Id") employeeId: String): Response<List<FavList>>

}


