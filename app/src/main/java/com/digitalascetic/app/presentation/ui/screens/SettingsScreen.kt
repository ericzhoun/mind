package com.digitalascetic.app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.digitalascetic.app.presentation.viewmodel.SettingsViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val preferences by viewModel.preferences.collectAsState()
    var showTimePicker1 by remember { mutableStateOf(false) }
    var showTimePicker2 by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Notifications Section
            SettingsSection(title = "Notifications") {
                SettingItem(
                    title = "Enable Notifications",
                    description = "Receive reminders for scheduled tasks"
                ) {
                    Switch(
                        checked = preferences.notificationsEnabled,
                        onCheckedChange = { viewModel.updateNotificationsEnabled(it) }
                    )
                }
            }

            // Timed Tasks Section
            SettingsSection(title = "Timed Tasks (e.g., Meditation)") {
                SettingItem(
                    title = "Reminder Before Start",
                    description = "Minutes before scheduled time"
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = {
                                if (preferences.timedTaskReminderMinutesBefore > 1) {
                                    viewModel.updateTimedTaskReminderMinutesBefore(
                                        preferences.timedTaskReminderMinutesBefore - 1
                                    )
                                }
                            }
                        ) {
                            Text("-", style = MaterialTheme.typography.titleLarge)
                        }
                        
                        Text(
                            text = "${preferences.timedTaskReminderMinutesBefore} min",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.width(60.dp)
                        )
                        
                        IconButton(
                            onClick = {
                                if (preferences.timedTaskReminderMinutesBefore < 60) {
                                    viewModel.updateTimedTaskReminderMinutesBefore(
                                        preferences.timedTaskReminderMinutesBefore + 1
                                    )
                                }
                            }
                        ) {
                            Text("+", style = MaterialTheme.typography.titleLarge)
                        }
                    }
                }
            }

            // Non-Timed Tasks Section
            SettingsSection(title = "Non-Timed Tasks (Videos, Checklists, etc.)") {
                SettingItem(
                    title = "First Daily Reminder",
                    description = "Default: 12:00 PM"
                ) {
                    TextButton(onClick = { showTimePicker1 = true }) {
                        Text(
                            text = formatTime(preferences.nonTimedTaskReminderTime1),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                SettingItem(
                    title = "Second Daily Reminder",
                    description = "Default: 8:00 PM"
                ) {
                    TextButton(onClick = { showTimePicker2 = true }) {
                        Text(
                            text = formatTime(preferences.nonTimedTaskReminderTime2),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            // Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ℹ️ About Notifications",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Timed tasks get one reminder before the scheduled time\n" +
                                "• Non-timed tasks get two daily reminders at your chosen times\n" +
                                "• All notifications require approval on Android 13+",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }

    // Time Picker Dialogs
    if (showTimePicker1) {
        TimePickerDialog(
            currentTime = preferences.nonTimedTaskReminderTime1,
            onDismiss = { showTimePicker1 = false },
            onConfirm = { time ->
                viewModel.updateNonTimedTaskReminderTime1(time)
                showTimePicker1 = false
            }
        )
    }

    if (showTimePicker2) {
        TimePickerDialog(
            currentTime = preferences.nonTimedTaskReminderTime2,
            onDismiss = { showTimePicker2 = false },
            onConfirm = { time ->
                viewModel.updateNonTimedTaskReminderTime2(time)
                showTimePicker2 = false
            }
        )
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    description: String,
    control: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        control()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    currentTime: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val timeParts = currentTime.split(":")
    val initialHour = timeParts.getOrNull(0)?.toIntOrNull() ?: 12
    val initialMinute = timeParts.getOrNull(1)?.toIntOrNull() ?: 0

    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = false
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val time = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                onConfirm(time)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        text = {
            TimePicker(state = timePickerState)
        }
    )
}

fun formatTime(time: String): String {
    return try {
        val localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
        localTime.format(DateTimeFormatter.ofPattern("h:mm a"))
    } catch (e: Exception) {
        time
    }
}
