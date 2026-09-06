package com.ganjianping.lab.ak.features.security.blockappduringcalls

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telecom.TelecomManager
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CallBlockingCoordinator(private val context: Context) {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val telecomManager = context.getSystemService(TelecomManager::class.java)
    private val telephonyManager = context.getSystemService(TelephonyManager::class.java)
    private val mutableState = MutableStateFlow(
        CallBlockingState(isEnabled = preferences.getBoolean(KEY_ENABLED, false))
    )

    val state: StateFlow<CallBlockingState> = mutableState.asStateFlow()

    private var callbackRegistered = false
    private val telephonyCallback = object : TelephonyCallback(), TelephonyCallback.CallStateListener {
        override fun onCallStateChanged(state: Int) {
            refreshCallStatus()
        }
    }

    fun setEnabled(enabled: Boolean) {
        preferences.edit().putBoolean(KEY_ENABLED, enabled).apply()
        if (!enabled) {
            stopMonitoring()
            mutableState.value = CallBlockingState(isEnabled = false)
            return
        }
        mutableState.value = mutableState.value.copy(isEnabled = true, isTestCallActive = false)
        startMonitoring()
    }

    fun onPermissionResult() {
        startMonitoring()
    }

    fun startMonitoring() {
        if (!mutableState.value.isEnabled) {
            mutableState.value = CallBlockingState(isEnabled = false)
            return
        }
        if (!hasPhoneStatePermission()) {
            stopMonitoring()
            mutableState.value = mutableState.value.copy(
                availability = CallMonitoringAvailability.PermissionRequired,
                hasActiveCall = false,
                isTestCallActive = false
            )
            return
        }
        if (telecomManager == null) {
            stopMonitoring()
            mutableState.value = mutableState.value.copy(
                availability = CallMonitoringAvailability.Unavailable,
                hasActiveCall = false,
                isTestCallActive = false
            )
            return
        }

        mutableState.value = mutableState.value.copy(
            availability = CallMonitoringAvailability.MonitoringActive
        )
        if (!registerTelephonyCallback()) return
        refreshCallStatus()
    }

    fun stopMonitoring() {
        if (callbackRegistered && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            telephonyManager?.unregisterTelephonyCallback(telephonyCallback)
            callbackRegistered = false
        }
    }

    fun refreshCallStatus() {
        if (!mutableState.value.isEnabled || !hasPhoneStatePermission() || telecomManager == null) return
        try {
            mutableState.value = mutableState.value.copy(
                availability = CallMonitoringAvailability.MonitoringActive,
                hasActiveCall = telecomManager.isInCall
            )
        } catch (_: SecurityException) {
            stopMonitoring()
            mutableState.value = mutableState.value.copy(
                availability = CallMonitoringAvailability.PermissionRequired,
                hasActiveCall = false,
                isTestCallActive = false
            )
        }
    }

    fun toggleTestCall() {
        val current = mutableState.value
        if (current.availability == CallMonitoringAvailability.MonitoringActive && current.isEnabled) {
            mutableState.value = current.copy(isTestCallActive = !current.isTestCallActive)
        }
    }

    private fun registerTelephonyCallback(): Boolean {
        if (callbackRegistered || Build.VERSION.SDK_INT < Build.VERSION_CODES.S || telephonyManager == null) {
            return true
        }
        return try {
            telephonyManager.registerTelephonyCallback(context.mainExecutor, telephonyCallback)
            callbackRegistered = true
            true
        } catch (_: SecurityException) {
            mutableState.value = mutableState.value.copy(
                availability = CallMonitoringAvailability.PermissionRequired,
                hasActiveCall = false,
                isTestCallActive = false
            )
            false
        }
    }

    private fun hasPhoneStatePermission(): Boolean =
        context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED

    private companion object {
        const val PREFERENCES_NAME = "security.block_app_during_calls"
        const val KEY_ENABLED = "enabled"
    }
}
