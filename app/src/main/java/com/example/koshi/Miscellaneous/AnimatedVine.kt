    package com.example.koshi.Miscellaneous

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min

@Composable
fun AnimatedCornerVine(
    modifier: Modifier = Modifier,
    vineColor: Color = Color(0xFF2E7D32), // dark green
    leafColor: Color = Color(0xFF66BB6A),
    strokeWidthDp: Dp = 6.dp,
    animationDurationMs: Int = 1600
) {
    BoxWithConstraints(modifier = modifier) {
        val density = LocalDensity.current
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }
        val strokePx = with(density) { strokeWidthDp.toPx() }

        val progress = remember { Animatable(0f) }

        LaunchedEffect(Unit) {
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = animationDurationMs)
            )
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            if (widthPx <= 0f || heightPx <= 0f) return@Canvas

            val topPath = Path().apply {
                moveTo(0f, 0f)
                lineTo(widthPx, 0f)
            }
            val leftPath = Path().apply {
                moveTo(0f, 0f)
                lineTo(0f, heightPx)
            }

            val topProgress = (progress.value / 0.5f).coerceIn(0f, 1f)
            val leftProgress = ((progress.value - 0.5f) / 0.5f).coerceIn(0f, 1f)

            val topLen = widthPx
            val leftLen = heightPx

            val topPhase = topLen * (1f - topProgress)
            val leftPhase = leftLen * (1f - leftProgress)

            // ✅ Correct usage: PathEffect via Stroke
            val topStroke = Stroke(width = strokePx, pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(topLen, topLen),
                topPhase
            ))
            val leftStroke = Stroke(width = strokePx, pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(leftLen, leftLen),
                leftPhase
            ))

            // Draw top vine
            drawPath(
                path = topPath,
                color = vineColor,
                style = topStroke
            )

            // Draw left vine
            drawPath(
                path = leftPath,
                color = vineColor,
                style = leftStroke
            )

            drawLeavesAlongTop(topProgress, widthPx, strokePx, leafColor)
            drawLeavesAlongLeft(leftProgress, heightPx, strokePx, leafColor)
        }
    }
}

private fun DrawScope.drawLeavesAlongTop(progress: Float, widthPx: Float, strokePx: Float, leafColor: Color) {
    val leafPositions = listOf(0.12f, 0.32f, 0.58f, 0.78f)
    val visibleCount = leafPositions.count { it <= progress }
    val leafRadius = min(12f, strokePx * 1.4f)
    repeat(visibleCount) { i ->
        val fx = leafPositions[i] * widthPx
        val fy = -leafRadius - (i % 2) * 4f
        drawCircle(color = leafColor, radius = leafRadius, center = Offset(fx, fy))
    }
}

private fun DrawScope.drawLeavesAlongLeft(progress: Float, heightPx: Float, strokePx: Float, leafColor: Color) {
    val leafPositions = listOf(0.18f, 0.38f, 0.60f, 0.85f)
    val visibleCount = leafPositions.count { it <= progress }
    val leafRadius = min(12f, strokePx * 1.4f)
    repeat(visibleCount) { i ->
        val fy = leafPositions[i] * heightPx
        val fx = -leafRadius - (i % 2) * 6f
        drawCircle(color = leafColor, radius = leafRadius, center = Offset(fx, fy))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AnimatedCornerVinePreview() {
    MaterialTheme {
        AnimatedCornerVine(modifier = Modifier.fillMaxSize())
    }
}
