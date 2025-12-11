package com.chatterbox.spotifyvibezcheck.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chatterbox.spotifyvibezcheck.models.Track

private val genres = listOf("Rock", "Pop", "Hip Hop", "Jazz", "Classical", "Electronic", "R&B", "Country")

@Composable
fun SongCardSearch(song: Track) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = {
            // onCardClick(playlist)
        }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* TODO */ }) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play Song")
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = song.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = song.artists.joinToString { it.name },
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = genres.random(),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            var checked by remember { mutableStateOf(false) }

            Checkbox(
                checked = checked,
                onCheckedChange = { checked = it }
            )
        }
    }
}
