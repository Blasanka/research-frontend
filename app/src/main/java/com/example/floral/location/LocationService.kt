package com.example.floral.location

import com.example.floral.knowledgebase.data.ClusterMap
import retrofit2.Call
import retrofit2.http.GET

interface LocationService {
    @GET("/map")
    fun getClusterMap() : Call<ClusterMap>

}