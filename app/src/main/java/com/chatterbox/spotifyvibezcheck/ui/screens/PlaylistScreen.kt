package com.chatterbox.spotifyvibezcheck.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.chatterbox.spotifyvibezcheck.ui.components.BottomNavigationBar
import com.chatterbox.spotifyvibezcheck.ui.components.FriendCard
import com.chatterbox.spotifyvibezcheck.ui.components.FriendCardSearch
import com.chatterbox.spotifyvibezcheck.ui.components.PlaylistCard
import com.chatterbox.spotifyvibezcheck.ui.components.SongCard
import com.chatterbox.spotifyvibezcheck.ui.components.SongCardSearch
import com.chatterbox.spotifyvibezcheck.util.SampleData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Playlists")
                },

                actions = {
                    IconButton(
                        onClick = {
                            //navController.navigate("note_editor")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Note"
                        )
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        LazyColumn(
            // Apply the padding provided by Scaffold to avoid content overlapping the TopAppBar
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
//            item{
//                PlaylistCard(playlist = SampleData.samplePlaylist)
//            }
//            item{
//                FriendCard(user = SampleData.sampleUser)
//            }
//            item{
//                SongCard(song = SampleData.sampleTrack)
//            }
//            item{
//                SongCardSearch(song = SampleData.sampleTrack)
//            }
//            item{
//                FriendCardSearch(user = SampleData.sampleUser)
//            }
        }
    }
}
