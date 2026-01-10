package com.digitalascetic.app.data.local.dao

import androidx.room.*
import com.digitalascetic.app.data.local.entity.DayNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DayNoteDao {
    @Query("SELECT * FROM day_notes WHERE dayId = :dayId")
    fun getDayNote(dayId: String): Flow<DayNoteEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDayNote(note: DayNoteEntity)
    
    @Query("DELETE FROM day_notes WHERE dayId = :dayId")
    suspend fun deleteDayNote(dayId: String)
}
