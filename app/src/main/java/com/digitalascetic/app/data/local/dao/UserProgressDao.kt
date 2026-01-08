package com.digitalascetic.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.digitalascetic.app.data.local.entity.UserProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProgressDao {
    @Query("SELECT * FROM user_progress WHERE taskId = :taskId")
    fun getProgressForTask(taskId: String): Flow<UserProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProgress(progress: UserProgressEntity)

    @Query("""
        SELECT up.* FROM user_progress up
        INNER JOIN tasks t ON up.taskId = t.id
        INNER JOIN program_days pd ON t.dayId = pd.id
        WHERE pd.programId = :programId
    """)
    suspend fun getProgressForProgram(programId: String): List<UserProgressEntity>
}
