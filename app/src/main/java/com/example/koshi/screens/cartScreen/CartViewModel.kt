package com.example.koshi.screens.cartScreen

import android.health.connect.datatypes.ExerciseRoute
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koshi.data.CartRepository
import com.example.koshi.model.Plant
import com.example.koshi.model.Service
import com.example.koshi.repository.LocationRepository
import com.example.koshi.repository.LocationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
//    private val locationRepository: LocationRepository
) : ViewModel() {

//    private val _locationState = MutableStateFlow<LocationResult>(LocationResult.PermissionDenied)

    val uiState: StateFlow<CartUiState> = cartRepository.cartItems.map { items ->
        val totalPrice = items.sumOf { (it.price * it.quantity).toLong() }
        val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        val displayPrice = format.format(totalPrice)

        CartUiState(
            plantItems = items.filterIsInstance<CartItem.PlantItem>(),
            serviceItems = items.filterIsInstance<CartItem.ServiceItem>(),
            totalPrice = displayPrice,
//            location = location
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CartUiState(plantItems = emptyList(), serviceItems = emptyList(), totalPrice = "0.00")
    )

//    fun fetchLocation() {
//        viewModelScope.launch {
//            _locationState.update { LocationResult.Error("Fetching...") }
//            _locationState.update { locationRepository.getUserAddress() }
//        }
//    }


    fun addPlantToCart(plant: Plant) {
        cartRepository.addItem(CartItem.PlantItem(plant))
    }

    fun addServiceToCart(service: Service) {
        cartRepository.addItem(CartItem.ServiceItem(service))
    }

    fun removeItem(item: CartItem) {
        cartRepository.removeItem(item)
    }

    fun updateQuantity(item: CartItem, quantity: Int) {
        cartRepository.updateQuantity(item, quantity)
    }

}