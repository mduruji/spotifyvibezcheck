package com.chatterbox.spotifyvibezcheck.services

import android.app.Activity
import android.content.Intent
import com.chatterbox.spotifyvibezcheck.objects.SpotifyConstants
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

class SpotifyAuthManager(
    private val activity: Activity,
    private val onAuthResult: (Boolean, String) -> Unit
) {

    fun authenticate() {
        val builder = AuthorizationRequest.Builder(
            SpotifyConstants.CLIENT_ID,
            AuthorizationResponse.Type.TOKEN,
            SpotifyConstants.REDIRECT_URI
        )

        builder.setScopes(SpotifyConstants.SCOPES)
        builder.setShowDialog(false)

        val request = builder.build()

        AuthorizationClient.openLoginActivity(
            activity,
            SpotifyConstants.REQUEST_CODE,
            request
        )
    }

    fun handleAuthResponse(response: AuthorizationResponse) {
        when (response.type) {
            AuthorizationResponse.Type.TOKEN -> {
                onAuthResult(true, "Token received")
            }

            AuthorizationResponse.Type.ERROR -> {
                onAuthResult(false, response.error ?: "Unknown error")
            }

            else -> {
                onAuthResult(false, "Cancelled")
            }
        }
    }
}

