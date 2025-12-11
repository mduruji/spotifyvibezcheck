package com.chatterbox.spotifyvibezcheck.util

import com.chatterbox.spotifyvibezcheck.data.User
import com.chatterbox.spotifyvibezcheck.models.Album
import com.chatterbox.spotifyvibezcheck.models.Artist
import com.chatterbox.spotifyvibezcheck.models.Playlist
import com.chatterbox.spotifyvibezcheck.models.Track

object SampleData {
    val sampleUser = User(
        userId = "1",
        username = "Michael Duruji",
        email = "michael@example.com",
        photoUrl = "https://example.com/profile.jpg"
    )

    val samplePlaylist = Playlist(
        id = "1",
        name = "My Awesome Playlist",
        images = emptyList()
    )

    val sampleTrack = Track(
        id = "1",
        name = "Bohemian Rhapsody",
        artists = listOf(Artist(id = "1", name = "Queen")),
        album = Album(id = "1", name = "A Night at the Opera", images = emptyList())
    )

    val sampleUsers = listOf(
        sampleUser,
        User(userId = "2", username = "Jane Doe", email = "jane@example.com", photoUrl = "https://example.com/profile2.jpg"),
        User(userId = "3", username = "John Smith", email = "john@example.com", photoUrl = "https://example.com/profile3.jpg")
    )

    val samplePlaylists = listOf(
        samplePlaylist,
        Playlist(id = "2", name = "Chill Vibes", images = emptyList()),
        Playlist(id = "3", name = "Workout Mix", images = emptyList())
    )

    val sampleTracks = listOf(
        sampleTrack,
        Track(id = "2", name = "Stairway to Heaven", artists = listOf(Artist(id = "2", name = "Led Zeppelin")), album = Album(id = "2", name = "Led Zeppelin IV", images = emptyList())),
        Track(id = "3", name = "Blinding Lights", artists = listOf(Artist(id = "3", name = "The Weeknd")), album = Album(id = "3", name = "After Hours", images = emptyList()))
    )
}