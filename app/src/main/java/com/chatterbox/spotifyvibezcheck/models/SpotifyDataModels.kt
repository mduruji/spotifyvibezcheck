package com.chatterbox.spotifyvibezcheck.models

import com.google.gson.annotations.SerializedName

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
    val album: Album
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
