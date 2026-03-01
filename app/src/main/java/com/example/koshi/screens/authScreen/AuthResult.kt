package com.example.koshi.screens.authScreen

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}