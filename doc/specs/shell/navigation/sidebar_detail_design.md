# Category dashboard detailed design

Status: Implemented, with known gaps

Requirements: [Category dashboard](sidebar_requirement.md)

## Implementation goal

Render `NavigationMenu.categories` as an adaptive card grid inside `MainActivity`, and hand the chosen category to `FeatureCatalogActivity` by `id`. The code keeps the iOS name `CategorySidebar`; Android uses Activity navigation rather than a split view ([decision 0003](../../../decisions/0003-activity-navigation.md)).

## Source map

| Source | Responsibility |
| --- | --- |
| [`CategorySidebar.kt`](../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/CategorySidebar.kt) | Width-based layout selection, header, grid, and category cards |
| [`NavigationMenu.kt`](../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/NavigationMenu.kt) | Category order, title, summary, and icon |
| [`MainActivity.kt`](../../../../app/src/main/java/com/ganjianping/lab/ak/shell/MainActivity.kt) | Hosts the dashboard and starts `FeatureCatalogActivity.createIntent(this, category)` |
| [`FeatureCatalogActivity.kt`](../../../../app/src/main/java/com/ganjianping/lab/ak/shell/navigation/FeatureCatalogActivity.kt) | Receives the category `id` extra |

## Layout model

`CategorySidebar` measures its width with `BoxWithConstraints` and picks a private `DashboardLayout`:

| Layout | Width | Columns | Card aspect ratio | Card style |
| --- | --- | --- | --- | --- |
| `Compact` | < 600dp | 2 | 1.1 | Smaller icon, `titleSmall` |
| `Medium` | 600–1099dp | 3 | 1.45 | `titleMedium` |
| `TabletLandscape` | ≥ 1100dp | 5 | 1.45 | `titleMedium` |

Each card is a Material 3 `Card` on `surface` with a 6dp `primary` top rail, an icon-and-title row, and the category `summary` (two lines at most). See the [Slate design system](../../common/theme/theme_detail_design.md#adaptive-dashboard).

## Ownership and state

The dashboard is stateless: it reads the static `NavigationMenu` and reports taps through `onCategorySelected(NavigationCategory)`. `MainActivity` owns navigation. Nothing is restored after process death; the app always starts at the splash.

## Known gaps

| Gap | Effect | Suggested fix |
| --- | --- | --- |
| Titles are limited to one line with ellipsis | "Jetpack Compose" may be cut off on narrow phones or at large font sizes | Allow two lines in the compact layout, or a shorter title |
| Summaries are clipped to two lines | Long text is cut without an ellipsis | Use `TextOverflow.Ellipsis`, or keep summaries short (a rule) |
| Fixed aspect-ratio cards | At large font sizes content can overflow the card | Let cards grow with content in the compact layout |
| Name differs from behavior | `CategorySidebar` is a grid, not a sidebar | Accepted for iOS name parity (decision 0001) |
| No UI test | Opening each category is unguarded | Add a Compose UI test that taps each card |

## Verification

- Previews in `CategorySidebar.kt`: phone, foldable, tablet, and phone dark.
- Unit test: `NavigationMenuTest` (unique category IDs, every route present once).
- Manual: SDB-AC-01 to SDB-AC-05 on a phone emulator, a foldable emulator (folded and unfolded), and a tablet emulator in landscape, with TalkBack and the largest font size.
