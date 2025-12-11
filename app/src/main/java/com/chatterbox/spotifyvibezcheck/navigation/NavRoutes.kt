package com.chatterbox.spotifyvibezcheck.navigation

sealed class NavRoutes(val route: String) {
    object Welcome : NavRoutes("welcome")
    object Login : NavRoutes("login")
    object Signup : NavRoutes("signup")
    object SpotifyAuth : NavRoutes("spotify_auth")
    object Playlist : NavRoutes("playlist")
}