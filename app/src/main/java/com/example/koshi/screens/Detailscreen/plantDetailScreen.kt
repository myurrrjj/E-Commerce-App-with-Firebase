package com.example.koshi.screens.Detailscreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.koshi.R
import com.example.koshi.model.ImageUrls
import com.example.koshi.model.Plant
import com.example.koshi.screens.cartScreen.CartViewModel
import com.example.koshi.ui.theme.KoshiTheme

@Composable
fun PlantDetailScreen(
    navigateBack: () -> Unit,
    detailViewModel: DetailViewModel,
    cartViewModel: CartViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val plant by detailViewModel.selectedPlant.collectAsStateWithLifecycle()
    val cartState by cartViewModel.uiState.collectAsStateWithLifecycle()

    if (plant == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val cartItem = cartState.plantItems.find { it.plant.id == plant!!.id }
        val quantity = cartItem?.quantity ?: 0

        PlantDetailContent(
            sharedTransitionScope = sharedTransitionScope,
            animatedContentScope = animatedContentScope,
            plant = plant!!,
            quantity = quantity,
            navigateBack = navigateBack,
            onAddToCart = { cartViewModel.addPlantToCart(plant!!) },
            onQuantityChange = { newQuantity ->
                if (cartItem != null) {
                    cartViewModel.updateQuantity(cartItem, newQuantity)
                }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlantDetailContent(
    plant: Plant,
    quantity: Int,
    navigateBack: () -> Unit,
    onAddToCart: () -> Unit,
    onQuantityChange: (Int) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    with(sharedTransitionScope) {
        Scaffold(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .sharedBounds(
                    resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
                    sharedContentState = rememberSharedContentState("${plant.name} Card"),
                    animatedVisibilityScope = animatedContentScope, zIndexInOverlay = 1.5f,
                    clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(16.dp))
                ),
            bottomBar = {
//                with(sharedTransitionScope) {
                    AddToCartBar(
                        price = plant.displayPrice,
                        quantity = quantity,
                        onAddToCart = onAddToCart,
                        onQuantityChange = onQuantityChange,
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState("bottomBar_to_addToCart"),
                                animatedVisibilityScope = animatedContentScope,
                                zIndexInOverlay = 2f,
                                resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                                clipInOverlayDuringTransition = OverlayClip((RoundedCornerShape(12.dp))),
                            )
                            .skipToLookaheadSize(),
                        modifier2 = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState("${plant.id} price"),
                                animatedVisibilityScope = animatedContentScope,
                                resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
                                zIndexInOverlay = 3f
                            )
                            .skipToLookaheadSize()
                        ,

                    )
//                }

            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))

                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(bottom = paddingValues.calculateBottomPadding())
            ) {
                item {
                    Box(contentAlignment = Alignment.TopCenter) {
                        ImageCarousel(
                            plant = plant,
                            animatedContentScope = animatedContentScope,
                            sharedTransitionScope = sharedTransitionScope
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .align(Alignment.BottomCenter)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            MaterialTheme.colorScheme.background
                                        )
                                    )
                                )
                        )
                        TopBar(onBackClicked = navigateBack)
                    }
                }
                item {
                    PlantInfoSection(
                        plant,
                        animatedContentScope = animatedContentScope,
                        sharedTransitionScope = sharedTransitionScope
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImageCarousel(
    plant: Plant,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope
) {
    if (plant.images.isNotEmpty()) {
        val pagerState = rememberPagerState { plant.images.size }
        Box(contentAlignment = Alignment.BottomCenter) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) { page ->
//                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(plant.images[page].original)
//                        .placeholderMemoryCacheKey(plant.images[page].thumbnail)
//                        .crossfade(true)
//                        .build(),
//                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
//                    contentDescription = "Plant Image ${page + 1}",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.fillMaxSize()
//                )


                with(sharedTransitionScope) {
                    SmartPlantImage(
                        thumbnailUrl = plant.images[page].thumbnail,
                        originalUrl = plant.images[page].original,
                        modifier = Modifier.fillMaxSize()
//                            .sharedElement(
//                            sharedContentState = rememberSharedContentState("${plant.name}_image"),
//                            animatedVisibilityScope = animatedContentScope
//                        )
                    )
                }
            }
            Row(
                Modifier
                    .padding(bottom = 50.dp)
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray.copy(
                            alpha = 0.5f
                        )
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)
                    )
                }
            }
        }
    } else {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Placeholder Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        )
    }
}

