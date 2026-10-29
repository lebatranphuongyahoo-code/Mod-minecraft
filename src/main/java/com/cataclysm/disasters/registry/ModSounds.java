package com.cataclysm.disasters.registry;

import com.cataclysm.disasters.CataclysmDisasters;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CataclysmDisasters.MOD_ID);
    public static final RegistryObject<SoundEvent> DISASTER_WARNING = sound("disaster_warning");
    public static final RegistryObject<SoundEvent> DISASTER_WIND = sound("disaster_wind");
    private static RegistryObject<SoundEvent> sound(String name) { return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CataclysmDisasters.MOD_ID, name))); }
    private ModSounds() { }
}
