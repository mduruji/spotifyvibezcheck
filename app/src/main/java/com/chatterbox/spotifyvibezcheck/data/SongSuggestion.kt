package com.chatterbox.spotifyvibezcheck.data


data class SongSuggestion(
    val songId: String = "",
    val suggestedBy: String = "",
    val voters: List<String> = emptyList(),   // Users who voted YES
)