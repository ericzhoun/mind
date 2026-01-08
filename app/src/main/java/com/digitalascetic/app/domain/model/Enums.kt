package com.digitalascetic.app.domain.model

enum class ProgramType {
    LINEAR,      // e.g., 21-Day Stoic (Sequence)
    SCHEDULE,    // e.g., 10-Day Vipassana (Strict Timetable)
    ACCUMULATOR  // e.g., YMCA Challenge (Gamified Metrics)
}

enum class TaskType {
    CHECKLIST,   // Simple boolean completion
    TIMED,       // Timer based (e.g., Meditation)
    METRIC,      // Numeric input (e.g., Laps)
    JOURNAL      // Text input (e.g., Reflection)
}

enum class TaskStatus {
    PENDING,
    COMPLETED,
    FAILED,
    SKIPPED
}
