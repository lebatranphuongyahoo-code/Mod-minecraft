package com.cataclysm.disasters.disaster;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;

public final class DisasterInstance {
    private final DisasterType type;
    private final int intensity;
    private final int duration;
    private final Vec3 origin;
    private int age;
    private final Random random = new Random();

    public DisasterInstance(DisasterType type, int intensity, int duration, Vec3 origin) {
        this.type = type; this.intensity = intensity; this.duration = duration; this.origin = origin;
    }
    public DisasterType type() { return type; }
    public int intensity() { return intensity; }
    public int remaining() { return Math.max(0, duration - age); }
    public Vec3 origin() { return origin; }
    public boolean tick(ServerLevel level, boolean destroyBlocks, double damageMultiplier) {
        age++;
        int radius = 8 + intensity * 5;
        AABB area = new AABB(origin, origin).inflate(radius);
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);
        for (LivingEntity entity : entities) applyEntityEffect(entity, damageMultiplier);
        switch (type) {
            case EARTHQUAKE -> earthquake(level, destroyBlocks, radius);
            case TORNADO -> tornado(level, destroyBlocks, radius);
            case VOLCANO -> volcano(level, destroyBlocks, radius);
            case METEOR -> meteor(level, destroyBlocks, radius);
            case TSUNAMI -> flood(level, radius);
            case WILDFIRE -> wildfire(level, destroyBlocks, radius);
            case BLIZZARD -> blizzard(level, radius);
            case THUNDERSTORM -> thunderstorm(level, area);
            case SANDSTORM -> sandstorm(level, radius);
        }
        return age >= duration;
    }
    private void applyEntityEffect(LivingEntity entity, double multiplier) {
        float damage = (float) (intensity * 0.35 * multiplier);
        if (type == DisasterType.BLIZZARD && entity.tickCount % 40 == 0) entity.hurt(entity.damageSources().freeze(), damage);
        if (type == DisasterType.SANDSTORM && entity.tickCount % 30 == 0) entity.hurt(entity.damageSources().generic(), damage);
        if (type == DisasterType.WILDFIRE && entity.tickCount % 20 == 0) entity.hurt(entity.damageSources().inFire(), damage);
        if (type == DisasterType.TORNADO && entity instanceof ServerPlayer player && entity.tickCount % 10 == 0) player.push((random.nextDouble() - .5) * intensity, .08 * intensity, (random.nextDouble() - .5) * intensity);
    }
    private void earthquake(ServerLevel level, boolean destroy, int radius) { if (destroy && age % 8 == 0) breakRandomBlocks(level, radius, Blocks.STONE); }
    private void tornado(ServerLevel level, boolean destroy, int radius) { if (age % 5 == 0) liftEntities(level, radius); if (destroy && age % 12 == 0) breakRandomBlocks(level, radius, Blocks.DIRT); }
    private void volcano(ServerLevel level, boolean destroy, int radius) { if (age % 15 == 0) placeHazard(level, Blocks.LAVA, radius / 2); }
    private void meteor(ServerLevel level, boolean destroy, int radius) { if (age % Math.max(10, 55 - intensity * 8) == 0) { breakRandomBlocks(level, radius / 2, Blocks.FIRE); level.explode(null, origin.x + random.nextInt(radius * 2) - radius, origin.y, origin.z + random.nextInt(radius * 2) - radius, intensity, ServerLevel.ExplosionInteraction.BLOCK); } }
    private void flood(ServerLevel level, int radius) { if (age % 10 == 0) placeHazard(level, Blocks.WATER, radius); }
    private void wildfire(ServerLevel level, boolean destroy, int radius) { if (destroy && age % 10 == 0) placeHazard(level, Blocks.FIRE, radius); }
    private void blizzard(ServerLevel level, int radius) { if (age % 20 == 0) placeHazard(level, Blocks.SNOW, radius); }
    private void thunderstorm(ServerLevel level, AABB area) {
        if (random.nextInt(Math.max(2, 18 - intensity * 2)) == 0) {
            var bolt = net.minecraft.world.entity.EntityType.LIGHTNING_BOLT.create(level);
            if (bolt != null) {
                bolt.moveTo(origin.x + random.nextInt(12) - 6, origin.y, origin.z + random.nextInt(12) - 6);
                level.addFreshEntity(bolt);
            }
        }
    }
    private void sandstorm(ServerLevel level, int radius) { }
    private void liftEntities(ServerLevel level, int radius) { for (Entity entity : level.getEntities(null, new AABB(origin, origin).inflate(radius))) if (!(entity instanceof ServerPlayer)) entity.push(0, .35 * intensity, 0); }
    private void placeHazard(ServerLevel level, net.minecraft.world.level.block.Block block, int radius) { for (int i = 0; i < intensity * 2; i++) { int x = (int) origin.x + random.nextInt(Math.max(1, radius * 2)) - radius; int z = (int) origin.z + random.nextInt(Math.max(1, radius * 2)) - radius; level.setBlock(new net.minecraft.core.BlockPos(x, level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, x, z), z), block.defaultBlockState(), 3); } }
    private void breakRandomBlocks(ServerLevel level, int radius, net.minecraft.world.level.block.Block replacement) { if (level.getRandom().nextFloat() > .45f) return; int x = (int) origin.x + random.nextInt(Math.max(1, radius * 2)) - radius; int z = (int) origin.z + random.nextInt(Math.max(1, radius * 2)) - radius; var pos = new net.minecraft.core.BlockPos(x, level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE, x, z) - 1, z); if (level.getBlockState(pos).getDestroySpeed(level, pos) >= 0) level.setBlock(pos, replacement.defaultBlockState(), 3); }
}
