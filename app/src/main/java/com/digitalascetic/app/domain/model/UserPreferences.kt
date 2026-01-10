package com.digitalascetic.app.domain.model

data class UserPreferences(
    val nonTimedTaskReminderTime1: String = "12:00", // 12 PM
    val nonTimedTaskReminderTime2: String = "20:00", // 8 PM
    val notificationsEnabled: Boolean = true,
    val timedTaskReminderMinutesBefore: Int = 5
)
