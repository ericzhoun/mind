package com.digitalascetic.app.data.repository

import com.digitalascetic.app.data.local.dao.DayNoteDao
import com.digitalascetic.app.data.local.entity.toDomain
import com.digitalascetic.app.data.local.entity.toEntity
import com.digitalascetic.app.domain.model.DayNote
import com.digitalascetic.app.domain.repository.DayNoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DayNoteRepositoryImpl @Inject constructor(
    private val dayNoteDao: DayNoteDao
) : DayNoteRepository {
    
    override fun getDayNote(dayId: String): Flow<DayNote?> {
        return dayNoteDao.getDayNote(dayId).map { it?.toDomain() }
    }
    
    override suspend fun saveDayNote(note: DayNote) {
        dayNoteDao.insertOrUpdateDayNote(note.toEntity())
    }
    
    override suspend fun deleteDayNote(dayId: String) {
        dayNoteDao.deleteDayNote(dayId)
    }
}
