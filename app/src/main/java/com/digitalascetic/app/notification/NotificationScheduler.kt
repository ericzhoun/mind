package com.digitalascetic.app.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.digitalascetic.app.domain.model.Task
import com.digitalascetic.app.domain.model.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    companion object {
        private const val TAG = "NotificationScheduler"
        private const val ACTION_TASK_REMINDER = "com.digitalascetic.app.ACTION_TASK_REMINDER"
    }

    fun scheduleDailyTasks(tasks: List<Task>, preferences: UserPreferences, date: LocalDate = LocalDate.now()) {
        if (!preferences.notificationsEnabled) {
            Log.d(TAG, "Notifications disabled, skipping scheduling")
            return
        }

        tasks.forEach { task ->
            when (task) {
                is Task.TimedTask -> scheduleTimedTask(task, preferences, date)
                else -> scheduleNonTimedTask(task, preferences, date)
            }
        }
    }

    private fun scheduleTimedTask(task: Task.TimedTask, preferences: UserPreferences, date: LocalDate) {
        try {
            // Parse start time (format: "HH:mm")
            val startTimeParts = task.startTime.split(":")
            if (startTimeParts.size != 2) return
            
            val startTime = LocalTime.of(startTimeParts[0].toInt(), startTimeParts[1].toInt())
            
            // Calculate reminder time (5 minutes before)
            val reminderTime = startTime.minusMinutes(preferences.timedTaskReminderMinutesBefore.toLong())
            val reminderDateTime = LocalDateTime.of(date, reminderTime)
            
            // Only schedule if reminder time is in the future
            if (reminderDateTime.isAfter(LocalDateTime.now())) {
                val triggerAtMillis = reminderDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                scheduleNotification(task.id, task.title, task.startTime, triggerAtMillis)
                Log.d(TAG, "Scheduled timed task: ${task.title} at $reminderDateTime")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error scheduling timed task", e)
        }
    }

    private fun scheduleNonTimedTask(task: Task, preferences: UserPreferences, date: LocalDate) {
        try {
            // Schedule for both reminder times
            scheduleNonTimedTaskAt(task, preferences.nonTimedTaskReminderTime1, date, 1)
            scheduleNonTimedTaskAt(task, preferences.nonTimedTaskReminderTime2, date, 2)
        } catch (e: Exception) {
            Log.e(TAG, "Error scheduling non-timed task", e)
        }
    }

    private fun scheduleNonTimedTaskAt(task: Task, timeString: String, date: LocalDate, reminderIndex: Int) {
        try {
            val timeParts = timeString.split(":")
            if (timeParts.size != 2) return
            
            val reminderTime = LocalTime.of(timeParts[0].toInt(), timeParts[1].toInt())
            val reminderDateTime = LocalDateTime.of(date, reminderTime)
            
            // Only schedule if time is in the future
            if (reminderDateTime.isAfter(LocalDateTime.now())) {
                val triggerAtMillis = reminderDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                val uniqueTaskId = "${task.id}_$reminderIndex"
                scheduleNotification(uniqueTaskId, task.title, null, triggerAtMillis)
                Log.d(TAG, "Scheduled non-timed task: ${task.title} at $reminderDateTime")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error scheduling non-timed task at time", e)
        }
    }

    private fun scheduleNotification(taskId: String, taskTitle: String, taskTime: String?, triggerAtMillis: Long) {
        val intent = Intent(context, TaskReminderReceiver::class.java).apply {
            action = ACTION_TASK_REMINDER
            putExtra(NotificationHelper.EXTRA_TASK_ID, taskId)
            putExtra(NotificationHelper.EXTRA_TASK_TITLE, taskTitle)
            putExtra(NotificationHelper.EXTRA_TASK_TIME, taskTime)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Use exact alarm for precise timing
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }

    fun cancelTaskNotification(taskId: String) {
        // Cancel both possible reminders for non-timed tasks
        cancelNotificationById(taskId)
        cancelNotificationById("${taskId}_1")
        cancelNotificationById("${taskId}_2")
    }

    private fun cancelNotificationById(taskId: String) {
        val intent = Intent(context, TaskReminderReceiver::class.java).apply {
            action = ACTION_TASK_REMINDER
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    fun cancelAllNotifications(tasks: List<Task>) {
        tasks.forEach { task ->
            cancelTaskNotification(task.id)
        }
    }
}
