package br.com.fiap.biomonitor.presentation.navigation

sealed class Screen(val route: String) {


    data object Splash : Screen("splash")


    data object Login : Screen("login")


    data object Register : Screen("register")


    data object Map : Screen("map")


    data object MySightings : Screen("my_sightings")


    data object Profile : Screen("profile")


    data object Gamification : Screen("gamification")


    data object NewSighting : Screen("new_sighting")


    data object SightingDetails : Screen("sighting_details/{sightingId}") {

        fun createRoute(sightingId: Long): String = "sighting_details/$sightingId"
    }

    companion object {

        const val SIGHTING_ID_ARG = "sightingId"
    }
}
