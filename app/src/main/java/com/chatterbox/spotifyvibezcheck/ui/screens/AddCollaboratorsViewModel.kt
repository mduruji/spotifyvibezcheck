package com.chatterbox.spotifyvibezcheck.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chatterbox.spotifyvibezcheck.data.User
import com.chatterbox.spotifyvibezcheck.services.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddCollaboratorsViewModel(application: Application) : AndroidViewModel(application) {

    private val userService = UserService()

    private val _searchResults = MutableStateFlow<List<User>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    fun searchUsers(query: String) {
        viewModelScope.launch {
            _searchResults.value = userService.searchUsersByUsername(query)
        }
    }

    fun addCollaborators(playlistId: String, collaboratorIds: List<String>) {
        viewModelScope.launch {
            val playlist = userService.getPlaylist(playlistId) ?: return@launch
            val updatedCollaborators = (playlist.collaborators + collaboratorIds).distinct()
            userService.updatePlaylistCollaborators(playlistId, updatedCollaborators)

            collaboratorIds.forEach { collaboratorId ->
                userService.addPlaylistToUser(collaboratorId, playlistId)
            }
        }
    }
}