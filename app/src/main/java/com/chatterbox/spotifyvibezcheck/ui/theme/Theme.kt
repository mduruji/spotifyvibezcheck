package com.chatterbox.spotifyvibezcheck.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = SpotifyGreen,
    background = SpotifyBlack,
    surface = SpotifySurface,
    onPrimary = SpotifyBlack,
    onBackground = SpotifyWhite,
    onSurface = SpotifyWhite
)

@Composable
fun SpotifyVibezCheckTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
