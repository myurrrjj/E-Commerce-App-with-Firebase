package com.example.koshi.screens.ProfileScreen

import com.example.koshi.model.Address
import com.example.koshi.model.Order

data class ProfileUiState(
    val orders: List<Order> = emptyList(),
    val isLoadingOrders: Boolean = false,
    val errorMessage: String? = null
)