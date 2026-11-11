package com.optihud.module;

import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.ARGB;

/**
 * HUD module: displays the current biome name.
 */
public class BiomeModule extends Module {
    private String biomeName = "Unknown";

    public BiomeModule() {
        super("BIOME", "HUD", "Shows the current biome name.");
        bool("hide", false, "Hide the module");
    }

    public void setBiome(String biome) {
        this.biomeName = biome;
    }

    @Override
    protected void onRender(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        if (bool("hide").getValue()) return;
        MoonRenderHelper.drawString(graphics, "BIOME : " + biomeName, (int) x, (int) y, ARGB.WHITE);
    }
}
