package br.com.fiap.biomonitor.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.fiap.biomonitor.presentation.screens.auth.LoginScreen
import br.com.fiap.biomonitor.presentation.screens.auth.RegisterScreen
import br.com.fiap.biomonitor.presentation.screens.gamification.GamificationScreen
import br.com.fiap.biomonitor.presentation.screens.map.MapScreen
import br.com.fiap.biomonitor.presentation.screens.profile.ProfileScreen
import br.com.fiap.biomonitor.presentation.screens.sighting.MySightingsScreen
import br.com.fiap.biomonitor.presentation.screens.sighting.NewSightingScreen
import br.com.fiap.biomonitor.presentation.screens.sighting.SightingDetailsScreen
import br.com.fiap.biomonitor.presentation.screens.splash.SplashScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(route = Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToMap = {
                    navController.navigate(Screen.Map.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }


        composable(route = Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Map.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }


        composable(route = Screen.Register.route) {
            RegisterScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.Map.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }


        composable(route = Screen.Map.route) {
            MapScreen(
                onNavigateToNewSighting = {
                    navController.navigate(Screen.NewSighting.route)
                },
                onNavigateToSightingDetails = { sightingId ->
                    navController.navigate(Screen.SightingDetails.createRoute(sightingId))
                }
            )
        }


        composable(route = Screen.MySightings.route) {
            MySightingsScreen(
                onNavigateToSightingDetails = { sightingId ->
                    navController.navigate(Screen.SightingDetails.createRoute(sightingId))
                },
                onNavigateToNewSighting = {
                    navController.navigate(Screen.NewSighting.route)
                }
            )
        }


        composable(route = Screen.Profile.route) {
            ProfileScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }


        composable(route = Screen.Gamification.route) {
            GamificationScreen()
        }


        composable(route = Screen.NewSighting.route) {
            NewSightingScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSightingSaved = {
                    navController.popBackStack()
                }
            )
        }


        composable(
            route = Screen.SightingDetails.route,
            arguments = listOf(
                navArgument(Screen.SIGHTING_ID_ARG) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val sightingId = backStackEntry.arguments?.getLong(Screen.SIGHTING_ID_ARG) ?: return@composable
            SightingDetailsScreen(
                sightingId = sightingId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSightingDeleted = {
                    navController.popBackStack()
                }
            )
        }
    }
}
