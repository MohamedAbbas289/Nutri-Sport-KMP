package com.nutrisport.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nutrisport.auth.AuthScreen
import com.nutrisport.home.HomeGraphScreen
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
                }
            )
        }
    }
}