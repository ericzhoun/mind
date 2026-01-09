package com.digitalascetic.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digitalascetic.app.domain.model.Program
import com.digitalascetic.app.domain.model.UserProgress
import com.digitalascetic.app.domain.usecase.GetActiveProgramUseCase
import com.digitalascetic.app.domain.usecase.SeedDatabaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Dashboard screen.
 * 
 * Loads the active program and user progress data.
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getActiveProgramUseCase: GetActiveProgramUseCase,
    private val seedDatabaseUseCase: SeedDatabaseUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        // Seed database on first launch
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            seedDatabaseUseCase()
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
        
        // Observe active program
        getActiveProgramUseCase()
            .onEach { program ->
                _uiState.value = _uiState.value.copy(
                    activeProgram = program,
                    hasActiveProgram = program != null
                )
            }
            .launchIn(viewModelScope)
    }
}

/**
 * UI state for Dashboard screen.
 */
data class DashboardUiState(
    val isLoading: Boolean = false,
    val activeProgram: Program? = null,
    val hasActiveProgram: Boolean = false,
    val todayProgress: List<UserProgress> = emptyList()
)
