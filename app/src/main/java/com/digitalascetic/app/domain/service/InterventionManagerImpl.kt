package com.digitalascetic.app.domain.service

import javax.inject.Inject

class InterventionManagerImpl @Inject constructor() : InterventionManager {
    override fun isAppBlocked(packageName: String): Boolean {
        // Placeholder implementation
        return false
    }

    override fun triggerIntervention(packageName: String) {
        // Placeholder implementation
    }
}
