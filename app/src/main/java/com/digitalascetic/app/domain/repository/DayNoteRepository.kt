package com.digitalascetic.app.domain.repository

import com.digitalascetic.app.domain.model.DayNote
import kotlinx.coroutines.flow.Flow

interface DayNoteRepository {
    fun getDayNote(dayId: String): Flow<DayNote?>
    suspend fun saveDayNote(note: DayNote)
    suspend fun deleteDayNote(dayId: String)
}
