package com.ganjianping.lab.ak.features.security.blockappduringcalls

enum class CallMonitoringAvailability {
    Off,
    PermissionRequired,
    Unavailable,
    MonitoringActive
}

data class CallBlockingState(
    val isEnabled: Boolean = false,
    val availability: CallMonitoringAvailability = CallMonitoringAvailability.Off,
    val hasActiveCall: Boolean = false,
    val isTestCallActive: Boolean = false
) {
    val isBlocking: Boolean
        get() = isEnabled && availability == CallMonitoringAvailability.MonitoringActive &&
            (hasActiveCall || isTestCallActive)
}
