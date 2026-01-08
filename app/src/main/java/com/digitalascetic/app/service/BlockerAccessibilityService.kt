package com.digitalascetic.app.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.digitalascetic.app.domain.service.InterventionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BlockerAccessibilityService : AccessibilityService() {

    @Inject
    lateinit var interventionManager: InterventionManager

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Phase 3 Implementation:
        // if (interventionManager.isAppBlocked(event?.packageName)) { ... }
    }

    override fun onInterrupt() {
        // Handle interruption
    }
}
