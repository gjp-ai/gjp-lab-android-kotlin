# Slate design system

Status: Implemented

## Intent

GJPLab uses a restrained, high-contrast Slate direction based on three reference colors: black, white, and neutral `#E8E8E8`. The system applies those colors through Material 3 semantic roles rather than assigning raw colors inside feature composables.

The result should feel direct and technical: strong hierarchy, neutral surfaces, generous contrast, and minimal decorative color. Error roles remain red because status must not rely on the brand palette alone.

## Core palette

| Token | Value | Primary use |
| --- | --- | --- |
| `SlateBlack` | `#000000` | Light-theme primary actions and launcher background |
| `SlateWhite` | `#FFFFFF` | Light surfaces, dark-theme primary actions, and icon mark |
| `SlateNeutral` | `#E8E8E8` | Containers, grouping, and neutral emphasis |

Supporting grays in [`Color.kt`](../../app/src/main/java/com/ganjianping/lab/ak/common/theme/Color.kt) create distinguishable surface elevations, outlines, and secondary content without introducing another hue.

## Material 3 role strategy

[`Theme.kt`](../../app/src/main/java/com/ganjianping/lab/ak/common/theme/Theme.kt) defines complete light and dark schemes.

| Intent | Light scheme | Dark scheme |
| --- | --- | --- |
| Primary action | Black with white content | White with black content |
| Primary container | `#E8E8E8` with black content | Dark gray with white content |
| Background | White with near-black content | Near-black with `#E8E8E8` content |
| Surface hierarchy | White through stepped neutral grays | Near-black through stepped charcoal grays |
| Secondary content | Dark gray | Light gray |
| Error | Material red role pair | Material dark-theme red role pair |

Feature code should consume `MaterialTheme.colorScheme` pairs such as:

- `primary` / `onPrimary` for the strongest action or brand mark;
- `primaryContainer` / `onPrimaryContainer` for emphasized cards;
- `surface` / `onSurface` for ordinary content;
- `surfaceContainer*` for tonal grouping and elevation;
- `onSurfaceVariant` for supporting copy;
- `error` / `onError` and `errorContainer` / `onErrorContainer` for failures.

Do not infer content colors manually. Always use the paired `on*` role. Do not use gray alone to communicate error, selection, disabled state, or progress.

## Dynamic color

The Slate palette is the default on every supported API level. `GJPLabTheme(dynamicColor = true)` remains available for an explicitly wallpaper-personalized surface, but ordinary app screens do not enable it. This prevents Android 12+ dynamic color from replacing the requested brand identity unexpectedly.

## Launcher icon

The launcher icon is a native adaptive icon:

- black full-bleed background;
- white circular field with an original flask-shaped negative-space cutout and liquid detail;
- artwork kept inside the adaptive-icon safe region so circle, squircle, rounded-square, and OEM masks remain legible;
- identical regular and round adaptive definitions because the launcher owns the final mask;
- monochrome layer on Android 13+ for themed icons.

Source resources:

- [`ic_launcher_background.xml`](../../app/src/main/res/drawable/ic_launcher_background.xml)
- [`ic_launcher_foreground.xml`](../../app/src/main/res/drawable/ic_launcher_foreground.xml)
- [`ic_launcher_monochrome.xml`](../../app/src/main/res/drawable/ic_launcher_monochrome.xml)
- [`mipmap-anydpi-v26`](../../app/src/main/res/mipmap-anydpi-v26/)

The manifest references `@mipmap/ic_launcher` and `@mipmap/ic_launcher_round`. Do not restore fixed-shape PNG icons for supported devices; adaptive masking and monochrome theming are part of the icon contract.

## Notification icon

Android status-bar notifications require a dedicated alpha-style small icon rather than the full launcher artwork. [`ic_notification.xml`](../../app/src/main/res/drawable/ic_notification.xml) uses the flask silhouette and lets Android apply the notification color treatment.

## Splash alignment

[`SplashScreen`](../../app/src/main/java/com/ganjianping/lab/ak/SplashScreen.kt) follows the active system mode: the surrounding screen uses semantic Material 3 background and text roles, and the brand tile inverts between black-on-light and white-on-dark. Both variants preserve the same flask mark. Light and dark previews verify the complete treatment.

## Accessibility and review checklist

- Verify every foreground/background pair through its Material role pairing.
- Keep essential text contrast suitable for normal text, not only large display text.
- Test light and dark previews and at least one physical or emulated device.
- Inspect adaptive masks and the Android 13 themed-icon treatment in a launcher.
- Confirm the notification icon remains recognizable at status-bar size.
- Preserve text or icon cues for state; never communicate meaning through color alone.
- Re-run `:app:assembleDebug` after changing vector paths, resource qualifiers, or color roles.
