package com.digitalascetic.app.domain.usecase

import com.digitalascetic.app.domain.model.Task
import com.digitalascetic.app.domain.repository.ProgramRepository
import javax.inject.Inject

/**
 * Get all tasks for a specific day.
 */
class GetDailyTasksUseCase @Inject constructor(
    private val programRepository: ProgramRepository
) {
    suspend operator fun invoke(dayId: String): List<Task> {
        return programRepository.getTasksForDay(dayId)
    }
}
