package com.digitalascetic.app.data.service

import com.digitalascetic.app.domain.service.ReflectionAI
import javax.inject.Inject

/**
 * Mock implementation of ReflectionAI for development and testing.
 * 
 * Returns stub responses without requiring actual AI model access.
 */
class MockReflectionAI @Inject constructor() : ReflectionAI {
    
    override suspend fun generateDailyPrompt(contextJson: String): String {
        return "Mock: What challenges did you face today? How did you respond to them with resilience?"
    }
    
    override suspend fun summarizeJournalEntries(entryIds: List<String>): String {
        return "Mock Summary: Key themes identified from ${entryIds.size} entries - " +
               "perseverance, mindfulness, and personal growth."
    }
}
