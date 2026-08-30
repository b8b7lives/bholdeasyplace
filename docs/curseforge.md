# CurseForge listing source

Canonical source for the CurseForge project. Project creation is web
only (authors.curseforge.com); file uploads go through the Upload API
once the project is approved. Edit here first, then apply.

## Project settings

| Field | Value |
|---|---|
| Name | bHold easyPlace |
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

## Description (paste as the body)

Same body as `docs/modrinth.md`, with two changes CurseForge asks for
on forks: the upstream link points at the CurseForge page for
Hold-Easy-Place, and the "what changed" statement is explicit.

Replace the second paragraph of the Modrinth body with:

---

This is the Minecraft 26.x continuation of
[Hold-Easy-Place](https://www.curseforge.com/minecraft/mc-mods/hold-easy-place)
by shane0411, published under the MIT license with the original
copyright retained. The last upstream release supports 1.21.x. This
version is a rewrite against current Litematica and malilib internals,
adds the server custom item toggles described below, and drops
upstream's fail-tip options, `cacheTime`, and the schematics-folder
button.

---

Swap Modrinth mod links for their CurseForge pages where they exist
(fabric-api, malilib, litematica all have CurseForge listings).

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
