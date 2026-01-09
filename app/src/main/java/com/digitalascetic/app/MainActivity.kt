package com.digitalascetic.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.digitalascetic.app.presentation.ui.screens.DashboardScreen
import com.digitalascetic.app.presentation.ui.screens.ProgramDetailScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "dashboard") {
                        composable("dashboard") {
                            DashboardScreen(
                                onProgramClick = { id -> navController.navigate("program/$id") }
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
                    }
                }
            }
        }
    }
}
