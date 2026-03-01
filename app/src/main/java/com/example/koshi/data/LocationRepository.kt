package com.example.koshi.repository

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.koshi.model.Address
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

sealed class LocationResult {
    data class Success(val address: String, val fullAddress: Address? = null) : LocationResult()
    data class Error(val message: String) : LocationResult()
    object PermissionDenied : LocationResult()
}

@Singleton
class LocationRepository @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val application: Application
) {
    private val geocoder = Geocoder(application, Locale.getDefault())
    private val db = Firebase.firestore
    private val auth = Firebase.auth

    suspend fun getUserAddress(): LocationResult {
        val hasPermission = ContextCompat.checkSelfPermission(
            application, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            return LocationResult.PermissionDenied
        }

        val location = getLastKnownLocation() ?: return LocationResult.Error("No location found")
        return getAddressFromLocation(location)
    }

    private fun getAddressFromLocation(location: Location): LocationResult {
        return try {
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            val fullAddress = addresses?.firstOrNull()

            val street = fullAddress?.thoroughfare ?: fullAddress?.subLocality ?: ""
            val city = fullAddress?.locality ?: fullAddress?.subAdminArea ?: ""
            val zip = fullAddress?.postalCode ?: ""

            val friendlyName = if (street.isNotEmpty()) "$street, $city" else city

            if (friendlyName.isNotEmpty()) {
                val mappedAddress = Address(
                    label = "Current Location",
                    street = street,
                    city = city,
                    postalCode = zip
                )
                LocationResult.Success(friendlyName, mappedAddress)
            } else {
                LocationResult.Error("Could not determine area")
            }
        } catch (e: Exception) {
            LocationResult.Error(e.message ?: "Geocoding failed")
        }
    }

    private suspend fun getLastKnownLocation(): Location? {
        return suspendCancellableCoroutine { continuation ->
            try {
                locationClient.lastLocation.addOnSuccessListener { location ->
                    continuation.resume(location)
                }.addOnFailureListener {
                    continuation.resume(null)
                }.addOnCanceledListener {
                    continuation.resume(null)
                }
            } catch (e: SecurityException) {
                continuation.resume(null)
            }
        }
    }

    suspend fun getSavedAddresses(): List<Address> {
        val uid = auth.currentUser?.uid ?: return emptyList()
        return try {
            val snapshot = db.collection("users").document(uid).collection("addresses").get().await()
            snapshot.documents.map { doc ->
                doc.toObject(Address::class.java)!!.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun saveAddress(address: Address) {
        val uid = auth.currentUser?.uid ?: return
        val collection = db.collection("users").document(uid).collection("addresses")

        if (address.id.isEmpty()) {
            collection.add(address).await()
        } else {
            collection.document(address.id).set(address).await()
        }
    }

    suspend fun deleteAddress(id: String) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).collection("addresses").document(id).delete().await()
    }
}