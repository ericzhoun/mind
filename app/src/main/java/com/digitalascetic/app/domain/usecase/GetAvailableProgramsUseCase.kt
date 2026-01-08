package com.digitalascetic.app.domain.usecase

import com.digitalascetic.app.domain.model.Program
import com.digitalascetic.app.domain.repository.ProgramRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAvailableProgramsUseCase @Inject constructor(
    private val programRepository: ProgramRepository
) {
    operator fun invoke(): Flow<List<Program>> {
        return programRepository.getAvailablePrograms()
    }
}
