package com.digitalascetic.app.domain.repository

import com.digitalascetic.app.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getUserPreferences(): Flow<UserPreferences>
    suspend fun updateNonTimedTaskReminderTime1(time: String)
    suspend fun updateNonTimedTaskReminderTime2(time: String)
    suspend fun updateNotificationsEnabled(enabled: Boolean)
    suspend fun updateTimedTaskReminderMinutesBefore(minutes: Int)
}
