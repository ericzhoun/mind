package com.digitalascetic.app.domain.service

import com.digitalascetic.app.domain.model.Task
import java.time.LocalDateTime

interface CalendarManager {
    suspend fun checkConflicts(startTime: LocalDateTime, endTime: LocalDateTime): List<String>
    suspend fun scheduleTask(task: Task, startTime: LocalDateTime, endTime: LocalDateTime): Boolean
}
