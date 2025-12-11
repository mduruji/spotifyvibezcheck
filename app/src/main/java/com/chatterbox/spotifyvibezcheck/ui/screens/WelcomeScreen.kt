package com.chatterbox.spotifyvibezcheck.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.chatterbox.spotifyvibezcheck.navigation.NavRoutes

@Composable
fun WelcomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome",
            color = Color.Black,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = { navController.navigate(NavRoutes.Login.route) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)),
            modifier = Modifier.size(width = 300.dp, height = 50.dp)
        ) {
            Text(text = "Log In", color = Color.Black, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate(NavRoutes.Signup.route) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            modifier = Modifier.size(width = 300.dp, height = 50.dp)
        ) {
            Text(text = "Sign up", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}
