package com.digitalascetic.app.domain.model

/**
 * Sealed interface representing polymorphic task types.
 * 
 * Each task type has specific payload properties relevant to its behavior.
 */
sealed interface Task {
    val id: String
    val dayId: String
    val title: String
    
    /**
     * Simple boolean completion task (e.g., "Take a cold shower").
     */
    data class ChecklistTask(
        override val id: String,
        override val dayId: String,
        override val title: String,
        val durationMinutes: Int? = null
    ) : Task
    
    /**
     * Timer-based task with scheduled time window (e.g., "Morning Meditation 04:30-06:30").
     */
    data class TimedTask(
        override val id: String,
        override val dayId: String,
        override val title: String,
        val startTime: String,
        val endTime: String,
        val strictMode: Boolean = false
    ) : Task
    
    /**
     * Numeric input task with target value (e.g., "Swim 20 laps").
     */
    data class MetricTask(
        override val id: String,
        override val dayId: String,
        override val title: String,
        val targetValue: Int,
        val unit: String
    ) : Task
    
    /**
     * Text input task for journaling (e.g., "Evening Reflection").
     */
    data class JournalTask(
        override val id: String,
        override val dayId: String,
        override val title: String,
        val promptText: String? = null
    ) : Task
    /**
     * Video task for lectures or guided sessions (e.g., "Dhamma Talk").
     */
    data class VideoTask(
        override val id: String,
        override val dayId: String,
        override val title: String,
        val videoUrl: String,
        val durationMinutes: Int? = null
    ) : Task
}

