package com.example.koshi.screens.addressScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koshi.model.Address
import com.example.koshi.repository.LocationRepository
import com.example.koshi.repository.LocationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddressUiState(
    val savedAddresses: List<Address> = emptyList(),
    val isLoading: Boolean = false,
    val isEditorOpen: Boolean = false,
    val currentEditAddress: Address? = null,
    val error: String? = null,
    val label: String = "Home",
    // Split the address components
    val houseNumber: String = "",
    val street: String = "", // This will now represent Area/Road/Sector
    val city: String = "",
    val postalCode: String = "",
    val isAutoFilling: Boolean = false
)

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddressUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadAddresses()
    }

    private fun loadAddresses() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val list = locationRepository.getSavedAddresses()
            _uiState.update { it.copy(savedAddresses = list, isLoading = false) }
        }
    }

    fun toggleEditor(address: Address? = null){
        _uiState.update {
            it.copy(
                isEditorOpen = !it.isEditorOpen,
                currentEditAddress = address,
                label = address?.label ?: "Home",
                // When editing, we put the whole string in 'street' as we can't easily split it back
                // When creating new, both start empty
                houseNumber = "",
                street = address?.street ?: "",
                city = address?.city ?: "",
                postalCode = address?.postalCode ?: "",
                error = null
            )
        }
    }

    fun updateField(
        label: String? = null,
        houseNumber: String? = null,
        street: String? = null,
        city: String? = null,
        postalCode: String? = null
    ) {
        _uiState.update {
            it.copy(
                label = label ?: it.label,
                houseNumber = houseNumber ?: it.houseNumber,
                street = street ?: it.street,
                city = city ?: it.city,
                postalCode = postalCode ?: it.postalCode,
                error = null // Clear error on typing
            )
        }
    }

    fun useCurrentLocation() {
        viewModelScope.launch {
            _uiState.update { it.copy(isAutoFilling = true) }

            when (val result = locationRepository.getUserAddress()) {
                is LocationResult.Success -> {
                    result.fullAddress?.let { gpsAddress ->
                        _uiState.update {
                            it.copy(
                                // We ONLY auto-fill the Area/Street, NOT the house number
                                street = gpsAddress.street,
                                city = gpsAddress.city,
                                postalCode = gpsAddress.postalCode,
                                isAutoFilling = false,
                                error = null
                            )
                        }
                    }
                }
                is LocationResult.Error -> _uiState.update { it.copy(error = result.message, isAutoFilling = false) }
                LocationResult.PermissionDenied -> _uiState.update { it.copy(error = "Permission Denied", isAutoFilling = false) }
            }
        }
    }

    fun saveAddress() {
        val state = _uiState.value

        // Strict Validation
        if (state.houseNumber.isBlank()) {
            _uiState.update { it.copy(error = "House / Flat Number is required") }
            return
        }
        if (state.street.isBlank() || state.city.isBlank()) {
            _uiState.update { it.copy(error = "Area and City details cannot be empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val fullStreetAddress = if (state.currentEditAddress == null) {
                "${state.houseNumber}, ${state.street}"
            } else {
                if (state.houseNumber.isNotBlank()) "${state.houseNumber}, ${state.street}" else state.street
            }

            val newAddress = Address(
                id = state.currentEditAddress?.id ?: "",
                label = state.label,
                street = fullStreetAddress,
                city = state.city,
                postalCode = state.postalCode
            )
            locationRepository.saveAddress(newAddress)
            loadAddresses()
            toggleEditor(null)
        }
    }

    fun deleteAddress(id: String) {
        viewModelScope.launch {
            locationRepository.deleteAddress(id)
            loadAddresses()
        }
    }
}