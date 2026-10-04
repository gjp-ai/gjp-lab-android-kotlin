package com.ganjianping.lab.ak.common.accessibility

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/** The system accessibility settings the Compose topics react to. */
data class AccessibilityStatus(
    /** TalkBack, or another service that explores by touch, is running. */
    val isTalkBackOn: Boolean = false,
    /** Settings → Accessibility → Remove animations (animator duration scale 0). */
    val isRemoveAnimationsOn: Boolean = false
) {
    /** Screens skip decorative motion and apply state changes instantly when this is true. */
    val reduceMotion: Boolean get() = isRemoveAnimationsOn
}

/**
 * Reads [AccessibilityStatus] and reports changes while collected, so screens update without being
 * reopened. Composables receive the values; they never query the platform themselves.
 */
class AccessibilitySettings(private val context: Context) {
    private val accessibilityManager = context.getSystemService(AccessibilityManager::class.java)

    fun current(): AccessibilityStatus = AccessibilityStatus(
        isTalkBackOn = accessibilityManager?.isTouchExplorationEnabled == true,
        isRemoveAnimationsOn = Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            1f
        ) == 0f
    )

    val status: Flow<AccessibilityStatus> = callbackFlow {
        trySend(current())
        val touchListener = AccessibilityManager.TouchExplorationStateChangeListener { trySend(current()) }
        val animatorObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                trySend(current())
            }
        }
        accessibilityManager?.addTouchExplorationStateChangeListener(touchListener)
        context.contentResolver.registerContentObserver(
            Settings.Global.getUriFor(Settings.Global.ANIMATOR_DURATION_SCALE),
            false,
            animatorObserver
        )
        awaitClose {
            accessibilityManager?.removeTouchExplorationStateChangeListener(touchListener)
            context.contentResolver.unregisterContentObserver(animatorObserver)
        }
    }.distinctUntilChanged()
}
