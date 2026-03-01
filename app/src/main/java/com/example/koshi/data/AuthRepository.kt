package com.example.koshi.repository

import com.example.koshi.screens.authScreen.AppUser
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor() {
    private val auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore

    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val currentUser = _currentUser.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser
        }
    }

    suspend fun login(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass).await()
    }

    suspend fun signUp(email: String, pass: String, name: String, isPartner: Boolean, phone: String?) {
        val authResult = auth.createUserWithEmailAndPassword(email, pass).await()
        val firebaseUser = authResult.user ?: throw IllegalStateException("User creation failed.")

        val profileUpdates = userProfileChangeRequest { displayName = name }
        firebaseUser.updateProfile(profileUpdates).await()

        val userRole = if (isPartner) "partner" else "customer"
        val user = AppUser(
            uid = firebaseUser.uid,
            name = name,
            email = email,
            role = userRole,
            phoneNumber = if (isPartner) phone else null
        )
        db.collection("users").document(firebaseUser.uid).set(user).await()
    }

    fun signOut() {
        auth.signOut()
    }
}