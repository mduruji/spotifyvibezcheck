package com.chatterbox.spotifyvibezcheck.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
import com.chatterbox.spotifyvibezcheck.ui.components.BottomNavigationBar
import com.chatterbox.spotifyvibezcheck.ui.components.PlaylistCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(navController: NavController, viewModel: PlaylistViewModel = viewModel()) {
    val playlists by viewModel.playlists.collectAsState()

    LaunchedEffect(navController.currentBackStackEntry) {
        viewModel.fetchPlaylists()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Playlists")
                },

                actions = {
                    IconButton(
                        onClick = { navController.navigate(NavRoutes.PlaylistCreation.route) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create Playlist"
                        )
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(playlists) {
                PlaylistCard(playlist = it) {
                    navController.navigate(NavRoutes.PlaylistRoom.createRoute(it.id))
                }
            }
        }
    }
}