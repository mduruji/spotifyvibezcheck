package com.chatterbox.spotifyvibezcheck.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chatterbox.spotifyvibezcheck.data.UserPlaylist

@Composable
fun PlaylistCard(
    playlist: UserPlaylist,
    onCardClick: (UserPlaylist) -> Unit,
    onPlayClick: (UserPlaylist) -> Unit,
    onDeleteClick: (UserPlaylist) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onCardClick(playlist) }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onPlayClick(playlist) }) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play Playlist")
            }
            Column(
                modifier = Modifier.weight(1f).padding(start = 10.dp)
            ) {
                Text(
                    text = playlist.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "${playlist.numberOfSongs} songs",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = { onDeleteClick(playlist) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Playlist")
            }
        }
    }
}