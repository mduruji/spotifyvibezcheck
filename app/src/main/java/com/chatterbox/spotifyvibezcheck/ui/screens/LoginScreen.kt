package com.chatterbox.spotifyvibezcheck.screens

import android.util.Log
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.chatterbox.spotifyvibezcheck.navigation.NavRoutes
import com.chatterbox.spotifyvibezcheck.services.AuthService
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController, authService: AuthService) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Log In",
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(48.dp))
        OutlinedTextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text("Email") },
            modifier = Modifier.size(width = 300.dp, height = 60.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text("Password") },
            modifier = Modifier.size(width = 300.dp, height = 60.dp)
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = { 
                Log.d("LoginScreen", "Login button clicked")
                scope.launch {
                    val user = authService.login(emailState.value, passwordState.value)
                    if (user != null) {
                        navController.navigate(NavRoutes.Home.route)
                    }
                }
             },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)),
            modifier = Modifier.size(width = 300.dp, height = 50.dp)
        ) {
            Text(text = "Log In", color = Color.Black, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            modifier = Modifier.size(width = 300.dp, height = 50.dp)
        ) {
            Text(text = "Back", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}
