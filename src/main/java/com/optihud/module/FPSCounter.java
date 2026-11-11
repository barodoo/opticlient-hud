package com.optihud.module;

import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.ARGB;

/**
 * HUD module: displays the current frames-per-second.
 *
 * <p>Format: <tt>FPS : 144</tt></p>
 */
public class FPSCounter extends Module {
    private int frameCount = 0;
    private int frameTime = 0;
    private int fps = 0;

    public FPSCounter() {
        super("FPS", "HUD", "Shows the current frames per second.");
        bool("hide", false, "Hide the module");
    }

    @Override
    public void onTick() {
        frameCount++;
        frameTime++;

        if (frameTime >= 20) {
            fps = frameCount * 5;
            frameCount = 0;
            frameTime = 0;
        }
    }

    @Override
    protected void onRender(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        if (bool("hide").getValue()) return;
        MoonRenderHelper.drawString(graphics, " FPS : " + fps, (int) x, (int) y, ARGB.WHITE);
    }
}