@Composable
private fun TopBar(onBackClicked: () -> Unit) {
    var isFavorited by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClicked,
            modifier = Modifier.background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                CircleShape
            )
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        IconButton(
            onClick = { isFavorited = !isFavorited },
            modifier = Modifier.background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                CircleShape
            )
        ) {
            Icon(
                imageVector = if (isFavorited) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (isFavorited) Color.Red else MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun PlantInfoSection(
    plant: Plant,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-32).dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp)
            .clip(RoundedCornerShape(16.dp))


    ) {
//        with(sharedTransitionScope) {

            Text(
                text = plant.name,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
//                modifier = Modifier.sharedBounds(
//                    sharedContentState = rememberSharedContentState("${plant.id} name"),
//                    animatedVisibilityScope = animatedContentScope,
//                    resizeMode = SharedTransitionScope.ResizeMode.scaleToBounds(),
//                    zIndexInOverlay = 3f
//                )
//                    .wrapContentWidth()
//                    .skipToLookaheadSize()
            )
//        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = plant.category,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InfoChip(
                icon = Icons.Default.LightMode,
                label = "Light",
                value = plant.light,
                modifier = Modifier.weight(1f)
            )
            InfoChip(
                icon = Icons.Default.Height,
                label = "Height",
                value = plant.height,
                modifier = Modifier.weight(1f)
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 24.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )

        Text(
            text = "About",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = plant.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Care Instructions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = plant.careInstructions,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}
//}

@Composable
private fun InfoChip(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.defaultMinSize(minHeight = 120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Composable
private fun AddToCartBar(
    price: String,
    quantity: Int,
    onAddToCart: () -> Unit,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier,
    modifier2: Modifier,
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        modifier = Modifier.clip(RoundedCornerShape(16.dp))

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Price",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    price,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = modifier2
                )
            }
            AnimatedContent(
                targetState = quantity > 0,
                transitionSpec = {
                    if (targetState) {
                        slideInVertically { height -> height } + fadeIn() togetherWith
                                slideOutVertically { height -> -height } + fadeOut()
                    } else {
                        slideInVertically { height -> -height } + fadeIn() togetherWith
                                slideOutVertically { height -> height } + fadeOut()
                    }.using(SizeTransform(clip = false))
                },
                label = "CartButtonAnimation"
            ) { isInCart ->
                if (isInCart) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.height(56.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onQuantityChange(quantity - 1) },
                            shape = CircleShape,
                            modifier = Modifier.size(40.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease Quantity")
                        }
                        Text(
                            text = "$quantity",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Button(
                            onClick = { onQuantityChange(quantity + 1) },
                            shape = CircleShape,
                            modifier = Modifier.size(40.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase Quantity")
                        }
                    }
                } else {
                    Button(
                        onClick = onAddToCart,
                        shape = RoundedCornerShape(16.dp),
                        modifier = modifier.height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            "Add to Cart",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

// Add this at the bottom of your file or in a separate file
@Composable
fun SmartPlantImage(
    thumbnailUrl: String?,
    originalUrl: String?,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(thumbnailUrl)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        var isHighResLoaded by remember { mutableStateOf(false) }

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(originalUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            onSuccess = { isHighResLoaded = true },
            modifier = modifier
                .fillMaxSize()
                .alpha(if (isHighResLoaded) 1f else 0f) // Invisible until loaded
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PlantDetailScreenPreview() {
    KoshiTheme {
        val samplePlant = Plant(
            id = 1,
            name = "Preview Plant",
            price = 35,
            category = "Indoor",
            images = listOf(ImageUrls(thumbnail = "", original = "")),
            light = "Bright, Indirect Sunlight",
            height = "30-40cm",
            description = "This is a sample description for the preview plant to see how the layout looks with text content.",
            careInstructions = "Water this sample plant once a week. Keep it in a location with good airflow."
        )
//        PlantDetailContent(plant = samplePlant, navigateBack = {})
    }
}

