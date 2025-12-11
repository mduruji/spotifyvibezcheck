package com.chatterbox.spotifyvibezcheck.data
data class User(
    val userId: String = "",
    val username: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val friends: List<String> = emptyList(),
    val spotifyUser: String = "",
    val spotifyProfileUri: String = ""
)
