package com.chatterbox.spotifyvibezcheck.services

import android.util.Log
import com.chatterbox.spotifyvibezcheck.data.SongSuggestion
import com.chatterbox.spotifyvibezcheck.data.User
import com.chatterbox.spotifyvibezcheck.data.UserPlaylist
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserService {

    private val db = FirebaseFirestore.getInstance()

    /**
     * Creates a new playlist in the `playlists` collection and adds the playlist ID to the user's `playlists` array.
     */
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

    /**
     * Gets a user's playlists.
     */
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

    suspend fun getPlaylist(playlistId: String): UserPlaylist? {
        return try {
            db.collection("playlists").document(playlistId).get().await().toObject(UserPlaylist::class.java)
        } catch (e: Exception) {
            Log.e("UserService", "Failed to get playlist: $playlistId", e)
            null
        }
    }

    suspend fun deletePlaylist(userId: String, playlistId: String): Boolean {
        return try {
            val userDocRef = db.collection("users").document(userId)
            userDocRef.update("playlists", FieldValue.arrayRemove(playlistId)).await()

            db.collection("playlists").document(playlistId).delete().await()
            Log.d("UserService", "Playlist deleted: $playlistId for user: $userId")
            true
        } catch (e: Exception) {
            Log.e("UserService", "Failed to delete playlist: $playlistId for user: $userId", e)
            false
        }
    }

    suspend fun addSongsToPlaylist(playlistId: String, trackIds: List<String>): Boolean {
        return try {
            db.collection("playlists").document(playlistId).update("trackIds", FieldValue.arrayUnion(*trackIds.toTypedArray())).await()
            true
        } catch (e: Exception) {
            Log.e("UserService", "Failed to add songs to playlist: $playlistId", e)
            false
        }
    }

    suspend fun addSongSuggestion(playlistId: String, suggestion: SongSuggestion): Boolean {
        return try {
            val playlistRef = db.collection("playlists").document(playlistId)
            db.runBatch {
                it.update(playlistRef, "suggestedSongs", FieldValue.arrayUnion(suggestion))
                it.update(playlistRef, "numberOfSongs", FieldValue.increment(1))
            }.await()
            true
        } catch (e: Exception) {
            Log.e("UserService", "Failed to add song suggestion to playlist: $playlistId", e)
            false
        }
    }

    suspend fun voteForSong(playlistId: String, trackId: String, userId: String): Boolean {
        return try {
            val playlist = getPlaylist(playlistId) ?: return false
            val updatedSuggestions = playlist.suggestedSongs.map {
                if (it.trackId == trackId) {
                    it.copy(votes = it.votes + userId)
                } else {
                    it
                }
            }
            db.collection("playlists").document(playlistId).update("suggestedSongs", updatedSuggestions).await()
            true
        } catch (e: Exception) {
            Log.e("UserService", "Failed to vote for song: $trackId in playlist: $playlistId", e)
            false
        }
    }

    suspend fun removeSuggestion(playlistId: String, trackId: String): Boolean {
        return try {
            val playlist = getPlaylist(playlistId) ?: return false
            val updatedSuggestions = playlist.suggestedSongs.filter { it.trackId != trackId }
            db.collection("playlists").document(playlistId).update("suggestedSongs", updatedSuggestions).await()
            true
        } catch (e: Exception) {
            Log.e("UserService", "Failed to remove suggestion: $trackId from playlist: $playlistId", e)
            false
        }
    }


    /**
     * Fetch a user document by userId.
     */
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


    /**
     * Update ANY user fields (partial update).
     * Example:
     * updateUserFields(userId, mapOf("username" to "newName"))
     */
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


    /**
     * Update the *entire* user document.
     * Uses `.set(user)` to overwrite the document with the new User object.
     */
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


    /**
     * Spotify updater (calls the generic partial update function).
     */
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

    /**
     * Gets a user's username by their ID.
     */
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

    /**
     * Gets a user's friends.
     */
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

    /**
     * Search for users by their username.
     */
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

    /**
     * Sends a friend request from one user to another.
     */
    suspend fun sendFriendRequest(senderId: String, receiverId: String): Boolean {
        return try {
            db.collection("users").document(receiverId).update("friendRequests", FieldValue.arrayUnion(senderId)).await()
            Log.d("UserService", "Friend request sent from $senderId to $receiverId")
            true
        } catch (e: Exception) {
            Log.e("UserService", "Failed to send friend request from $senderId to $receiverId", e)
            false
        }
    }

    /**
     * Accepts a friend request.
     * This adds each user to the other's friends list and removes the request.
     */
    suspend fun acceptFriendRequest(currentUserId: String, requesterId: String): Boolean {
        return try {
            val currentUserDocRef = db.collection("users").document(currentUserId)
            val requesterDocRef = db.collection("users").document(requesterId)

            db.runBatch { batch ->
                batch.update(currentUserDocRef, "friends", FieldValue.arrayUnion(requesterId))
                batch.update(currentUserDocRef, "friendRequests", FieldValue.arrayRemove(requesterId))
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
            val requestIds = document.get("friendRequests") as? List<String> ?: emptyList()
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
            db.collection("users").document(currentUserId).update("friendRequests", FieldValue.arrayRemove(requesterId)).await()
            Log.d("UserService", "Friend request from $requesterId declined by $currentUserId")
            true
        } catch (e: Exception) {
            Log.e("UserService", "Failed to decline friend request from $requesterId by $currentUserId", e)
            false
        }
    }
}