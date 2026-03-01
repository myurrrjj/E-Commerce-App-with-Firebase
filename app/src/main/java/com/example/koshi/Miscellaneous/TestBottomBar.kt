import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.koshi.screens.Homescreen.BottomBarItem
import com.example.koshi.screens.Homescreen.Screen
import com.example.koshi.ui.theme.KoshiTheme

@Composable
fun FloatingPillBottomBar(
    currentScreen: Screen,
    onItemSelected: (Screen) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp)
            .height(64.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp,
            tonalElevation = 3.dp
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { onItemSelected(Screen.Shop) }) {
                    Icon(
                        imageVector = Screen.Shop.icon,
                        contentDescription = "Shop",
                        tint = if (currentScreen == Screen.Shop) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                FilledIconButton(
                    onClick = { onItemSelected(Screen.Camera) },
                    modifier = Modifier.size(48.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Screen.Camera.icon,
                        contentDescription = "Camera",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                IconButton(onClick = { onItemSelected(Screen.Services) }) {
                    Icon(
                        imageVector = Screen.Services.icon,
                        contentDescription = "Services",
                        tint = if (currentScreen == Screen.Services) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FloatingPillBottomBarPreview() {
    KoshiTheme {
        Box(Modifier.padding(16.dp)) {
            FloatingPillBottomBar(
                currentScreen = Screen.Shop,
                onItemSelected = {}
            )
        }
    }
}

@Composable
fun OverlapBottomBar(
    currentScreen: Screen,
    onItemSelected: (Screen) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .align(Alignment.BottomCenter),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                BottomBarItem(
                    icon = Screen.Shop.icon,
                    label = Screen.Shop.label,
                    isSelected = currentScreen == Screen.Shop,
                    onClick = { onItemSelected(Screen.Shop) }
                )

                Spacer(modifier = Modifier.width(56.dp))

                BottomBarItem(
                    icon = Screen.Services.icon,
                    label = Screen.Services.label,
                    isSelected = currentScreen == Screen.Services,
                    onClick = { onItemSelected(Screen.Services) }
                )
            }
        }

        FloatingActionButton(
            onClick = { onItemSelected(Screen.Camera) },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 4.dp),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(
                imageVector = Screen.Camera.icon,
                contentDescription = "Camera",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OverlapBottomBarPreview() {
    KoshiTheme {
        OverlapBottomBar(
            currentScreen = Screen.Shop,
            onItemSelected = {}
        )
    }
}

@Composable
fun MinimalistBottomBar(
    currentScreen: Screen,
    onItemSelected: (Screen) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Screen.Shop.icon, contentDescription = null) },
            label = { Text("Shop") },
            selected = currentScreen == Screen.Shop,
            onClick = { onItemSelected(Screen.Shop) }
        )

        NavigationBarItem(
            icon = {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(width = 64.dp, height = 32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Screen.Camera.icon, contentDescription = null)
                    }
                }
            },
            label = { Text("Search") },
            selected = false,
            onClick = { onItemSelected(Screen.Camera) }
        )

        NavigationBarItem(
            icon = { Icon(Screen.Services.icon, contentDescription = null) },
            label = { Text("Services") },
            selected = currentScreen == Screen.Services,
            onClick = { onItemSelected(Screen.Services) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MinimalistBottomBarPreview() {
    KoshiTheme {
        MinimalistBottomBar(
            currentScreen = Screen.Shop,
            onItemSelected = {}
        )
    }
}