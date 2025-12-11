package com.chatterbox.spotifyvibezcheck.objects

object SpotifyConstants {
    const val CLIENT_ID = "b4bb5abaebcc4111990b9efd861b5011"
    const val REDIRECT_URI = "spotifyvibezcheck://spotify-auth"
    const val REQUEST_CODE = 1337

    // Spotify scopes - adjust based on your needs
    val SCOPES = arrayOf(
        "user-read-private",
        "user-read-email",
        "user-read-playback-state",
        "user-modify-playback-state",
        "user-read-currently-playing",
        "playlist-read-private",
        "playlist-read-collaborative",
        "playlist-modify-public",
        "playlist-modify-private"
    )
}