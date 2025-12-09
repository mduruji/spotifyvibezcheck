package com.chatterbox.spotifyvibezcheck.navigation

sealed class NavRoutes(val route: String) {
    object Welcome : NavRoutes("welcome")
    object Login : NavRoutes("login")
    object Signup : NavRoutes("signup")
    object Home : NavRoutes("home")
}