# Modrinth listing source

Canonical source for the text on the Modrinth listing. Edit here first,
then apply to the listing.

## Project settings

| Field | Value |
|---|---|
| Name | bHold easyPlace |
| URL slug | `bhold-easy-place` |
| Visibility | Public (goes to review queue first, typically 24-48h) |
| Summary | Litematica Easy Place without holding the use key. Minecraft 26.x continuation of Hold-Easy-Place, with optional support for server custom items. |
| Categories | Utility |
| Loaders | Fabric |
| Environments | Client-side: required; Server-side: unsupported |
| License | MIT |
| Source | https://github.com/b8b7lives/bholdeasyplace |
| Issues | https://github.com/b8b7lives/bholdeasyplace/issues |
| Icon | `icon/bholdeasyplace-icon-512.png` (512x512, hold ring, under Modrinth's 256KB cap) |
| Gallery | `gallery/banner.png` as the featured image, `gallery/banner-social-1280x640.png` if a smaller upload is needed |

## Description (paste as the body)

---

Build from schematics without wearing out your right mouse button.
**bHold easyPlace** keeps [Litematica](https://modrinth.com/mod/litematica)'s
Easy Place mode firing every client tick, with no use key held. Look at
the schematic and the blocks go in.

This is the Minecraft 26.x continuation of
[Hold-Easy-Place](https://modrinth.com/mod/hold-easy-place) by shane0411
(MIT). The last upstream release supports 1.21.x. This version was
rewritten against current Litematica and malilib internals.

## How it works

1. Turn on Easy Place in Litematica as usual (`easyPlaceMode`).
2. Enable **Hold Easy Place Mode** in Litematica config > Generic, or
   bind **Toggle Hold Easy Place** in the Hotkeys tab.
3. Aim at the schematic. Blocks place with no key held.

Both settings save to `litematica.json` like any other Litematica
option.

## What to expect

- This is Litematica's own Easy Place, minus the held key. It is not
  a printer. Placement uses vanilla reach, needs the target block under
  your crosshair, and makes one placement attempt per tick. Nothing gets
  placed behind you or through walls.
- Steps aside whenever Litematica should own the tick: in REBUILD tool
  mode, while a GUI is open, and while you physically hold the use key.
- Works with both the legacy Easy Place path and
  `easyPlacePostRewrite`.
- Hold-mode placement failures stay out of chat. Your own clicks keep
  stock Litematica feedback.

## Server custom items (optional)

Some servers hand out custom items such as infinite blocks. Stock Easy
Place refuses to place from them because the item data does not match
the schematic exactly. bHold easyPlace adds four toggles for that,
all in Litematica config > Generic:

- **Lenient Item Match**: a custom item counts as a match when the
  schematic wants the plain block. Items that need exact data, like
  filled shulkers, stay exact.
- **Skip Pick Block When Held**: leaves your hotbar alone when what you
  already hold satisfies the target.
- **Protect Custom Items**: pick block never moves another item into a
  slot holding a custom item.
- **Custom Item Loss Watchdog**: warns with a timestamp when a custom
  item leaves your inventory and is not given back within two seconds,
  so a lost infinite block becomes a support ticket with evidence.

Running EasyPlaceFix too? On multiplayer it handles placement with its
own settings, so enable its `nbtIgnore` and `loosenMode` options for
custom items there as well.

## Requirements

Minecraft 26.2, Fabric, plus
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

## Version upload defaults

| Field | Value |
|---|---|
| Channel | Release |
| Loaders | Fabric |
| Game versions | 26.2 |
| Dependencies | Litematica (required), malilib (required), Fabric API (required), Hold Easy Place (incompatible) |
| Changelog | The matching section from CHANGELOG.md |

Publishable via the Modrinth API (project id `8srTTqwk`); dependency
project ids are on any existing version.
