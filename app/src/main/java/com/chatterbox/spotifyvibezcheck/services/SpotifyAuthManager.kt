package com.chatterbox.spotifyvibezcheck.services

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.chatterbox.spotifyvibezcheck.objects.SpotifyConstants
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

class SpotifyAuthManager(
    private val activity: Activity,
    private val onAuthResult: (Boolean, String) -> Unit
) {

    private val prefs: SharedPreferences =
        activity.getSharedPreferences("spotify_prefs", Context.MODE_PRIVATE)

    fun authenticate() {
        // Clear cached token before starting new auth
        prefs.edit().remove("access_token").apply()

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
                val token = response.accessToken
                // Save new token in SharedPreferences
                prefs.edit().putString("access_token", token).apply()
                onAuthResult(true, token)
            }

            AuthorizationResponse.Type.ERROR -> {
                onAuthResult(false, response.error ?: "Unknown error")
            }

            else -> {
                onAuthResult(false, "Cancelled")
            }
        }
    }

    fun getCachedToken(): String? {
        return prefs.getString("access_token", null)
    }
}
