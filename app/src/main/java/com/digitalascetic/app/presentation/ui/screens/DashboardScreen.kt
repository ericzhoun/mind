package com.digitalascetic.app.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.digitalascetic.app.presentation.viewmodel.ProgramViewModel

@Composable
fun DashboardScreen(
    viewModel: ProgramViewModel = hiltViewModel(),
    onProgramClick: (String) -> Unit
) {
    val programs = viewModel.programs.collectAsState().value

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(text = "Available Programs", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        items(programs) { program ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProgramClick(program.id) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = program.title, style = MaterialTheme.typography.titleMedium)
                    Text(text = "${program.durationDays} Days • ${program.type}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
