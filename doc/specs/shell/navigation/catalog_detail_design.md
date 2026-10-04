# Category catalogue detailed design

Status: Implemented, with known gaps

Requirements: [Category catalogue](catalog_requirement.md)

## Implementation goal

Render one category's topics as a table-style card, derive availability from whether a topic has a route, and let `FeatureCatalogActivity` open the matching feature Activity with an explicit `Intent`.

## Source map

| Source | Responsibility |
| --- | --- |
| [`FeatureCatalogActivity.kt`](../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureCatalogActivity.kt) | Reads the category `id` extra, hosts the screen and call blocking, maps `FeatureRoute` to an Activity |
| [`FeatureCatalogScreen.kt`](../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureCatalogScreen.kt) | Header, `CatalogTable`, and `CatalogTableRow` |
| [`NavigationMenu.kt`](../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationMenu.kt) | Every category and its topics, in display order; a topic with a `route` is available |
| [`FeatureRoute.kt`](../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureRoute.kt) | One case per implemented feature |
| [`AndroidManifest.xml`](../../../../app/src/main/AndroidManifest.xml) | Non-exported catalogue and feature Activities |

## Ownership and navigation

`FeatureCatalogActivity.createIntent(context, category)` puts `category.id` in `EXTRA_CATEGORY`. On create, the Activity looks the id up with `NavigationMenu.category(id)` and finishes if it is missing. The screen is stateless: it receives the `NavigationCategory` and reports taps through `onFeatureSelected(FeatureRoute)` and `onBack`. Only rows with a route get a `clickable` modifier, so planned rows cannot open anything.

`openFeature` is the only place that maps a route to an Activity:

| `FeatureRoute` | Activity |
| --- | --- |
| `DeviceInfo` | `DeviceInfoActivity` |
| `HttpURLConnection` | `HttpURLConnectionActivity` |
| `Firebase` | `FirebaseFeatureActivity` |
| `BlockAppDuringCalls` | `BlockAppDuringCallsActivity` |

## Current topics

| Category | Available | Planned |
| --- | --- | --- |
| Jetpack Compose | — | Material 3, layouts, text and input, buttons, selection, lists and grids, navigation, animation, drawing, accessibility |
| HTTP Client | HttpsURLConnection | Retrofit |
| Security | Block App During Calls | Screenshot, screen sharing, and screen recording detection |
| Integration | Firebase | — |
| Others | OS & Hardware | — |

## Accessibility

The forward arrow's content description is "Open <title>" and the clock's is "Planned". Rows are not merged into a single semantics node, so TalkBack reads the title, description, and icon separately.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| Back uses a text button ("‹  Dashboard") instead of a top app bar | Differs from Material navigation patterns | Use a `TopAppBar` with a navigation icon |
| Rows are not merged for accessibility | TalkBack needs several swipes per row | `Modifier.semantics(mergeDescendants = true)` on each row |
| Planned rows give no feedback when tapped | Users may think the tap failed | Accepted by the requirement (CAT-AC-03); revisit if confusing |
| Topic title "HttpsURLConnection" differs from the API name `HttpURLConnection` | Inconsistent naming in the catalogue | Align the title with the API |
| No UI test | Opening each topic is unguarded | Add a Compose UI test that opens each available topic |

## Verification

- Previews in `FeatureCatalogScreen.kt`: HTTP client catalogue (light and dark) and Compose catalogue on a tablet.
- Unit test: `NavigationMenuTest` (every `FeatureRoute` appears exactly once, category lookup by id).
- Manual: CAT-AC-01 to CAT-AC-05 on a phone and a tablet emulator, with TalkBack and the largest font size.
