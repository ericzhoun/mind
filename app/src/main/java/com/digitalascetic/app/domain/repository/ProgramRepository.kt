package com.digitalascetic.app.domain.repository

import com.digitalascetic.app.domain.model.Program
import com.digitalascetic.app.domain.model.ProgramDay
import com.digitalascetic.app.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface ProgramRepository {
    fun getAvailablePrograms(): Flow<List<Program>>
    suspend fun getProgramById(programId: String): Program?
    suspend fun getDaysForProgram(programId: String): List<ProgramDay>
    suspend fun getTasksForDay(dayId: String): List<Task>
    suspend fun getTaskById(taskId: String): Task?
    
    // For Phase 1 seeding
    suspend fun insertProgram(program: Program)
    suspend fun insertProgramDay(day: ProgramDay)
    suspend fun insertTask(task: Task)
}
