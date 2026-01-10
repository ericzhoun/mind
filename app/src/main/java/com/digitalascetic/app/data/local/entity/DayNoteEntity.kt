package com.digitalascetic.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.digitalascetic.app.domain.model.DayNote
import java.time.LocalDateTime

@Entity(
    tableName = "day_notes",
    foreignKeys = [
        ForeignKey(
            entity = ProgramDayEntity::class,
            parentColumns = ["id"],
            childColumns = ["dayId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DayNoteEntity(
    @PrimaryKey val dayId: String,
    val noteText: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

fun DayNoteEntity.toDomain() = DayNote(
    dayId = dayId,
    noteText = noteText,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun DayNote.toEntity() = DayNoteEntity(
    dayId = dayId,
    noteText = noteText,
    createdAt = createdAt,
    updatedAt = updatedAt
)
