package com.example.koshi.screens.Homescreen

//import com.google.common.collect.ImmutableList
//import okhttp3.internal.toImmutableList
import PlantUiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koshi.model.Plant
import com.example.koshi.repository.LocationRepository
import com.example.koshi.repository.LocationResult
import com.example.koshi.repository.PlantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val plantRepository: PlantRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {


    val plantUiState: StateFlow<PlantUiState> = plantRepository.plants
    val serviceUiState: StateFlow<ServiceUiState> = plantRepository.services

    private val _locationState = MutableStateFlow<LocationResult>(LocationResult.PermissionDenied)
    val locationState = _locationState.asStateFlow()
    private val searchQuery = MutableStateFlow("")
    private val selectedCategory = MutableStateFlow("")
    val searchQueryState = searchQuery.asStateFlow()
    val selectedCategoryState = selectedCategory.asStateFlow()
    private val _hasAnimatedIn = MutableStateFlow(false)
    val isDataLoaded: StateFlow<Boolean> =
        combine(plantUiState, serviceUiState) { plants, services ->
            plants is PlantUiState.Success && services is ServiceUiState.Success
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun fetchLocation() {
        viewModelScope.launch {
            _locationState.value = LocationResult.Error("Fetching...")
            _locationState.value = locationRepository.getUserAddress()
        }
    }

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun onCategorySelected(category: String) {
        selectedCategory.value =
            if (selectedCategory.value == category) "" else category
    }

    val filteredPlants: StateFlow<ImmutableList<Plant>> =
        combine(
            plantUiState,
            searchQuery,
            selectedCategory
        ) { plantState, query, category ->

            if (plantState is PlantUiState.Success) {
                plantState.featuredPlants
                    .filter { plant ->
                        (query.isBlank() || plant.name.contains(query, ignoreCase = true)) &&
                                (category.isBlank() || plant.category == category)
                    }
                    .toImmutableList()
            } else {
                persistentListOf<Plant>()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = persistentListOf<Plant>()
        )


//    val serviceUiState:StateFlow<ServiceUiState> = plantRepository.plants


//    val plantUiState: StateFlow<PlantUiState> = plantRepository.getPlants()
//        .map<List<Plant>, PlantUiState> { PlantUiState.Success(it) }
//        .catch { emit(PlantUiState.Error(it.message ?: "An unknown error occurred.")) }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = PlantUiState.Loading
//        )

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Shop)
    val currentScreen = _currentScreen.asStateFlow()

    fun onTabSelected(screen: Screen) {
        _currentScreen.value = screen
    }

    var selectedPlantId: Int? = null
    var selectedServiceId: Int? = null


}
