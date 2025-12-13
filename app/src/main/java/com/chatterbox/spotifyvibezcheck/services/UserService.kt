package com.chatterbox.spotifyvibezcheck.services

import android.util.Log
import com.chatterbox.spotifyvibezcheck.data.User
import com.chatterbox.spotifyvibezcheck.data.UserPlaylist
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserService {

    private val db = FirebaseFirestore.getInstance()

    suspend fun createPlaylistForUser(userId: String, playlist: UserPlaylist): UserPlaylist? {
        return try {
            val playlistDocRef = db.collection("playlists").add(playlist).await()
            val userDocRef = db.collection("users").document(userId)

            userDocRef.update("playlists", FieldValue.arrayUnion(playlistDocRef.id)).await()

            Log.d("UserService", "Playlist created for user $userId with ID: ${playlistDocRef.id}")
            playlist.copy(id = playlistDocRef.id)
        } catch (e: Exception) {
            Log.e("UserService", "Failed to create playlist for user: $userId", e)
            null
        }
    }

    suspend fun getUserPlaylists(userId: String): List<UserPlaylist> {
        return try {
            val userDoc = db.collection("users").document(userId).get().await()
            val playlistIds = userDoc.get("playlists") as? List<String> ?: emptyList()

            playlistIds.mapNotNull { playlistId ->
                val playlistDoc = db.collection("playlists").document(playlistId).get().await()
                playlistDoc.toObject(UserPlaylist::class.java)
            }
        } catch (e: Exception) {
            Log.e("UserService", "Failed to get playlists for user: $userId", e)
            emptyList()
        }
    }


    suspend fun getUser(userId: String): User? {
        return try {
            val snapshot = db.collection("users")
                .document(userId)
                .get()
                .await()

            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            Log.e("UserService", "Failed to fetch user: $userId", e)
            null
        }
    }

    suspend fun updateUserFields(userId: String, fields: Map<String, Any?>): Boolean {
        return try {
            db.collection("users")
                .document(userId)
                .update(fields)
                .await()

            Log.d("UserService", "Updated fields for user: $userId")
            true
        } catch (e: Exception) {
            Log.e("UserService", "Failed to update user fields: $userId", e)
            false
        }
    }

    suspend fun updateEntireUser(userId: String, user: User): Boolean {
        return try {
            db.collection("users")
                .document(userId)
                .set(user)
                .await()

            Log.d("UserService", "User document fully replaced for: $userId")
            true
        } catch (e: Exception) {
            Log.e("UserService", "Failed to replace entire user document: $userId", e)
            false
        }
    }

    suspend fun updateSpotifyData(
        userId: String,
        spotifyUser: String,
        spotifyProfileUrl: String
    ): Boolean {

        val updateMap = mapOf(
            "spotifyUser" to spotifyUser,
            "spotifyProfileUri" to spotifyProfileUrl
        )

        return updateUserFields(userId, updateMap)
    }

    suspend fun getUsername(userId: String): String? {
        return try {
            val document = db.collection("users").document(userId).get().await()
            document.getString("username")
        } catch (e: Exception) {
            Log.e("UserService", "Failed to get username for user: $userId", e)
            null
        }
    }

    suspend fun getSpotifyUserId(userId: String): String? {
        return try {
            val document = db.collection("users").document(userId).get().await()
            document.getString("spotifyUser")
        } catch (e: Exception) {
            Log.e("UserService", "Failed to get spotifyUser for user: $userId", e)
            null
        }
    }

    suspend fun getFriends(userId: String): List<User> {
        return try {
            val document = db.collection("users").document(userId).get().await()
            val friendIds = document.get("friends") as? List<String> ?: emptyList()
            friendIds.mapNotNull { friendId ->
                val friendDoc = db.collection("users").document(friendId).get().await()
                friendDoc.toObject(User::class.java)?.copy(userId = friendDoc.id)
            }
        } catch (e: Exception) {
            Log.e("UserService", "Failed to get friends for user: $userId", e)
            emptyList()
        }
    }

    suspend fun searchUsersByUsername(username: String): List<User> {
        return try {
            val querySnapshot = db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .await()

            querySnapshot.documents.mapNotNull { document ->
                document.toObject(User::class.java)?.copy(userId = document.id)
            }
        } catch (e: Exception) {
            Log.e("UserService", "Failed to search for users by username: $username", e)
            emptyList()
        }
    }

    suspend fun sendFriendRequest(senderId: String, receiverId: String): Boolean {
        return try {
            db.collection("users").document(receiverId).update("requests", FieldValue.arrayUnion(senderId)).await()
            Log.d("UserService", "Friend request sent from $senderId to $receiverId")
            true
        } catch (e: Exception) {
            Log.e("UserService", "Failed to send friend request from $senderId to $receiverId", e)
            false
        }
    }

    suspend fun acceptFriendRequest(currentUserId: String, requesterId: String): Boolean {
        return try {
            val currentUserDocRef = db.collection("users").document(currentUserId)
            val requesterDocRef = db.collection("users").document(requesterId)

            db.runBatch { batch ->
                batch.update(currentUserDocRef, "friends", FieldValue.arrayUnion(requesterId))
                batch.update(currentUserDocRef, "requests", FieldValue.arrayRemove(requesterId))
                batch.update(requesterDocRef, "friends", FieldValue.arrayUnion(currentUserId))
            }.await()

            Log.d("UserService", "Friend request from $requesterId accepted by $currentUserId")
            true
        } catch (e: Exception) {
            Log.e("UserService", "Failed to accept friend request from $requesterId by $currentUserId", e)
            false
        }
    }

    suspend fun getFriendRequests(userId: String): List<User> {
        return try {
            val document = db.collection("users").document(userId).get().await()
            val requestIds = document.get("requests") as? List<String> ?: emptyList()
            requestIds.mapNotNull { friendId ->
                val friendDoc = db.collection("users").document(friendId).get().await()
                friendDoc.toObject(User::class.java)?.copy(userId = friendDoc.id)
            }
        } catch (e: Exception) {
            Log.e("UserService", "Failed to get friend requests for user: $userId", e)
            emptyList()
        }
    }

    suspend fun declineFriendRequest(currentUserId: String, requesterId: String): Boolean {
        return try {
            db.collection("users").document(currentUserId).update("requests", FieldValue.arrayRemove(requesterId)).await()
            Log.d("UserService", "Friend request from $requesterId declined by $currentUserId")
            true
        } catch (e: Exception) {
            Log.e("UserService", "Failed to decline friend request from $requesterId by $currentUserId", e)
            false
        }
    }
}