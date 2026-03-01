package com.example.koshi.ui.navhost

//import com.example.koshi.screens.cameraScreen.CameraHandlerScreen
//import com.example.koshi.screens.ProfileScreen.ProfileScreen
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.koshi.screens.Detailscreen.DetailViewModel
import com.example.koshi.screens.Detailscreen.PlantDetailScreen
import com.example.koshi.screens.Detailscreen.ServiceDetailScreen
import com.example.koshi.screens.Homescreen.HomeScreen
import com.example.koshi.screens.Homescreen.HomeViewModel
import com.example.koshi.screens.ProfileScreen.OrderHistoryScreen
import com.example.koshi.screens.ProfileScreen.ProfileScreen
import com.example.koshi.screens.ProfileScreen.ProfileViewModel
import com.example.koshi.screens.SplashScreen.SplashScreen
import com.example.koshi.screens.addressScreen.AddressScreen
import com.example.koshi.screens.authScreen.AuthScreen
import com.example.koshi.screens.authScreen.AuthViewModel
import com.example.koshi.screens.bookingScreen.BookingScreen
import com.example.koshi.screens.cameraScreen.CameraScreenRoute
import com.example.koshi.screens.cameraScreen.CameraViewModel
import com.example.koshi.screens.cart.CartScreen
import com.example.koshi.screens.cartScreen.CartViewModel
import com.example.koshi.sharedActivityViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KoshiNavHost(
    navController: NavHostController, modifier: Modifier = Modifier
) {
//    val authViewModel: AuthViewModel = hiltViewModel()
//    val detailViewModel: DetailViewModel=  hiltViewModel()
//    val cartViewModel: CartViewModel = hiltViewModel()
//    val profileViewModel: ProfileViewModel = hiltViewModel()


    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = AppDestinations.SplashScreen.name,
            modifier = modifier,
        ) {
            composable(AppDestinations.SplashScreen.name, exitTransition = { splashExit }) {
                val authViewModel: AuthViewModel = sharedActivityViewModel()
                val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    authViewModel.checkCurrentUser()
//                    delay(1900)
//                    isUserChecked = true
                }

                SplashScreen(
//                    isUserChecked = isUserChecked,
                    onAnimationFinished = {
                        if (currentUser != null) {
                            navController.navigate(AppDestinations.HomeScreen.name) {
                                popUpTo(AppDestinations.SplashScreen.name) { inclusive = true }
                            }
                        } else {
                            navController.navigate(AppDestinations.AuthScreen.name) {
                                popUpTo(AppDestinations.SplashScreen.name) { inclusive = true }
                            }
                        }
                    })
            }

            composable(
                AppDestinations.AuthScreen.name,
                enterTransition = { authEnter },
                exitTransition = { authExit }) {
                val authViewModel: AuthViewModel = sharedActivityViewModel()
                AuthScreen(
                    onAuthComplete = {
                    navController.navigate(AppDestinations.HomeScreen.name) {
                        popUpTo(AppDestinations.AuthScreen.name) { inclusive = true }
                    }
                }, onSkip = {
                    navController.navigate(AppDestinations.HomeScreen.name) {
                        popUpTo(AppDestinations.AuthScreen.name) { inclusive = true }
                    }
                }, authViewModel = authViewModel
                )
            }

            composable(
                AppDestinations.HomeScreen.name,
//                    enterTransition = { homeEnter },
//                    exitTransition = { homeExit },
//                    popEnterTransition = { homePopEnter }
            ) {
                val authViewModel: AuthViewModel = sharedActivityViewModel()

                val homeViewModel: HomeViewModel = sharedActivityViewModel()
                val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()
                val detailViewModel: DetailViewModel = sharedActivityViewModel()
                val cartViewModel: CartViewModel = sharedActivityViewModel()
                HomeScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@composable,
                    homeViewModel = homeViewModel,
                    onProfilePressed = {
                        if (currentUser != null) {
                            navController.navigate(AppDestinations.ProfileScreen.name)
                        } else {
                            navController.navigate(AppDestinations.AuthScreen.name)
                        }
                    },
                    onDetailsPressed = { plant ->
                        detailViewModel.setSelectedPlant(plant)
                        navController.navigate(AppDestinations.DetailScreen.name)
                    },
                    onServiceDetailsPressed = { service ->
                        detailViewModel.setSelectedService(service)
                        navController.navigate(AppDestinations.ServiceDetail.name)
                    },
                    onCameraPressed = { navController.navigate(AppDestinations.CameraScreen.name) },
                    onCartButtonPressed = { navController.navigate(AppDestinations.CartScreen.name) },
                    cartViewModel = cartViewModel
                )
            }

            composable(
                AppDestinations.DetailScreen.name,
//                    enterTransition = { detailEnter },
//                    popExitTransition = { detailPopExit }
            ) {
                val detailViewModel: DetailViewModel = sharedActivityViewModel()
                val cartViewModel: CartViewModel = sharedActivityViewModel()
                PlantDetailScreen(
                    navigateBack = { navController.navigateUp() },
                    detailViewModel = detailViewModel,
                    cartViewModel = cartViewModel,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@composable
                )
            }

            composable(
                AppDestinations.ServiceDetail.name,
                enterTransition = { detailEnter },
                popExitTransition = { detailPopExit }) {
                val detailViewModel: DetailViewModel = sharedActivityViewModel()
                ServiceDetailScreen(
                    navigateBack = { navController.navigateUp() },
                    detailViewModel = detailViewModel,
                    onBookAppointment = {
                        navController.navigate(AppDestinations.BookingScreen.name)
                    },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@composable
                )
            }

            composable(
                AppDestinations.ProfileScreen.name,
                enterTransition = { profileEnter },
                popExitTransition = { profileExit }) {
                val profileViewModel: ProfileViewModel = sharedActivityViewModel()
                ProfileScreen(
                    navigateBack = { navController.navigateUp() },
                    onLogout = {
                        profileViewModel.signOut()
                        navController.navigate(AppDestinations.AuthScreen.name) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    },
                    profileViewModel = profileViewModel,
                    onNavigateToAddresses = { navController.navigate(AppDestinations.AddressScreen.name) },
                    onNavigateToOrders = { navController.navigate(AppDestinations.OrdersScreen.name) },
                )
            }

            composable(
                AppDestinations.CartScreen.name,
                enterTransition = { cartEnter },
                exitTransition = { cartExit }) {
                val cartViewModel: CartViewModel = sharedActivityViewModel()
                CartScreen(
                    navigateBack = { navController.navigateUp() }, cartViewModel = cartViewModel
                )
            }

            composable(
                AppDestinations.BookingScreen.name,
                enterTransition = { bookingEnter },
                exitTransition = { bookingExit }) {
                val detailViewModel: DetailViewModel = sharedActivityViewModel()
                val cartViewModel: CartViewModel = sharedActivityViewModel()
                val service by detailViewModel.selectedService.collectAsStateWithLifecycle()
                BookingScreen(
                    navigateBack = { navController.navigateUp() },
                    cartViewModel = cartViewModel,
                    detailViewModel = detailViewModel,
                    onConfirmBooking = {
                        cartViewModel.addServiceToCart(service!!)
                        navController.navigate(AppDestinations.HomeScreen.name)
                    })
            }

            composable(AppDestinations.CameraScreen.name, enterTransition = { bookingEnter }, exitTransition = { bookingExit }) {
                val cameraViewModel: CameraViewModel = hiltViewModel()
                CameraScreenRoute(cameraViewModel)
//                    CameraScreen(
//                        onImageCaptured = {},
//                        onError = {}
//                    )
//                    CameraHandlerScreen(onSearchLaunched = { navController.navigate(AppDestinations.HomeScreen.name) })
            }
            composable(AppDestinations.AddressScreen.name) {
                AddressScreen(
                    navigateBack = { navController.popBackStack() },
                )

            }
            composable(AppDestinations.OrdersScreen.name) {
                val profileViewModel: ProfileViewModel = sharedActivityViewModel()
                OrderHistoryScreen(
                    navigateBack = { navController.popBackStack() },
                    profileViewModel = profileViewModel
                )
            }
        }
    }
}

