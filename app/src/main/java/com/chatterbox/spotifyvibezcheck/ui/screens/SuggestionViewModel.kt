package com.chatterbox.spotifyvibezcheck.ui.screens

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chatterbox.spotifyvibezcheck.data.UserPlaylist
import com.chatterbox.spotifyvibezcheck.services.SpotifyService
import com.chatterbox.spotifyvibezcheck.services.UserService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SuggestionViewModel(application: Application) : AndroidViewModel(application) {

    private val userService = UserService()
    private val spotifyService = SpotifyService(application.applicationContext) { getApplication<Application>().getSharedPreferences("spotify_prefs", 0).getString("spotify_token", null) }

    private val _playlist = MutableStateFlow<UserPlaylist?>(null)
    val playlist = _playlist.asStateFlow()

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser = _currentUser.asStateFlow()

    init {
        spotifyService.connectRemote { }
    }

    fun loadPlaylist(playlistId: String) {
        viewModelScope.launch {
            val playlistData = userService.getPlaylist(playlistId)
            val updatedPlaylist = playlistData?.copy(
                suggestedSongs = playlistData.suggestedSongs.map { it.copy(genre = "R&B") }
            )
            _playlist.value = updatedPlaylist
        }
    }

    fun loadCurrentUser() {
        _currentUser.value = FirebaseAuth.getInstance().currentUser
    }

    fun voteForSong(playlistId: String, trackId: String) {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch

            if (userService.voteForSong(playlistId, trackId, userId)) {
                val updatedPlaylist = userService.getPlaylist(playlistId)
                val suggestion = updatedPlaylist?.suggestedSongs?.find { it.trackId == trackId }

                if (updatedPlaylist != null && suggestion != null) {
                    val collaboratorCount = if (updatedPlaylist.collaborators.isNotEmpty()) updatedPlaylist.collaborators.size else 1

                    if (suggestion.votes.size >= collaboratorCount) {

                        val realSpotifyId = updatedPlaylist.spotifyId

                        if (realSpotifyId.isNotEmpty()) {
                            spotifyService.addTracksToPlaylist(realSpotifyId, listOf("spotify:track:$trackId"))

                            userService.addSongsToPlaylist(playlistId, listOf(trackId))
                            userService.removeSuggestion(playlistId, trackId)
                        }
                    }
                }

                loadPlaylist(playlistId)
            }
        }
    }

    fun playTrack(trackId: String) {
        spotifyService.playTrack("spotify:track:$trackId")
    }
}