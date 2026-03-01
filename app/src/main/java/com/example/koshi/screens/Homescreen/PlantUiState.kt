import com.example.koshi.model.Plant

sealed class PlantUiState {
    object Loading : PlantUiState()
    data class Success(val featuredPlants: List<Plant>) : PlantUiState()
    data class Error(val message: String) : PlantUiState()
}