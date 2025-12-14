package com.chatterbox.spotifyvibezcheck.services

import android.content.Context
import android.util.Log
import com.chatterbox.spotifyvibezcheck.objects.SpotifyConstants
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.client.Subscription
import com.spotify.protocol.types.PlayerState
import android.graphics.Bitmap
import com.spotify.protocol.types.ImageUri

class SpotifyPlaybackManager(private val context: Context) {

    private var spotifyAppRemote: SpotifyAppRemote? = null
    private var playerStateSubscription: Subscription<PlayerState>? = null

    private val connectionParams = ConnectionParams.Builder(SpotifyConstants.CLIENT_ID)
        .setRedirectUri(SpotifyConstants.REDIRECT_URI)
        .showAuthView(true)
        .build()

    fun connect(callback: (Boolean) -> Unit) {
        SpotifyAppRemote.connect(context, connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(appRemote: SpotifyAppRemote) {
                    spotifyAppRemote = appRemote
                    Log.d("SpotifyRemote", "Connected to Spotify App Remote")
                    callback(true)
                }

                override fun onFailure(error: Throwable) {
                    Log.e("SpotifyRemote", "Failed to connect", error)
                    callback(false)
                }
            })
    }

    fun fetchCoverArt(
        imageUri: ImageUri,
        callback: (Bitmap?) -> Unit
    ) {
        spotifyAppRemote
            ?.imagesApi
            ?.getImage(imageUri)
            ?.setResultCallback { bitmap ->
                callback(bitmap)
            }
            ?.setErrorCallback {
                Log.e("SpotifyRemote", "Failed to load cover art", it)
                callback(null)
            }
    }
    fun disconnect() {
        playerStateSubscription?.cancel()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }

    fun play(uri: String) {
        spotifyAppRemote?.playerApi?.play(uri)
    }

    fun pause() {
        spotifyAppRemote?.playerApi?.pause()
    }

    fun resume() {
        spotifyAppRemote?.playerApi?.resume()
    }

    fun skipToNext() {
        spotifyAppRemote?.playerApi?.skipNext()
    }

    fun skipToPrevious() {
        spotifyAppRemote?.playerApi?.skipPrevious()
    }

    fun getPlayerState(callback: (PlayerState?) -> Unit) {
        spotifyAppRemote?.playerApi
            ?.playerState
            ?.setResultCallback { state -> callback(state) }
    }

    fun subscribeToPlayerState(callback: (PlayerState) -> Unit) {
        playerStateSubscription = spotifyAppRemote?.playerApi?.subscribeToPlayerState()
            ?.setEventCallback(callback)
            ?.setErrorCallback {
                Log.e("SpotifyRemote", "Cannot get player state", it)
            } as Subscription<PlayerState>?
    }
}
