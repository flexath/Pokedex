package com.flexath.pokedex.ui.nav_graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.flexath.pokedex.ui.screens.DetailScreen
import com.flexath.pokedex.ui.screens.HomeScreen

@Composable
fun NavHost(navController: NavHostController) {
    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = HOME_SCREEN
    ) {

        composable(
            route = Screen.Home.route
        ) {
            HomeScreen(navController = navController)
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument(ARG_DOMINANT_COLOR) {
                    type = NavType.IntType
                },
                navArgument(ARG_POKEDEX_NAME) {
                    type = NavType.StringType
                }
            )
        ) {
            val dominantColor = it.arguments?.getInt(ARG_DOMINANT_COLOR)
            val pokedexName = it.arguments?.getString(ARG_POKEDEX_NAME)
            DetailScreen(navController = navController,dominantColor,pokedexName)
        }
    }
}