# Category catalogue detailed design

Status: Implemented, with known gaps

Requirements: [Category catalogue](catalog_requirement.md)

## Implementation goal

Render a category's topics as a list of cards in the catalogue pane, derive availability from whether a topic has a route, and let `ContentView` show the selected feature through `FeatureDestination`.

## Source map

| Source | Responsibility |
| --- | --- |
| [`FeatureCatalogScreen.kt`](../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureCatalogScreen.kt) | Description row, topic cards, and `CatalogRow` |
| [`LabListCard.kt`](../../../../app/src/main/java/com/ganjianping/lab/ak/common/theme/LabListCard.kt) | Bordered row card shared with the sidebar |
| [`NavigationMenu.kt`](../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationMenu.kt) | Every category and its topics, in display order; a topic with a `route` is available |
| [`FeatureRoute.kt`](../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureRoute.kt) | One case per implemented feature |
| [`ContentView.kt`](../../../../app/src/main/java/com/ganjianping/lab/ak/shell/ContentView.kt) | Owns `selectedTopic` and places the catalogue pane |
| [`FeatureDestination.kt`](../../../../app/src/main/java/com/ganjianping/lab/ak/shell/FeatureDestination.kt) | Maps the selected `FeatureRoute` to its screen |

## Ownership and selection

The screen receives a `NavigationCategory`, the currently selected `FeatureRoute?`, and `onFeatureSelected`. It is stateless. A row with a route gets a click handler; a planned row gets none, so it can never become the selection. The screen never builds a destination: `FeatureDestination` maps the selected route to its screen:

| `FeatureRoute` | Screen |
| --- | --- |
| `DeviceInfo` | `DeviceInfoScreen` |
| `HttpURLConnection` | `HttpURLConnectionScreen` (pushes `DetailRoute.Response` → `HttpResponseScreen`) |
| `Firebase` | `FirebaseFeatureScreen` |
| `BlockAppDuringCalls` | `BlockAppDuringCallsScreen` |

See the [sidebar detailed design](sidebar_detail_design.md#navigation-model) for how the panes collapse on narrow windows.

## Current topics

| Category | Available | Planned |
| --- | --- | --- |
| Kotlin | All 10 topics (values and types, null safety, collections, functions and lambdas, classes, interfaces and generics, error handling, coroutines, extensions and scope functions, strings and regex); see [`doc/specs/features/kotlin/`](../../features/kotlin/) | — |
| Jetpack Compose | All 10 topics (Material 3, layouts, text and input, buttons, selection, lists and grids, navigation, animation, drawing, accessibility); see [`doc/specs/features/compose/`](../../features/compose/) | — |
| HTTP Client | HttpURLConnection | Retrofit |
| Security | Block App During Calls | Screenshot, screen sharing, and screen recording detection |
| Integration | Firebase | — |
| Others | OS & Hardware | — |

## Accessibility

The category description is a plain first item, so it scrolls with the list. Each topic card is one element (clickable rows merge their children; planned rows use `mergeDescendants`). The chevron is announced as "Open" and the clock as "Planned"; available rows have the click label "Open". Descriptions wrap at large text sizes.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| Category copy is out of date | Security's description mentions only screen capture, though it also lists call blocking | Update the `description` in `NavigationMenu` |
| Planned rows give no feedback when tapped | Users may think the tap failed | Accepted by the requirement (CAT-AC-03); revisit if confusing |
| UI tests cover only the Kotlin and Compose categories | Opening HTTP Client, Security, Integration, and Others topics is unguarded | Add a UI test that opens every topic in those categories |

## Verification

- Previews in `FeatureCatalogScreen.kt`: HTTP client catalogue (available and planned topics) and Compose catalogue (available only), each in light and dark.
- Unit tests: `NavigationMenuTest` (unique IDs and titles, every `FeatureRoute` exactly once, category and topic lookup).
- UI tests: `KotlinTopicsTest.everyKotlinTopicOpens` and `ComposeTopicsTest.everyComposeTopicOpens` scroll the catalogue, open each topic, and return with Back.
- Manual: CAT-AC-01 to CAT-AC-05 on a phone and at a large window width, with TalkBack and the largest font size.
