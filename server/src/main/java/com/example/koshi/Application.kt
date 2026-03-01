package com.example.koshi

import com.example.koshi.model.PlantDiseaseResponse
import com.example.koshi.services.PlantDiseaseAnalyzer
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.io.InputStream

fun main() {
    embeddedServer(Netty, port = 8081, host = "0.0.0.0", module = Application::module)
        .start(wait = true)

}

fun Application.module() {
    val logger = LoggerFactory.getLogger("KoshiServer")
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    try {
        PlantDiseaseAnalyzer.init()

    } catch (e: Exception) {
        logger.error(
            "CRITICAL: AI Model failed to load. Server will run but detection will fail.",
            e
        )
    }
    routing {
        route("/api/v1") {
            post("/detect-disease") {
                val multipart = call.receiveMultipart()
                var imageStream: InputStream? = null
                try {
                    multipart.forEachPart { part ->
                        when (part) {
                            is PartData.FileItem->{
                                if (part.name =="image"){
                                    imageStream= part.streamProvider()
                                }
                            }
                            else -> part.dispose()
                        }
                    }
                    if (imageStream != null) {
                        val result : PlantDiseaseResponse = PlantDiseaseAnalyzer.analyse(imageStream!!)
                        call.respond(HttpStatusCode.OK,result)
                        } else {
                        call.respond(HttpStatusCode.BadRequest, "No image part found in the request")
                    }
                }catch (e: Exception) {
                    logger.error("Inference Error", e)
                    call.respond(HttpStatusCode.InternalServerError, "AI Error: ${e.localizedMessage}")
                } finally {
                    imageStream?.close()
                }
            }
        }
    }

}