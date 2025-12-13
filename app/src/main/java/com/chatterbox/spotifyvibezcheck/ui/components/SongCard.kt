package com.chatterbox.spotifyvibezcheck.ui.components

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
import com.chatterbox.spotifyvibezcheck.models.Track

private val genres = listOf("Rock", "Pop", "Hip Hop", "Jazz", "Classical", "Electronic", "R&B", "Country")

@Composable
fun SongCard(track: Track, onPlayClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = {
            onPlayClick(track.uri)
        }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onPlayClick(track.uri) }) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play Song")
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = track.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = track.artists.joinToString { it.name },
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = genres.random(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(
                modifier = Modifier.padding(5.dp),
                onClick = {
                    // onDeleteClick(note)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Song"
                )
            }
        }
    }
}
