package com.chatterbox.spotifyvibezcheck.services

import com.chatterbox.spotifyvibezcheck.interfaces.SpotifyWebAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpotifyApiClient {
    companion object {
        private const val BASE_URL = "https://api.spotify.com/v1/"

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
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SpotifyWebAPI::class.java)
        }
    }
}