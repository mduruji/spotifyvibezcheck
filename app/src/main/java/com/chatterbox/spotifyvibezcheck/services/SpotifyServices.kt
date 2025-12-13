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

    suspend fun getCurrentSpotifyUser(): Response<User> =
        api.getCurrentUser()

    suspend fun getUserPlaylists() =
        api.getUserPlaylists()

    suspend fun getPlaylistTracks(playlistId: String): Response<PlaylistTracksResponse> =
        api.getPlaylistTracks(playlistId)

    suspend fun createPlaylist(userId: String, name: String): Response<Playlist> =
        api.createPlaylist(userId, CreatePlaylistRequest(name))

    suspend fun addTracksToPlaylist(
        playlistId: String,
        trackUris: List<String>
    ): Response<Unit> =
        api.addTracksToPlaylist(playlistId, AddTracksToPlaylistRequest(trackUris))

    suspend fun searchTracks(query: String): Response<SearchResponse> =
        api.searchTracks(query)
}
