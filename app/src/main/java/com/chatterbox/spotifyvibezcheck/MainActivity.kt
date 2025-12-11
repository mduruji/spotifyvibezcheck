package com.chatterbox.spotifyvibezcheck

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chatterbox.spotifyvibezcheck.navigation.NavRoutes
import com.chatterbox.spotifyvibezcheck.ui.screens.SpotifyAuthScreen
import com.chatterbox.spotifyvibezcheck.ui.screens.LoginScreen
import com.chatterbox.spotifyvibezcheck.ui.screens.PlaylistScreen
import com.chatterbox.spotifyvibezcheck.ui.screens.SignupScreen
import com.chatterbox.spotifyvibezcheck.ui.screens.WelcomeScreen
import com.chatterbox.spotifyvibezcheck.services.AuthService
import com.chatterbox.spotifyvibezcheck.services.RegisterService
import com.chatterbox.spotifyvibezcheck.services.SpotifyAuthManager
import com.chatterbox.spotifyvibezcheck.objects.SpotifyConstants
import com.chatterbox.spotifyvibezcheck.services.SpotifyService
import com.chatterbox.spotifyvibezcheck.services.UserService
import com.google.firebase.auth.FirebaseAuth
import com.spotify.sdk.android.auth.AuthorizationClient
import com.chatterbox.spotifyvibezcheck.ui.theme.SpotifyVibezCheckTheme
import kotlinx.coroutines.launch

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
            if (::spotifyAuthManager.isInitialized) {
                spotifyAuthManager.handleAuthResponse(response)
            }
        }
    }
}

@Composable
fun MainScreen(activity: MainActivity) {

    val navController = rememberNavController()
    val authService = remember { AuthService() }
    val registerService = remember { RegisterService(authService) }
    val scope = rememberCoroutineScope()

    var spotifyAuthStatus by remember { mutableStateOf<String?>("UNAUTHENTICATED") }

    val userService = remember { UserService() }

    val onSpotifyAuthComplete: suspend (String) -> Unit = { accessToken ->

        val firebaseUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (firebaseUserId == null) {
            spotifyAuthStatus = "ERROR: Firebase user not logged in."
        } else {
            val spotifyService = SpotifyService { accessToken }

            val response = spotifyService.getSpotifyUserProfile(accessToken)
            if (response.isSuccessful) {
                val spotifyUser = response.body()
                if (spotifyUser != null) {
                    val spotifyId = spotifyUser.id
                    val spotifyUrl = spotifyUser.externalUrls.spotify

                    userService.updateSpotifyData(
                        userId = firebaseUserId,
                        spotifyUser = spotifyId,
                        spotifyProfileUrl = spotifyUrl
                    )
                    spotifyAuthStatus = "SUCCESS: Spotify data updated."
                    navController.navigate(NavRoutes.Playlist.route) {
                        popUpTo(NavRoutes.SpotifyAuth.route) { inclusive = true }
                    }
                } else {
                    spotifyAuthStatus = "ERROR: Spotify user profile body is null."
                }
            } else {
                spotifyAuthStatus = "ERROR: Failed to fetch Spotify profile. Code: ${response.code()}"
            }
        }
    }

    val spotifyAuthManager = remember {
        SpotifyAuthManager(activity) { success, tokenOrMsg ->
            if (success && tokenOrMsg != null) {
                spotifyAuthStatus = "TOKEN RECEIVED, UPDATING DATA..."
                scope.launch { onSpotifyAuthComplete(tokenOrMsg) }
            } else {
                spotifyAuthStatus = "FAILED: $tokenOrMsg"
            }
        }
    }

    DisposableEffect(Unit) {
        activity.spotifyAuthManager = spotifyAuthManager
        onDispose { }
    }

    val cachedToken = spotifyAuthManager.getCachedToken()

    LaunchedEffect(cachedToken) {
        if (cachedToken != null && spotifyAuthStatus == "UNAUTHENTICATED") {
            spotifyAuthStatus = "USING CACHED TOKEN..."
            scope.launch { onSpotifyAuthComplete(cachedToken) }
        }
    }

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Welcome.route
    ) {

        composable(NavRoutes.Welcome.route) {
            WelcomeScreen(navController)
        }

        composable(NavRoutes.Login.route) {
            LoginScreen(navController, authService)
        }

        composable(NavRoutes.Signup.route) {
            SignupScreen(navController, registerService)
        }

        composable(NavRoutes.SpotifyAuth.route) {
            SpotifyAuthScreen(
                navController = navController,
                spotifyAuthManager = spotifyAuthManager,
                authStatus = spotifyAuthStatus,
                onStartSpotifyAuth = { spotifyAuthManager.authenticate() }
            )
        }

        composable(NavRoutes.Playlist.route) {
            PlaylistScreen()
        }
    }
}