package com.nutrisport.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.nutrisport.admin_panel.AdminPanelScreen
import com.nutrisport.auth.AuthScreen
import com.nutrisport.details.DetailsScreen
import com.nutrisport.home.HomeGraphScreen
import com.nutrisport.manage_product.ManageProductScreen
import com.nutrisport.profile.ProfileScreen
import com.nutrisport.shared.navigation.Screen

@Composable
fun SetUpNavGraph(
    startDestination: Screen = Screen.Auth
) {
    val navController = rememberNavController()
    //there is 2 navHost in this application till now one for the auth and home screen
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
    }
}