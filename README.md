# Cataclysm: Natural Disasters

Forge 1.20.1 mod (`cataclysm_disasters`) built for Java 17 and Forge 47.2.0. License: MIT for the original source and metadata in this repository. No third-party assets are bundled.

## Features

- Nine server-authoritative disaster simulations: earthquake, tornado, volcanic eruption, meteor shower, flood, wildfire, blizzard, lightning storm, and sandstorm.
- Warning phase, configurable intensity 1-5, automatic frequency, cooldown, safe mode, block-destruction toggle, and damage multiplier.
- Commands with permission level 2: `/disaster start <type> [intensity] [duration]`, `/disaster stop`, `/disaster list`, `/disaster schedule <on|off>`.
- Disaster Radar, Survival Kit, Emergency Shelter, and Storm Cellar content with recipes.
- Client HUD showing the active disaster, intensity, and remaining time. Models intentionally reuse vanilla textures as safe placeholders, so no purple/black missing-texture blocks appear.

## Build and install

The project uses ForgeGradle 6 and Gradle 8.8. Use a JDK 17 installation:

```text
gradle build
```

The compiled file is `build/libs/cataclysm_disasters-1.0.0.jar` (the reobfuscated jar). Install Forge 47.2.x for Minecraft 1.20.1, copy the jar into the instance `mods` directory, and start the game. The server generates `config/cataclysm_disasters-common.toml` on first launch.

## Configuration

`enabled`, `frequency_minutes`, `cooldown_minutes`, `warning_seconds`, `max_intensity`, `block_destruction`, `entity_damage_multiplier`, `visual_quality`, `safe_mode`, and `allow_multiple_major_disasters` are available in the generated TOML file. `safe_mode=true` prevents disaster block changes while retaining warnings and entity effects.

## Minecraft PE / điện thoại

Forge `.jar` mods do **not** run in the native Minecraft Bedrock/PE application. To use this exact mod on Android, install **PojavLauncher**, a Java Edition launcher, create a Minecraft 1.20.1 Forge 47.2.x instance, install Java 17, then copy the jar into the instance `mods` folder. Performance is device-dependent; use `visual_quality="Low"` and `safe_mode=true` on weaker phones.

The normal Minecraft PE/Bedrock app requires a separately authored `.mcaddon` behavior/resource pack. This repository does not claim Bedrock compatibility and the Forge jar cannot be renamed into an addon.

## Native Bedrock / PE add-on

A separate native Bedrock implementation is included under `bedrock-addon/`. The installable package is:

```text
build/bedrock/Cataclysm_Natural_Disasters.mcaddon
```

It contains a Behavior Pack and Resource Pack using Bedrock Script API. On Android, tap the `.mcaddon` file and open it with Minecraft, activate both imported packs in world settings, and enable Beta APIs/Script API if the installed version exposes that option. Bedrock commands use chat prefixes such as `!disaster start tornado 3 60`; see `bedrock-addon/README.md` for the complete list and limitations.

## Known limitations and future art

- The gameplay effects are implemented with vanilla blocks/entities and lightweight server ticks; custom tornado mesh, post-processing, PBR textures, and spatial OGG layers require a later client art pass.
- `sounds.json` declares warning and wind events, but no copyrighted audio is included. Add original `.ogg` files at `assets/cataclysm_disasters/sounds/` before enabling sound playback.
- Suggested future work: dedicated network packets for client particle density, custom particle providers, armor sets with temperature protection, persistent survival score advancements, and data generation for tags/loot.
