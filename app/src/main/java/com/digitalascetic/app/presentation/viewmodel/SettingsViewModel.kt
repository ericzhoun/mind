package com.digitalascetic.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digitalascetic.app.domain.model.UserPreferences
import com.digitalascetic.app.domain.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    val preferences: StateFlow<UserPreferences> = preferencesRepository.getUserPreferences()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferences()
        )

    fun updateNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.updateNotificationsEnabled(enabled)
        }
    }

    fun updateNonTimedTaskReminderTime1(time: String) {
        viewModelScope.launch {
            preferencesRepository.updateNonTimedTaskReminderTime1(time)
        }
    }

    fun updateNonTimedTaskReminderTime2(time: String) {
        viewModelScope.launch {
            preferencesRepository.updateNonTimedTaskReminderTime2(time)
        }
    }

    fun updateTimedTaskReminderMinutesBefore(minutes: Int) {
        viewModelScope.launch {
            preferencesRepository.updateTimedTaskReminderMinutesBefore(minutes)
        }
    }
}
