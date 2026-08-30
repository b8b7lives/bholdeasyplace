First CurseForge release. Notes below cover every version since the
custom item work landed; the full history is in CHANGELOG.md on GitHub.

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
