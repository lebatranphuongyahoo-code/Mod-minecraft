# Cataclysm: Natural Disasters Bedrock Edition

This is a native Minecraft Bedrock add-on. It is not the Forge JAR converted by renaming; the behavior pack is rewritten in Bedrock Script API JavaScript and the resource pack contains Bedrock resources.

## Android installation

1. Extract or download `Cataclysm_Natural_Disasters.mcaddon` onto the phone.
2. Tap the `.mcaddon` file in a file manager and choose Minecraft.
3. Wait for Minecraft to import both `Cataclysm Natural Disasters BP` and `Cataclysm Natural Disasters RP`.
4. Create or edit a world.
5. In `Behavior Packs`, activate the behavior pack.
6. In `Resource Packs`, activate the resource pack.
7. In world settings, enable `Beta APIs`/`Script API` if the installed Bedrock version displays that toggle.
8. Start the world. The add-on prints a welcome message in chat.

The add-on targets Bedrock 1.20.80+ and uses `@minecraft/server` 1.12.0. Newer Bedrock versions may require changing the dependency version in the behavior manifest if Microsoft changes module compatibility.

## Commands

Bedrock chat commands use `!disaster` so they work without command registration:

```text
!disaster list
!disaster start tornado 3 60
!disaster stop
!disaster schedule on
!disaster schedule off
!disaster safe on
!disaster safe off
```

The final argument is duration in seconds. The normal Bedrock `/give`, `/tp`, and other built-in commands remain unchanged.

## Scope and limitations

The script implements warning messages, automatic scheduling, intensity, cooldown, entity damage/status effects, particles, fire, lava, water, lightning, explosions and lightweight block changes. Bedrock does not expose the same Forge renderer APIs, so custom shader post-processing, Java entity renderers, and Forge config TOML are not portable. Runtime settings are intentionally kept in the script constants for reliable phone/server behavior.
