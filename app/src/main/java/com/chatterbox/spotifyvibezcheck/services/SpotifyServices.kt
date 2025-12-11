package com.chatterbox.spotifyvibezcheck.services

import com.chatterbox.spotifyvibezcheck.interfaces.SpotifyWebAPI
import com.chatterbox.spotifyvibezcheck.models.User
import retrofit2.Response

class SpotifyService(private val tokenProvider: () -> String?) {

    private val api: SpotifyWebAPI = SpotifyApiClient.create(tokenProvider)

    suspend fun getSpotifyUserProfile(accessToken: String): Response<User> {
        val token = tokenProvider() ?: throw IllegalStateException("No Spotify token available")
        return api.getCurrentUser("Bearer $token")
    }
}
