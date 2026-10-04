# Decision records

Short notes on project choices that the code alone does not explain. Read them before reversing one of these choices; write a new record (rather than editing an old one) when a decision changes.

The iOS lab (the sibling `gjp-lab-ios-swift` project) is the reference for folder structure and names. These records say where Android follows it and where it deliberately differs.

## Format

File name: `NNNN-short-title.md`, numbered in order. Each record has:

- **Status:** Accepted, or Superseded by a later record (with a link).
- **Context:** the problem and the forces at play.
- **Decision:** what was chosen.
- **Consequences:** what becomes easier, what becomes harder, and the rules that follow.

## Records

| # | Decision | Date |
| --- | --- | --- |
| [0001](0001-folder-structure-mirrors-ios.md) | Folder structure and names mirror the iOS lab; the app shell lives in `shell/` | 2026-10-04 |
| [0002](0002-sdk-code-in-integration-features.md) | SDK code lives in `features/integration/<sdk>/` | 2026-10-04 |
| [0003](0003-activity-navigation.md) | Navigation stays Activity-based with explicit Intents | 2026-10-04 |
| [0004](0004-navigation-menu-in-kotlin.md) | Dashboard and catalogue content in one `NavigationMenu.kt` | 2026-10-04 |
