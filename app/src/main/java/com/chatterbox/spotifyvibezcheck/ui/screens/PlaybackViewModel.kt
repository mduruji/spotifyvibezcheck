package com.chatterbox.spotifyvibezcheck.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.chatterbox.spotifyvibezcheck.services.SpotifyPlaybackManager
import com.spotify.protocol.types.PlayerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlaybackViewModel(application: Application) : AndroidViewModel(application) {

    private val playbackManager = SpotifyPlaybackManager(application.applicationContext)

    private val _playerState = MutableStateFlow<PlayerState?>(null)
    val playerState = _playerState.asStateFlow()

    private val _connected = MutableStateFlow(false)
    val connected = _connected.asStateFlow()

    fun connect() {
        playbackManager.connect { success ->
            _connected.value = success
            if (success) {
                playbackManager.subscribeToPlayerState { state ->
                    _playerState.value = state
                }
            }
        }
    }

    fun play(uri: String) = playbackManager.play(uri)
    fun pause() = playbackManager.pause()
    fun resume() = playbackManager.resume()
    fun next() = playbackManager.skipToNext()
    fun prev() = playbackManager.skipToPrevious()

    override fun onCleared() {
        super.onCleared()
        playbackManager.disconnect()
    }
}
