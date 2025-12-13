package com.chatterbox.spotifyvibezcheck.services

import android.util.Log
import com.chatterbox.spotifyvibezcheck.data.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RegisterService(private val authService: AuthService) {
    private val db = FirebaseFirestore.getInstance()

    suspend fun register(username: String, email: String, password: String): FirebaseUser? {
        return try {
            val user = authService.signup(email, password)
            if (user != null) {
                val newUser = User(
                    userId = user.uid,
                    username = username,
                    email = email,
                    photoUrl = ""
                )
                db.collection("users").document(user.uid).set(newUser).await()
            }
            user
        } catch (e: Exception) {
            Log.e("RegisterService", "Registration failed", e)
            null
        }
    }
}