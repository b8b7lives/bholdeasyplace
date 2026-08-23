![bHold easyPlace](gallery/banner.webp)

# bHold easyPlace

A [Litematica](https://modrinth.com/mod/litematica) addon for Fabric that
keeps Easy Place running every client tick, so you don't have to hold the
use key while building from a schematic. It also makes Easy Place
work with server custom items such as infinite blocks.

This is a continuation of
[Hold-Easy-Place](https://github.com/shane04111/Hold-Easy-Place) by
[shane0411](https://github.com/shane04111) (MIT). The last upstream
release (v0.1.8) supports Minecraft 1.21.x. bHold easyPlace brings the
same idea to Minecraft 26.x, rewritten against current Litematica and
malilib internals.

## What it does

- Adds a **Hold Easy Place Mode** toggle to Litematica's Generic config
  tab and a **Toggle Hold Easy Place** hotkey to the Hotkeys tab. Both
  persist in `litematica.json` via Litematica's own config handler.
- While enabled (and Litematica's `easyPlaceMode` is on), Easy Place
  fires every client tick as if you were holding the use key.
- Stays inactive whenever Litematica should own the tick: in REBUILD
  tool mode, while a GUI is open, and while you physically hold the use
  key.
- Supports both Litematica code paths (legacy and
  `easyPlacePostRewrite`).
- Hold-mode placement failures are silent. Manual clicks keep stock
  Litematica messaging, so you still get feedback when you click.
- **Custom item servers.** Easy Place accepts server custom items such
  as infinite blocks when the schematic wants a plain block. Items that
  need exact data, like filled shulkers, stay exact.
- **Inventory safety.** Easy Place leaves your hotbar alone when what
  you hold already matches the target, and pick block never moves
  another item into a slot holding a server custom item.
- **Loss watchdog.** Warns with a timestamp when a server custom item
  leaves your inventory and is not given back within two seconds, so a
  lost infinite block becomes a support ticket with evidence.
- Every feature has its own toggle in Litematica config > Generic. The
  toggles are named Lenient Item Match, Skip Pick Block When Held,
  Protect Custom Items, and Custom Item Loss Watchdog.

Running EasyPlaceFix as well? On multiplayer it handles placement with
its own settings, so also enable its nbtIgnore and loosenMode options
for custom items there.

## Requirements

- Minecraft 26.2, Fabric Loader >= 0.19.0
- [Fabric API](https://modrinth.com/mod/fabric-api)
- [malilib](https://modrinth.com/mod/malilib)
- [Litematica](https://modrinth.com/mod/litematica)

Client-side only. Remove the original Hold-Easy-Place jar if you still
have it. This mod declares a conflict with it (`breaks`), because both
mods would drive Easy Place on the same tick.

## Usage

1. Enable Easy Place mode in Litematica as usual (`easyPlaceMode`).
2. Enable **Hold Easy Place Mode** in Litematica config > Generic, or
   bind **Toggle Hold Easy Place** in the Hotkeys tab.
3. Blocks place while you look at the schematic, with no key held.

## Differences from the original

Upstream's fail-tip config options, `cacheTime`, and the
schematics-folder button were intentionally dropped. The core behavior
of placing blocks without holding the key is unchanged.

## Building from source

```
./gradlew build
```

26.x mods are unobfuscated, so the build compiles directly against local
jars in `libs/` with no remapping. Drop these two files in `libs/`
first:

- `litematica-fabric-26.2-0.28.4.jar` from [Litematica on Modrinth](https://modrinth.com/mod/litematica)
- `malilib-fabric-26.2-0.29.3.jar` from [malilib on Modrinth](https://modrinth.com/mod/malilib)

When Litematica or malilib bump, update `libs/` and the two
`compileOnly` lines in `build.gradle`.

## Credits and license

- [shane0411](https://github.com/shane04111) wrote the original
  [Hold-Easy-Place](https://github.com/shane04111/Hold-Easy-Place),
  which this mod continues.
- [masa](https://modrinth.com/user/masa) wrote Litematica and malilib.

MIT, see [LICENSE](LICENSE). Copyright shane0411 (original) and b8b7
(26.x continuation).
