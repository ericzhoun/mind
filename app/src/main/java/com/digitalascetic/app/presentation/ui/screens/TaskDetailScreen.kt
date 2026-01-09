package com.digitalascetic.app.presentation.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.digitalascetic.app.domain.model.Task
import com.digitalascetic.app.presentation.viewmodel.TaskDetailViewModel

@Composable
fun TaskDetailScreen(
    taskId: String,
    viewModel: TaskDetailViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        val task = uiState.task
        
        Scaffold(
            topBar = {
                // You might want a Back button here normally
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (task != null) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    if (task is Task.VideoTask) {
                        VideoLauncherCard(
                            videoUrl = task.videoUrl, 
                            onClick = {
                                val videoId = extractVideoId(task.videoUrl)
                                if (videoId != null) {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))
                                    context.startActivity(intent)
                                }
                            }
                        )
                        if (task.durationMinutes != null) {
                            Text("Duration: ${task.durationMinutes} mins", style = MaterialTheme.typography.bodyMedium)
                        }
                    } else {
                        // Show other task details generically
                        Text(
                           text = when(task) {
                               is Task.TimedTask -> "Scheduled: ${task.startTime} - ${task.endTime}"
                               is Task.MetricTask -> "Target: ${task.targetValue} ${task.unit}" 
                               is Task.ChecklistTask -> "Duration: ${task.durationMinutes ?: "?"} mins"
                               else -> "Simple Task"
                           }
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { 
                            viewModel.completeTask()
                            onBack()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Mark as Complete")
                    }
                } else if (uiState.error != null) {
                    Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun VideoLauncherCard(videoUrl: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play Video",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tap to Watch on YouTube",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun extractVideoId(embedUrl: String): String? {
    // Format: https://www.youtube.com/embed/VIDEO_ID?params...
    val regex = "embed/([^?]+)".toRegex()
    return regex.find(embedUrl)?.groupValues?.get(1)
}
