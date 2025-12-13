package com.chatterbox.spotifyvibezcheck.data

data class SongSuggestion(
    val trackId: String = "",
    val trackName: String = "",
    val artistName: String = "",
    val albumArtUrl: String? = null,
    val suggestedBy: String = "", // UserID of the person who suggested the song
    val votes: List<String> = emptyList() // List of UserIDs who have voted for the song
)
