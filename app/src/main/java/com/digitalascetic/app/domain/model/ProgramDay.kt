package com.digitalascetic.app.domain.model

data class ProgramDay(
    val id: String,
    val programId: String,
    val dayIndex: Int, // 1-based index (Day 1, Day 2...)
    val theme: String,
    val instructionText: String
)
