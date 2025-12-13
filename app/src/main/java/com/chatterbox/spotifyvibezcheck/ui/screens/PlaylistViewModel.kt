package com.chatterbox.spotifyvibezcheck.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chatterbox.spotifyvibezcheck.data.UserPlaylist
import com.chatterbox.spotifyvibezcheck.services.SpotifyService
import com.chatterbox.spotifyvibezcheck.services.UserService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistViewModel(application: Application) : AndroidViewModel(application) {

    private val userService = UserService()
    private val spotifyService = SpotifyService(application.applicationContext) {
        getApplication<Application>().getSharedPreferences("spotify_prefs", 0)
            .getString("spotify_token", null)
    }

    private val _playlists = MutableStateFlow<List<UserPlaylist>>(emptyList())
    val playlists = _playlists.asStateFlow()

    init {
        spotifyService.connectRemote { }
    }

    fun fetchPlaylists() {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                _playlists.value = userService.getUserPlaylists(userId)
            }
        }
    }

    fun playPlaylist(playlist: UserPlaylist) {
        if (playlist.spotifyId.isNotEmpty()) {
            spotifyService.playTrack("spotify:playlist:${playlist.spotifyId}")
        }
    }

    fun deletePlaylist(playlist: UserPlaylist) {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                spotifyService.unfollowPlaylist(playlist.spotifyId)
                userService.deletePlaylist(userId, playlist.id)
                fetchPlaylists() // Refresh the list
            }
        }
    }
}