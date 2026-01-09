package com.digitalascetic.app.domain.usecase

import com.digitalascetic.app.domain.model.Program
import com.digitalascetic.app.domain.repository.ProgramRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Get the currently active program for the user.
 * 
 * In Phase 1, returns the first available program as the "active" one.
 * Future phases will track user enrollment and progress.
 */
class GetActiveProgramUseCase @Inject constructor(
    private val programRepository: ProgramRepository
) {
    operator fun invoke(): Flow<Program?> {
        return programRepository.getAvailablePrograms().map { programs ->
            programs.firstOrNull()
        }
    }
}
