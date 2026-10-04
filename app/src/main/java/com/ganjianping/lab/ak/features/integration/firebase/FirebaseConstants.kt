package com.ganjianping.lab.ak.features.integration.firebase

object FirebaseConstants {
    const val EventAppStarted = "app_started"
    // Firebase reserves the firebase_, google_, and ga_ prefixes for system events.
    const val EventFirebaseFeatureOpened = "feature_firebase_opened"
    const val CrashlyticsAppVersionKey = "app_version"
    const val CrashlyticsDemoKey = "firebase_demo"
    const val RemoteConfigMaintenanceEnabled = "gjp_lab_maintenance_enabled"
    const val PerformanceDemoTrace = "firebase_demo_trace"
    const val MessagingDemoTopic = "gjp_lab_demo"
    const val MessagingNotificationChannelId = "gjp_lab_firebase_messages"
    const val MessagingNotificationChannelName = "Firebase messages"
}
