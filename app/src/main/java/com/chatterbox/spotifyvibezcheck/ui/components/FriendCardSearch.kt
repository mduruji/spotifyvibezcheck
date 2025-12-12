package com.chatterbox.spotifyvibezcheck.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.chatterbox.spotifyvibezcheck.R
import com.chatterbox.spotifyvibezcheck.data.User

@Composable
fun FriendCardSearch(user: User, isSelected: Boolean, onSelectionChanged: (Boolean) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { onSelectionChanged(!isSelected) }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.photoUrl,
                contentDescription = "User profile picture",
                placeholder = painterResource(id = R.drawable.spotify_icon),
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier.weight(1f).padding(start = 10.dp),
                text = user.username,
                style = MaterialTheme.typography.titleLarge
            )

            Checkbox(
                checked = isSelected,
                onCheckedChange = { onSelectionChanged(it) }
            )
        }
    }
}