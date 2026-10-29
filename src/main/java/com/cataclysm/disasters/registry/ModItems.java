package com.cataclysm.disasters.registry;

import com.cataclysm.disasters.CataclysmDisasters;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CataclysmDisasters.MOD_ID);
    public static final RegistryObject<Item> DISASTER_RADAR = ITEMS.register("disaster_radar", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SURVIVAL_KIT = ITEMS.register("survival_kit", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> EMERGENCY_SHELTER = ITEMS.register("emergency_shelter", () -> new BlockItem(ModBlocks.EMERGENCY_SHELTER.get(), new Item.Properties()));
    public static final RegistryObject<Item> STORM_CELLAR = ITEMS.register("storm_cellar", () -> new BlockItem(ModBlocks.STORM_CELLAR.get(), new Item.Properties()));
    private ModItems() { }
}
