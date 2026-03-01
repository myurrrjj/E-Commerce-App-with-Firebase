package com.example.koshi.services

import ai.djl.Application
import ai.djl.modality.Classifications
import ai.djl.modality.cv.Image
import ai.djl.modality.cv.ImageFactory
import ai.djl.repository.zoo.Criteria
import ai.djl.repository.zoo.ZooModel
import ai.djl.training.util.ProgressBar
import com.example.koshi.model.PlantDiseaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.io.InputStream

object PlantDiseaseAnalyzer {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private var model: ZooModel<Image, Classifications>? = null

    private data class DiseaseInfo(val name: String, val symptoms: String, val treatment: String)

    private val diseaseDatabase = mapOf(
        "0" to DiseaseInfo(
            "Tomato Early Blight",
            "Dark spots, concentric rings",
            "Use copper-based fungicide"
        ),
        "1" to DiseaseInfo(
            "Potato Late Blight",
            "Water-soaked lesions",
            "Remove infected parts, improve airflow"
        ),
        "2" to DiseaseInfo("Healthy Plant", "None", "Keep up the good work!")
    )

    fun init() {
        if (model != null) return

        val criteria = Criteria.builder()
            .setTypes(Image::class.java, Classifications::class.java)
            .optApplication(Application.CV.IMAGE_CLASSIFICATION)
            .optGroupId("ai.djl.pytorch")
            .optArtifactId("resnet")
            .optFilter("layers", "50")
            .optProgress(ProgressBar())
            .build()

        try {
            model = criteria.loadModel()
            logger.info("Plant Disease Model loaded successfully")
        } catch (e: Exception) {
            logger.error("Failed to load model", e)
            throw RuntimeException("Critical: Failed to initialize AI Model", e)
        }
    }

    suspend fun analyse(imageStream: InputStream): PlantDiseaseResponse =
        withContext(Dispatchers.IO) {
            if (model == null) init()

            val img: Image = ImageFactory.getInstance().fromInputStream(imageStream)

            val predictions: Classifications = model!!.newPredictor().use { predictor ->
                predictor.predict(img)
            }

            val bestItem: Classifications.Classification = predictions.best()
            val label = bestItem.className
            val confidence = bestItem.probability.toFloat()

            val info = diseaseDatabase[label] ?: DiseaseInfo(
                label,
                "Unknown symptoms",
                "Consult an expert"
            )

            PlantDiseaseResponse(
                diseaseName = info.name,
                confidence = confidence,
                symptoms = info.symptoms,
                treatment = info.treatment,
                isHealthy = info.name == "Healthy Plant"
            )
        }
}