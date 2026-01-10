package com.digitalascetic.app.domain.usecase

import com.digitalascetic.app.domain.model.Program
import com.digitalascetic.app.domain.model.ProgramDay
import com.digitalascetic.app.domain.model.ProgramType
import com.digitalascetic.app.domain.model.Task
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
            Task.ChecklistTask(
                id = "stoic_d1_t1",
                dayId = day1Id,
                title = "Take a 2 minute cold shower",
                durationMinutes = 2
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

        // Vipassana Day 1-10 Videos
        val vipassanaVideos = listOf(
            "cz7QHNvNFfA", // Day 1
            "cYG5VvHry7c", // Day 2
            "rXXnSK2a47w", // Day 3
            "UvKl0Wpwbn0", // Day 4
            "dB0TB7tQoYY", // Day 5
            "Yxp0mZeK2a47w", // Day 6
            "u4twJT1RfiM", // Day 7
            "Us5Iq302eNU", // Day 8
            "OeCO_EQ0vN8", // Day 9
            "NzrQ2HMFOuo"  // Day 10
        )
        
        // Morning Chanting Videos (S.N. Goenka)
        val morningChantingVideos = listOf(
            "N54JkBa-ukM", // Day 1
            "H5aQeGH4_hs", // Day 2
            "t9t7yGffyTk", // Day 3
            "kUfsIWgjzZo", // Day 4
            "c-v1itVJQqo", // Day 5
            "Ivg3eJX0r_Q", // Day 6
            "11glwqynH90", // Day 7
            "Wr8r6DSJ250", // Day 8
            "wbziqG5XP_Y", // Day 9
            "pZeuHjipwcc", // Day 10
            "NkVVzGgVhzA"  // Day 11
        )

        for (i in 1..10) {
            val dayId = "vip_d$i"
            programRepository.insertProgramDay(
                ProgramDay(
                    id = dayId, 
                    programId = vipId, 
                    dayIndex = i, 
                    theme = if (i < 4) "Anapana (Respiration)" else "Vipassana (Sensation)", 
                    instructionText = "Observe the reality as it is."
                )
            )

            // Morning Meditation (Timed Task)
            programRepository.insertTask(
                Task.TimedTask(
                    id = "vip_d${i}_t1",
                    dayId = dayId,
                    title = "Morning Meditation",
                    startTime = "04:30",
                    endTime = "06:30",
                    strictMode = true
                )
            )
            
            // Morning Chanting Video
            if (i <= morningChantingVideos.size) {
                programRepository.insertTask(
                    Task.VideoTask(
                        id = "vip_d${i}_chant",
                        dayId = dayId,
                        title = "Morning Chanting - Day $i",
                        videoUrl = "https://www.youtube.com/embed/${morningChantingVideos[i-1]}?playsinline=1",
                        durationMinutes = 15
                    )
                )
            }
            
            // Evening Discourse (Video)
            if (i <= vipassanaVideos.size) {
                programRepository.insertTask(
                    Task.VideoTask(
                        id = "vip_d${i}_video",
                        dayId = dayId,
                        title = "Day $i Discourse",
                        videoUrl = "https://www.youtube.com/embed/${vipassanaVideos[i-1]}?playsinline=1",
                        durationMinutes = 70
                    )
                )
            }
        }
    }
}

