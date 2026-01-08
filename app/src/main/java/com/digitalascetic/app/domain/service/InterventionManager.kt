package com.digitalascetic.app.domain.service

interface InterventionManager {
    fun isAppBlocked(packageName: String): Boolean
    fun triggerIntervention(packageName: String)
}
