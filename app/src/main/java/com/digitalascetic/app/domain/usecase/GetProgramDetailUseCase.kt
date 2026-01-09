package com.digitalascetic.app.domain.usecase

import com.digitalascetic.app.domain.model.Program
import com.digitalascetic.app.domain.model.ProgramDay
import com.digitalascetic.app.domain.repository.ProgramRepository
import javax.inject.Inject

/**
 * Get full program details including all days.
 */
class GetProgramDetailUseCase @Inject constructor(
    private val programRepository: ProgramRepository
) {
    /**
     * @param programId The ID of the program to fetch
     * @return A pair of Program and its days, or null if not found
     */
    suspend operator fun invoke(programId: String): ProgramDetail? {
        val program = programRepository.getProgramById(programId) ?: return null
        val days = programRepository.getDaysForProgram(programId)
        return ProgramDetail(program, days)
    }
}

/**
 * Data class holding full program details.
 */
data class ProgramDetail(
    val program: Program,
    val days: List<ProgramDay>
)
