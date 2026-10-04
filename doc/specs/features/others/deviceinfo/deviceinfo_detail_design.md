# OS & hardware detailed design

Status: Implemented, with known gaps

Requirements: [OS & hardware](deviceinfo_requirement.md)

## Implementation goal

Read platform values once through a small repository and present them as two labeled cards, without identifiers or personal data.

## Source map

| Source | Responsibility |
| --- | --- |
| [`DeviceInfoScreen.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/others/deviceinfo/DeviceInfoScreen.kt) | Screen layout and `InfoSection` cards |
| [`DeviceInfoRepository.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/others/deviceinfo/DeviceInfoRepository.kt) | Reads all values and returns one list of rows |
| [`InfoRow.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/features/others/deviceinfo/InfoRow.kt) | Label and value pair |
| [`AppModule.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/AppModule.kt) | Registers the repository as a Koin singleton; `MainActivity` injects it |
| [`FeatureDestination.kt`](../../../../../app/src/main/java/com/ganjianping/lab/ak/shell/FeatureDestination.kt) | Shows `DeviceInfoScreen` for `FeatureRoute.DeviceInfo` |

## Data sources

| Row | Source |
| --- | --- |
| Android version | `Build.VERSION.RELEASE` |
| SDK level | `Build.VERSION.SDK_INT` |
| Codename | `Build.VERSION.CODENAME` (`REL` on release builds) |
| Screen | `resources.displayMetrics` width × height in pixels |
| Manufacturer, Model, Device, Hardware | `Build.MANUFACTURER`, `Build.MODEL`, `Build.DEVICE`, `Build.HARDWARE` |
| CPU cores | `Runtime.availableProcessors()` |
| Memory | `ActivityManager.MemoryInfo.totalMem` in MB |
| Supported ABIs | `Build.SUPPORTED_ABIS` |

## Ownership

`MainActivity` injects `DeviceInfoRepository`, and `FeatureDestination` passes it to the screen, which calls `read()` once inside `remember(repository)`. The pane supplies the title "OS & Hardware" and, on phones, the back arrow. The screen splits the list by position: the first four rows are **Android OS**, the rest **Hardware**. No state is shared or persisted.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| Sections split by list position (`take(4)`) | Adding a row to the OS section silently moves a row between cards | Return named sections from the repository |
| Screen size uses display metrics | Can exclude system bars and change with orientation | Use `WindowManager.currentWindowMetrics` bounds |
| Memory uses binary MB and no formatting | Large values are hard to read | Format with `Formatter.formatFileSize` |
| No automated tests | Formatting is unguarded | Unit-test formatting with injected values |

## Verification

- Build and test with the commands in [application architecture](../../../../architecture/application.md#build-and-verification).
- Previews: `DeviceInfoContent` with sample rows, light and dark.
- Automated: none.
- Manual: DEV-AC-01 to DEV-AC-03 on an emulator and a physical device, and at the largest font size.
