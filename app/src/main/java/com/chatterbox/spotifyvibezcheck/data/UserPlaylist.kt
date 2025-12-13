package com.chatterbox.spotifyvibezcheck.data

import com.google.firebase.firestore.DocumentId

data class UserPlaylist(
    @DocumentId val id: String = "",
    val name: String = "",
    val ownerId: String = "",
    val imageUrl: String? = null,
    val collaborators: List<String> = emptyList(),
    val trackIds: List<String> = emptyList(),
    val suggestedSongs: List<SongSuggestion> = emptyList(),
    val numberOfSongs: Int = 0
)
