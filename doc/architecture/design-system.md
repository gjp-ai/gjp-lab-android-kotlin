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

The dashboard uses white `surface` cards on the warm light background, with a black `primary` top rail and restrained Material elevation. Do not restore gray-filled category cards.

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

## Adaptive dashboard

[`MainScreen`](../../app/src/main/java/com/ganjianping/lab/ak/MainScreen.kt) adapts to the available window width, so the same category dashboard works on a phone, a foldable's larger window, and a tablet without relying on a device-name check. A category opens the table-style [`FeatureCatalogScreen`](../../app/src/main/java/com/ganjianping/lab/ak/features/catalog/FeatureCatalogScreen.kt), which constrains its readable content width on larger displays.

| Window width | Layout | Purpose |
| --- | --- | --- |
| Under `600dp` | Two-category grid below a concise header | Compact phones retain readable, tap-friendly cards. |
| `600dp` to under `840dp` | Three-category grid with compact, top-aligned cards | Foldables and medium windows use added horizontal space without oversized card interiors. |
| `840dp` to under `1100dp` | The same three-category grid as medium windows | Medium tablets retain the foldable layout and its compact, top-aligned cards. |
| `1100dp` and above | Five-category grid in one row | Wide tablet windows use the available horizontal space without an empty grid cell. |

The dashboard categories are Jetpack Compose, HTTP client, Security, Integration, and Others. Every category card uses one inline icon-and-title row followed by concise, complete supporting copy; sequence numbers and a separate icon row are intentionally omitted. Two-column phone cards use a smaller icon and `titleSmall` scale so category names remain visible. Medium windows use a three-column compact-card layout. At `1100dp` and wider, the tablet layout places all five categories in one row. Catalogue rows for implemented labs show an open affordance; unimplemented learning topics show a planned affordance and must not imply that a feature already exists. When changing the dashboard, maintain the phone, foldable, tablet, and dark-phone Compose previews; use width breakpoints rather than model-specific branches.

The feature catalogue uses the same warm canvas, white elevated surface, 24dp corners, and 6dp primary accent rail as the dashboard. Its table structure remains intentionally denser because it presents component rows rather than top-level categories.

## Accessibility and review checklist

- Verify every foreground/background pair through its Material role pairing.
- Keep essential text contrast suitable for normal text, not only large display text.
- Test light and dark previews and at least one physical or emulated device.
- Inspect adaptive masks and the Android 13 themed-icon treatment in a launcher.
- Confirm the notification icon remains recognizable at status-bar size.
- Preserve text or icon cues for state; never communicate meaning through color alone.
- Re-run `:app:assembleDebug` after changing vector paths, resource qualifiers, or color roles.
