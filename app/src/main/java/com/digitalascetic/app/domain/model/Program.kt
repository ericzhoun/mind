package com.digitalascetic.app.domain.model

data class Program(
    val id: String,
    val title: String,
    val description: String,
    val type: ProgramType,
    val durationDays: Int
)
