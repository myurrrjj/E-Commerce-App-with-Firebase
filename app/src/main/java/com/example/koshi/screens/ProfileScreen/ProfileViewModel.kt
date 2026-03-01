package com.example.koshi.screens.ProfileScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koshi.model.Order
import com.example.koshi.repository.AuthRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val db = Firebase.firestore

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    val user: StateFlow<FirebaseUser?> = authRepository.currentUser.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = authRepository.currentUser.value
    )

    init {
        loadOrderHistory()
    }

    fun signOut() {
        authRepository.signOut()
    }

    private fun getUserId(): String? = authRepository.currentUser.value?.uid

    fun loadOrderHistory() {
        val userId = getUserId() ?: return
        _uiState.update { it.copy(isLoadingOrders = true) }

        viewModelScope.launch {
            try {
                val snapshot = db.collection("users").document(userId)
                    .collection("orders")
                    .orderBy("orderDate", Query.Direction.DESCENDING)
                    .get().await()

                val orderList = snapshot.documents.map { doc ->
                    doc.toObject(Order::class.java)!!
                        .copy(id = doc.id)
                }

                _uiState.update {
                    it.copy(orders = orderList, isLoadingOrders = false, errorMessage = null)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoadingOrders = false, errorMessage = e.message)
                }
            }
        }
    }
}