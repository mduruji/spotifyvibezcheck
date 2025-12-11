package com.chatterbox.spotifyvibezcheck.interfaces

import com.chatterbox.spotifyvibezcheck.models.CurrentlyPlaying
import com.chatterbox.spotifyvibezcheck.models.PlaylistResponse
import com.chatterbox.spotifyvibezcheck.models.SearchResponse
import com.chatterbox.spotifyvibezcheck.models.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
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
