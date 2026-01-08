package com.digitalascetic.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.digitalascetic.app.domain.model.Task
import com.digitalascetic.app.domain.model.TaskType

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = ProgramDayEntity::class,
            parentColumns = ["id"],
            childColumns = ["dayId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskEntity(
    @PrimaryKey val id: String,
    val dayId: String,
    val type: TaskType,
    val title: String,
    val payloadJson: String,
    val appBlockRulesJson: String?
)

fun TaskEntity.toDomain() = Task(
    id = id,
    dayId = dayId,
    type = type,
    title = title,
    payloadJson = payloadJson,
    appBlockRulesJson = appBlockRulesJson
)

fun Task.toEntity() = TaskEntity(
    id = id,
    dayId = dayId,
    type = type,
    title = title,
    payloadJson = payloadJson,
    appBlockRulesJson = appBlockRulesJson
)
