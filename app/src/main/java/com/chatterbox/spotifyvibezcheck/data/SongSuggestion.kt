package com.chatterbox.spotifyvibezcheck.data

data class SongSuggestion(
    val trackId: String = "",
    val trackName: String = "",
    val artistName: String = "",
    val albumArtUrl: String? = null,
    val suggestedBy: String = "",
    val votes: List<String> = emptyList(),
    val genre: String = ""
)
