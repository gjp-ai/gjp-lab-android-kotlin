# Feature: OS & hardware

Status: Implemented

## Goal

Show a read-only snapshot of the Android version and hardware the app is running on, as a learning sample for reading platform information.

## Scope

### In scope

- Android details: release version, SDK level, codename, and screen resolution.
- Hardware details: manufacturer, model, device, hardware name, CPU core count, total memory, and supported ABIs.

### Out of scope

- Live values such as battery, storage, network, or thermal state.
- Device identifiers (Android ID, serial number, IMEI) or any personal data.
- Editing or exporting the values.

## Behavior

- The screen reads the values when it opens and shows them without user action.
- Values do not update while the screen is open.

## UI & navigation

- Entry point: **Others** category → **OS & Hardware** catalogue item.
- Pane title "OS & Hardware" (the topic title), a one-line introduction, and two cards: **Android OS** and **Hardware**, each a list of label and value rows.
- Light and dark themes and enlarged text are supported; on phones, Back returns to the catalogue.

## Rules & constraints

- Use only public APIs (`Build`, `ActivityManager.MemoryInfo`, display metrics, `Runtime`).
- Do not read or display identifiers or anything that identifies the user; no permission is required.

## Platform limitations

- On an emulator, manufacturer, model, and hardware show emulator values (for example `Google`, `sdk_gphone64_arm64`, `ranchu`).
- Screen size comes from display metrics and excludes system bars on some devices.

## Acceptance criteria

| ID | Scenario | Expected result |
| --- | --- | --- |
| DEV-AC-01 | Open the screen | Android version, SDK level, codename, and screen resolution are shown. |
| DEV-AC-02 | Hardware card | Manufacturer, model, device, hardware, CPU cores, memory, and ABIs are shown. |
| DEV-AC-03 | Large font size | Labels and values wrap and stay readable. |

## Technical implementation constraints

- Source lives in `features/others/deviceinfo/`.
- Platform APIs are called only by `DeviceInfoRepository`, which is injected through Koin and passed to the screen.
- No new dependencies.

## Related documents

- [Detailed design](deviceinfo_detail_design.md)
- [Application architecture](../../../../architecture/application.md)
