package com.digitalascetic.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.digitalascetic.app.domain.model.Program
import com.digitalascetic.app.domain.model.ProgramType

@Entity(tableName = "programs")
data class ProgramEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val type: ProgramType,
    val durationDays: Int
)

fun ProgramEntity.toDomain() = Program(
    id = id,
    title = title,
    description = description,
    type = type,
    durationDays = durationDays
)

fun Program.toEntity() = ProgramEntity(
    id = id,
    title = title,
    description = description,
    type = type,
    durationDays = durationDays
)
