package com.nutrisport.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.nutrisport.admin_panel.AdminPanelScreen
import com.nutrisport.auth.AuthScreen
import com.nutrisport.category_search.CategorySearchScreen
import com.nutrisport.checkout.CheckoutScreen
import com.nutrisport.details.DetailsScreen
import com.nutrisport.home.HomeGraphScreen
import com.nutrisport.manage_product.ManageProductScreen
import com.nutrisport.payment_completed.PaymentCompletedScreen
import com.nutrisport.profile.ProfileScreen
import com.nutrisport.shared.domain.ProductCategory
import com.nutrisport.shared.navigation.Screen
import com.nutrisport.shared.util.PreferencesRepository


@Composable
fun SetUpNavGraph(
    startDestination: Screen = Screen.Auth
) {
    val navController = rememberNavController()
//    val intentHandler = koinInject<IntentHandler>()
//    val navigateTo by intentHandler.navigateTo.collectAsState()
//
//    LaunchedEffect(navigateTo) {
//        navigateTo?.let { paymentCompleted ->
//            println("NAVIGATING TO PAYMENT COMPLETED")
//            navController.navigate(paymentCompleted)
//            intentHandler.resetNavigation()
//        }
//    }
    val preferencesData by PreferencesRepository.readPayPalDataFlow()
        .collectAsState(initial = null)

    LaunchedEffect(preferencesData) {
        preferencesData?.let { paymentCompleted ->
            if (paymentCompleted.token != null) {
                navController.navigate(paymentCompleted)
                PreferencesRepository.reset()
            }
        }
    }

    //there is 2 navHost in this application, one for the auth and home screen
    //the other one for the bottom bar in the home screen
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.Auth> {
            AuthScreen(
                navigateToHome = {
                    navController.navigate(Screen.HomeGraph) {
                        popUpTo<Screen.Auth> { inclusive = true }
                    }
                }
            )
        }
        composable<Screen.HomeGraph> {
            HomeGraphScreen(
                navigateToAuth = {
                    navController.navigate(Screen.Auth) {
                        popUpTo<Screen.HomeGraph> { inclusive = true }
                    }
                },
                navigateToProfile = {
                    navController.navigate(Screen.Profile)
                },
                navigateToAdminPanel = {
                    navController.navigate(Screen.AdminPanel)
                },
                navigateToDetails = { productId ->
                    navController.navigate(Screen.Details(productId))
                },
                navigateToCategorySearch = { category ->
                    navController.navigate(Screen.CategorySearch(category))
                },
                navigateToCheckout = { totalAmount ->
                    navController.navigate(Screen.Checkout(totalAmount))
                }
            )
        }
        composable<Screen.Profile> {
            ProfileScreen(
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }
        composable<Screen.AdminPanel> {
            AdminPanelScreen(
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToManageProduct = { id ->
                    navController.navigate(Screen.ManageProduct(id))
                }
            )
        }
        composable<Screen.ManageProduct> {
            val id = it.toRoute<Screen.ManageProduct>().id
            ManageProductScreen(
                navigateBack = {
                    navController.navigateUp()
                },
                id = id
            )
        }
        composable<Screen.Details> {
            DetailsScreen(
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }
        composable<Screen.CategorySearch> {
            val category = ProductCategory.valueOf(it.toRoute<Screen.CategorySearch>().category)
            CategorySearchScreen(
                category = category,
                navigateToDetails = { id ->
                    navController.navigate(Screen.Details(id))
                },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable<Screen.Checkout> {
            val totalAmount = it.toRoute<Screen.Checkout>().totalAmount
            CheckoutScreen(
                navigateBack = { navController.navigateUp() },
                navigateToPaymentCompleted = { isSuccess, error ->
                    println("navGraph: totalAmount: $totalAmount")
                    println("navGraph: isSuccess: $isSuccess")
                    println("navGraph: error: $error")
                    navController.navigate(Screen.PaymentCompleted(isSuccess, error))
                },
                totalAmount = totalAmount.toDoubleOrNull() ?: 0.0
            )
        }
        composable<Screen.PaymentCompleted> {
            PaymentCompletedScreen(
                navigateBack = {
                    navController.navigate(Screen.HomeGraph) {
                        launchSingleTop = true
                        // this will clear backstack completely
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}