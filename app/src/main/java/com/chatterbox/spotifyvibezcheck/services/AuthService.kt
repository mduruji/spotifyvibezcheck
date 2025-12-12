package com.chatterbox.spotifyvibezcheck.services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthService {
    private val auth = FirebaseAuth.getInstance()

    suspend fun login(email: String, password: String): FirebaseUser? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Log.d("AuthService", "Login successful for user: ${result.user?.uid}")
            result.user
        } catch (e: Exception) {
            Log.e("AuthService", "Login failed", e)
            null
        }
    }

    suspend fun signup(email: String, password: String): FirebaseUser? {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Log.d("AuthService", "Signup successful for user: ${result.user?.uid}")
            result.user
        } catch (e: Exception) {
            Log.e("AuthService", "Signup failed", e)
            null
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
