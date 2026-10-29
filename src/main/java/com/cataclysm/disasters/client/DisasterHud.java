package com.cataclysm.disasters.client;

import com.cataclysm.disasters.CataclysmDisasters;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CataclysmDisasters.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class DisasterHud {
    @SubscribeEvent
    public static void render(RenderGuiOverlayEvent.Post event) {
        var active = CataclysmDisasters.DISASTERS.active();
        if (active.isEmpty()) return;
        var disaster = active.get();
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        GuiGraphics graphics = event.getGuiGraphics();
        String title = disaster.type().display() + " | Intensity " + disaster.intensity();
        String timer = "Remaining: " + (disaster.remaining() / 20) + "s";
        int width = mc.getWindow().getGuiScaledWidth();
        graphics.fill(width / 2 - 105, 8, width / 2 + 105, 34, 0xAA150B0B);
        graphics.drawCenteredString(mc.font, title, width / 2, 12, 0xFFFFD0A0);
        graphics.drawCenteredString(mc.font, timer, width / 2, 23, 0xFFFFFFFF);
    }
    private DisasterHud() { }
}
