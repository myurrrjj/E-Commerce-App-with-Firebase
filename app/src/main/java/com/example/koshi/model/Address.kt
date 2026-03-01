package com.example.koshi.model

import androidx.annotation.Keep

@Keep
data class Address (
    val id:String="",
    val label:String="",
    val street:String="",
    val city:String="",
    val postalCode:String=""

)