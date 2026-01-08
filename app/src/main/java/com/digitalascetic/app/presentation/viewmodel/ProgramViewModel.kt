package com.digitalascetic.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digitalascetic.app.domain.model.Program
import com.digitalascetic.app.domain.usecase.GetAvailableProgramsUseCase
import com.digitalascetic.app.domain.usecase.SeedDatabaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgramViewModel @Inject constructor(
    private val getAvailableProgramsUseCase: GetAvailableProgramsUseCase,
    private val seedDatabaseUseCase: SeedDatabaseUseCase
) : ViewModel() {

    private val _programs = MutableStateFlow<List<Program>>(emptyList())
    val programs: StateFlow<List<Program>> = _programs.asStateFlow()

    init {
        // In a real app, seeding would happen in a Worker or Splash screen
        viewModelScope.launch {
            seedDatabaseUseCase() 
        }
        
        getAvailableProgramsUseCase()
            .onEach { _programs.value = it }
            .launchIn(viewModelScope)
    }
}
