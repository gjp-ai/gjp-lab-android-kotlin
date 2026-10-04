# Feature: Category dashboard

Status: Implemented

The code name is `CategorySidebar`, matching the iOS lab; on Android it is a full-screen grid of category cards.

## Goal

Give users one starting point that lists every lab category with a short summary and opens that category's catalogue in one tap, on phones, foldables, and tablets.

## Scope

### In scope

- The dashboard shown after startup: header and category cards.
- Choosing a category to open its catalogue.
- Adapting the grid to the window width.

### Out of scope

- The catalogue's contents (see the [catalogue requirement](catalog_requirement.md)) and the feature screens.
- Search, favorites, recently used items, deep links, and restoring the last screen after relaunch.

## Behavior

- After startup (unless maintenance is on) the app shows the dashboard.
- Tapping a category card opens that category's catalogue; Back returns to the dashboard.
- Categories appear in this order: Jetpack Compose, HTTP Client, Security, Integration, Others.

## UI & navigation

- Header "Android lab" with a one-line introduction.
- One card per category: icon and title on one line, then a short summary.
- The grid uses the window width, not the device model:

| Window width | Columns |
| --- | --- |
| Under 600dp | 2 |
| 600dp to under 1100dp | 3 |
| 1100dp and above | 5 (all categories in one row) |

- Supports light and dark themes and enlarged text.

## Rules & constraints

- Category titles, summaries, and icons come from one source (`NavigationMenu`); the dashboard does not hard-code them.
- Topic availability is shown in the catalogue, not on the dashboard.
- Use width breakpoints, never device-name or model checks.

## Platform limitations

- Foldables and resizable windows change width at run time; the grid re-lays out on each configuration change.

## Acceptance criteria

| ID | Scenario | Expected result |
| --- | --- | --- |
| SDB-AC-01 | Startup completes with maintenance off | The dashboard lists all five categories in order with icon, title, and summary. |
| SDB-AC-02 | Tap a category | Its catalogue opens; Back returns to the dashboard. |
| SDB-AC-03 | Phone, foldable (unfolded), and tablet landscape | 2, 3, and 5 columns respectively. |
| SDB-AC-04 | TalkBack on a card | The card is announced as one clickable element with its title and summary. |
| SDB-AC-05 | Dark theme and a large font size | Cards stay readable; titles are not clipped mid-word. |

## Technical implementation constraints

- Source lives in `shell/navigation/`; `MainActivity` hosts the dashboard and opens `FeatureCatalogActivity` with the category `id`.
- Adding a category means adding an entry to `NavigationMenu`; the dashboard updates automatically.
- No new dependencies.

## Related documents

- [Detailed design](sidebar_detail_design.md)
- [Catalogue requirement](catalog_requirement.md)
- [Slate design system](../../common/theme/theme_detail_design.md)
