# OptiHud

A complete client-side **Fabric mod** for Minecraft **26.2** — a Lunar/Badlion-style HUD mod with a menu, tracking modules, and a drag-and-drop HUD editor.

## Features

- **Menu** — press RIGHT SHIFT to open the mod menu
- **Category tabs** — HUD, COMBAT, MOVEMENT, VISUAL, UTILITY
- **Search bar** — filter modules by name
- **Per-module toggle + settings** — ON/OFF switch, settings button, "Reset to defaults"
- **HUD EDITOR** — drag-and-drop HUD elements, snap-to-edge/center guides, arrow-key nudge, ESC to save & exit
- **12 HUD modules** — FPS, CPS, KEYSTROKES, PING, COORDINATES, DIRECTION, CLOCK, MEMORY, SPEED, SERVER IP, BIOME, PLAYER COUNT
- **3 COMBAT modules** — ARMOR STATUS, POTION STATUS, ITEM COUNTER
- **1 MOVEMENT module** — TOGGLE SPRINT with on-screen status
- **1 VISUAL module** — ZOOM with hold keybind + scroll wheel
- **1 UTILITY module** — CUSTOM TEXT / WATERMARK with `%fps%`, `%ping%`, `%x%`, `%y%`, `%z%` placeholders

## Versions

| Dependency | Version |
|---|---|
| Minecraft | 26.2 |
| Fabric Loader | 0.20.1 |
| Fabric API | 0.161.0+26.2 |
| Fabric Loom | 1.19-SNAPSHOT |
| Java | 25 |

## Build

```bash
./gradlew build
```

The jar is produced at `build/libs/OptiHud-1.0.0.jar`.

## Install

1. Install **Fabric Loader** for Minecraft 26.2
2. Install **Fabric API 0.161.0+26.2**
3. Put the jar in `.minecraft/mods/`
4. Launch Minecraft

## Common errors

*(see full guide in the source code comments)*

- **Mapping errors** — 26.1+ ships unobfuscated; remove any `mappings` block from `build.gradle`
- **Java version** — JDK 25 required
- **Fabric repo missing** — add `https://maven.fabricmc.net/` to `repositories` in `build.gradle`
