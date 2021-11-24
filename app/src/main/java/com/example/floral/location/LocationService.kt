package com.example.floral.location

import com.example.floral.knowledgebase.data.ClusterMap
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationService {
    @GET("/map")
    fun getClusterMap(@Query("lat") lat: Double,
                      @Query("long") long: Double) : Call<ClusterMap>

}