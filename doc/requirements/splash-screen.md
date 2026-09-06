# Feature: Splash Screen

Status: Baseline behavior; Android implementation is partial

## Goal

Show a recognizable startup experience while resolving whether the user should enter the normal application or see maintenance. Startup must remain bounded when connectivity or Remote Config is unavailable.

## Platform considerations

The observable startup rules target Android and iOS. Each platform should use its native lifecycle, connectivity, accessibility, and navigation conventions while preserving the same timing, fallback, race, and destination decisions. This repository implements and verifies only the Android side; an iOS project must maintain its own technical design and evidence.

In this document, **feature splash** means the application-owned loading experience after the operating system launch screen. A **usable network** is a path the platform reports as capable of internet access; the remote request can still fail after that signal.

## Scope

In scope:

- cold-launch feature splash;
- brand and progress presentation;
- minimum display duration;
- network-aware maintenance lookup;
- failure, timeout, and late-result behavior;
- exactly-once transition to main or maintenance;
- light, dark, text-scaling, and reduced-motion behavior.

Out of scope:

- platform launch-screen artwork before the feature Activity renders;
- authentication, onboarding, consent, updates, advertising, or content preload;
- visual design of the dashboard or maintenance destination;
- showing the feature splash during warm resume or ordinary navigation.

## Rules & constraints

| Decision | Value |
| --- | --- |
| Minimum feature-splash duration | 3 seconds |
| Remote request timeout | 5 seconds after a request starts |
| Offline fallback | Maintenance disabled |
| Failed or timed-out request fallback | Maintenance disabled when no accepted value is available |
| Race behavior | First terminal result wins |
| Navigation | Exactly once; splash removed from history |
| Maintenance retry | Allowed without recreating the splash |

## Functional requirements

### Presentation

| ID | Requirement |
| --- | --- |
| SPL-FR-01 | Show the feature splash once during a cold launch. |
| SPL-FR-02 | Do not show it during warm resume or after the user enters the application. |
| SPL-FR-03 | Show the approved brand mark, application name, and an understandable indeterminate progress state. |
| SPL-FR-04 | Use the active light or dark appearance and keep essential content readable at supported text scales and safe areas. |
| SPL-FR-05 | Continue automatically without user interaction. |

### Resolution and timing

| ID | Requirement |
| --- | --- |
| SPL-FR-10 | Keep the feature splash visible for at least 3 seconds after it becomes visible. |
| SPL-FR-11 | Determine whether a validated internet connection is available before starting Remote Config. |
| SPL-FR-12 | When offline, skip the remote request and resolve maintenance as disabled. |
| SPL-FR-13 | When online, request the Boolean maintenance setting and wait no more than 5 seconds. |
| SPL-FR-14 | Accept the first terminal remote result or timeout and ignore later completions for startup navigation. |
| SPL-FR-15 | Navigate only after both the minimum-duration gate and maintenance-resolution gate complete. |
| SPL-FR-16 | Navigate without an additional intentional delay when the second gate completes. |

### Destination

| ID | Requirement |
| --- | --- |
| SPL-FR-20 | Open maintenance when the accepted value is enabled; otherwise open the dashboard. |
| SPL-FR-21 | Leave the splash flow exactly once and prevent Back from revealing it. |
| SPL-FR-22 | Let the user retry from maintenance without returning to the splash. |

## Decision table

| Network | Remote outcome | Outcome time | Destination | Earliest navigation |
| --- | --- | --- | --- | --- |
| Unavailable | Request skipped | Immediate | Dashboard | 3 seconds |
| Available | Disabled | Before 3 seconds | Dashboard | 3 seconds |
| Available | Enabled | Before 3 seconds | Maintenance | 3 seconds |
| Available | Disabled | 3–5 seconds | Dashboard | On response |
| Available | Enabled | 3–5 seconds | Maintenance | On response |
| Available | Failure with no accepted value | Before timeout | Dashboard | Later of failure or 3 seconds |
| Available | No terminal response | 5 seconds | Dashboard | 5 seconds |
| Available | Result after timeout | After 5 seconds | Unchanged | Already navigated |

The minimum-duration and maintenance-resolution timers are independent. Both gates must complete; neither extends the other.

## Non-functional requirements

| ID | Requirement |
| --- | --- |
| SPL-NFR-01 | Offline startup performs no Remote Config request. |
| SPL-NFR-02 | Completion and navigation are idempotent under timeout/callback races. |
| SPL-NFR-03 | Essential text and progress semantics are available to accessibility services. |
| SPL-NFR-04 | Non-essential animation respects the platform reduced-motion preference. |
| SPL-NFR-05 | Startup does not collect, display, or log personal information. |
| SPL-NFR-06 | Timing, connectivity, remote lookup, presentation, and navigation remain independently testable where practical. |
| SPL-NFR-07 | Automated tests use controllable network and remote outcomes rather than production services. |
| SPL-NFR-08 | Android and iOS preserve equivalent user-visible decisions while using platform-native implementations. |

## Acceptance scenarios

| ID | Scenario | Expected result |
| --- | --- | --- |
| SPL-AC-01 | Offline cold launch | No remote request; splash lasts at least 3 seconds; dashboard opens once. |
| SPL-AC-02 | Disabled response before 3 seconds | Dashboard opens at the minimum duration. |
| SPL-AC-03 | Enabled response before 3 seconds | Maintenance opens at the minimum duration. |
| SPL-AC-04 | Response between 3 and 5 seconds | Matching destination opens immediately on response. |
| SPL-AC-05 | No response by 5 seconds | Dashboard opens at timeout. |
| SPL-AC-06 | Callback arrives after timeout | Callback is ignored and the destination does not change. |
| SPL-AC-07 | Warm resume or Back from destination | Feature splash is not shown. |
| SPL-AC-08 | Light/dark mode and enlarged text | Approved content remains readable and unclipped. |
| SPL-AC-09 | Reduced motion | Non-essential animation is reduced; progress remains understandable. |

## Technical implementation constraints

Verification requires controllable inputs for connectivity, enabled/disabled maintenance, success, failure, delayed response, no response, callback races, appearance, text scale, and reduced motion. The Android implementation status and concrete test seams are tracked in [the detailed design](../detail-design/splash-screen.md).

## Open decisions

- Final logo artwork, brand colors, subtitle, and loading copy.
- Whether a previously activated `true` value should remain authoritative when refresh fails.
- Whether the 3-second minimum should remain fixed after the learning exercise.
- Required startup analytics events and permitted event fields.
- Whether activity recreation must continue an in-progress startup or may restart it.

## Related documents

- [Detailed design](../detail-design/splash-screen.md)
- [Application architecture](../architecture/application.md)
