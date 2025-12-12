package com.chatterbox.spotifyvibezcheck.services

import android.content.Context
import com.chatterbox.spotifyvibezcheck.interfaces.SpotifyWebAPI
import com.chatterbox.spotifyvibezcheck.models.User
import com.spotify.protocol.types.PlayerState
import retrofit2.Response

class SpotifyService(
    private val context: Context,
    private val tokenProvider: () -> String?
) {
    private val api: SpotifyWebAPI = SpotifyApiClient.create(tokenProvider)
    private val playback = SpotifyPlaybackManager(context)

    suspend fun getCurrentSpotifyUser(): Response<User> {
        val token = tokenProvider() ?: throw IllegalStateException("Missing Spotify token")
        return api.getCurrentUser("Bearer $token")
    }

    suspend fun getUserPlaylists() = api.getUserPlaylists("Bearer ${tokenProvider()}")

    fun connectToSpotifyRemote(callback: (Boolean) -> Unit) {
        playback.connect(callback)
    }

    fun playTrack(trackId: String) {
        playback.play("spotify:track:$trackId")
    }

    fun playPlaylist(playlistId: String) {
        playback.play("spotify:playlist:$playlistId")
    }

    fun pause() = playback.pause()
    fun resume() = playback.resume()
    fun next() = playback.skipToNext()
    fun previous() = playback.skipToPrevious()

    fun getPlayerState(callback: (PlayerState?) -> Unit) =
        playback.getPlayerState(callback)
}
