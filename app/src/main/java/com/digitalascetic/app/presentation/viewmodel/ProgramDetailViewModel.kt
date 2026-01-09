package com.digitalascetic.app.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digitalascetic.app.domain.model.Program
import com.digitalascetic.app.domain.model.ProgramDay
import com.digitalascetic.app.domain.model.Task
import com.digitalascetic.app.domain.model.TaskStatus
import com.digitalascetic.app.domain.usecase.GetDailyTasksUseCase
import com.digitalascetic.app.domain.usecase.GetProgramDetailUseCase
import com.digitalascetic.app.domain.usecase.UpdateTaskStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Program Detail screen.
 * 
 * Loads program details, days, and tasks.
 */
@HiltViewModel
class ProgramDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProgramDetailUseCase: GetProgramDetailUseCase,
    private val getDailyTasksUseCase: GetDailyTasksUseCase,
    private val updateTaskStatusUseCase: UpdateTaskStatusUseCase
) : ViewModel() {

    private val programId: String = savedStateHandle.get<String>("programId") ?: ""
    
    private val _uiState = MutableStateFlow(ProgramDetailUiState())
    val uiState: StateFlow<ProgramDetailUiState> = _uiState.asStateFlow()

    init {
        loadProgramDetails()
    }

    private fun loadProgramDetails() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val detail = getProgramDetailUseCase(programId)
            if (detail != null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    program = detail.program,
                    days = detail.days,
                    selectedDayIndex = 0
                )
                
                // Load tasks for first day
                if (detail.days.isNotEmpty()) {
                    loadTasksForDay(detail.days.first().id)
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Program not found"
                )
            }
        }
    }

    fun selectDay(dayIndex: Int) {
        val days = _uiState.value.days
        if (dayIndex in days.indices) {
            _uiState.value = _uiState.value.copy(selectedDayIndex = dayIndex)
            loadTasksForDay(days[dayIndex].id)
        }
    }

    private fun loadTasksForDay(dayId: String) {
        viewModelScope.launch {
            val tasks = getDailyTasksUseCase(dayId)
            _uiState.value = _uiState.value.copy(tasks = tasks)
        }
    }

    fun completeTask(taskId: String, value: String? = null) {
        viewModelScope.launch {
            updateTaskStatusUseCase(taskId, TaskStatus.COMPLETED, value)
        }
    }
}

/**
 * UI state for Program Detail screen.
 */
data class ProgramDetailUiState(
    val isLoading: Boolean = false,
    val program: Program? = null,
    val days: List<ProgramDay> = emptyList(),
    val selectedDayIndex: Int = 0,
    val tasks: List<Task> = emptyList(),
    val error: String? = null
)
