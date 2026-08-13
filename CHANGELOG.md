# Changelog

## 0.3.1+26.2 (2026-08-13)

- Replaced the icon with the handmade version (the 0.3.0 icon export
  exceeded Modrinth's icon size cap).
- Marked the two extended Litematica config lists `@Mutable`, removing
  two cosmetic "@Final field should be final" warnings at startup.

## 0.3.0+26.2 (2026-08-13)

- Renamed to **bHold Easy Place** (mod id `bholdeasyplace`) for the
  public release. Saved Litematica config values carry over unchanged.
- Declares a conflict (`breaks`) with the original `holdeasyplace` mod
  id so a leftover 0.1.x jar produces a clear error instead of firing
  Easy Place twice per tick.
- Added mod icon.
- First release published to GitHub and Modrinth.

## 0.2.0+26.2 (2026-08-10)

- Rewritten for Minecraft 26.2 against Litematica 0.28.4 / malilib
  0.29.3 (upstream Hold-Easy-Place ends at v0.1.8 for 1.21.x).
- Supports both the legacy and `easyPlacePostRewrite` Litematica paths.
- Inactive in REBUILD tool mode, with a GUI open, or while the real use
  key is held, since Litematica's native paths own those ticks.
- Hold-mode placement failures are silent. Manual clicks keep stock
  Litematica messaging.
- Dropped from upstream: fail-tip configs, `cacheTime`,
  schematics-folder button.

## Upstream

Hold-Easy-Place v0.1.8 by shane0411 (MIT) is the last upstream release,
for Minecraft 1.21.x: https://github.com/shane04111/Hold-Easy-Place
