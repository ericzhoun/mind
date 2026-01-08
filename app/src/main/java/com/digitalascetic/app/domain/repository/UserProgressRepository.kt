package com.digitalascetic.app.domain.repository

import com.digitalascetic.app.domain.model.UserProgress
import kotlinx.coroutines.flow.Flow

interface UserProgressRepository {
    fun getProgressForTask(taskId: String): Flow<UserProgress?>
    suspend fun updateProgress(progress: UserProgress)
    suspend fun getProgressForProgram(programId: String): List<UserProgress>
}
