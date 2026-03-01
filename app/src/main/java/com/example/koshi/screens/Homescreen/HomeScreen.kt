package com.example.koshi.screens.Homescreen

import PlantUiState
import android.Manifest
import android.R.attr.scaleX
import android.R.attr.scaleY
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.koshi.R
import com.example.koshi.model.Plant
import com.example.koshi.model.Service
import com.example.koshi.model.plantCategories
import com.example.koshi.repository.LocationResult
import com.example.koshi.screens.cartScreen.CartViewModel
import com.example.koshi.ui.theme.KoshiTheme
import com.example.koshi.utils.bouncyClick
//import com.google.common.collect.ImmutableList
import kotlinx.coroutines.launch
import kotlinx.collections.immutable.ImmutableList

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Shop : Screen("shop", "Shop", Icons.Default.ShoppingCart)
    object Services : Screen("services", "Services", Icons.Default.Edit)
    object Camera : Screen("camera", "Search", Icons.Default.Camera)
}

val bottomNavItems = listOf(
    Screen.Shop, Screen.Camera, Screen.Services
)

@Suppress("EffectKeys")
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    cartViewModel: CartViewModel,
    onProfilePressed: () -> Unit,
    onDetailsPressed: (Plant) -> Unit,
    onServiceDetailsPressed: (Service) -> Unit,
    onCameraPressed: () -> Unit,
    onCartButtonPressed: () -> Unit,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope
) {


    val currentScreen by homeViewModel.currentScreen.collectAsStateWithLifecycle()
    val context  = LocalContext.current

    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            homeViewModel.fetchLocation()
        }
    }


    val cartState by cartViewModel.uiState.collectAsStateWithLifecycle()
    val cartQuantity by remember(cartState) {
        derivedStateOf {
            cartState.plantItems.sumOf { it.quantity } + cartState.serviceItems.sumOf { it.quantity }

        }
    }
    var isNavigating by remember { mutableStateOf(false) }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        isNavigating = false

    }

    val scope = rememberCoroutineScope()
    val shopGridState = rememberLazyStaggeredGridState()
    val servicesListState = rememberLazyListState()

    Scaffold(floatingActionButton = {
        Box(Modifier, contentAlignment = Alignment.Center) {
            AnimatedVisibility(
                enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it / 2 }) + fadeOut(),
                visible = cartQuantity > 0 && !isNavigating
            ) {
//                with(sharedTransitionScope){
                    FloatingActionButton(
                        onClick = {
                            isNavigating = true
                            onCartButtonPressed() },
                        shape = CircleShape,
                        containerColor = MaterialTheme.colorScheme.primary,
                        elevation = FloatingActionButtonDefaults.elevation(8.dp),
//                        modifier = Modifier.renderInSharedTransitionScopeOverlay(1f)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .zIndex(.6f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "View Cart",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(Modifier.width(8.dp))
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.onPrimary,
                                        contentColor = MaterialTheme.colorScheme.primary
                                    ) {
                                        Text(
                                            text = cartQuantity.toString(),
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Cart",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
//                    }
                }
            }
        }
    }, floatingActionButtonPosition = FabPosition.Center, bottomBar = {
        with(sharedTransitionScope) {

            AnimatedVisibility(
                enter = slideInVertically(initialOffsetY = { it })  ,
                exit = slideOutVertically(targetOffsetY = { it }) ,
                visible = !isNavigating
            ) {
                FloatingPillBottomBar(
                    currentScreen = currentScreen,
                    onItemSelected = { screen ->
                        if (screen == Screen.Camera) {
                            isNavigating = true
                            onCameraPressed()
                        } else {
                            homeViewModel.onTabSelected(screen)
                        }
                    },
                    modifier = Modifier
                        .renderInSharedTransitionScopeOverlay(1f)
                    ,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope,

                )
            }
//                }
        }

    }) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(bottom = 0.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        if (dragAmount > 30) {
                            if (currentScreen != Screen.Shop) {
                                scope.launch { homeViewModel.onTabSelected(Screen.Shop) }
                            }
                        } else if (dragAmount < -30) {
                            if (currentScreen != Screen.Services) {
                                scope.launch { homeViewModel.onTabSelected(Screen.Services) }
                            }
                        }
                    }
                }) {
            AnimatedContent(
                targetState = currentScreen, modifier = Modifier.fillMaxSize(), transitionSpec = {
                    val initialIndex = bottomNavItems.indexOf(initialState)
                    val targetIndex = bottomNavItems.indexOf(targetState)
                    if (initialIndex == -1 || targetIndex == -1) {
                        fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                    } else {
                        val direction =
                            if (targetIndex > initialIndex) AnimatedContentTransitionScope.SlideDirection.Left
                            else AnimatedContentTransitionScope.SlideDirection.Right
                        slideIntoContainer(direction, tween(300)) togetherWith slideOutOfContainer(
                            direction,
                            tween(300)
                        )
                    }
                }) { targetScreen ->
                when (targetScreen) {
                    Screen.Shop -> ShopScreen(
                        gridState = shopGridState,
                        onProfilePressed = {
                            isNavigating = true
                            onProfilePressed()
                                           },
                        onDetailsPressed = onDetailsPressed,
                        homeViewModel = homeViewModel,
                        onCartButtonPressed = onCartButtonPressed,
                        cartViewModel = cartViewModel,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedContentScope

                    )

                    Screen.Services -> ServicesScreen(
                        listState = servicesListState,
                        homeViewModel = homeViewModel,
                        onProfilePressed = {
                            isNavigating = true
                            onProfilePressed()
                        },
                        onServiceDetailsPressed = onServiceDetailsPressed,
                        onCartButtonPressed = onCartButtonPressed,
                        animatedContentScope = animatedContentScope,
                        sharedTransitionScope = sharedTransitionScope
                    )

                    Screen.Camera -> {}
                }
            }
        }

    }
}

