package com.chatterbox.spotifyvibezcheck.data

import com.google.firebase.firestore.DocumentId

data class UserPlaylist(
    @DocumentId val id: String = "",      // The Firestore Document ID (for Voting/Nav)
    val spotifyId: String = "",           // ✅ NEW: The Spotify Playlist ID (for fetching tracks)
    val name: String = "",
    val ownerId: String = "",
    val imageUrl: String? = null,
    val collaborators: List<String> = emptyList(),
    val trackIds: List<String> = emptyList(),
    val suggestedSongs: List<SongSuggestion> = emptyList(),
    val numberOfSongs: Int = 0
)