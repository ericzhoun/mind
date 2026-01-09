package com.digitalascetic.app.domain.usecase

import com.digitalascetic.app.domain.model.TaskStatus
import com.digitalascetic.app.domain.model.UserProgress
import com.digitalascetic.app.domain.repository.UserProgressRepository
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Update the status of a task.
 * 
 * Supports COMPLETED, FAILED, SKIPPED, or PENDING statuses.
 */
class UpdateTaskStatusUseCase @Inject constructor(
    private val userProgressRepository: UserProgressRepository
) {
    suspend operator fun invoke(
        taskId: String,
        status: TaskStatus,
        value: String? = null
    ) {
        val progress = UserProgress(
            taskId = taskId,
            status = status,
            completedAt = if (status == TaskStatus.COMPLETED) LocalDateTime.now() else null,
            value = value
        )
        userProgressRepository.updateProgress(progress)
    }
}
