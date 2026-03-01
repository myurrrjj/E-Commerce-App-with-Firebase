package com.example.koshi.screens.cartScreen

import com.example.koshi.model.Plant
import com.example.koshi.model.Service

sealed interface CartItem {
    val id: Any
    val name: String
    val price: Int
    var quantity: Int
    val imageUrl: String?


    data class PlantItem(val plant: Plant, override var quantity: Int = 1) : CartItem {
        override val id: Any
            get() = plant.id
        override val name: String
            get() = plant.name
        override val price: Int
            get() = plant.price
        override val imageUrl: String?
            get() = plant.images.firstOrNull()?.thumbnail
    }

        data class ServiceItem(val service: Service, override var quantity: Int = 1) : CartItem {
            override val id: Any get() = service.id
            override val name: String get() = service.name

            override val price: Int get() = service.price
            override val imageUrl: String? get() = service.imageUrls.firstOrNull()?.thumbnail
        }


    }



