package com.optihud.module;

import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.ARGB;

/**
 * HUD module: displays the player's XYZ coordinates.
 *
 * <p>Options: show decimals, show facing direction and biome.</p>
 */
public class CoordinatesModule extends Module {
    private double x = 0;
    private double y = 0;
    private double z = 0;
    private boolean showDecimals = false;
    private boolean showFacing = false;
    private boolean showBiome = false;

    public CoordinatesModule() {
        super("COORDINATES", "HUD", "Shows the player's XYZ position.");
        bool("decimals", false, "Show decimal places");
        bool("facing", false, "Show the facing direction");
        bool("biome", false, "Show the current biome");
    }

    public boolean isShowDecimals() { return showDecimals; }
    public boolean isShowFacing() { return showFacing; }
    public boolean isShowBiome() { return showBiome; }

    public void setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    protected void onRender(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        if (bool("hide").getValue()) return;

        String s = "XYZ : " + (int) x + " " + (int) y + " " + (int) z;
        MoonRenderHelper.drawString(graphics, s, (int) x, (int) y, ARGB.WHITE);
    }
}
