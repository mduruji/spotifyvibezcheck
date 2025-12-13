package com.chatterbox.spotifyvibezcheck.util

import com.chatterbox.spotifyvibezcheck.data.User
import com.chatterbox.spotifyvibezcheck.data.UserPlaylist

object SampleData {
    val user = User(
        userId = "1",
        username = "Michael",
        photoUrl = "https://example.com/michael.jpg"
    )

    val playlists = listOf(
        UserPlaylist(
            id = "1",
            name = "My Awesome Playlist",
            ownerId = "1",
            imageUrl = "https://example.com/playlist1.jpg",
        ),
        UserPlaylist(
            id = "2",
            name = "Chill Vibes",
            ownerId = "1",
            imageUrl = "https://example.com/playlist2.jpg",
        ),
        UserPlaylist(
            id = "3",
            name = "Workout Mix",
            ownerId = "1",
            imageUrl = "https://example.com/playlist3.jpg",
        )
    )
}