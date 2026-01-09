package com.digitalascetic.app.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.digitalascetic.app.domain.model.Task
import com.digitalascetic.app.presentation.viewmodel.ProgramDetailViewModel

@Composable
fun ProgramDetailScreen(
    programId: String,
    onTaskClick: (String) -> Unit,
    viewModel: ProgramDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (uiState.error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
        }
    } else {
        val program = uiState.program
        if (program != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Program Header
                item {
                    Text(
                        text = program.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = program.description,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Duration: ${program.durationDays} Days",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                // Days Row
                item {
                    Text(
                        text = "Schedule",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(uiState.days) { index, day ->
                            val isSelected = index == uiState.selectedDayIndex
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.selectDay(index) },
                                label = { Text("Day ${day.dayIndex}") }
                            )
                        }
                    }
                }

                // Selected Day Details
                if (uiState.days.isNotEmpty() && uiState.selectedDayIndex < uiState.days.size) {
                    val selectedDay = uiState.days[uiState.selectedDayIndex]
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Theme: ${selectedDay.theme}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                if (selectedDay.instructionText.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = selectedDay.instructionText,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }

                // Tasks List
                item {
                    Text(
                        text = "Tasks for Day",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                items(uiState.tasks) { task ->
                    TaskItem(task = task, onClick = { onTaskClick(task.id) })
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            
            // Display type-specific details
            when (task) {
                is Task.ChecklistTask -> {
                    task.durationMinutes?.let {
                        Text("Duration: $it mins", style = MaterialTheme.typography.bodySmall)
                    }
                }
                is Task.TimedTask -> {
                    Text("Time: ${task.startTime} - ${task.endTime}", style = MaterialTheme.typography.bodySmall)
                }
                is Task.MetricTask -> {
                    Text("Target: ${task.targetValue} ${task.unit}", style = MaterialTheme.typography.bodySmall)
                }
                is Task.JournalTask -> {
                    task.promptText?.let {
                        Text("Prompt: $it", style = MaterialTheme.typography.bodySmall, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                    }
                }
                is Task.VideoTask -> {
                    Text("Video: ${task.durationMinutes ?: "?"} mins", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
