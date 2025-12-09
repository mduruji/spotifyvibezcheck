package com.chatterbox.spotifyvibezcheck

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chatterbox.spotifyvibezcheck.navigation.NavRoutes
import com.chatterbox.spotifyvibezcheck.screens.HomeScreen
import com.chatterbox.spotifyvibezcheck.screens.LoginScreen
import com.chatterbox.spotifyvibezcheck.screens.SignupScreen
import com.chatterbox.spotifyvibezcheck.screens.WelcomeScreen
import com.chatterbox.spotifyvibezcheck.services.AuthService
import com.chatterbox.spotifyvibezcheck.services.RegisterService
import com.chatterbox.spotifyvibezcheck.services.SpotifyAuthManager
import com.chatterbox.spotifyvibezcheck.objects.SpotifyConstants
import com.spotify.sdk.android.auth.AuthorizationClient
import com.chatterbox.spotifyvibezcheck.ui.theme.SpotifyVibezCheckTheme

class MainActivity : ComponentActivity() {

    lateinit var spotifyAuthManager: SpotifyAuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SpotifyVibezCheckTheme {
                MainScreen(this)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SpotifyConstants.REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, data)
            spotifyAuthManager.handleAuthResponse(response)
        }
    }
}

@Composable
fun MainScreen(activity: MainActivity) {

    val navController = rememberNavController()

    val authService = AuthService()
    val registerService = RegisterService(authService)

    // Track the auth response text
    var spotifyAuthStatus by remember { mutableStateOf<String?>(null) }

    // Create SpotifyAuthManager ONCE with remember
    val spotifyAuthManager = remember {
        SpotifyAuthManager(activity) { success, msg ->
            spotifyAuthStatus = if (success) {
                "SUCCESS: $msg"
            } else {
                "FAILED: $msg"
            }
            println("SpotifyAuthLog → $spotifyAuthStatus")
        }
    }

    // Assign it to MainActivity for onActivityResult
    activity.spotifyAuthManager = spotifyAuthManager

    NavHost(navController = navController, startDestination = NavRoutes.Welcome.route) {

        composable(NavRoutes.Welcome.route) {
            WelcomeScreen(navController)
        }

        composable(NavRoutes.Login.route) {
            LoginScreen(navController, authService)
        }

        composable(NavRoutes.Signup.route) {
            SignupScreen(navController, registerService)
        }

        composable(NavRoutes.Home.route) {
            HomeScreen(
                navController = navController,
                spotifyAuthManager = spotifyAuthManager,
                authStatus = spotifyAuthStatus,
                onStartSpotifyAuth = { spotifyAuthManager.authenticate() }
            )
        }
    }
}
