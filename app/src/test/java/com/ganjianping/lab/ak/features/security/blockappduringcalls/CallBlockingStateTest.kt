package com.ganjianping.lab.ak.features.security.blockappduringcalls

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CallBlockingStateTest {
    @Test
    fun blocksOnlyWhenMonitoringIsActiveAndACallIsPresent() {
        val activeCall = CallBlockingState(
            isEnabled = true,
            availability = CallMonitoringAvailability.MonitoringActive,
            hasActiveCall = true
        )

        assertTrue(activeCall.isBlocking)
        assertFalse(activeCall.copy(isEnabled = false).isBlocking)
        assertFalse(activeCall.copy(availability = CallMonitoringAvailability.PermissionRequired).isBlocking)
        assertFalse(activeCall.copy(hasActiveCall = false).isBlocking)
    }

    @Test
    fun simulatedCallUsesTheSameBlockingDecision() {
        val simulatedCall = CallBlockingState(
            isEnabled = true,
            availability = CallMonitoringAvailability.MonitoringActive,
            isTestCallActive = true
        )

        assertTrue(simulatedCall.isBlocking)
        assertFalse(simulatedCall.copy(isTestCallActive = false).isBlocking)
    }
}
