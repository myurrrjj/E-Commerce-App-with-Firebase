package com.example.koshi.model

import kotlinx.serialization.Serializable

@Serializable
data class PlantDiseaseResponse(
    val diseaseName: String,
    val confidence: Float,
    val symptoms: String,
    val treatment: String,
    val isHealthy: Boolean
)