@Composable
fun FloatingPillBottomBar(
    currentScreen: Screen,
    onItemSelected: (Screen) -> Unit,
    modifier: Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val barHeight = 64.dp
    val scale by animateFloatAsState(
        targetValue = if (currentScreen == Screen.Shop) 1.2f else 1f, animationSpec = tween(300)
    )
    val scaleB by animateFloatAsState(
        targetValue = if (currentScreen == Screen.Services) 1.2f else 1f, animationSpec = tween(300)
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp)
            .height(64.dp), contentAlignment = Alignment.BottomCenter
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
                        modifier = Modifier
                            .graphicsLayer { scaleX = scale; scaleY = scale }
                            .bouncyClick(onClick = { onItemSelected(Screen.Shop) }),
                        imageVector = Screen.Shop.icon,
                        contentDescription = "Shop",
                        tint = if (currentScreen == Screen.Shop) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                with(sharedTransitionScope) {
                    FilledIconButton(
                        onClick = { onItemSelected(Screen.Camera) },
                        modifier = Modifier
                            .size(48.dp)
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState("bottomBar_to_addToCart"),
                                animatedVisibilityScope = animatedContentScope,
                                zIndexInOverlay = 2f,
                                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                                clipInOverlayDuringTransition = OverlayClip((CircleShape)),
                            ),
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
                }


                IconButton(onClick = { }) {
                    Icon(
                        modifier = Modifier
                            .graphicsLayer { scaleX = scaleB; scaleY = scaleB }
                            .bouncyClick(onClick = { onItemSelected(Screen.Services) }),
                        imageVector = Screen.Services.icon,
                        contentDescription = "Services",
                        tint = if (currentScreen == Screen.Services) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun MeltingBottomBar(
    currentScreen: Screen, onItemSelected: (Screen) -> Unit
) {
    val fabSize = 64.dp
    val meltDepth = 36.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp), contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter)
                .shadow(
                    elevation = 16.dp,
                    shape = LiquidBarShape(fabSize, meltDepth),
                    ambientColor = Color.Black.copy(alpha = 0.2f),
                    spotColor = Color.Black.copy(alpha = 0.2f)
                ),
            color = MaterialTheme.colorScheme.surface,
            shape = LiquidBarShape(fabSize, meltDepth),
            tonalElevation = 3.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    BottomBarItem(
                        icon = Screen.Shop.icon,
                        label = Screen.Shop.label,
                        isSelected = currentScreen.route == Screen.Shop.route,
                        onClick = { onItemSelected(Screen.Shop) })
                }

                Spacer(modifier = Modifier.width(fabSize))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    BottomBarItem(
                        icon = Screen.Services.icon,
                        label = Screen.Services.label,
                        isSelected = currentScreen.route == Screen.Services.route,
                        onClick = { onItemSelected(Screen.Services) })
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-10).dp)
                .size(fabSize)
                .shadow(8.dp, CircleShape)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() }, indication = null
                ) { onItemSelected(Screen.Camera) }, contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Screen.Camera.icon,
                contentDescription = Screen.Camera.label,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun BottomBarItem(
    icon: ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit
) {
    val color by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(
            alpha = 0.6f
        ), label = "color"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.5f else 1.0f, animationSpec = tween(300), label = "scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() }, indication = null
            ) { onClick() }
            .padding(8.dp)) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier
                .size(26.dp)
                .graphicsLayer { scaleX = scale; scaleY = scale }
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

