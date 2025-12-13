package com.chatterbox.spotifyvibezcheck.services

import com.chatterbox.spotifyvibezcheck.data.Playlist
import com.chatterbox.spotifyvibezcheck.data.SongSuggestion
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PlaylistService {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getPlaylist(playlistId: String): Playlist? {
        return try {
            val doc = db.collection("playlists").document(playlistId).get().await()
            doc.toObject(Playlist::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun suggestSong(playlistId: String, song: com.chatterbox.spotifyvibezcheck.models.Track, userId: String) {
        val suggestion = SongSuggestion(
            trackId = song.id,
            trackName = song.name,
            artistName = song.artists.firstOrNull()?.name ?: "Unknown Artist",
            albumArtUrl = song.album.images.firstOrNull()?.url,
            suggestedBy = userId,
            votes = listOf(userId) // auto-vote
        )

        db.collection("playlists")
            .document(playlistId)
            .update("suggestedSongs", FieldValue.arrayUnion(suggestion))
            .await()
    }

    suspend fun voteForSong(playlistId: String, trackId: String, userId: String) {
        val playlist = getPlaylist(playlistId) ?: return
        val updated = playlist.suggestedSongs.map { s ->
            if (s.trackId == trackId) s.copy(votes = (s.votes + userId).distinct())
            else s
        }

        db.collection("playlists")
            .document(playlistId)
            .update("suggestedSongs", updated)
            .await()

        processVotingThreshold(playlistId)
    }

    suspend fun processVotingThreshold(playlistId: String) {
        val playlist = getPlaylist(playlistId) ?: return

        val half = playlist.collaborators.size / 2

        val approved = playlist.suggestedSongs.filter { it.votes.size >= half }
        if (approved.isEmpty()) return

        val newTrackIds = playlist.trackIds.toMutableList()
        approved.forEach { newTrackIds.add(it.trackId) }

        val remaining = playlist.suggestedSongs.filter { it.votes.size < half }

        db.collection("playlists").document(playlistId)
            .update(
                mapOf(
                    "trackIds" to newTrackIds,
                    "suggestedSongs" to remaining,
                    "numberOfSongs" to newTrackIds.size
                )
            )
            .await()
    }
}