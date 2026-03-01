import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
//import androidx.compose.material3.CircularWavyProgressIndicator
//import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.koshi.screens.cameraScreen.CameraScreen
import com.example.koshi.screens.cameraScreen.CameraUiState
import com.example.koshi.screens.cameraScreen.CameraViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale





@Composable
fun CameraHandlerScreen(
    onSearchLaunched: () -> Unit
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var hasTakenImage by remember { mutableStateOf(false) }

    fun createImageUri(context: Context): Uri {
        val imageFile = File.createTempFile(
            "plant_image_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}",
            ".jpg",
            context.cacheDir
        )
        return FileProvider.getUriForFile(
            context, "${context.packageName}.provider", imageFile
        )
    }

    // --- THIS FUNCTION IS NOW SIMPLIFIED ---
    fun launchImageSearch(context: Context, uri: Uri) {
        val packageManager = context.packageManager

        // 1. Create the base intent
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        // 2. Define the main Google App package
        val googleAppPackage = "com.google.android.googlequicksearchbox"

        // 3. Check if the "Google" app is available
        val googleAppIntent = Intent(intent).setPackage(googleAppPackage)
        if (packageManager.resolveActivity(googleAppIntent, 0) != null) {
            context.startActivity(googleAppIntent)
            onSearchLaunched()
            return
        }

        // 4. If it's not available, fall back to the standard share chooser
        Toast.makeText(context, "Google app not found. Opening share sheet.", Toast.LENGTH_SHORT)
            .show()
        val chooserIntent = Intent.createChooser(intent, "Search plant with...")
        context.startActivity(chooserIntent)
        onSearchLaunched()
    }


    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(), onResult = { success ->
            if (success) {
                hasTakenImage = true
            } else {
                Toast.makeText(context, "Failed to take photo", Toast.LENGTH_SHORT).show()
                imageUri = null
                onSearchLaunched()
            }
        })

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(), onResult = { isGranted ->
            if (isGranted) {
                val newUri = createImageUri(context)
                imageUri = newUri
                cameraLauncher.launch(newUri)
            } else {
                Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
                onSearchLaunched()
            }
        })

    LaunchedEffect(hasTakenImage, imageUri) {
        if (hasTakenImage && imageUri != null) {
            launchImageSearch(context, imageUri!!)
            hasTakenImage = false
        }
    }

    LaunchedEffect(Unit) {
        when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
            PackageManager.PERMISSION_GRANTED -> {
                val newUri = createImageUri(context)
                imageUri = newUri
                cameraLauncher.launch(newUri)
            }

            else -> {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
}