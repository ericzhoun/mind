package com.digitalascetic.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.digitalascetic.app.domain.model.UserPreferences
import com.digitalascetic.app.domain.repository.PreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class PreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PreferencesRepository {

    private object PreferencesKeys {
        val NON_TIMED_REMINDER_TIME_1 = stringPreferencesKey("non_timed_reminder_time_1")
        val NON_TIMED_REMINDER_TIME_2 = stringPreferencesKey("non_timed_reminder_time_2")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val TIMED_TASK_REMINDER_MINUTES = intPreferencesKey("timed_task_reminder_minutes")
    }

    override fun getUserPreferences(): Flow<UserPreferences> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                UserPreferences(
                    nonTimedTaskReminderTime1 = preferences[PreferencesKeys.NON_TIMED_REMINDER_TIME_1] ?: "12:00",
                    nonTimedTaskReminderTime2 = preferences[PreferencesKeys.NON_TIMED_REMINDER_TIME_2] ?: "20:00",
                    notificationsEnabled = preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true,
                    timedTaskReminderMinutesBefore = preferences[PreferencesKeys.TIMED_TASK_REMINDER_MINUTES] ?: 5
                )
            }
    }

    override suspend fun updateNonTimedTaskReminderTime1(time: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NON_TIMED_REMINDER_TIME_1] = time
        }
    }

    override suspend fun updateNonTimedTaskReminderTime2(time: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NON_TIMED_REMINDER_TIME_2] = time
        }
    }

    override suspend fun updateNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    override suspend fun updateTimedTaskReminderMinutesBefore(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TIMED_TASK_REMINDER_MINUTES] = minutes
        }
    }
}
