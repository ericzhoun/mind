package com.digitalascetic.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.digitalascetic.app.domain.model.TaskType

/**
 * Room entity for storing tasks with polymorphic payloads.
 * 
 * The payloadJson field stores type-specific data as JSON.
 * Use TaskMapper to convert between TaskEntity and Task sealed class.
 */
@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = ProgramDayEntity::class,
            parentColumns = ["id"],
            childColumns = ["dayId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["dayId"])]
)
data class TaskEntity(
    @PrimaryKey val id: String,
    val dayId: String,
    val type: TaskType,
    val title: String,
    val payloadJson: String,
    val appBlockRulesJson: String?
)

