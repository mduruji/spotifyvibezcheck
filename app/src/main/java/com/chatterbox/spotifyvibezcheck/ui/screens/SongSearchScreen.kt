package com.chatterbox.spotifyvibezcheck.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.chatterbox.spotifyvibezcheck.models.Track
import com.chatterbox.spotifyvibezcheck.ui.components.SongCardSearch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongSearchScreen(navController: NavController, playlistId: String, viewModel: SongSearchViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState()
    val selectedTracks = remember { mutableStateMapOf<String, Track>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Songs") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.addSuggestions(playlistId, selectedTracks.values.toList())
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Confirm")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search for songs") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { viewModel.searchTracks(searchQuery) },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Search")
            }
            LazyColumn(
                modifier = Modifier.padding(top = 16.dp)
            ) {
                items(searchResults) { track ->
                    SongCardSearch(track = track, isSelected = selectedTracks.containsKey(track.id)) {
                        if (selectedTracks.containsKey(track.id)) {
                            selectedTracks.remove(track.id)
                        } else {
                            selectedTracks[track.id] = track
                        }
                    }
                }
            }
        }
    }
}
