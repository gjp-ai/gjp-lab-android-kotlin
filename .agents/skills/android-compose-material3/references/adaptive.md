# Adaptive layout guide

Use this reference only when the product scope includes changing window sizes, tablets, foldables, desktop windows, multi-window mode, or multiple display postures.

## Decide from available space

- Adapt to the current window size and available space, not a hard-coded device category or orientation.
- Preserve task continuity while the window resizes. Avoid destroying user input, selection, scroll position, or navigation context.
- Choose a canonical pattern that fits the content relationship: list-detail, supporting pane, or a responsive single-pane layout.
- Adapt top-level navigation deliberately. Do not swap navigation components merely because a width threshold was crossed if the destinations or user flow do not justify it.

## Implementation boundary

- Prefer adaptive APIs already present in the project. Adding Material 3 Adaptive dependencies requires an explicit need and compatibility check.
- Keep compact behavior complete; expanded layouts should reveal useful simultaneous content rather than stretch controls across empty space.
- Test at representative compact, medium, and expanded widths relevant to the app, plus live resize or posture changes when applicable.
