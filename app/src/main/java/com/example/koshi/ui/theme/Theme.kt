package com.example.koshi.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme


import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val KoshiGreen = Color(0xFF34A853)
private val KoshiDarkGreen = Color(0xFF1E8E3E)
private val KoshiLightGreen = Color(0xFFE6F4EA)
private val KoshiTextPrimary = Color(0xFF1F2937)
private val KoshiTextSecondary = Color(0xFF6B7280)

private val DarkColorPalette = darkColorScheme(
    primary = KoshiGreen,
    onPrimary = Color.White,
    secondary = KoshiLightGreen,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onBackground = Color.White,
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFB0B0B0)
)

private val LightColorPalette = lightColorScheme(
    primary = KoshiGreen,
    onPrimary = Color.White,
    secondary = KoshiDarkGreen,
    background = Color.White,
    surface = Color(0xFFF9FAFB),
    onBackground = KoshiTextPrimary,
    onSurface = KoshiTextPrimary,
    surfaceVariant = KoshiLightGreen,
    onSurfaceVariant = KoshiDarkGreen
)

@Composable
fun KoshiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorPalette
        else -> LightColorPalette
    }
//    val view = LocalView.current
//    if (!view.isInEditMode) {
//        SideEffect {
//            val window = (view.context as Activity).window
//            window.statusBarColor = colorScheme.primary.toArgb()
//            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
//        }
//    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

