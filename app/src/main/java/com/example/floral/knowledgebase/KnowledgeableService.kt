package com.example.floral.knowledgebase

import com.example.floral.knowledgebase.data.DiseaseDetails
import com.example.floral.knowledgebase.data.ClusterMap
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface KnowledgeableService {
    @POST("/api/v1.0/disease-help")
    fun getHelpForDisease(@Body details: DiseaseDetails) : Call<ClusterMap>

}