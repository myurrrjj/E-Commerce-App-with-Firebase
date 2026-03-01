package com.example.koshi.screens.cartScreen

import android.location.Location
import com.example.koshi.repository.LocationResult

data class CartUiState(
    val plantItems: List<CartItem.PlantItem>,
    val serviceItems: List<CartItem.ServiceItem>,
    val totalPrice: String = "₹0.00",
//    val location : LocationResult = LocationResult.PermissionDenied
)
