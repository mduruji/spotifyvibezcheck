package com.chatterbox.spotifyvibezcheck.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.chatterbox.spotifyvibezcheck.R
import com.chatterbox.spotifyvibezcheck.data.SongSuggestion
import com.chatterbox.spotifyvibezcheck.navigation.NavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionScreen(navController: NavController, playlistId: String, viewModel: SuggestionViewModel = viewModel()) {

    val playlist by viewModel.playlist.collectAsState()

    LaunchedEffect(playlistId) {
        viewModel.loadPlaylist(playlistId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Suggestions") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(NavRoutes.SongSearch.createRoute(playlistId)) }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Suggestion")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(playlist?.suggestedSongs ?: emptyList()) { suggestion ->
                SuggestionCard(suggestion = suggestion) {
                    viewModel.voteForSong(playlistId, suggestion.trackId)
                }
            }
        }
    }
}

@Composable
fun SuggestionCard(suggestion: SongSuggestion, onVote: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = suggestion.albumArtUrl,
                contentDescription = "Album Art",
                placeholder = painterResource(id = R.drawable.spotify_icon),
                modifier = Modifier.size(40.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.weight(1f).padding(start = 10.dp)
            ) {
                Text(text = suggestion.trackName, style = MaterialTheme.typography.titleLarge)
                Text(text = suggestion.artistName, style = MaterialTheme.typography.bodyMedium)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "${suggestion.votes.size}")
                IconButton(onClick = onVote) {
                    Icon(Icons.Default.ThumbUp, contentDescription = "Vote")
                }
            }
        }
    }
}
