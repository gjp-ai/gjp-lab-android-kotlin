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
| `SlateWarmCanvas` | `#FFFCF8` | Light-theme application background |

Supporting grays in [`Color.kt`](../../../../app/src/main/java/com/ganjianping/lab/ak/common/theme/Color.kt) create distinguishable surface elevations, outlines, and secondary content without introducing another hue.

## Material 3 role strategy

[`Theme.kt`](../../../../app/src/main/java/com/ganjianping/lab/ak/common/theme/Theme.kt) defines complete light and dark schemes.

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

Navigation rows are `surface` cards with a hairline `outlineVariant` border on the warm `background` canvas (see [Adaptive navigation](#adaptive-navigation)). Do not restore gray-filled category cards.

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

- [`ic_launcher_background.xml`](../../../../app/src/main/res/drawable/ic_launcher_background.xml)
- [`ic_launcher_foreground.xml`](../../../../app/src/main/res/drawable/ic_launcher_foreground.xml)
- [`ic_launcher_monochrome.xml`](../../../../app/src/main/res/drawable/ic_launcher_monochrome.xml)
- [`mipmap-anydpi-v26`](../../../../app/src/main/res/mipmap-anydpi-v26)

The manifest references `@mipmap/ic_launcher` and `@mipmap/ic_launcher_round`. Do not restore fixed-shape PNG icons for supported devices; adaptive masking and monochrome theming are part of the icon contract.

## Notification icon

Android status-bar notifications require a dedicated alpha-style small icon rather than the full launcher artwork. [`ic_notification.xml`](../../../../app/src/main/res/drawable/ic_notification.xml) uses the flask silhouette and lets Android apply the notification color treatment.

## Splash alignment

[`SplashScreen`](../../../../app/src/main/java/com/ganjianping/lab/ak/shell/startup/SplashScreen.kt) follows the active system mode: the surrounding screen uses semantic Material 3 background and text roles, and the brand tile inverts between black-on-light and white-on-dark. Both variants preserve the same flask mark. Light and dark previews verify the complete treatment.

## Adaptive navigation

[`ContentView`](../../../../app/src/main/java/com/ganjianping/lab/ak/shell/ContentView.kt) arranges the category sidebar, the catalogue, and the feature by window width, using width breakpoints rather than device-model checks:

| Window width | Layout |
| --- | --- |
| Under `840dp` | One stack: categories → catalogue → feature, with a back arrow in each pane's top app bar |
| `840dp` to under `1200dp` | Two panes: categories or catalogue (360dp), then the feature |
| `1200dp` and above | Three panes: categories (320dp), catalogue (360dp), feature; separated by `outlineVariant` dividers |

**Every pane uses the same Slate canvas in each theme (`#FFFCF8` light, `#0D0D0D` dark).** [`NavigationPane`](../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationPane.kt) gives each pane a `TopAppBar` on the `background` colour, and limits its content to 720dp, centred. Empty panes show [`NavigationPlaceholder`](../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationPane.kt) ("Choose a category", "Choose a topic") in `onSurfaceVariant`.

Sidebar and catalogue rows are each their own card through [`LabListCard`](../../../../app/src/main/java/com/ganjianping/lab/ak/common/theme/LabListCard.kt): a `surface` rounded rectangle (18dp corners) with a 0.5dp `outlineVariant` border, 16dp padding, spaced 12dp apart. A selected row gets a 1dp `primary` border instead, which is how the current category and topic stay visible when panes sit side by side. Sidebar rows add a 44dp `primaryContainer` icon tile with `onPrimaryContainer` content.

In the catalogue, implemented topics end with a chevron; planned topics end with a clock and cannot be selected. They must not imply that a feature already exists. Maintain the phone and tablet `ContentView` previews in light and dark when changing navigation.

## Accessibility and review checklist

- Verify every foreground/background pair through its Material role pairing.
- Keep essential text contrast suitable for normal text, not only large display text.
- Test light and dark previews and at least one physical or emulated device.
- Inspect adaptive masks and the Android 13 themed-icon treatment in a launcher.
- Confirm the notification icon remains recognizable at status-bar size.
- Preserve text or icon cues for state; never communicate meaning through color alone.
- Re-run `:app:assembleDebug` after changing vector paths, resource qualifiers, or color roles.
