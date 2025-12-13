package com.chatterbox.spotifyvibezcheck.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chatterbox.spotifyvibezcheck.data.SongSuggestion
import com.chatterbox.spotifyvibezcheck.models.Track
import com.chatterbox.spotifyvibezcheck.services.SpotifyService
import com.chatterbox.spotifyvibezcheck.services.UserService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SongSearchViewModel(application: Application) : AndroidViewModel(application) {

    private val spotifyService = SpotifyService(application.applicationContext) { getApplication<Application>().getSharedPreferences("spotify_prefs", 0).getString("spotify_token", null) }
    private val userService = UserService()

    private val _searchResults = MutableStateFlow<List<Track>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    fun searchTracks(query: String) {
        viewModelScope.launch {
            val response = spotifyService.searchTracks(query)
            if (response.isSuccessful) {
                _searchResults.value = response.body()?.tracks?.items ?: emptyList()
            }
        }
    }

    fun addSuggestions(playlistId: String, tracks: List<Track>) {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            tracks.forEach { track ->
                val suggestion = SongSuggestion(
                    trackId = track.id,
                    trackName = track.name,
                    artistName = track.artists.firstOrNull()?.name ?: "Unknown",
                    albumArtUrl = track.album.images.firstOrNull()?.url,
                    suggestedBy = userId,
                    votes = listOf(userId)
                )
                userService.addSongSuggestion(playlistId, suggestion)
            }
        }
    }
}