package com.chatterbox.spotifyvibezcheck.services

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserService {

    private val db = FirebaseFirestore.getInstance()

    suspend fun updateSpotifyData(
        userId: String,
        spotifyUser: String,
        spotifyProfileUrl: String
    ) {
        val updateMap = mapOf(
            "spotifyUser" to spotifyUser,
            "spotifyProfileUri" to spotifyProfileUrl
        )

        try {
            db.collection("users")
                .document(userId)
                .update(updateMap)
                .await()
            Log.d("UserService", "Spotify data updated successfully for user: $userId")
        } catch (e: Exception) {
            Log.e("UserService", "Failed to update Spotify data for user: $userId", e)
        }
    }
}
