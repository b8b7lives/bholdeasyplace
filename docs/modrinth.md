# Modrinth listing kit

Everything needed to create the Modrinth project by hand. Fill the web
form from this file top to bottom.

## Project settings

| Field | Value |
|---|---|
| Name | bHold Easy Place |
| URL slug | `bhold-easy-place` |
| Visibility | Public (goes to review queue first, typically 24-48h) |
| Summary | Keeps Litematica's Easy Place running without holding the use key. 26.x continuation of Hold-Easy-Place. |
| Categories | Utility |
| Loaders | Fabric |
| Environments | Client-side: required; Server-side: unsupported |
| License | MIT |
| Source | https://github.com/b8b7lives/bholdeasyplace |
| Issues | https://github.com/b8b7lives/bholdeasyplace/issues |
| Icon | `icon/bholdeasyplace-handmade-icon.png` (512x512, 27KB, under Modrinth's 256KB cap) |

## Description (paste as the body)

---

Build from schematics without wearing out your right mouse button.
**bHold Easy Place** keeps
[Litematica](https://modrinth.com/mod/litematica)'s Easy Place mode
firing every client tick, with no use key held.

This is the Minecraft 26.x continuation of
[Hold-Easy-Place](https://modrinth.com/mod/hold-easy-place) by shane0411
(MIT). The last upstream release supports 1.21.x. This version was
rewritten against current Litematica and malilib internals.

## Features

- Adds *Hold Easy Place Mode* to Litematica's Generic config tab and a
  *Toggle Hold Easy Place* hotkey to its Hotkeys tab. Settings persist
  in `litematica.json` like any other Litematica option.
- Stays inactive in REBUILD tool mode, while a GUI is open, and while
  you physically hold the use key, so Litematica's native handling
  keeps working normally.
- Works with both the legacy Easy Place path and
  `easyPlacePostRewrite`.
- Hold-mode placement failures don't spam chat. Your own clicks keep
  stock Litematica feedback.

## Requirements

[Fabric API](https://modrinth.com/mod/fabric-api),
[malilib](https://modrinth.com/mod/malilib), and
[Litematica](https://modrinth.com/mod/litematica). Client-side only.

If you still have the original Hold-Easy-Place jar installed, remove
it. This mod declares a conflict with it, because both mods would drive
Easy Place on the same tick.

## Credits

- [shane0411](https://github.com/shane04111) wrote the original
  Hold-Easy-Place.
- [masa](https://modrinth.com/user/masa) wrote Litematica and malilib.

Source on [GitHub](https://github.com/b8b7lives/bholdeasyplace), MIT.

---

## First version upload

| Field | Value |
|---|---|
| File | `build/libs/bholdeasyplace-0.3.0+26.2.jar` |
| Version number | 0.3.0+26.2 |
| Version title | bHold Easy Place 0.3.0 |
| Channel | Release |
| Loaders | Fabric |
| Game versions | 26.2 |
| Dependencies | Litematica (required), malilib (required), Fabric API (required) |
| Changelog | Paste the 0.3.0 + 0.2.0 sections from CHANGELOG.md |
