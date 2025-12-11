package com.chatterbox.spotifyvibezcheck.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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

    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordMismatchError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var registrationError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    fun checkPasswordMismatch() {
        passwordMismatchError = passwordState.value != confirmPasswordState.value && confirmPasswordState.value.isNotEmpty()
    }

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
                usernameError = validateUsername(cleanUsername)
            },
            label = { Text("Username") },
            isError = usernameError != null,
            supportingText = {
                usernameError?.let {
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
            onValueChange = {
                passwordState.value = it
                checkPasswordMismatch()
            },
            label = { Text("Password") },
            modifier = Modifier.width(300.dp),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = confirmPasswordState.value,
            onValueChange = {
                confirmPasswordState.value = it
                checkPasswordMismatch()
            },
            label = { Text("Confirm Password") },
            modifier = Modifier.width(300.dp),
            singleLine = true,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(imageVector = image, contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password")
                }
            },
            isError = passwordMismatchError,
            supportingText = {
                if (passwordMismatchError) {
                    Text("Passwords do not match.", color = Color.Red)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        registrationError?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(bottom = 8.dp))
        }

        if (isLoading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                isLoading = true
                registrationError = null
                scope.launch {
                    try {
                        val user = registerService.register(
                            usernameState.value,
                            emailState.value,
                            passwordState.value
                        )
                        if (user != null) {
                            navController.navigate(NavRoutes.Login.route)
                        } else {
                            registrationError = "Registration failed. The email might already be in use."
                        }
                    } catch (e: Exception) {
                        registrationError = "An unexpected error occurred: ${e.message}"
                    } finally {
                        isLoading = false
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954)),
            modifier = Modifier.width(300.dp),
            enabled = !isLoading && usernameError == null &&
                    usernameState.value.isNotEmpty() &&
                    emailState.value.isNotEmpty() &&
                    passwordState.value.isNotEmpty() &&
                    !passwordMismatchError
        ) {
            Text(text = "Sign Up", color = Color.Black, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            modifier = Modifier.width(300.dp),
            enabled = !isLoading
        ) {
            Text(text = "Back", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}
