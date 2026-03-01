package com.example.koshi.screens.cameraScreen

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.koshi.model.PlantDiseaseResponse

@Composable
fun DiseaseResultScreen(
    result: PlantDiseaseResponse,
    imageUri: Uri,
    onScanAgain: () -> Unit
) {
    // Determine colors based on health status
    val statusColor = if (result.isHealthy) Color(0xFF4CAF50) else Color(0xFFFF5252)
    val statusIcon = if (result.isHealthy) Icons.Default.CheckCircle else Icons.Default.Warning
    val containerColor = MaterialTheme.colorScheme.surfaceContainer

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Hero Image (Background/Top)
        AsyncImage(
            model = imageUri,
            contentDescription = "Captured Leaf",
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
            contentScale = ContentScale.Crop
        )

        // 2. Sliding Sheet Content
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 300.dp), // Overlaps the image slightly
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = containerColor,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Drag Handle (Visual cue)
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
                )
                Spacer(modifier = Modifier.height(20.dp))

                // Status Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(100))
                        .background(statusColor.copy(alpha = 0.1f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = statusIcon,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (result.isHealthy) "HEALTHY" else "INFECTED",
                        color = statusColor,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Disease Name
                Text(
                    text = result.diseaseName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Confidence Meter
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "AI Confidence: ${(result.confidence * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    LinearProgressIndicator(
                        progress = { result.confidence },
                        modifier = Modifier.weight(1f).height(6.dp),
                        color = statusColor,
                        trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        strokeCap = StrokeCap.Round,
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(24.dp))

                // Information Cards
                if (!result.isHealthy) {
                    InfoSection(
                        title = "Symptoms Detected",
                        content = result.symptoms,
                        icon = Icons.Outlined.Info,
                        iconTint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoSection(
                        title = "Recommended Treatment",
                        content = result.treatment,
                        icon = Icons.Default.Healing,
                        iconTint = statusColor
                    )
                } else {
                    InfoSection(
                        title = "Good News!",
                        content = "No signs of disease were found. Keep monitoring your plant and ensure it gets enough water and sunlight.",
                        icon = Icons.Default.CheckCircle,
                        iconTint = statusColor
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Scan Again Button
                Button(
                    onClick = onScanAgain,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Scan Another Plant", style = MaterialTheme.typography.titleMedium)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun InfoSection(title: String, content: String, icon: ImageVector, iconTint: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DiseaseResultScreenInfectedPreview() {
    val mockInfected = PlantDiseaseResponse(
        diseaseName = "Tomato Early Blight",
        confidence = 0.98f,
        symptoms = "Dark spots on lower leaves, concentric rings, yellowing tissue.",
        treatment = "Remove infected leaves immediately. Apply copper-based fungicide. Improve air circulation around the plant.",
        isHealthy = false
    )

    // Using Uri.EMPTY as a placeholder since we can't load real files in Preview
    DiseaseResultScreen(
        result = mockInfected,
        imageUri = Uri.EMPTY,
        onScanAgain = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun DiseaseResultScreenHealthyPreview() {
    val mockHealthy = PlantDiseaseResponse(
        diseaseName = "Healthy Plant",
        confidence = 0.99f,
        symptoms = "None",
        treatment = "None",
        isHealthy = true
    )

    DiseaseResultScreen(
        result = mockHealthy,
        imageUri = Uri.EMPTY,
        onScanAgain = {}
    )
}