package com.digitalascetic.app.presentation.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.digitalascetic.app.domain.model.Task
import com.digitalascetic.app.presentation.viewmodel.TaskDetailViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

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
                    
                    // Task-specific UI
                    when (task) {
                        is Task.TimedTask -> {
                            TimedTaskContent(
                                task = task, 
                                viewModel = viewModel, 
                                onBack = onBack,
                                currentProgress = uiState.userProgress?.minutesSpent ?: 0
                            )
                        }
                        is Task.VideoTask -> {
                            VideoTaskContent(task = task, context = context)
                            DefaultCompleteButton(viewModel = viewModel, onBack = onBack)
                        }
                        is Task.MetricTask -> {
                            Text("Target: ${task.targetValue} ${task.unit}", style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.weight(1f))
                            DefaultCompleteButton(viewModel = viewModel, onBack = onBack)
                        }
                        is Task.ChecklistTask -> {
                            if (task.durationMinutes != null) {
                                Text("Duration: ${task.durationMinutes} mins", style = MaterialTheme.typography.bodyMedium)
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            DefaultCompleteButton(viewModel = viewModel, onBack = onBack)
                        }
                        is Task.JournalTask -> {
                            task.promptText?.let {
                                Text(it, style = MaterialTheme.typography.bodyMedium, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            DefaultCompleteButton(viewModel = viewModel, onBack = onBack)
                        }
                    }
                } else if (uiState.error != null) {
                    Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun TimedTaskContent(
    task: Task.TimedTask, 
    viewModel: TaskDetailViewModel, 
    onBack: () -> Unit,
    currentProgress: Int
) {
    // Initialize with saved progress (convert minutes to seconds)
    // Use saved state to prevent reset on recomposition
    var isRunning by remember { mutableStateOf(false) }
    var elapsedSeconds by remember { mutableStateOf(currentProgress * 60) }
    
    // Save progress when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            if (elapsedSeconds > 0) {
                // Save progress even if not completed
                viewModel.saveProgress(elapsedSeconds / 60)
            }
        }
    }
    
    // Timer effect
    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1.seconds)
            elapsedSeconds++
        }
    }
    
    val hours = elapsedSeconds / 3600
    val minutes = (elapsedSeconds % 3600) / 60
    val seconds = elapsedSeconds % 60
    
    // Adaptive Layout Logic
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val useVerticalLayout = screenWidth < 360.dp // Threshold for switching to vertical layout
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Scheduled: ${task.startTime} - ${task.endTime}",
                    style = MaterialTheme.typography.bodyLarge
                )
                
                // Timer Display
                Text(
                    text = String.format("%02d:%02d:%02d", hours, minutes, seconds),
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                
                // Timer Controls
                if (useVerticalLayout) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TimerControlButtons(
                            isRunning = isRunning,
                            onToggle = { 
                                if (isRunning) { 
                                    viewModel.saveProgress(elapsedSeconds / 60) 
                                }
                                isRunning = !isRunning 
                            },
                            onReset = { 
                                isRunning = false
                                elapsedSeconds = 0 
                            },
                            canReset = elapsedSeconds > 0,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Use a Box to center the Row content effectively if needed, or just let spacer handle it
                        Spacer(modifier = Modifier.weight(1f))
                        TimerControlButtons(
                            isRunning = isRunning,
                            onToggle = { 
                                if (isRunning) { 
                                    viewModel.saveProgress(elapsedSeconds / 60) 
                                }
                                isRunning = !isRunning 
                            },
                            onReset = { 
                                isRunning = false
                                elapsedSeconds = 0 
                            },
                            canReset = elapsedSeconds > 0
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Complete Button (saves progress)
        Button(
            onClick = {
                viewModel.completeTaskWithProgress(elapsedSeconds / 60) // Save minutes
                onBack()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = elapsedSeconds > 0
        ) {
            Text("Complete Session (${elapsedSeconds / 60} min)")
        }
    }
}

@Composable
fun TimerControlButtons(
    isRunning: Boolean,
    onToggle: () -> Unit,
    onReset: () -> Unit,
    canReset: Boolean,
    modifier: Modifier = Modifier
) {
    // Start/Pause Button
    FilledTonalButton(
        onClick = onToggle,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = if (isRunning) "Pause" else "Start"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(if (isRunning) "Pause" else "Start")
    }
    
    // Stop Button
    if (canReset) {
        OutlinedButton(
            onClick = onReset,
            modifier = modifier
        ) {
            Icon(Icons.Filled.Stop, contentDescription = "Stop")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Reset")
        }
    }
}

@Composable
fun VideoTaskContent(task: Task.VideoTask, context: android.content.Context) {
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
}

@Composable
fun DefaultCompleteButton(viewModel: TaskDetailViewModel, onBack: () -> Unit) {
    Button(
        onClick = {
            viewModel.completeTask()
            onBack()
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Mark as Complete")
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
