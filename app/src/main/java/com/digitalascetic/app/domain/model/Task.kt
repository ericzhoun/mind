package com.digitalascetic.app.domain.model

data class Task(
    val id: String,
    val dayId: String,
    val type: TaskType,
    val title: String,
    // JSON payloads stored as raw strings in the DB, parsed by the UI/Logic
    val payloadJson: String, 
    val appBlockRulesJson: String? = null
)
