package com.chatterbox.spotifyvibezcheck.ui.screens

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chatterbox.spotifyvibezcheck.data.UserPlaylist
import com.chatterbox.spotifyvibezcheck.services.SpotifyService
import com.chatterbox.spotifyvibezcheck.services.UserService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class PlaylistCreationViewModel(application: Application) : AndroidViewModel(application) {

    private val spotifyService = SpotifyService(application.applicationContext) { getApplication<Application>().getSharedPreferences("spotify_prefs", 0).getString("spotify_token", null) }
    private val userService = UserService()

    private val _playlistCreated = MutableSharedFlow<Unit>()
    val playlistCreated = _playlistCreated.asSharedFlow()

    fun createPlaylist(name: String) {
        viewModelScope.launch {
            Log.d("PlaylistCreation", "Starting playlist creation process for: $name")

            val firebaseUserId = FirebaseAuth.getInstance().currentUser?.uid
            val spotifyToken = getApplication<Application>().getSharedPreferences("spotify_prefs", 0).getString("spotify_token", null)

            if (firebaseUserId == null) {
                Log.e("PlaylistCreation", "Firebase user ID is null. Aborting.")
                return@launch
            }
            if (spotifyToken == null) {
                Log.e("PlaylistCreation", "Spotify token is null. Aborting.")
                return@launch
            }

            Log.d("PlaylistCreation", "Fetching Spotify User ID for Firebase user: $firebaseUserId")
            val spotifyUserId = userService.getSpotifyUserId(firebaseUserId)

            if (spotifyUserId == null) {
                Log.e("PlaylistCreation", "Could not fetch Spotify User ID. Aborting.")
                return@launch
            }

            Log.d("PlaylistCreation", "Creating playlist on Spotify for user: $spotifyUserId")
            val response = spotifyService.createPlaylist(spotifyUserId, name)

            if (response.isSuccessful) {
                val spotifyPlaylist = response.body()
                Log.d("PlaylistCreation", "Spotify playlist created successfully: ${spotifyPlaylist?.name}")

                if (spotifyPlaylist != null) {
                    val userPlaylist = UserPlaylist(
                        id = spotifyPlaylist.id,
                        name = spotifyPlaylist.name,
                        ownerId = firebaseUserId,
                        imageUrl = spotifyPlaylist.images.firstOrNull()?.url
                    )

                    Log.d("PlaylistCreation", "Saving playlist to Firebase for user: $firebaseUserId")
                    val firebasePlaylist = userService.createPlaylistForUser(firebaseUserId, userPlaylist)
                    if (firebasePlaylist != null) {
                        Log.d("PlaylistCreation", "Playlist saved to Firebase. Emitting event.")
                        _playlistCreated.emit(Unit)
                    } else {
                        Log.e("PlaylistCreation", "Failed to save playlist to Firebase.")
                    }
                } else {
                    Log.e("PlaylistCreation", "Spotify API response body was null.")
                }

            } else {
                val errorBody = response.errorBody()?.string() ?: "No error body"
                Log.e("PlaylistCreation", "Failed to create Spotify playlist. Code: ${response.code()}, Error: $errorBody")
            }
        }
    }
}