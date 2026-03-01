package com.example.koshi.screens.Homescreen

import com.example.koshi.model.Service

sealed class ServiceUiState{
    object Loading : ServiceUiState()
    data class Success(val featuredServices: List<Service>) : ServiceUiState()
    data class Error(val message: String) : ServiceUiState()
}