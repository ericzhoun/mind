package com.digitalascetic.app.domain.service

/**
 * Phase 2 interface for AI-powered reflection capabilities.
 * 
 * Generates personalized prompts and summarizes journal entries using on-device AI.
 */
interface ReflectionAI {
    /**
     * Generate a daily reflection prompt based on program context.
     * @param contextJson JSON containing program, day, and user progress data
     * @return Generated prompt text
     */
    suspend fun generateDailyPrompt(contextJson: String): String
    
    /**
     * Summarize multiple journal entries into key insights.
     * @param entryIds List of journal entry IDs to summarize
     * @return Summary text with key themes and insights
     */
    suspend fun summarizeJournalEntries(entryIds: List<String>): String
}
