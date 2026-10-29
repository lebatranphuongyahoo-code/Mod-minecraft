package com.cataclysm.disasters.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class DisasterConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue ENABLED;
    public static final ForgeConfigSpec.BooleanValue SAFE_MODE;
    public static final ForgeConfigSpec.BooleanValue BLOCK_DESTRUCTION;
    public static final ForgeConfigSpec.BooleanValue ALLOW_MULTIPLE;
    public static final ForgeConfigSpec.IntValue FREQUENCY_MINUTES;
    public static final ForgeConfigSpec.IntValue COOLDOWN_MINUTES;
    public static final ForgeConfigSpec.IntValue WARNING_SECONDS;
    public static final ForgeConfigSpec.IntValue MAX_INTENSITY;
    public static final ForgeConfigSpec.DoubleValue ENTITY_DAMAGE_MULTIPLIER;
    public static final ForgeConfigSpec.ConfigValue<String> QUALITY;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();
        b.push("disaster_mode");
        ENABLED = b.define("enabled", true);
        SAFE_MODE = b.define("safe_mode", false);
        BLOCK_DESTRUCTION = b.define("block_destruction", true);
        ALLOW_MULTIPLE = b.define("allow_multiple_major_disasters", false);
        FREQUENCY_MINUTES = b.defineInRange("frequency_minutes", 30, 1, 1440);
        COOLDOWN_MINUTES = b.defineInRange("cooldown_minutes", 10, 0, 1440);
        WARNING_SECONDS = b.defineInRange("warning_seconds", 30, 0, 600);
        MAX_INTENSITY = b.defineInRange("max_intensity", 5, 1, 5);
        ENTITY_DAMAGE_MULTIPLIER = b.defineInRange("entity_damage_multiplier", 1.0, 0.0, 10.0);
        QUALITY = b.define("visual_quality", "High");
        b.pop();
        SPEC = b.build();
    }
    private DisasterConfig() { }
}
