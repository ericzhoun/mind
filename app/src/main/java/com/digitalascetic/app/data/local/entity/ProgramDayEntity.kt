package com.digitalascetic.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.digitalascetic.app.domain.model.ProgramDay

@Entity(
    tableName = "program_days",
    foreignKeys = [
        ForeignKey(
            entity = ProgramEntity::class,
            parentColumns = ["id"],
            childColumns = ["programId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProgramDayEntity(
    @PrimaryKey val id: String,
    val programId: String,
    val dayIndex: Int,
    val theme: String,
    val instructionText: String
)

fun ProgramDayEntity.toDomain() = ProgramDay(
    id = id,
    programId = programId,
    dayIndex = dayIndex,
    theme = theme,
    instructionText = instructionText
)

fun ProgramDay.toEntity() = ProgramDayEntity(
    id = id,
    programId = programId,
    dayIndex = dayIndex,
    theme = theme,
    instructionText = instructionText
)
