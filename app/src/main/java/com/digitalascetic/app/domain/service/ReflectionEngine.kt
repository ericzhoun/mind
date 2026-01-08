package com.digitalascetic.app.domain.service

interface ReflectionEngine {
    suspend fun generateDailyPrompt(contextJson: String): String
    suspend fun summarizeJournalEntries(entryIds: List<String>): String
}
