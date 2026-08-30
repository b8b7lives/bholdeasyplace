# CurseForge listing source

Canonical source for the CurseForge project. Project creation is web
only (authors.curseforge.com); file uploads go through the Upload API
once the project is approved. Edit here first, then apply.

## Project settings

| Field | Value |
|---|---|
| Name | bHold easyPlace |
| URL slug | `bhold-easy-place` (same as Modrinth) |
| Class | Mods |
| Main category | Utility & QoL |
| Extra categories | Map and Information (optional) |
| Summary | Litematica Easy Place without holding the use key. Minecraft 26.x continuation of Hold-Easy-Place, with optional support for server custom items. |
| License | MIT |
| Logo | `icon/bholdeasyplace-icon-512.png` (512x512 PNG) |
| Source | https://github.com/b8b7lives/bholdeasyplace |
| Issues | https://github.com/b8b7lives/bholdeasyplace/issues |
| Author Rewards | leave opted out |
| Project Distribution Toggle | operator decision (License tab; controls third-party launcher access) |
| Description mode | Markdown |

## Description (paste as the body, Markdown mode)

Links point at CurseForge pages. Mods without a CurseForge listing are
named without a link.

---

Build from schematics without wearing out your right mouse button.
**bHold easyPlace** keeps [Litematica](https://www.curseforge.com/minecraft/mc-mods/litematica)'s
Easy Place mode firing every client tick, with no use key held. Look at
the schematic and the blocks go in.

This is the Minecraft 26.x continuation of
[Hold-Easy-Place](https://www.curseforge.com/minecraft/mc-mods/hold-easy-place)
by shane0411, published under the MIT license with the original
copyright retained. The last upstream release supports 1.21.x. This
version is a rewrite against current Litematica and malilib internals,
adds the server custom item toggles described below, and drops
upstream's fail-tip options, `cacheTime`, and the schematics-folder
button.

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

Running the EasyPlaceFix mod too? On multiplayer it handles placement
with its own settings, so enable its `nbtIgnore` and `loosenMode`
options for custom items there as well.

## Requirements

Minecraft 26.2, Fabric, plus
[Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api),
[MaLiLib](https://www.curseforge.com/minecraft/mc-mods/malilib), and
[Litematica](https://www.curseforge.com/minecraft/mc-mods/litematica).
Client-side only.

If you still have the original Hold-Easy-Place jar installed, remove
it. This mod declares a conflict with it, because both mods would drive
Easy Place on the same tick.

## Credits

- [shane0411](https://github.com/shane04111) wrote the original
  Hold-Easy-Place.
- masa wrote Litematica and MaLiLib.

Source on [GitHub](https://github.com/b8b7lives/bholdeasyplace), MIT.

---

## File upload (after approval)

Token: generated at Author Tools > API Tokens, stored at
`~/bhep-curseforge-token` (0600). Header `X-Api-Token`.

```
POST https://upload.curseforge.com/api/projects/{projectId}/upload-file
multipart: metadata=<json>, file=<jar>
```

Metadata:

```json
{
  "changelog": "<matching CHANGELOG.md section>",
  "changelogType": "markdown",
  "displayName": "bHold easyPlace 0.5.0 for 26.2",
  "gameVersions": [<ids from GET /api/game/versions for 26.2, Fabric, Client>],
  "releaseType": "release",
  "relations": {
    "projects": [
      {"slug": "fabric-api", "type": "requiredDependency"},
      {"slug": "malilib", "type": "requiredDependency"},
      {"slug": "litematica", "type": "requiredDependency"},
      {"slug": "hold-easy-place", "type": "incompatible"}
    ]
  }
}
```

Game version ids: `GET https://upload.curseforge.com/api/game/versions`
with the token; pick the entries named `26.2`, `Fabric`, and `Client`.
