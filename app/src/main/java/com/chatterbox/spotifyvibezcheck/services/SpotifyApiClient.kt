package com.chatterbox.spotifyvibezcheck.services

import com.chatterbox.spotifyvibezcheck.interfaces.SpotifyWebAPI
import com.chatterbox.spotifyvibezcheck.models.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpotifyApiClient {
    private val spotifyApiService: SpotifyWebAPI

    constructor(token: String) {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.spotify.com/v1/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        spotifyApiService = retrofit.create(SpotifyWebAPI::class.java)
    }

    suspend fun getCurrentUser(): Response<User> {
        return spotifyApiService.getCurrentUser()
    }

    suspend fun getUser(spotifyUserId: String): Response<User> {
        return spotifyApiService.getUser(spotifyUserId)
    }

    companion object {
        fun create(tokenProvider: () -> String?): SpotifyWebAPI {
            val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor { chain ->
                    val requestBuilder = chain.request().newBuilder()
                    tokenProvider()?.let { requestBuilder.addHeader("Authorization", "Bearer $it") }
                    chain.proceed(requestBuilder.build())
                }
                .build()

            return Retrofit.Builder()
                .baseUrl("https://api.spotify.com/v1/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SpotifyWebAPI::class.java)
        }
    }
}