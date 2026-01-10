package com.digitalascetic.app.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digitalascetic.app.domain.model.DayNote
import com.digitalascetic.app.domain.model.Program
import com.digitalascetic.app.domain.model.ProgramDay
import com.digitalascetic.app.domain.model.Task
import com.digitalascetic.app.domain.model.TaskStatus
import com.digitalascetic.app.domain.model.UserProgress
import com.digitalascetic.app.domain.repository.DayNoteRepository
import com.digitalascetic.app.domain.repository.PreferencesRepository
import com.digitalascetic.app.domain.repository.UserProgressRepository
import com.digitalascetic.app.domain.usecase.GetDailyTasksUseCase
import com.digitalascetic.app.domain.usecase.GetProgramDetailUseCase
import com.digitalascetic.app.domain.usecase.UpdateTaskStatusUseCase
import com.digitalascetic.app.notification.NotificationScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalTime
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
    private val updateTaskStatusUseCase: UpdateTaskStatusUseCase,
    private val dayNoteRepository: DayNoteRepository,
    private val preferencesRepository: PreferencesRepository,
    private val notificationScheduler: NotificationScheduler,
    private val userProgressRepository: UserProgressRepository
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
                    val firstDay = detail.days.first()
                    loadTasksForDay(firstDay.id)
                    observeDayNote(firstDay.id)
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
            val dayId = days[dayIndex].id
            loadTasksForDay(dayId)
            observeDayNote(dayId)
        }
    }

    private fun loadTasksForDay(dayId: String) {
        viewModelScope.launch {
            val tasks = getDailyTasksUseCase(dayId)
            _uiState.value = _uiState.value.copy(tasks = tasks)
            
            // Schedule notifications for today's tasks
            preferencesRepository.getUserPreferences().collect { preferences ->
                notificationScheduler.scheduleDailyTasks(tasks, preferences)
            }
        }
        
        // Observe progress for all tasks to calculate percentage
        viewModelScope.launch {
            val tasks = getDailyTasksUseCase(dayId)
            if (tasks.isEmpty()) {
                _uiState.value = _uiState.value.copy(dayProgressPercentage = 0)
                return@launch
            }

            val progressFlows = tasks.map { task ->
                userProgressRepository.getProgressForTask(task.id)
            }

            combine(progressFlows) { progressList ->
                calculateProgressPercentage(tasks, progressList.toList())
            }.collect { percentage ->
                _uiState.value = _uiState.value.copy(dayProgressPercentage = percentage)
            }
        }
    }
    
    private fun calculateProgressPercentage(tasks: List<Task>, progressList: List<UserProgress?>): Int {
        var totalEstimatedMinutes = 0
        var totalActualMinutes = 0
        
        tasks.forEachIndexed { index, task ->
            val progress = progressList.getOrNull(index)
            val estimatedMinutes = getEstimatedDuration(task)
            totalEstimatedMinutes += estimatedMinutes
            
            if (progress != null) {
                // Use recorded minutes if available
                if (progress.minutesSpent != null) {
                    totalActualMinutes += progress.minutesSpent
                } 
                // Or full credit if completed (fallback for non-timed tasks or legacy data)
                else if (progress.status == TaskStatus.COMPLETED) {
                    totalActualMinutes += estimatedMinutes
                }
            }
        }
        
        return if (totalEstimatedMinutes > 0) {
            ((totalActualMinutes.toFloat() / totalEstimatedMinutes) * 100).toInt().coerceAtMost(100)
        } else {
            0
        }
    }
    
    private fun getEstimatedDuration(task: Task): Int {
        return when (task) {
            is Task.TimedTask -> {
                try {
                    val start = LocalTime.parse(task.startTime)
                    val end = LocalTime.parse(task.endTime)
                    java.time.Duration.between(start, end).toMinutes().toInt()
                } catch (e: Exception) {
                    0
                }
            }
            is Task.VideoTask -> task.durationMinutes ?: 0
            is Task.ChecklistTask -> task.durationMinutes ?: 0
            else -> 0
        }
    }
    
    private fun observeDayNote(dayId: String) {
        viewModelScope.launch {
            dayNoteRepository.getDayNote(dayId).collect { note ->
                _uiState.value = _uiState.value.copy(currentDayNote = note)
            }
        }
    }

    fun completeTask(taskId: String, value: String? = null) {
        viewModelScope.launch {
            updateTaskStatusUseCase(taskId, TaskStatus.COMPLETED, value)
        }
    }
    
    fun showDayNoteDialog() {
        _uiState.value = _uiState.value.copy(
            showDayNoteDialog = true,
            dayNoteEditText = _uiState.value.currentDayNote?.noteText ?: ""
        )
    }
    
    fun hideDayNoteDialog() {
        _uiState.value = _uiState.value.copy(showDayNoteDialog = false)
    }
    
    fun updateDayNoteText(text: String) {
        _uiState.value = _uiState.value.copy(dayNoteEditText = text)
    }
    
    fun saveDayNote() {
        val days = _uiState.value.days
        val selectedDayIndex = _uiState.value.selectedDayIndex
        if (selectedDayIndex in days.indices) {
            val dayId = days[selectedDayIndex].id
            val noteText = _uiState.value.dayNoteEditText
            
            viewModelScope.launch {
                val note = DayNote(
                    dayId = dayId,
                    noteText = noteText
                )
                dayNoteRepository.saveDayNote(note)
                hideDayNoteDialog()
            }
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
    val error: String? = null,
    val currentDayNote: DayNote? = null,
    val showDayNoteDialog: Boolean = false,
    val dayNoteEditText: String = "",
    val dayProgressPercentage: Int = 0
)
