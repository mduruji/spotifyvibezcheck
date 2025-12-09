package com.chatterbox.spotifyvibezcheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
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
import com.chatterbox.spotifyvibezcheck.ui.theme.SpotifyVibezCheckTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SpotifyVibezCheckTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val authService = AuthService()
    val registerService = RegisterService(authService)

    NavHost(navController = navController, startDestination = NavRoutes.Welcome.route) {
        composable(NavRoutes.Welcome.route) {
            WelcomeScreen(navController = navController)
        }
        composable(NavRoutes.Login.route) {
            LoginScreen(navController = navController, authService = authService)
        }
        composable(NavRoutes.Signup.route) {
            SignupScreen(navController = navController, registerService = registerService)
        }
        composable(NavRoutes.Home.route) {
            HomeScreen()
        }
    }
}
