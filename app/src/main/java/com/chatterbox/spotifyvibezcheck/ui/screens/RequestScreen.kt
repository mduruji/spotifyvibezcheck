package com.chatterbox.spotifyvibezcheck.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chatterbox.spotifyvibezcheck.data.User
import com.chatterbox.spotifyvibezcheck.services.SpotifyApiClient
import com.chatterbox.spotifyvibezcheck.services.UserService
import com.chatterbox.spotifyvibezcheck.ui.components.FriendCardRequest
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestScreen(navController: NavController) {
    val userService = remember { UserService() }
    var requests by remember { mutableStateOf<List<User>>(emptyList()) }
    val scope = rememberCoroutineScope()
    val spotifyApiClient = remember { SpotifyApiClient(token = "") } // This token needs to be properly managed

    LaunchedEffect(Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val friendRequests = userService.getFriendRequests(currentUser.uid)
            val fullUserDetails = friendRequests.mapNotNull { user ->
                val spotifyUser = spotifyApiClient.getUser(user.spotifyUser).body()
                spotifyUser?.let {
                    user.copy(photoUrl = it.images.firstOrNull()?.url ?: "")
                }
            }
            requests = fullUserDetails
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Friend Requests")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(requests) { user ->
                    FriendCardRequest(
                        user = user,
                        onAccept = {
                            scope.launch {
                                val currentUser = FirebaseAuth.getInstance().currentUser
                                if (currentUser != null) {
                                    userService.acceptFriendRequest(currentUser.uid, user.userId)
                                    requests = requests.filter { it.userId != user.userId } // Refresh list
                                }
                            }
                        },
                        onDecline = {
                            scope.launch {
                                val currentUser = FirebaseAuth.getInstance().currentUser
                                if (currentUser != null) {
                                    userService.declineFriendRequest(currentUser.uid, user.userId)
                                    requests = requests.filter { it.userId != user.userId } // Refresh list
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
