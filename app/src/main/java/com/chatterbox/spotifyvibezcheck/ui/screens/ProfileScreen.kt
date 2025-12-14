package com.chatterbox.spotifyvibezcheck.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.chatterbox.spotifyvibezcheck.data.User
import com.chatterbox.spotifyvibezcheck.navigation.NavRoutes
import com.chatterbox.spotifyvibezcheck.services.AuthService
import com.chatterbox.spotifyvibezcheck.services.UserService
import com.chatterbox.spotifyvibezcheck.ui.components.BottomNavigationBar
import com.chatterbox.spotifyvibezcheck.ui.components.FriendCard
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, authService: AuthService) {
    val userService = remember { UserService() }
    var user by remember { mutableStateOf<User?>(null) }
    var friends by remember { mutableStateOf<List<User>>(emptyList()) }

    LaunchedEffect(Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            user = userService.getUser(userId)
            friends = userService.getFriends(userId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Profile")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { navController.navigate(NavRoutes.FriendSearch.route) }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Search for Friends"
                        )
                    }
                    IconButton(onClick = { navController.navigate(NavRoutes.Request.route) }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Friend Requests"
                        )
                    }
                    IconButton(
                        onClick = {
                            authService.signOut()
                            navController.navigate(NavRoutes.Login.route) {
                                popUpTo(NavRoutes.Profile.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Sign Out"
                        )
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (user?.photoUrl?.isNotEmpty() == true) {
                    AsyncImage(
                        model = user?.photoUrl,
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.size(120.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = user?.username ?: "loading...")
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 200.dp)
            ) {
                items(friends) { friend ->
                    FriendCard(user = friend)
                }
            }
        }
    }
}