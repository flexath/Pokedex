package com.flexath.pokedex.ui.nav_graph

const val HOME_SCREEN = "home_screen"
const val DETAIL_SCREEN = "detail_screen"

const val ARG_DOMINANT_COLOR = "color"
const val ARG_POKEDEX_NAME = "name"

sealed class Screen(val route: String) {
    data object Home : Screen(route = HOME_SCREEN)
    data object Detail : Screen(route = "$DETAIL_SCREEN/{$ARG_DOMINANT_COLOR}/{$ARG_POKEDEX_NAME}") {
        fun passByColorAndName(dominantColor: Int, pokedexName: String): String {
            return "$DETAIL_SCREEN/$dominantColor/$pokedexName"
        }
    }
}
