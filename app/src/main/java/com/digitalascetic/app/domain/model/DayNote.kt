package com.digitalascetic.app.domain.model

import java.time.LocalDateTime

data class DayNote(
    val dayId: String,
    val noteText: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
