package com.chatterbox.spotifyvibezcheck.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chatterbox.spotifyvibezcheck.services.SpotifyAuthManager

@Composable
fun SpotifyAuthScreen(
    navController: NavController,
    spotifyAuthManager: SpotifyAuthManager,
    authStatus: String?,                     // NEW
    onStartSpotifyAuth: () -> Unit           // NEW callback
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (!authStatus.isNullOrEmpty()) {
            val color = if (authStatus.contains("success", true)) Color(0xFF1DB954) else Color.Red

            Text(
                text = authStatus,
                color = color,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }

        Button(
            onClick = { onStartSpotifyAuth() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)),
            modifier = Modifier.size(width = 300.dp, height = 50.dp)
        ) {
            Text(
                text = "Login to Spotify",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
