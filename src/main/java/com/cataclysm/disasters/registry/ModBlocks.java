package com.cataclysm.disasters.registry;

import com.cataclysm.disasters.CataclysmDisasters;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CataclysmDisasters.MOD_ID);
    public static final RegistryObject<Block> EMERGENCY_SHELTER = BLOCKS.register("emergency_shelter", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(50, 1200).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> STORM_CELLAR = BLOCKS.register("storm_cellar", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(8, 40).requiresCorrectToolForDrops()));
    private ModBlocks() { }
}