class LiquidBarShape(
    private val fabSize: Dp, private val meltDepth: Dp
) : Shape {
    override fun createOutline(
        size: Size, layoutDirection: LayoutDirection, density: Density
    ): Outline {
        return Outline.Generic(
            path = Path().apply {
                val width = size.width
                val height = size.height

                val fabSizePx = with(density) { fabSize.toPx() }
                val depthPx = with(density) { meltDepth.toPx() }

                val centerX = width / 2f
                val gapWidth = fabSizePx * 1.6f

                moveTo(0f, 0f)
                lineTo(centerX - gapWidth, 0f)

                cubicTo(
                    x1 = centerX - gapWidth + (gapWidth / 2f),
                    y1 = 0f,
                    x2 = centerX - (fabSizePx / 2f),
                    y2 = depthPx,
                    x3 = centerX,
                    y3 = depthPx
                )

                cubicTo(
                    x1 = centerX + (fabSizePx / 2f),
                    y1 = depthPx,
                    x2 = centerX + gapWidth - (gapWidth / 2f),
                    y2 = 0f,
                    x3 = centerX + gapWidth,
                    y3 = 0f
                )

                lineTo(width, 0f)
                lineTo(width, height)
                lineTo(0f, height)
                close()
            })
    }
}

@Composable
fun ShopScreen(
    homeViewModel: HomeViewModel,
    onProfilePressed: () -> Unit,
    onDetailsPressed: (Plant) -> Unit,
    onCartButtonPressed: () -> Unit,
    cartViewModel: CartViewModel,
    gridState: LazyStaggeredGridState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedContentScope
) {
//    var searchQuery by remember { mutableStateOf("") }
//    var selectedCategory by remember { mutableStateOf("") }
    val plantState by homeViewModel.plantUiState.collectAsStateWithLifecycle()
    val cartState by cartViewModel.uiState.collectAsStateWithLifecycle()
    val locationState by homeViewModel.locationState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(), onResult = { isGranted ->
            if (isGranted) {
                homeViewModel.fetchLocation()
            }
        })
//    val filteredPlants by remember(plantState, searchQuery, selectedCategory) {
//        derivedStateOf {
//            if (plantState is PlantUiState.Success) {
//                (plantState as PlantUiState.Success).featuredPlants.filter { plant ->
//                    (searchQuery.isBlank() || plant.name.contains(
//                        searchQuery, ignoreCase = true
//                    )) && (selectedCategory.isBlank() || plant.category == selectedCategory)
//                }
//            } else {
//                emptyList()
//            }
//        }
//    }

    val filteredPlants by homeViewModel.filteredPlants.collectAsStateWithLifecycle()
    val searchQuery by homeViewModel.searchQueryState.collectAsStateWithLifecycle()
    val selectedCategory by homeViewModel.selectedCategoryState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Column(Modifier.align(Alignment.CenterVertically)) {
                Text(
                    "Find your perfect",
                    style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Text(
                    "green companion",
                    style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.primary)
                )
                TextButton(
                    modifier = Modifier.align(Alignment.Start),
                    contentPadding = PaddingValues(0.dp),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.KeyboardTap)
                        permissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                    }) {
                    Row {
                        Icon(Icons.Default.LocationOn, contentDescription = "Location")
                        Spacer(
                            Modifier.width(4.dp)
                        )
                        Text(
                            text = when (val location = locationState) {
                                is LocationResult.Success -> "Delivering to : ${location.address}"
                                is LocationResult.Error -> "Finding location..."
                                is LocationResult.PermissionDenied -> "Set your location"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )

                    }
                }

            }
            Spacer(Modifier.weight(1f))

            FilledIconButton(
                onClick = onProfilePressed, colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle, contentDescription = "Profile Icon"
                )
            }
        }

        SearchBar(query = searchQuery, onQueryChanged = homeViewModel::onSearchQueryChange)
        Spacer(modifier = Modifier.height(16.dp))

        PlantCategoriesRow(
            selectedCategory = selectedCategory,
            onCategorySelect = homeViewModel::onCategorySelected
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = plantState) {
            is PlantUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                ImagePreloadingScreen(modifier = Modifier.align(Alignment.Center))
            }

            is PlantUiState.Success -> {


                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    PlantGrid(
                        cartViewModel = cartViewModel,
                        plants = filteredPlants,
                        onDetailsPressed = onDetailsPressed,
                        gridState = gridState,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }
            }

            is PlantUiState.Error -> {
                Text(text = state.message)
            }
        }
    }
}

