package com.chatterbox.spotifyvibezcheck.ui.components

import androidx.compose.foundation.Image
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
import com.chatterbox.spotifyvibezcheck.models.Playlist

@Composable
fun PlaylistCard(playlist: Playlist){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = {
            //onCardClick(playlist)
        }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play Playlist" )
            }
            Text(
                modifier = Modifier.weight(1f),
                text = playlist.name,
                style = MaterialTheme.typography.titleLarge
            )
            IconButton(
                modifier = Modifier.padding(5.dp),
                onClick = {
                    //onDeleteClick(note)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Home"
                )
            }
        }
    }
}