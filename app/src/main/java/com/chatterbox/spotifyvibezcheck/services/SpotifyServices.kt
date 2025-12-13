package com.chatterbox.spotifyvibezcheck.services

import android.content.Context
import com.chatterbox.spotifyvibezcheck.interfaces.SpotifyWebAPI
import com.chatterbox.spotifyvibezcheck.models.AddTracksToPlaylistRequest
import com.chatterbox.spotifyvibezcheck.models.CreatePlaylistRequest
import com.chatterbox.spotifyvibezcheck.models.Playlist
import com.chatterbox.spotifyvibezcheck.models.PlaylistTracksResponse
import com.chatterbox.spotifyvibezcheck.models.SearchResponse
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

    suspend fun getPlaylistTracks(playlistId: String): Response<PlaylistTracksResponse> {
        val token = tokenProvider() ?: throw IllegalStateException("Missing Spotify token")
        return api.getPlaylistTracks("Bearer $token", playlistId)
    }

    suspend fun createPlaylist(userId: String, name: String): Response<Playlist> {
        val token = tokenProvider() ?: throw IllegalStateException("Missing Spotify token")
        return api.createPlaylist("Bearer $token", userId, CreatePlaylistRequest(name))
    }

    suspend fun addTracksToPlaylist(playlistId: String, trackUris: List<String>): Response<Unit> {
        val token = tokenProvider() ?: throw IllegalStateException("Missing Spotify token")
        return api.addTracksToPlaylist("Bearer $token", playlistId, AddTracksToPlaylistRequest(trackUris))
    }

    suspend fun searchTracks(query: String): Response<SearchResponse> {
        val token = tokenProvider() ?: throw IllegalStateException("Missing Spotify token")
        return api.searchTracks("Bearer $token", query)
    }

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