# Feature: Block App During Calls

Status: Partial implementation

## Goal

Offer an optional, best-effort privacy control that prevents interaction with GJPLab while Android reports a system-exposed ongoing call. The control must be clear about what Android cannot observe and must never imply that it can protect against every phone, VoIP, or video call.

## Scope

### In scope

- A Security catalogue entry and a dedicated settings screen.
- An explicit user choice to enable call-state monitoring and grant the minimum runtime permission.
- A persisted preference, current availability/status, simulated-call verification path, and app-wide blocking overlay.
- Best-effort monitoring of calls exposed through public Android Telecom APIs while GJPLab is in the foreground.

### Out of scope

- Placing, answering, recording, screening, or otherwise controlling calls.
- Becoming the default dialer, declaring an `InCallService`, or requesting the dialer role.
- Accessibility-service, notification-listener, usage-access, overlay, private-API, or app-specific workarounds to infer call state.
- Detection of calls from apps that Android does not expose through Telecom.
- Blocking other applications, background monitoring, or a guarantee that every call will be detected.

## Behavior

- The feature starts disabled. Selecting **Enable call blocking** explains the `READ_PHONE_STATE` purpose and starts Android's runtime-permission request; it must not request permission at app launch.
- When enabled and permission is granted, the app reads aggregate call presence with `TelecomManager.isInCall()` whenever the process enters the foreground and when a feature Activity resumes. A true result blocks interaction with every currently supported GJPLab Activity.
- `TelephonyCallback.CallStateListener` may update the overlay promptly for mobile telephony call-state changes on API 31 and above. It is not evidence of third-party VoIP/video call state.
- The app removes the overlay after a subsequent supported check reports no active call, when the user disables the feature, or when the simulated call ends.
- The app stores only the enabled preference. It must not collect, display, persist, log, or send phone numbers, call handles, contact data, call metadata, or provider names.
- The simulated-call control exercises the same derived blocking condition as a real call but never invokes Telecom, requests a permission, or places a call.

## UI & navigation

- Add **Block App During Calls** as a planned Security-catalogue item; make it navigable only with the feature implementation.
- The destination must show the enabled preference and one unambiguous status: **Off**, **Permission required**, **Unavailable on this device**, **Monitoring active**, or **Call detected**.
- Before requesting `READ_PHONE_STATE`, explain that the permission lets GJPLab determine whether Android reports an ongoing call; it does not give access to call content or contacts.
- If the user denies or later revokes permission, show **Permission required**, provide a non-coercive route to app settings when appropriate, and leave the app usable. Do not repeatedly prompt.
- The blocking overlay is app-owned, accessible, and shown above the destination content. It has no bypass for a real call; a simulated call exposes only a test-only **End simulated call** action. It says that access resumes when Android no longer reports a call or the feature is turned off, and it does not identify the call or its provider.
- Follow the existing Material 3, Activity-navigation, dark-theme, text-scale, and Back-navigation patterns.

## Rules & constraints

| Decision | Requirement |
| --- | --- |
| Default | Off until the user explicitly enables monitoring. |
| Permission | Declare and request only `READ_PHONE_STATE`; request it from the settings action, not at startup. |
| Call source | Use public `TelecomManager.isInCall()` for aggregate presence; do not infer state from audio focus, notifications, installed packages, accessibility events, or network activity. |
| Updates | Use lifecycle refreshes; use `TelephonyCallback.CallStateListener` only as a mobile-telephony update signal on API 31+. |
| Unsupported state | A denied permission, no telephony/Telecom capability, API failure, or unobservable provider leaves the app unblocked and clearly reports the limitation. |
| Default-dialer role | Never request it merely to improve detection. It would make GJPLab responsible for a complete dialer and in-call experience. |
| Storage and logging | Persist only the Boolean preference. Keep call data out of intents, analytics, logs, notifications, clipboard, backups, and crash reports. |
| Background behavior | Do not register a background service, periodic worker, or persistent notification. Re-evaluate when GJPLab returns to the foreground. |

## Platform limitations