@Composable
fun ServicesScreen(
    homeViewModel: HomeViewModel,
    onProfilePressed: () -> Unit,
    onServiceDetailsPressed: (Service) -> Unit,
    onCartButtonPressed: () -> Unit,
    listState: LazyListState,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        val serviceState by homeViewModel.serviceUiState.collectAsStateWithLifecycle()
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Column(Modifier.align(Alignment.CenterVertically)) {
                Text(
                    "Our Services",
                    style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
                Text(
                    "Expert care",
                    style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.primary)
                )
            }
            Spacer(Modifier.weight(1f))
            Column {
                FilledIconButton(
                    onClick = onProfilePressed, colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile Icon"
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        when (val state = serviceState) {
            is ServiceUiState.Error -> {}
            is ServiceUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is ServiceUiState.Success -> LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(
                    start = 10.dp, end = 10.dp, top = 10.dp, bottom = 120.dp
                )
            ) {
                items(state.featuredServices) { service ->
                    ServiceCard(
                        service,
                        onServiceDetailsPressed,
                        sharedTransitionScope,
                        animatedContentScope
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = { Text("Search for plants...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        trailingIcon = {
            if (query.isNotBlank()) {
                IconButton(onClick = { onQueryChanged("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear Search")
                }
            }
        })
}

@Composable
fun PlantCategoriesRow(selectedCategory: String, onCategorySelect: (String) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(plantCategories) { category ->
            Chip(
                label = category.name,
                onCategorySelect,
                isSelected = category.name == selectedCategory
            )
        }
    }
}

@Composable
fun Chip(label: String, onCategorySelect: (String) -> Unit, isSelected: Boolean) {
    val color by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer else {
            MaterialTheme.colorScheme.secondaryContainer
        }
    )
    Surface(
        shape = CircleShape, color = color, modifier = Modifier
//            .clickable {
//                onCategorySelect(label)
//            }
            .bouncyClick(onClick = { onCategorySelect(label) })
            .animateContentSize(spring(dampingRatio = 0.4f))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AnimatedVisibility(isSelected) {
                Row {
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Selected",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Composable
fun PlantGrid(
    cartViewModel: CartViewModel,
    plants: ImmutableList<Plant>,
    onDetailsPressed: (Plant) -> Unit,
    gridState: LazyStaggeredGridState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedContentScope
) {
    val cartState by cartViewModel.uiState.collectAsStateWithLifecycle()
    val quantities by remember(cartState.plantItems) {
        derivedStateOf { cartState.plantItems.associate { it.plant.id to it.quantity } }
    }
    LazyVerticalStaggeredGrid(
        state = gridState,
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier

            .fillMaxSize()
            .overscroll(rememberOverscrollEffect()),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(
            start = 10.dp, end = 10.dp, top = 10.dp, bottom = 120.dp
        )
    ) {
        items(plants, key = { plant -> plant.id }) { plant ->
            PlantCard(
                plant = plant,
                quantity = quantities[plant.id] ?: 0,
                onAdd = { cartViewModel.addPlantToCart(plant) },
                onIncrement = {
                    cartState.plantItems
                        .firstOrNull { it.plant.id == plant.id }
                        ?.let { cartViewModel.updateQuantity(it, it.quantity + 1) }
                },
                onDecrement = {
                    cartState.plantItems
                        .firstOrNull { it.plant.id == plant.id }
                        ?.let { cartViewModel.updateQuantity(it, it.quantity - 1) }
                },
                onDetailsPressed = onDetailsPressed,
                modifier = Modifier.animateItem(),
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedVisibilityScope

            )
        }
    }
}

@Composable
fun PlantCard(
    plant: Plant,
    onDetailsPressed: (Plant) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    quantity: Int,
    onIncrement: () -> Unit,
    onAdd: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageRequest = remember(plant.images) {
        ImageRequest.Builder(context)
            .data(plant.images.firstOrNull()?.thumbnail)
//            .size(300,300)
//            .scale(coil.size.Scale.FIT)
//            .crossfade(true)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground).crossfade(200).build()
    }


    with(sharedTransitionScope) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = modifier

                .sharedBounds(
                    resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
                    sharedContentState = rememberSharedContentState("${plant.name} Card"),
                    animatedVisibilityScope = animatedContentScope, zIndexInOverlay = 0.5f,
                    clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(16.dp))

                )
                .fillMaxWidth()
//            .clickable { onDetailsPressed(plant) }
                .bouncyClick(onClick = { onDetailsPressed(plant) }),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {

            Column(

            ) {
                Box {
                    AsyncImage(
                        model = imageRequest,
                        contentDescription = plant.name,
//                    error = painterResource(R.drawable.ic_launcher_background),
//                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                        modifier = Modifier
//                            .sharedElement(
//                            sharedContentState = rememberSharedContentState("${plant.name}_image"),
//                            animatedVisibilityScope = animatedContentScope
//                        )
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                        contentScale = ContentScale.FillWidth
                    )

                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        AnimatedContent(
                            targetState = quantity > 0, transitionSpec = {
                                (fadeIn(animationSpec = tween(150, 150)) + scaleIn(
                                    initialScale = 0.92f, animationSpec = tween(250, 150)
                                )).togetherWith(
                                    fadeOut(animationSpec = tween(150)) + scaleOut(
                                        targetScale = 0.92f, animationSpec = tween(150)
                                    )
                                ).using(SizeTransform(clip = false))
                            }, label = "QuantityAnimation"
                        ) { isInCart ->
                            if (isInCart) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    IconButton(
                                        onClick = onDecrement, modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Remove,
                                            contentDescription = "Decrease Quantity",
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Text(
                                            text = "$quantity",
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimary,

                                            )
                                    }
                                    IconButton(
                                        onClick =
                                            onIncrement, modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Add,
                                            contentDescription = "Increase Quantity",
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            } else {
                                IconButton(
                                    onClick = onAdd,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Add to Cart",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        plant.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
//                            .sharedBounds(
//                                sharedContentState = rememberSharedContentState("${plant.id} name"),
//                                animatedVisibilityScope = animatedContentScope,
//                                resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
//                                zIndexInOverlay = 2f
//                            )
//                            .wrapContentWidth()
//                            .skipToLookaheadSize()
                    )
                    Text(
                        plant.displayPrice,
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState("${plant.id} price"),
                                animatedVisibilityScope = animatedContentScope,
                                resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
                                zIndexInOverlay = 2f
                            )
                            .skipToLookaheadSize()
                    )
                }
            }
        }
    }
}

@Composable
fun ImagePreloadingScreen(modifier: Modifier = Modifier) {
    Column(
//        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.plantanimation))

        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Watering your plants...",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Getting everything ready for you.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ServiceCard(
    service: Service,
    onServiceDetailsPressed: (Service) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    with(sharedTransitionScope) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .sharedBounds(
                    sharedContentState = rememberSharedContentState("${service.id}_card"),
                    animatedVisibilityScope = animatedContentScope,
                    resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
                    zIndexInOverlay = 0.5f
                )
                .fillMaxWidth()
                .bouncyClick(onClick = { onServiceDetailsPressed(service) }),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = service.name,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(service.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        service.description,
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        maxLines = 2
                    )
                    Text(
                        service.displayPrice, style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        ), modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Icon(
                    Icons.Default.AccountBox,
                    contentDescription = "Book Now",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

}

@Composable
fun MyPlantsScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "My Plants Screen",
            style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.primary)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    KoshiTheme {
        // preview left empty
    }
}
