package com.cataclysm.disasters;

import com.cataclysm.disasters.command.DisasterCommands;
import com.cataclysm.disasters.config.DisasterConfig;
import com.cataclysm.disasters.disaster.DisasterManager;
import com.cataclysm.disasters.registry.ModBlocks;
import com.cataclysm.disasters.registry.ModItems;
import com.cataclysm.disasters.registry.ModSounds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CataclysmDisasters.MOD_ID)
public final class CataclysmDisasters {
    public static final String MOD_ID = "cataclysm_disasters";
    public static final DisasterManager DISASTERS = new DisasterManager();

    public CataclysmDisasters() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        ModSounds.SOUNDS.register(bus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DisasterConfig.SPEC);
        MinecraftForge.EVENT_BUS.register(DISASTERS);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        DisasterCommands.register(event.getDispatcher());
    }
}
