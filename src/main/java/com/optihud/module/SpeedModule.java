package com.optihud.module;

import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.ARGB;

/**
 * HUD module: displays the player's speed in blocks per second (BPS).
 *
 * <p>Calculates the distance travelled over the last second using the
 * player's velocity.</p>
 */
public class SpeedModule extends Module {
    private double prevX = 0;
    private double prevY = 0;
    private double prevZ = 0;
    private double speedBPS = 0;

    public SpeedModule() {
        super("SPEED (BPS)", "HUD", "Shows the player's speed in blocks per second.");
        bool("hide", false, "Hide the module");
    }

    public void tick() {
        PlayerEntity p = Minecraft.getInstance().player;
        if (p == null) return;
        double dx = p.getX() - prevX;
        double dy = p.getY() - prevY;
        double dz = p.getZ() - prevZ;
        prevX = p.getX();
        prevY = p.getY();
        prevZ = p.getZ();
        speedBPS = Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    protected void onRender(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        if (bool("hide").getValue()) return;
        MoonRenderHelper.drawString(graphics, "SPEED : " + (int) speedBPS + " BPS", (int) x, (int) y, ARGB.WHITE);
    }
}
