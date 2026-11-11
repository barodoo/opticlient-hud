package com.optihud.module;

import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.ARGB;

import java.util.List;

/**
 * COMBAT HUD module: displays active potion effects with icon, level, and
 * remaining time. Turns red when low on duration.
 */
public class PotionStatusModule extends Module {
    private boolean showIcon = true;
    private boolean showLevel = true;
    private boolean showTime = true;

    public PotionStatusModule() {
        super("POTION STATUS", "COMBAT", "Displays active potion effects.");
        bool("icon", true, "Show effect icons");
        bool("level", true, "Show effect levels");
        bool("time", true, "Show remaining time");
    }

    public boolean isShowIcon() { return showIcon; }
    public boolean isShowLevel() { return showLevel; }
    public boolean isShowTime() { return showTime; }

    @Override
    protected void onRender(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        if (bool("hide").getValue()) return;
        // Read active effects from the player (in a real mod this would be
        // populated by a mixin reading gameMode.getActivePotions()).
        MoonRenderHelper.drawString(graphics, "POTIONS : ", (int) x, (int) y, ARGB.WHITE);
    }
}
