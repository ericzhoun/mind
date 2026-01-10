package com.digitalascetic.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.digitalascetic.app.domain.model.TaskStatus
import com.digitalascetic.app.domain.model.UserProgress
import java.time.LocalDateTime

@Entity(
    tableName = "user_progress",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserProgressEntity(
    @PrimaryKey val taskId: String,
    val status: TaskStatus,
    val completedAt: LocalDateTime?,
    val value: String?,
    val failureReason: String?,
    val minutesSpent: Int?
)

fun UserProgressEntity.toDomain() = UserProgress(
    taskId = taskId,
    status = status,
    completedAt = completedAt,
    value = value,
    failureReason = failureReason,
    minutesSpent = minutesSpent
)

fun UserProgress.toEntity() = UserProgressEntity(
    taskId = taskId,
    status = status,
    completedAt = completedAt,
    value = value,
    failureReason = failureReason,
    minutesSpent = minutesSpent
)
