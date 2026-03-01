package com.example.koshi.repository

import com.example.koshi.model.PlantDiseaseResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

interface DiseaseRepository {
    suspend fun detectDisease(imageFile: File): Result<PlantDiseaseResponse>
}

@Singleton
class NetworkDiseaseRepository @Inject constructor(
    private val client: HttpClient
) : DiseaseRepository {

    override suspend fun detectDisease(imageFile: File): Result<PlantDiseaseResponse> {
        return try {
            val response = client.post("http://192.168.1.80:8081/api/v1/detect-disease") {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("image", imageFile.readBytes(), Headers.build {
                                append(HttpHeaders.ContentType, "image/jpeg")
                                append(HttpHeaders.ContentDisposition, "filename=\"${imageFile.name}\"")
                            })
                        }
                    )
                )
            }
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}