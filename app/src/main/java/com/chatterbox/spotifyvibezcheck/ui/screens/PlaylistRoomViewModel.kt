package com.chatterbox.spotifyvibezcheck.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chatterbox.spotifyvibezcheck.models.PlaylistTrack
import com.chatterbox.spotifyvibezcheck.services.SpotifyService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistRoomViewModel(application: Application) : AndroidViewModel(application) {

    // Retrieve token from SharedPreferences (same strategy as CreationViewModel)
    private val spotifyService = SpotifyService(application.applicationContext) {
        getApplication<Application>().getSharedPreferences("spotify_prefs", 0)
            .getString("spotify_token", null)
    }

    private val _tracks = MutableStateFlow<List<PlaylistTrack>>(emptyList())
    val tracks = _tracks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadTracks(playlistId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val response = spotifyService.getPlaylistTracks(playlistId)
            if (response.isSuccessful && response.body() != null) {
                _tracks.value = response.body()!!.items
            } else {
                //
            }
            _isLoading.value = false
        }
    }

    fun playTrack(trackUri: String) {
        // Implement playback logic here using spotifyService.play(trackUri)
    }
}