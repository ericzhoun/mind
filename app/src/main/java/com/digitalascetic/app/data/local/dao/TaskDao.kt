package com.digitalascetic.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.digitalascetic.app.data.local.entity.TaskEntity

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE dayId = :dayId")
    suspend fun getTasksForDay(dayId: String): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)
}
