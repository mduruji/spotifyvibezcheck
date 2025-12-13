package com.chatterbox.spotifyvibezcheck.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.chatterbox.spotifyvibezcheck.R
import com.chatterbox.spotifyvibezcheck.data.UserPlaylist

@Composable
fun PlaylistCard(playlist: UserPlaylist, onCardClick: (UserPlaylist) -> Unit) {
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
            AsyncImage(
                model = playlist.imageUrl,
                contentDescription = "Playlist artwork",
                placeholder = painterResource(id = R.drawable.spotify_icon),
                modifier = Modifier.size(40.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier.weight(1f).padding(start = 10.dp),
                text = playlist.name,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}