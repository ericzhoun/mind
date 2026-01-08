package com.digitalascetic.app.domain.usecase

import com.digitalascetic.app.domain.model.Program
import com.digitalascetic.app.domain.model.ProgramDay
import com.digitalascetic.app.domain.model.ProgramType
import com.digitalascetic.app.domain.model.Task
import com.digitalascetic.app.domain.model.TaskType
import com.digitalascetic.app.domain.repository.ProgramRepository
import javax.inject.Inject

class SeedDatabaseUseCase @Inject constructor(
    private val programRepository: ProgramRepository
) {
    suspend operator fun invoke() {
        // Seed 21-Day Stoic
        val stoicId = "stoic_21"
        programRepository.insertProgram(
            Program(
                id = stoicId,
                title = "21-Day Stoic Freedom",
                description = "A linear progression of cognitive and physical challenges.",
                type = ProgramType.LINEAR,
                durationDays = 21
            )
        )
        
        // Day 1: Physical Intervention
        val day1Id = "stoic_d1"
        programRepository.insertProgramDay(
            ProgramDay(id = day1Id, programId = stoicId, dayIndex = 1, theme = "Intervention", instructionText = "Take a cold shower.")
        )
        programRepository.insertTask(
            Task(
                id = "stoic_d1_t1",
                dayId = day1Id,
                type = TaskType.CHECKLIST,
                title = "Take a 2 minute cold shower",
                payloadJson = "{\"duration_min\": 2}"
            )
        )

        // Seed 10-Day Vipassana
        val vipId = "vip_10"
        programRepository.insertProgram(
            Program(
                id = vipId,
                title = "10-Day Vipassana",
                description = "A rigorous schedule of silence and meditation.",
                type = ProgramType.SCHEDULE,
                durationDays = 10
            )
        )

        // Day 1 Schedule
        val vipD1Id = "vip_d1"
        programRepository.insertProgramDay(
            ProgramDay(id = vipD1Id, programId = vipId, dayIndex = 1, theme = "Sila (Morality)", instructionText = "Follow the schedule strictly.")
        )
        programRepository.insertTask(
            Task(
                id = "vip_d1_t1",
                dayId = vipD1Id,
                type = TaskType.TIMED,
                title = "Morning Meditation",
                payloadJson = "{\"start\": \"04:30\", \"end\": \"06:30\"}",
                appBlockRulesJson = "{\"strict_mode\": true}"
            )
        )
    }
}
