package com.chatterbox.spotifyvibezcheck.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.chatterbox.spotifyvibezcheck.navigation.NavRoutes
import com.chatterbox.spotifyvibezcheck.ui.components.SuggestionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionScreen(navController: NavController, playlistId: String, viewModel: SuggestionViewModel = viewModel()) {

    val playlist by viewModel.playlist.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    LaunchedEffect(playlistId) {
        viewModel.loadPlaylist(playlistId)
        viewModel.loadCurrentUser()
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
                SuggestionCard(
                    suggestion = suggestion,
                    currentUserId = currentUser?.uid ?: "",
                    onVote = {
                        viewModel.voteForSong(playlistId, suggestion.trackId)
                    },
                    onPlay = { viewModel.playTrack(suggestion.trackId) }
                )
            }
        }
    }
}