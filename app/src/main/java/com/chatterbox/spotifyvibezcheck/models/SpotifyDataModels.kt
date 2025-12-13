package com.chatterbox.spotifyvibezcheck.models

import com.google.gson.annotations.SerializedName

data class AddTracksToPlaylistRequest(
    val uris: List<String>
)

data class CreatePlaylistRequest(
    val name: String,
    val public: Boolean = false
)

data class User(
    val id: String,
    @SerializedName("display_name") val displayName: String,
    val images: List<Image>,
    @SerializedName("external_urls") val externalUrls: ExternalUrls
)

data class ExternalUrls(
    val spotify: String
)

data class PlaylistResponse(
    val items: List<Playlist>
)

data class Playlist(
    val id: String,
    val name: String,
    val images: List<Image>
)

data class PlaylistTracksResponse(
    val items: List<PlaylistTrack>
)

data class PlaylistTrack(
    val track: Track
)

data class CurrentlyPlaying(
    val item: Track
)

data class SearchResponse(
    val tracks: TrackResponse
)

data class TrackResponse(
    val items: List<Track>
)

data class Track(
    val id: String,
    val name: String,
    val artists: List<Artist>,
    val album: Album,
    val uri: String
)

data class Artist(
    val id: String,
    val name: String
)

data class Album(
    val id: String,
    val name: String,
    val images: List<Image>
)

data class Image(
    val url: String,
    val height: Int,
    val width: Int
)
