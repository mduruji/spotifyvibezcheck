package com.chatterbox.spotifyvibezcheck.navigation

sealed class NavRoutes(val route: String) {
    object Welcome : NavRoutes("welcome")
    object Login : NavRoutes("login")
    object Signup : NavRoutes("signup")
    object SpotifyAuth : NavRoutes("spotify_auth")
    object Playlist : NavRoutes("playlist")
    object Playback : NavRoutes("playback")
    object Profile : NavRoutes("profile")
    object FriendSearch : NavRoutes("friend_search")
    object Request : NavRoutes("request")
}