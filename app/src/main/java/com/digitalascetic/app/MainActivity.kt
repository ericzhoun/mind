package com.digitalascetic.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.digitalascetic.app.presentation.ui.screens.DashboardScreen
import com.digitalascetic.app.presentation.ui.screens.ProgramDetailScreen
import com.digitalascetic.app.presentation.ui.screens.SettingsScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    val snackbarHostState = remember { SnackbarHostState() }
                    val scope = rememberCoroutineScope()
                    
                    // Permission handling for Android 13+
                    var showPermissionRationale by remember { mutableStateOf(false) }
                    
                    val notificationPermissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission()
                    ) { isGranted ->
                        if (isGranted) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Notifications enabled")
                            }
                        } else {
                            showPermissionRationale = true
                        }
                    }
                    
                    // Check and request notification permission on first launch
                    LaunchedEffect(Unit) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            val hasPermission = ContextCompat.checkSelfPermission(
                                this@MainActivity,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                            
                            if (!hasPermission) {
                                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        }
                    }
                    
                    Box(modifier = Modifier.fillMaxSize()) {
                        NavHost(navController = navController, startDestination = "dashboard") {
                            composable("dashboard") {
                                DashboardScreen(
                                    onProgramClick = { id -> navController.navigate("program/$id") },
                                    onSettingsClick = { navController.navigate("settings") }
                                )
                            }
                            composable("program/{programId}") { backStackEntry ->
                                val programId = backStackEntry.arguments?.getString("programId") ?: ""
                                ProgramDetailScreen(
                                    programId = programId,
                                    onTaskClick = { taskId -> navController.navigate("task/$taskId") }
                                )
                            }
                            composable("task/{taskId}") { backStackEntry ->
                                val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
                                com.digitalascetic.app.presentation.ui.screens.TaskDetailScreen(
                                    taskId = taskId,
                                    onBack = { navController.popBackStack() }
                                )
                            }
                            composable("settings") {
                                SettingsScreen(
                                    onBack = { navController.popBackStack() }
                                )
                            }
                        }
                        
                        // Snackbar for permission rationale
                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(16.dp)
                        )
                    }
                    
                    // Show permission rationale with action
                    LaunchedEffect(showPermissionRationale) {
                        if (showPermissionRationale) {
                            val result = snackbarHostState.showSnackbar(
                                message = "Notifications are needed for task reminders",
                                actionLabel = "Grant",
                                duration = androidx.compose.material3.SnackbarDuration.Long
                            )
                            
                            if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    notificationPermissionLauncher.launch(
                                        Manifest.permission.POST_NOTIFICATIONS
                                    )
                                }
                            }
                            showPermissionRationale = false
                        }
                    }
                }
            }
        }
    }
}
