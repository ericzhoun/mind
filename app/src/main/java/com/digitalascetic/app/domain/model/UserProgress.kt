package com.digitalascetic.app.domain.model

import java.time.LocalDateTime

data class UserProgress(
    val taskId: String,
    val status: TaskStatus,
    val completedAt: LocalDateTime? = null,
    val value: String? = null, // For metrics or journal entry IDs
    val failureReason: String? = null,
    val minutesSpent: Int? = null // For tracking partial progress on timed tasks
)
