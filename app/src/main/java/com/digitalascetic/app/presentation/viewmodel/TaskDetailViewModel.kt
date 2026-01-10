package com.digitalascetic.app.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digitalascetic.app.domain.model.Task
import com.digitalascetic.app.domain.model.TaskStatus
import com.digitalascetic.app.domain.model.UserProgress
import com.digitalascetic.app.domain.repository.UserProgressRepository
import com.digitalascetic.app.domain.usecase.GetTaskDetailUseCase
import com.digitalascetic.app.domain.usecase.UpdateTaskStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTaskDetailUseCase: GetTaskDetailUseCase,
    private val updateTaskStatusUseCase: UpdateTaskStatusUseCase,
    private val userProgressRepository: UserProgressRepository
) : ViewModel() {

    private val taskId: String = savedStateHandle.get<String>("taskId") ?: ""

    private val _uiState = MutableStateFlow(TaskDetailUiState())
    val uiState: StateFlow<TaskDetailUiState> = _uiState.asStateFlow()

    init {
        loadTask()
    }

    private fun loadTask() {
        if (taskId.isBlank()) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val task = getTaskDetailUseCase(taskId)
            
            if (task != null) {
                // Load progress
                userProgressRepository.getProgressForTask(taskId).collect { progress ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        task = task,
                        userProgress = progress
                    )
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Task not found"
                )
            }
        }
    }

    fun completeTask(value: String? = null) {
        viewModelScope.launch {
            updateTaskStatusUseCase(taskId, TaskStatus.COMPLETED, value)
        }
    }
    
    fun completeTaskWithProgress(minutesSpent: Int) {
        viewModelScope.launch {
            updateTaskStatusUseCase(taskId, TaskStatus.COMPLETED, minutesSpent = minutesSpent)
        }
    }

    fun saveProgress(minutesSpent: Int) {
        viewModelScope.launch {
            // Keep status as PENDING but update elapsed time
            updateTaskStatusUseCase(taskId, TaskStatus.PENDING, minutesSpent = minutesSpent)
        }
    }
}

data class TaskDetailUiState(
    val isLoading: Boolean = false,
    val task: Task? = null,
    val userProgress: UserProgress? = null,
    val error: String? = null
)
