package com.chatterbox.spotifyvibezcheck.interfaces

import com.chatterbox.spotifyvibezcheck.models.AddTracksToPlaylistRequest
import com.chatterbox.spotifyvibezcheck.models.CreatePlaylistRequest
import com.chatterbox.spotifyvibezcheck.models.CurrentlyPlaying
import com.chatterbox.spotifyvibezcheck.models.Playlist
import com.chatterbox.spotifyvibezcheck.models.PlaylistResponse
import com.chatterbox.spotifyvibezcheck.models.PlaylistTracksResponse
import com.chatterbox.spotifyvibezcheck.models.SearchResponse
import com.chatterbox.spotifyvibezcheck.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyWebAPI {
    @GET("me")
    suspend fun getCurrentUser(
        @Header("Authorization") authorization: String
    ): Response<User>

    @GET("me/playlists")
    suspend fun getUserPlaylists(
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int = 20
    ): Response<PlaylistResponse>

    @GET("playlists/{playlist_id}/tracks")
    suspend fun getPlaylistTracks(
        @Header("Authorization") authorization: String,
        @Path("playlist_id") playlistId: String
    ): Response<PlaylistTracksResponse>

    @POST("users/{user_id}/playlists")
    suspend fun createPlaylist(
        @Header("Authorization") authorization: String,
        @Path("user_id") userId: String,
        @Body body: CreatePlaylistRequest
    ): Response<Playlist>

    @POST("playlists/{playlist_id}/tracks")
    suspend fun addTracksToPlaylist(
        @Header("Authorization") authorization: String,
        @Path("playlist_id") playlistId: String,
        @Body body: AddTracksToPlaylistRequest
    ): Response<Unit>

    @GET("me/player/currently-playing")
    suspend fun getCurrentlyPlaying(
        @Header("Authorization") authorization: String
    ): Response<CurrentlyPlaying>

    @GET("search")
    suspend fun searchTracks(
        @Header("Authorization") authorization: String,
        @Query("q") query: String,
        @Query("type") type: String = "track",
        @Query("limit") limit: Int = 20
    ): Response<SearchResponse>
}
