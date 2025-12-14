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
    object PlaylistCreation : NavRoutes("playlist_creation")
    object PlaylistRoom : NavRoutes("playlist_room/{playlistId}") {
        fun createRoute(playlistId: String) = "playlist_room/$playlistId"
    }
    object Suggestion : NavRoutes("suggestion/{playlistId}") {
        fun createRoute(playlistId: String) = "suggestion/$playlistId"
    }
    object AddCollaborators : NavRoutes("add_collaborators/{playlistId}") {
        fun createRoute(playlistId: String) = "add_collaborators/$playlistId"
    }
    object SongSearch : NavRoutes("song_search/{playlistId}") {
        fun createRoute(playlistId: String) = "song_search/$playlistId"
    }
    object ChatRoom : NavRoutes("chat_room/{playlistId}") {
        fun createRoute(playlistId: String) = "chat_room/$playlistId"
    }
}