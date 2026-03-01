package com.example.koshi.model

import androidx.annotation.Keep
import com.example.koshi.R
import java.text.NumberFormat
import java.util.Locale

@Keep
data class Service(
    val id: Int=0,
    val name: String="",
    val description: String="",
    val price: Int=0,
    val imageUrls: List<ImageUrls> = emptyList()
){

    val displayPrice: String
        get() {
            val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
            return format.format(price/1.00)
        }
}

//val availableServices = listOf(
//    Service(
//        1,
//        "Plant Repotting",
//        "We'll give your plant a new home in a bigger pot with fresh soil.",
//        1000,
//        R.drawable.ic_launcher_background
//    ),
//    Service(
//        2,
//        "Pest Control",
//        "Organic and safe pest control solutions for your beloved plants.",
//        1000,
//        R.drawable.ic_launcher_background
//    ),
//    Service(
//        3,
//        "Plant Health Checkup",
//        "A complete health assessment to ensure your plant is thriving.",
//        1200,
//        R.drawable.ic_launcher_background
//    ),
//    Service(
//        4,
//        "Vacation Care",
//        "We'll take care of your plants while you're away.",
//        1000,
//        R.drawable.ic_launcher_background
//    )
//)
