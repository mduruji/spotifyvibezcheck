package com.chatterbox.spotifyvibezcheck.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
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
import com.chatterbox.spotifyvibezcheck.models.Track

@Composable
fun SongCardSearch(track: Track, isSelected: Boolean, onSelectionChanged: (Boolean) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onSelectionChanged(!isSelected) }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = track.album.images.firstOrNull()?.url,
                contentDescription = "Album Art",
                placeholder = painterResource(id = R.drawable.spotify_icon),
                modifier = Modifier.size(40.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier.weight(1f).padding(start = 10.dp),
                text = track.name,
                style = MaterialTheme.typography.titleLarge
            )

            Checkbox(
                checked = isSelected,
                onCheckedChange = { onSelectionChanged(it) }
            )
        }
    }
}