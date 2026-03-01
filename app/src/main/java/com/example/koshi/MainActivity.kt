package com.example.koshi

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.rememberNavController
import com.example.koshi.screens.Homescreen.HomeViewModel
import com.example.koshi.screens.authScreen.AuthViewModel
import com.example.koshi.ui.navhost.KoshiApp
import com.example.koshi.ui.navhost.KoshiNavHost
import com.example.koshi.ui.theme.KoshiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        homeViewModel
        authViewModel

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val display = this.display
            val supportedRefreshRates = display?.supportedModes?.map { it.refreshRate } ?: emptyList()
            val highestRefreshRate = supportedRefreshRates.maxOrNull()

            if (highestRefreshRate != null) {
                val window: Window = window
                val layoutParams: WindowManager.LayoutParams = window.attributes
                layoutParams.preferredRefreshRate = highestRefreshRate
                window.attributes = layoutParams
            }
        }
//        private val homeViewModel: HomeViewModel by viewModels()
//        private val authViewModel: AuthViewModel by viewModels()
        setContent {
            val darkTheme = isSystemInDarkTheme()

            val view = LocalView.current
            DisposableEffect(darkTheme) {
                val window = (view.context as ComponentActivity).window
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !darkTheme
                onDispose {}
            }

            KoshiTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KoshiApp()
                }
            }
        }
    }
}



@Composable
inline fun <reified T : ViewModel> sharedActivityViewModel(): T {
    val activity = LocalActivity.current as ComponentActivity
    return hiltViewModel(activity)
}
