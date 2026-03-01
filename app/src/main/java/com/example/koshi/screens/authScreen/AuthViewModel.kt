package com.example.koshi.screens.authScreen

import AuthMode
import AuthUiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koshi.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppUser(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "customer",
    val phoneNumber: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _authResult = MutableSharedFlow<AuthResult>()
    val authResult = _authResult.asSharedFlow()

    val currentUser = authRepository.currentUser

    fun userDetailsFetch(): FirebaseUser? {
        return authRepository.currentUser.value
    }

    fun checkCurrentUser() {
        // No-op as flow is reactive, kept for compatibility
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        _uiState.update { it.copy(phoneNumber = phoneNumber) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onConfirmPasswordChange(password: String) {
        _uiState.update { it.copy(confirmPassword = password) }
    }

    fun onRegisterAsPartnerChange(isPartner: Boolean) {
        _uiState.update { it.copy(isRegisteringAsPartner = isPartner) }
    }

    fun toggleAuthMode() {
        val newMode =
            if (_uiState.value.authMode == AuthMode.LOGIN) AuthMode.SIGN_UP else AuthMode.LOGIN
        _uiState.update { it.copy(authMode = newMode, errorMessage = null) }
    }

    fun signOut() {
        authRepository.signOut()
    }

    fun onAuthenticate() {
        val state = _uiState.value
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                when (state.authMode) {
                    AuthMode.LOGIN -> {
                        authRepository.login(state.email.trim(), state.password)
                    }

                    AuthMode.SIGN_UP -> {
                        if (state.name.isBlank()) {
                            throw IllegalArgumentException("Name cannot be empty.")
                        }
                        if (state.email.isBlank()) throw IllegalArgumentException("Email cannot be empty.")

                        if (state.password != state.confirmPassword) {
                            throw IllegalArgumentException("Passwords do not match")
                        }
                        if (state.password.length < 6) {
                            throw IllegalArgumentException("Password must be at least 6 characters long.")
                        }
                        if (state.isRegisteringAsPartner && state.phoneNumber.isBlank()) {
                            throw IllegalArgumentException("Phone number is required for partners.")
                        }

                        authRepository.signUp(
                            state.email.trim(),
                            state.password,
                            state.name.trim(),
                            state.isRegisteringAsPartner,
                            state.phoneNumber?.trim()
                        )
                    }
                }
                _authResult.emit(AuthResult.Success)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "An unknown error occurred."
                _uiState.update { it.copy(errorMessage = errorMessage) }
                _authResult.emit(AuthResult.Error(errorMessage))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}