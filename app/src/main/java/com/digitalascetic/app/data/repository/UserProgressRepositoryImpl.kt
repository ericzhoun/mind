package com.digitalascetic.app.data.repository

import com.digitalascetic.app.data.local.dao.UserProgressDao
import com.digitalascetic.app.data.local.entity.toDomain
import com.digitalascetic.app.data.local.entity.toEntity
import com.digitalascetic.app.domain.model.UserProgress
import com.digitalascetic.app.domain.repository.UserProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserProgressRepositoryImpl @Inject constructor(
    private val userProgressDao: UserProgressDao
) : UserProgressRepository {

    override fun getProgressForTask(taskId: String): Flow<UserProgress?> {
        return userProgressDao.getProgressForTask(taskId).map { it?.toDomain() }
    }

    override suspend fun updateProgress(progress: UserProgress) {
        userProgressDao.updateProgress(progress.toEntity())
    }

    override suspend fun getProgressForProgram(programId: String): List<UserProgress> {
        return userProgressDao.getProgressForProgram(programId).map { it.toDomain() }
    }
}
