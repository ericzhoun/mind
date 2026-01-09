package com.digitalascetic.app.domain.usecase

import com.digitalascetic.app.domain.model.Task
import com.digitalascetic.app.domain.repository.ProgramRepository
import javax.inject.Inject

class GetTaskDetailUseCase @Inject constructor(
    private val programRepository: ProgramRepository
) {
    suspend operator fun invoke(taskId: String): Task? {
        return programRepository.getTaskById(taskId)
    }
}
