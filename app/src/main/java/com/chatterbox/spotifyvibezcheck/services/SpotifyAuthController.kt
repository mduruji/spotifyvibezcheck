package com.chatterbox.spotifyvibezcheck.services

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.chatterbox.spotifyvibezcheck.objects.SpotifyConstants
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationResponse

class SpotifyAuthController(
    private val activity: Activity,
    private val onSuccess: (String) -> Unit,
    private val onError: (String) -> Unit
) {

    fun handleAuthResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != SpotifyConstants.REQUEST_CODE) return

        val response = AuthorizationClient.getResponse(resultCode, data)

        when (response.type) {
            AuthorizationResponse.Type.TOKEN -> {
                val token = response.accessToken
                onSuccess(token)
            }

            AuthorizationResponse.Type.ERROR -> {
                val error = response.error
                onError("Spotify Auth Error: $error")
            }

            else -> {
                Log.d("SpotifyAuth", "Authentication cancelled")
            }
        }
    }
}
