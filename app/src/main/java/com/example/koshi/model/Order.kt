package com.example.koshi.model

import androidx.annotation.Keep

@Keep
data class Order(
    val id: String = "",
    val orderDate: Long = 0L,
    val totalAmount: Double = 0.0,
    val itemsSummary: String = "No items",
    val status: String = "Pending"
)