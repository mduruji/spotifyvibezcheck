package com.chatterbox.spotifyvibezcheck.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.chatterbox.spotifyvibezcheck.ui.components.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaybackScreen(navController: NavController, viewModel: PlaybackViewModel = viewModel()) {

    val connected by viewModel.connected.collectAsState()
    val playerState by viewModel.playerState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.connect()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Now Playing") }) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            if (!connected) {
                LoadingView()
                return@Column
            }

            val track = playerState?.track
            val isPaused = playerState?.isPaused ?: true

            val targetProgress = if (track != null && track.duration > 0) {
                (playerState?.playbackPosition?.toFloat() ?: 0f) / track.duration
            } else {
                0f
            }

            val smoothProgress by animateFloatAsState(
                targetValue = targetProgress,
                animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
                label = "ProgressAnimation"
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                if (track != null) {
                    Text(
                        text = track.name ?: "Unknown Track",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = track.artist.name ?: "Unknown Artist",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text("No track is currently playing.")
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (track != null) {
                    LinearProgressIndicator(
                        progress = { smoothProgress },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = formatDuration(playerState?.playbackPosition ?: 0),
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = formatDuration(track.duration),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                MediaControls(
                    isPlaying = !isPaused,
                    onPrevClick = { viewModel.prev() },
                    onNextClick = { viewModel.next() },
                    onPlayPauseClick = { if (isPaused) viewModel.resume() else viewModel.pause() },
                    enabled = track != null
                )
            }
        }
    }
}

@Composable
fun LoadingView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Connecting to Spotify…")
        Spacer(Modifier.height(16.dp))
        CircularProgressIndicator()
    }
}

@Composable
fun MediaControls(
    isPlaying: Boolean,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    enabled: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrevClick, enabled = enabled) {
            Icon(Icons.Default.SkipPrevious, contentDescription = "Previous", modifier = Modifier.size(48.dp))
        }

        IconButton(
            onClick = onPlayPauseClick,
            modifier = Modifier.size(80.dp),
            enabled = enabled
        ) {
            Icon(
                if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = "Play/Pause",
                modifier = Modifier.size(80.dp)
            )
        }

        IconButton(onClick = onNextClick, enabled = enabled) {
            Icon(Icons.Default.SkipNext, contentDescription = "Next", modifier = Modifier.size(48.dp))
        }
    }
}

private fun formatDuration(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}