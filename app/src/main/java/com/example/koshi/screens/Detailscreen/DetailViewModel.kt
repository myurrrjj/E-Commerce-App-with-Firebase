package com.example.koshi.screens.Detailscreen

import androidx.lifecycle.ViewModel
import com.example.koshi.model.Plant
import com.example.koshi.model.Service
import com.example.koshi.repository.PlantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val plantRepository: PlantRepository
) : ViewModel() {

    private val _selectedPlant = MutableStateFlow<Plant?>(null)
    val selectedPlant = _selectedPlant.asStateFlow()

    private val _selectedService = MutableStateFlow<Service?>(null)
    val selectedService = _selectedService.asStateFlow()

    fun setSelectedPlant(plant: Plant) {
        _selectedPlant.value = plant
    }

    fun setSelectedService(service: Service) {
        _selectedService.value = service
    }

    fun getPlantById(id: Int?) {

    }

//    fun getServiceById(id: Int?) {
//        _selectedService.value = availableServices.find { it.id == id }
//    }
}
