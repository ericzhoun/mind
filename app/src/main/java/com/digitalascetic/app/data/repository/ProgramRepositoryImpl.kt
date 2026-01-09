package com.digitalascetic.app.data.repository

import com.digitalascetic.app.data.local.dao.ProgramDao
import com.digitalascetic.app.data.local.dao.TaskDao
import com.digitalascetic.app.data.local.entity.toDomain
import com.digitalascetic.app.data.local.entity.toEntity
import com.digitalascetic.app.data.mapper.TaskMapper
import com.digitalascetic.app.domain.model.Program
import com.digitalascetic.app.domain.model.ProgramDay
import com.digitalascetic.app.domain.model.Task
import com.digitalascetic.app.domain.repository.ProgramRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProgramRepositoryImpl @Inject constructor(
    private val programDao: ProgramDao,
    private val taskDao: TaskDao,
    private val taskMapper: TaskMapper
) : ProgramRepository {

    override fun getAvailablePrograms(): Flow<List<Program>> {
        return programDao.getAllPrograms().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getProgramById(programId: String): Program? {
        return programDao.getProgramById(programId)?.toDomain()
    }

    override suspend fun getDaysForProgram(programId: String): List<ProgramDay> {
        return programDao.getDaysForProgram(programId).map { it.toDomain() }
    }

    override suspend fun getTasksForDay(dayId: String): List<Task> {
        return taskDao.getTasksForDay(dayId).map { taskMapper.toTask(it) }
    }

    override suspend fun insertProgram(program: Program) {
        programDao.insertProgram(program.toEntity())
    }

    override suspend fun insertProgramDay(day: ProgramDay) {
        programDao.insertProgramDay(day.toEntity())
    }

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(taskMapper.toEntity(task))
    }
}

