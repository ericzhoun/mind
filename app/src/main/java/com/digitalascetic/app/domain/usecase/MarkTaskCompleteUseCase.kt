package com.digitalascetic.app.domain.usecase

import com.digitalascetic.app.domain.model.TaskStatus
import com.digitalascetic.app.domain.model.UserProgress
import com.digitalascetic.app.domain.repository.UserProgressRepository
import java.time.LocalDateTime
import javax.inject.Inject

class MarkTaskCompleteUseCase @Inject constructor(
    private val userProgressRepository: UserProgressRepository
) {
    suspend operator fun invoke(taskId: String, value: String? = null) {
        val progress = UserProgress(
            taskId = taskId,
            status = TaskStatus.COMPLETED,
            completedAt = LocalDateTime.now(),
            value = value
        )
        userProgressRepository.updateProgress(progress)
    }
}
