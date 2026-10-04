# Feature: Firebase lab

Status: Implemented

## Goal

Let a developer trigger each Firebase service used by GJP Lab on demand and see the result, to verify the integration and learn how each service behaves.

## Scope

### In scope

- One action per service: Analytics, Crashlytics, Remote Config, Performance Monitoring, and Cloud Messaging.
- A status line per service showing the last result.
- Showing, selecting, and copying the FCM registration token, and subscribing to the demo topic.
- Requesting notification permission on Android 13 and newer.

### Out of scope

- App startup configuration and the maintenance decision (see the [Firebase detailed design](firebase_detail_design.md) and the [splash requirement](../../../shell/startup/splash_requirement.md)).
- Sending push notifications, editing Remote Config values, or viewing console data.
- Forcing a real crash.

## Behavior

- Opening the screen logs `feature_firebase_opened` and, on Android 13+, asks for notification permission if it is not granted.
- **Log event** sends `feature_firebase_opened` again.
- **Record exception** records a non-fatal exception with a log line and a `firebase_demo` custom key; the app keeps running.
- **Fetch flag** fetches and activates Remote Config and shows the `gjp_lab_maintenance_enabled` value.
- **Run trace** runs the short `firebase_demo_trace` custom trace and shows its duration.
- **Get token** loads the FCM registration token and shows it with a copy action.
- **Subscribe to demo topic** subscribes to `gjp_lab_demo` and shows the result.
- Denying notification permission leaves token retrieval available.

## UI & navigation

- Entry point: **Integration** category → **Firebase** catalogue item.
- Title "Firebase", an introduction, one card per service (title, description, status, action button), the token row when loaded, and the subscribe button.
- Light and dark themes and enlarged text are supported; on phones, Back returns to the catalogue.

## Rules & constraints

- Event names, keys, trace names, topics, and channel IDs come from `FirebaseConstants`; renaming one is an integration change.
- Actions send no personal data and no event parameters.
- The full FCM token is shown only on this screen and copied only on request; it is never logged.
- No service accounts, server keys, or App Check debug tokens in the app.

## Platform limitations

- Console data (Analytics, Crashlytics, Performance) can take minutes to appear.
- Notifications are not shown without `POST_NOTIFICATIONS` permission on Android 13+; push delivery needs Google Play services.

## Acceptance criteria

| ID | Scenario | Expected result |
| --- | --- | --- |
| FB-AC-01 | Open the screen | All five service cards are shown; on Android 13+ the notification prompt appears once if not yet granted. |
| FB-AC-02 | Tap **Log event** | The Analytics status reports the event was sent. |
| FB-AC-03 | Tap **Record exception** | The Crashlytics status reports a recorded non-fatal; the app does not crash. |
| FB-AC-04 | Tap **Fetch flag** with and without network | The status shows the current flag value. |
| FB-AC-05 | Tap **Run trace** | The status shows the trace completed with a duration. |
| FB-AC-06 | Tap **Get token**, then copy | The token is shown, the copy action confirms, and the clipboard holds the token. |
| FB-AC-07 | Tap **Subscribe to demo topic** | The Messaging status reports success or failure. |

## Technical implementation constraints

- All Firebase source lives in `features/integration/firebase/`; code calls Firebase only through the Koin-provided `FirebaseIntegration`.
- `FirebaseFeatureScreen` receives `FirebaseIntegration` and owns the status state and the notification-permission launcher.
- No new dependencies.

## Related documents

- [Detailed design](firebase_detail_design.md)
- [Application architecture](../../../../architecture/application.md)
