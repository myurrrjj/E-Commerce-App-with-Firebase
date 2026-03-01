package com.example.koshi.repository

import PlantUiState
import android.app.Application
import coil.Coil.imageLoader
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.koshi.model.Plant
import com.example.koshi.model.Service
import com.example.koshi.screens.Homescreen.ServiceUiState
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

interface PlantRepository {
    val plants: StateFlow<PlantUiState>
    val services: StateFlow<ServiceUiState>
}

@Singleton
class OfflinePlantRepository @Inject constructor(
    private val applicationScope: CoroutineScope,
) : PlantRepository {

    private val db = Firebase.firestore

    private val _plants = MutableStateFlow<PlantUiState>(PlantUiState.Loading)
    override val plants = _plants.asStateFlow()

    private val _services = MutableStateFlow<ServiceUiState>(ServiceUiState.Loading)
    override val services = _services.asStateFlow()
//    private val _services = MutableStateFlow<>

    init {
        applicationScope.launch {
            val collection = db.collection("plants")

            collection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _plants.value =
                        PlantUiState.Error(error.message ?: "An unknown error occurred.")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val plantList = snapshot.toObjects(Plant::class.java)

                    _plants.value = PlantUiState.Success(plantList)
//                    preloadPlantImages(plantList)

                }
            }
        }
        applicationScope.launch {
            val collection = db.collection("services")
            collection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _services.value =
                        ServiceUiState.Error(error.message ?: "An unknown error occurred.")
                    return@addSnapshotListener

                }
                if (snapshot != null) {
                    val serviceList = snapshot.toObjects(Service::class.java)
                    _services.value = ServiceUiState.Success(serviceList)
//                    preloadServicesImages(serviceList)
                }
            }
        }
    }


}

