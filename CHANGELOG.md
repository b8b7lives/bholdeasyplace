# Changelog

## 0.5.0+26.2 (2026-08-22)

- New icon, a hold ring in the mod's red on the dark background.
- New banner art for the repo and listing.
- Display name is now bHold easyPlace. The mod id and your configs are
  unchanged.

## 0.4.1+26.2 (2026-08-22)

- Fix, the loss watchdog no longer false alarms on infinite blocks and
  cyclers. Server custom items change their hidden data every time the
  server gives one back or the player cycles a mode, so tracking exact
  items produced a warning on nearly every placement. The watchdog now
  tracks the total count of custom items instead, which survives both.
  Warnings are also rate limited to one per thirty seconds.

## 0.4.0+26.2 (2026-08-22)

- Custom item server support. Easy place now accepts server custom
  items such as infinite blocks. Litematica's misplacement guard
  demanded exact item data on the held stack while the rest of easy
  place matched by base item, so any custom item failed and clicks on
  existing blocks were cancelled. When the schematic wants a plain
  block, the guard now matches by base item. Items that need exact
  data, like filled shulkers, stay exact. The toggle is named Lenient Item Match.
- Easy place leaves your hotbar alone when what you hold already
  matches the target. The toggle is named Skip Pick Block When Held.
- Pick block never moves another item into a slot holding a server
  custom item. The toggle is named Protect Custom Items.
- New watchdog warns when a server custom item leaves your inventory
  and is not given back within two seconds, with a timestamped log
  line for lost item tickets. Detection only, the loss itself happens
  server side. The toggle is named Custom Item Loss Watchdog.
- New loadcheck harness, the production jar now boots against the full
  play modlist in CI before any release.

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
