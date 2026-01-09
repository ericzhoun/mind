package com.digitalascetic.app.data.mapper

import com.digitalascetic.app.data.local.entity.TaskEntity
import com.digitalascetic.app.domain.model.Task
import com.digitalascetic.app.domain.model.TaskType
import com.google.gson.Gson
import com.google.gson.JsonObject
import javax.inject.Inject

/**
 * Maps between TaskEntity (Room persistence with JSON payload) and Task sealed class.
 * 
 * Converts JSON payloads to strongly-typed polymorphic Task implementations.
 */
class TaskMapper @Inject constructor(
    private val gson: Gson
) {
    
    /**
     * Convert TaskEntity to domain Task sealed class.
     */
    fun toTask(entity: TaskEntity): Task {
        val payload = if (entity.payloadJson.isNotBlank()) {
            gson.fromJson(entity.payloadJson, JsonObject::class.java)
        } else {
            JsonObject()
        }
        
        return when (entity.type) {
            TaskType.CHECKLIST -> Task.ChecklistTask(
                id = entity.id,
                dayId = entity.dayId,
                title = entity.title,
                durationMinutes = payload.get("duration_min")?.asInt
            )
            TaskType.TIMED -> {
                val appBlockRules = entity.appBlockRulesJson?.let {
                    gson.fromJson(it, JsonObject::class.java)
                }
                Task.TimedTask(
                    id = entity.id,
                    dayId = entity.dayId,
                    title = entity.title,
                    startTime = payload.get("start")?.asString ?: "",
                    endTime = payload.get("end")?.asString ?: "",
                    strictMode = appBlockRules?.get("strict_mode")?.asBoolean ?: false
                )
            }
            TaskType.METRIC -> Task.MetricTask(
                id = entity.id,
                dayId = entity.dayId,
                title = entity.title,
                targetValue = payload.get("target")?.asInt ?: 0,
                unit = payload.get("unit")?.asString ?: ""
            )
            TaskType.JOURNAL -> Task.JournalTask(
                id = entity.id,
                dayId = entity.dayId,
                title = entity.title,
                promptText = payload.get("prompt")?.asString
            )
        }
    }
    
    /**
     * Convert domain Task sealed class to TaskEntity for Room persistence.
     */
    fun toEntity(task: Task): TaskEntity {
        return when (task) {
            is Task.ChecklistTask -> TaskEntity(
                id = task.id,
                dayId = task.dayId,
                type = TaskType.CHECKLIST,
                title = task.title,
                payloadJson = gson.toJson(mapOf("duration_min" to task.durationMinutes)),
                appBlockRulesJson = null
            )
            is Task.TimedTask -> TaskEntity(
                id = task.id,
                dayId = task.dayId,
                type = TaskType.TIMED,
                title = task.title,
                payloadJson = gson.toJson(mapOf("start" to task.startTime, "end" to task.endTime)),
                appBlockRulesJson = if (task.strictMode) {
                    gson.toJson(mapOf("strict_mode" to true))
                } else null
            )
            is Task.MetricTask -> TaskEntity(
                id = task.id,
                dayId = task.dayId,
                type = TaskType.METRIC,
                title = task.title,
                payloadJson = gson.toJson(mapOf("target" to task.targetValue, "unit" to task.unit)),
                appBlockRulesJson = null
            )
            is Task.JournalTask -> TaskEntity(
                id = task.id,
                dayId = task.dayId,
                type = TaskType.JOURNAL,
                title = task.title,
                payloadJson = gson.toJson(mapOf("prompt" to task.promptText)),
                appBlockRulesJson = null
            )
        }
    }
}
