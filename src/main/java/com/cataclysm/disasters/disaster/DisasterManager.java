package com.cataclysm.disasters.disaster;

import com.cataclysm.disasters.config.DisasterConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public final class DisasterManager {
    private DisasterInstance active;
    private int warningTicks;
    private int cooldownTicks;
    private boolean automatic = true;
    private int automaticTicks;
    private final AtomicInteger blockBudget = new AtomicInteger();

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !DisasterConfig.ENABLED.get()) return;
        if (cooldownTicks > 0) cooldownTicks--;
        if (active == null && automatic && cooldownTicks == 0) automaticTicks++;
        if (active == null && automatic && automaticTicks >= DisasterConfig.FREQUENCY_MINUTES.get() * 1200) {
            automaticTicks = 0;
            ServerLevel level = activeLevelFromPlayers();
            if (level != null && !level.players().isEmpty()) {
                DisasterType[] types = DisasterType.values();
                start(level, types[level.random.nextInt(types.length)], 1 + level.random.nextInt(DisasterConfig.MAX_INTENSITY.get()), 120 + level.random.nextInt(240));
            }
        }
        if (active != null) {
            ServerLevel level = activeLevel();
            if (level != null && warningTicks > 0) { warningTicks--; if (warningTicks % 40 == 0) broadcast(level, "Warning: " + active.type().display() + " in " + (warningTicks / 20) + "s"); }
            else if (level != null && active.tick(level, DisasterConfig.BLOCK_DESTRUCTION.get() && !DisasterConfig.SAFE_MODE.get(), DisasterConfig.ENTITY_DAMAGE_MULTIPLIER.get())) { active = null; cooldownTicks = DisasterConfig.COOLDOWN_MINUTES.get() * 1200; broadcast(level, "The disaster has ended."); }
        }
    }
    private ServerLevel activeLevel() { return active == null ? null : activeLevelFromPlayers(); }
    private ServerLevel activeLevelFromPlayers() { return net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer() == null ? null : net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer().overworld(); }
    public boolean start(ServerLevel level, DisasterType type, int intensity, int duration) {
        if (active != null && !DisasterConfig.ALLOW_MULTIPLE.get()) return false;
        if (cooldownTicks > 0) return false;
        intensity = Math.max(1, Math.min(DisasterConfig.MAX_INTENSITY.get(), intensity));
        active = new DisasterInstance(type, intensity, Math.max(20, duration * 20), level.getSharedSpawnPos().getCenter());
        warningTicks = DisasterConfig.WARNING_SECONDS.get() * 20;
        broadcast(level, "Disaster warning: " + type.display() + " (intensity " + intensity + ")");
        return true;
    }
    public void stop(ServerLevel level) { active = null; warningTicks = 0; automaticTicks = 0; broadcast(level, "Disaster stopped."); }
    public Optional<DisasterInstance> active() { return Optional.ofNullable(active); }
    public void setAutomatic(boolean enabled) { automatic = enabled; }
    public boolean automatic() { return automatic; }
    private void broadcast(ServerLevel level, String message) { for (ServerPlayer player : level.players()) player.displayClientMessage(Component.literal(message), false); }
}
