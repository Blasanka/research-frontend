package com.example.floral.disease

import com.google.gson.annotations.SerializedName

data class DiseaseResultResponse(
    @SerializedName("filename") val fileName: String,
    val flowerName: String,
    val identifiedDisease: String,
    val diseaseDescription: String,
    val accuracyLevel: Double
)
