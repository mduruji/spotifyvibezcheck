package com.chatterbox.spotifyvibezcheck.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chatterbox.spotifyvibezcheck.models.PlaylistTrack
import com.chatterbox.spotifyvibezcheck.services.SpotifyService
import com.chatterbox.spotifyvibezcheck.services.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistRoomViewModel(application: Application) : AndroidViewModel(application) {

    private val userService = UserService()
    private val spotifyService = SpotifyService(application.applicationContext) {
        getApplication<Application>().getSharedPreferences("spotify_prefs", 0)
            .getString("spotify_token", null)
    }

    private val _tracks = MutableStateFlow<List<PlaylistTrack>>(emptyList())
    val tracks = _tracks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // We receive the FIRESTORE ID here
    fun loadTracks(firestorePlaylistId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            // 1. Get the Playlist Document from Firestore first
            val playlist = userService.getPlaylist(firestorePlaylistId)

            if (playlist != null && playlist.spotifyId.isNotEmpty()) {
                // 2. Use the stored spotifyId to call the API
                val response = spotifyService.getPlaylistTracks(playlist.spotifyId)

                if (response.isSuccessful && response.body() != null) {
                    _tracks.value = response.body()!!.items
                }
            } else {
                // Handle error: Playlist not found or missing Spotify ID
            }
            _isLoading.value = false
        }
    }
}