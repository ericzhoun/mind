package com.digitalascetic.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.digitalascetic.app.data.local.entity.ProgramDayEntity
import com.digitalascetic.app.data.local.entity.ProgramEntity
import com.digitalascetic.app.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgramDao {
    @Query("SELECT * FROM programs")
    fun getAllPrograms(): Flow<List<ProgramEntity>>

    @Query("SELECT * FROM programs WHERE id = :id")
    suspend fun getProgramById(id: String): ProgramEntity?

    @Query("SELECT * FROM program_days WHERE programId = :programId ORDER BY dayIndex ASC")
    suspend fun getDaysForProgram(programId: String): List<ProgramDayEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgram(program: ProgramEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgramDay(day: ProgramDayEntity)
}
