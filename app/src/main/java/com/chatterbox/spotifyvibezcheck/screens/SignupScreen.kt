package com.chatterbox.spotifyvibezcheck.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.chatterbox.spotifyvibezcheck.services.RegisterService
import kotlinx.coroutines.launch

private fun validateUsername(username: String): String? {
    return when {
        username.length !in 3..30 -> "Username must be between 3 and 30 characters."
        username.first() !in 'a'..'z' -> "Username must start with a letter."
        username.endsWith(".") -> "Username cannot end with a period."
        username.contains("..") -> "Username cannot contain consecutive periods."
        !"^[a-z0-9_.]+$".toRegex().matches(username) -> "Can only contain lowercase letters, numbers, underscores, and periods."
        else -> null
    }
}

@Composable
fun SignupScreen(navController: NavController, registerService: RegisterService) {
    val usernameState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val confirmPasswordState = remember { mutableStateOf("") }
    val usernameError = remember { mutableStateOf<String?>(null) }

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
            text = "Create an account",
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(
            value = usernameState.value,
            onValueChange = {
                val cleanUsername = it.lowercase()
                usernameState.value = cleanUsername
                usernameError.value = validateUsername(cleanUsername)
            },
            label = { Text("Username") },
            isError = usernameError.value != null,
            supportingText = {
                usernameError.value?.let {
                    Text(text = it, color = Color.Red)
                }
            },
            modifier = Modifier.width(300.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text("Email") },
            modifier = Modifier.width(300.dp),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text("Password") },
            modifier = Modifier.width(300.dp),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = confirmPasswordState.value,
            onValueChange = { confirmPasswordState.value = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.width(300.dp),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                scope.launch {
                    val user = registerService.register(
                        usernameState.value,
                        emailState.value,
                        passwordState.value
                    )
                    if (user != null) {
                        navController.navigate(NavRoutes.Login.route)
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)),
            modifier = Modifier.width(300.dp),
            enabled = usernameError.value == null &&
                    usernameState.value.isNotEmpty() &&
                    emailState.value.isNotEmpty() &&
                    passwordState.value.isNotEmpty() &&
                    passwordState.value == confirmPasswordState.value
        ) {
            Text(text = "Sign Up", color = Color.Black, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            modifier = Modifier.width(300.dp)
        ) {
            Text(text = "Back", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}
