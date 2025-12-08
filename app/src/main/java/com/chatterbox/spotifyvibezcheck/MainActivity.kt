package com.chatterbox.spotifyvibezcheck

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.chatterbox.spotifyvibezcheck.ui.theme.SpotifyVibezCheckTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

// Spotify imports
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

import com.chatterbox.spotifyvibezcheck.objects.SpotifyConstants

class MainActivity : ComponentActivity() {

    private var spotifyAppRemote: SpotifyAppRemote? = null

    // TODO: Replace this with your actual Spotify Client ID
    private val CLIENT_ID = SpotifyConstants.CLIENT_ID
    private val REDIRECT_URI = SpotifyConstants.REDIRECT_URI

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SpotifyVibezCheckTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    // Spotify connection happens in onStart (best practice)
    override fun onStart() {
        super.onStart()
        connectToSpotify()
    }

    // Disconnect in onStop (avoid leaks)
    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }

    private fun connectToSpotify() {
        val params = ConnectionParams.Builder(CLIENT_ID)
            .setRedirectUri(REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(this, params,
            object : Connector.ConnectionListener {
                override fun onConnected(remote: SpotifyAppRemote) {
                    spotifyAppRemote = remote
                    Log.d("SpotifyTest", "Connected to Spotify successfully!")
                }

                override fun onFailure(error: Throwable) {
                    Log.e("SpotifyTest", "Failed to connect: ${error.message}", error)
                }
            }
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpotifyVibezCheckTheme {
        Greeting("Android")
    }
}
