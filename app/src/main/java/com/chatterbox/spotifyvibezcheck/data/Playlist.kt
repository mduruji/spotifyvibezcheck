package com.chatterbox.spotifyvibezcheck.data

data class Playlist(
    val playlistId: String = "",
    val spotifyPlaylistId: String = "",
    val collaborators: List<String> = emptyList(),
    val playlistImage: String = "",
    val trackIds: List<String> = emptyList(),
    val suggestedSongs: List<SongSuggestion> = emptyList(),
    val numberOfSongs: Int = 0
)

