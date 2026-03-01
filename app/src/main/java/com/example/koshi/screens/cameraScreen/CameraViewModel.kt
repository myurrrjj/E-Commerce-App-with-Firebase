package com.example.koshi.screens.cameraScreen

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koshi.dependency.DiseaseRepositoryModule
import com.example.koshi.model.PlantDiseaseResponse
import com.example.koshi.repository.DiseaseRepository
import com.google.maps.android.ktx.model.cameraPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


sealed interface CameraUiState {
    data object Idle : CameraUiState
    data object Loading : CameraUiState
    data class Success(val result: PlantDiseaseResponse, val imageUri: Uri) : CameraUiState
    data class Error(val message: String) : CameraUiState
}
@HiltViewModel
class CameraViewModel @Inject constructor(private val diseaseRepository: DiseaseRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<CameraUiState>(CameraUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun onPhotoCaptured(file: File, uri: Uri) {
        _uiState.value = CameraUiState.Loading
        viewModelScope.launch {
            delay(1500)

            val mockScenarios = listOf(
                // Scenario A: Healthy Plant (Green UI)
                PlantDiseaseResponse(
                    diseaseName = "Healthy Plant",
                    confidence = 0.99f,
                    symptoms = "None",
                    treatment = "None",
                    isHealthy = true
                ),
                // Scenario B: Tomato Early Blight (Red UI)
                PlantDiseaseResponse(
                    diseaseName = "Tomato Early Blight",
                    confidence = 0.94f,
                    symptoms = "Dark spots on lower leaves, concentric rings, yellowing tissue.",
                    treatment = "Remove infected leaves immediately. Apply copper-based fungicide. Improve air circulation around the plant.",
                    isHealthy = false
                ),
                // Scenario C: Potato Late Blight (Red UI, different text)
                PlantDiseaseResponse(
                    diseaseName = "Potato Late Blight",
                    confidence = 0.88f,
                    symptoms = "Water-soaked lesions on leaves. White fungal growth on undersides in humid weather.",
                    treatment = "Apply fungicides containing mandipropamid or chlorothalonil. Destroy all infected tubers to prevent spread.",
                    isHealthy = false
                )
            )

//            // 3. Pick one randomly
//            val randomResult = mockScenarios.random()
//
//            // Force success immediately
//            _uiState.value = CameraUiState.Success(randomResult, uri)


            Log.d("KoshiCamera", "Sending image to server: ${file.absolutePath}")
            val result = diseaseRepository.detectDisease(file)
            result.onSuccess {response->
                Log.d("KoshiCamera","AnalysisComplete:${response.diseaseName}")
                _uiState.value = CameraUiState.Success(response,uri)

            }.onFailure { exception ->
                Log.e("KoshiCamera", "Analysis Failed", exception)
                val errorMessage = when {
                    exception.message?.contains("ConnectException") == true ->
                        "Could not connect to server. Is your laptop running?"
                    exception.message?.contains("timeout") == true ->
                        "Server took too long to respond."
                    else -> exception.localizedMessage ?: "Unknown error occurred"
                }
                _uiState.value = CameraUiState.Error(errorMessage)
            }


        }

    }
    fun resetCamera(){
        _uiState.value = CameraUiState.Idle
    }
}