- `READ_PHONE_STATE` is a runtime permission. Without it, GJPLab cannot call the relevant public call-state APIs and must not claim to be protecting the user.
- `TelecomManager.isInCall()` reports only calls Android's Telecom framework exposes. It can include managed or self-managed `ConnectionService` calls, but it does not provide a portable guarantee for WhatsApp, WeChat, Telegram, or any other named provider.
- `TelephonyCallback.CallStateListener` reports mobile telephony state for a subscription; it is not an aggregate VoIP/video-call listener.
- Devices without the necessary telephony or Telecom capability, OEM variations, and a provider's choice not to expose a call can make monitoring unavailable or incomplete.
- Android cannot make GJPLab's overlay block other apps. The control applies only while this app is visible and only to Activities that integrate the shared blocking state.

## Acceptance criteria

| ID | Scenario | Expected result |
| --- | --- | --- |
| BAC-AC-01 | First open of the settings screen | The feature is off and no phone-state permission prompt appears. |
| BAC-AC-02 | User selects enable and grants permission; Android reports an active call while GJPLab is foregrounded | An app-wide overlay blocks every implemented GJPLab route without exposing call details or a real-call bypass. |
| BAC-AC-03 | Android later reports no active call | The overlay is removed automatically and access is restored. |
| BAC-AC-04 | User disables the setting during an active or simulated call | The overlay is removed and the app remains usable; the preference survives restart. |
| BAC-AC-05 | User denies or revokes permission | The app remains usable, reports **Permission required**, and does not repeatedly prompt. |
| BAC-AC-06 | GJPLab launches or returns to the foreground while Android reports an active system-exposed call | The current state is refreshed and the overlay appears when monitoring is enabled and permission is granted. |
| BAC-AC-07 | User starts the simulated-call test while monitoring is enabled | The same overlay appears without placing a real call or reading call state. |
| BAC-AC-08 | A third-party call is not exposed through Android Telecom | The app does not claim to detect or block it; the settings screen explains the limitation. |
| BAC-AC-09 | Device lacks the required capability or a public API call fails | The app remains usable and reports that monitoring is unavailable. |

## Implementation status

| Area | Status | Evidence or gap |
| --- | --- | --- |
| Security catalogue, settings Activity, preference, and simulated-call path | Implemented | `FeatureRoute`, `FeatureCatalogScreen`, and `features/security/blockappduringcalls/` |
| App-wide overlay for the current Activities | Implemented | Each current Activity hosts `CallBlockingHost` |
| Explicit runtime permission | Implemented | Settings Activity requests `READ_PHONE_STATE` only after enable |
| Foreground aggregate state refresh | Implemented | `TelecomManager.isInCall()` on Activity resume |
| Prompt mobile call changes on API 31+ | Implemented | `TelephonyCallback.CallStateListener` while an Activity is foregrounded |
| Real-call device verification | Planned | Requires a suitable physical device, granted permission, and system-exposed call |
| VoIP/video provider coverage | Open | Android determines whether a provider exposes its call through Telecom |

## Technical implementation constraints

- Add the Security catalogue route, a feature Activity/screen under `features/security/blockappduringcalls/`, and a small platform adapter; do not construct Android services in a composable.
- Keep the persisted preference and derived `isBlocking` state separate from Android API calls. Inject the adapter through Koin and make the simulated state controllable in tests.
- The app-wide overlay must be integrated at every feature-Activity root or through an explicitly introduced shared host. A screen-local overlay does not satisfy the app-wide requirement.
- Keep the runtime-permission launcher in the settings Activity. Declare `READ_PHONE_STATE` in the manifest only when the feature is implemented.
- Register callbacks only while an appropriate foreground lifecycle owner is active, unregister them deterministically, and handle `SecurityException` as unavailable rather than crashing or weakening the design.
- Add deterministic JVM tests for derived blocking state and Compose/instrumented coverage for permission-denied, simulated-call, lifecycle-refresh, overlay, and preference behavior. Tests must not depend on a live call or a third-party app.
- Do not add a dependency, default-dialer component, foreground service, or unrelated architecture migration solely for this feature.

## Related documents

- [Detailed design](../../detail-design/security/block_app_during_calls.md)
- [Application architecture](../../architecture/application.md)
- [Android platform call-state APIs](https://developer.android.com/reference/android/telecom/TelecomManager#isInCall())
- [Telephony callback call-state API](https://developer.android.com/reference/android/telephony/TelephonyCallback.CallStateListener)
- [Default phone application requirements](https://developer.android.com/develop/connectivity/telecom/dialer-app)
