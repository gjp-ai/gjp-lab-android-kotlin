# Decision records

Short notes on project choices that the code alone does not explain. Read them before reversing one of these choices; write a new record (rather than editing an old one) when a decision changes.

## Format

File name: `NNNN-short-title.md`, numbered in order. Each record has:

- **Status:** Accepted, or Superseded by a later record (with a link).
- **Context:** the problem and the forces at play.
- **Decision:** what was chosen.
- **Consequences:** what becomes easier, what becomes harder, and the rules that follow.

A record may end with a one-line **Related** note pointing to the matching choice in the iOS lab (`gjp-lab-ios-swift`).

## Records

| # | Decision | Date |
| --- | --- | --- |
| [0001](0001-flat-feature-folders.md) | One flat folder per feature; the app shell lives in `shell/` | 2026-10-04 |
| [0002](0002-sdk-code-in-integration-features.md) | SDK code lives in `features/integration/<sdk>/` | 2026-10-04 |
| [0003](0003-activity-navigation.md) | Activity-based navigation with explicit Intents (superseded by 0005) | 2026-10-04 |
| [0004](0004-navigation-menu-in-kotlin.md) | Dashboard and catalogue content in one `NavigationMenu.kt` | 2026-10-04 |
| [0005](0005-adaptive-pane-navigation.md) | One navigation Activity with adaptive panes | 2026-10-04 |
