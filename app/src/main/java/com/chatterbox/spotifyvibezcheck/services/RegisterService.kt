package com.chatterbox.spotifyvibezcheck.services

import android.util.Log
import com.chatterbox.spotifyvibezcheck.objects.FirebaseConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RegisterService(private val authService: AuthService) {

    private val db = FirebaseFirestore.getInstance()

    // Default profile image
    private val defaultProfileImage = FirebaseConstants.DEFAULT_PROFILE_IMG_URL

    suspend fun register(username: String, email: String, password: String): FirebaseUser? {
        return try {
            // 1. Sign up user
            val user = authService.signup(email, password)
            if (user == null) {
                Log.e("RegisterService", "Firebase signup returned null")
                return null
            }

            // 2. Confirm FirebaseAuth has current user
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser == null) {
                Log.e("RegisterService", "Current user is null after signup")
                return null
            }

            val userMap = hashMapOf(
                "userId" to currentUser.uid,
                "username" to username,
                "email" to email,
                "photoUrl" to defaultProfileImage,
                "friends" to emptyList<String>(),
                "friendRequests" to emptyList<String>(),
                "playlists" to emptyList<String>(),
                "spotifyUser" to "",
                "spotifyProfileUri" to ""
            )

            // 4. Write to Firestore
            try {
                db.collection("users")
                    .document(currentUser.uid)
                    .set(userMap)
                    .await()
                Log.d("RegisterService", "Firestore user created: ${currentUser.uid}")
            } catch (e: Exception) {
                Log.e("RegisterService", "Error writing user to Firestore", e)
            }

            user
        } catch (e: Exception) {
            Log.e("RegisterService", "Error during registration", e)
            null
        }
    }
}
