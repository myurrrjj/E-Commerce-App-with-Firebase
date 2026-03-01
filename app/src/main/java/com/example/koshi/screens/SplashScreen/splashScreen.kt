package com.example.koshi.screens.SplashScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.koshi.screens.Homescreen.ImagePreloadingScreen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onAnimationFinished: () -> Unit
) {
    val scale = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    var shift by remember { mutableStateOf(false) }
    LaunchedEffect(onAnimationFinished) {
        // Bounce-in scale animation
        scale.animateTo(
            targetValue = 1.2f,
            animationSpec = tween(durationMillis = 800)
        )

        // Wait for user check to complete (~1900ms matches your nav delay)
        delay(1200)

        // Slide splash upward to reveal the next screen
//        offsetY.animateTo(
//            targetValue = -200f,
//            animationSpec = tween(durationMillis = 800)
//        )
//        delay(50)

        shift = true
        delay(1700)

        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { translationY = offsetY.value }
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column {

            Text(
                text = "Koshi",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.scale(scale.value).align(Alignment.CenterHorizontally)
            )
            AnimatedVisibility(shift) {
                ImagePreloadingScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun splashScreenPreview(onAnimationFinished: () -> Unit = {}) {
    SplashScreen(onAnimationFinished = onAnimationFinished)
}
