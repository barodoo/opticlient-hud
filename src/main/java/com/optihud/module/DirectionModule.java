package com.optihud.module;

import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.ARGB;

/**
 * HUD module: displays the player's facing direction (N, NE, E, ...) and yaw.
 */
public class DirectionModule extends Module {
    private float yaw = 0;
    private String direction = "N";

    public DirectionModule() {
        super("DIRECTION", "HUD", "Shows facing direction and yaw angle.");
        bool("hide", false, "Hide the module");
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        this.direction = convertYaw(yaw);
    }

    private String convertYaw(float yaw) {
        int val = (int) (yaw / 45) % 8;
        if (val < 0) val += 8;
        String[] dirs = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        return dirs[val];
    }

    public String getDirection() { return direction; }
    public float getYaw() { return yaw; }

    @Override
    protected void onRender(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        if (bool("hide").getValue()) return;
        MoonRenderHelper.drawString(graphics, "DIR : " + direction + " (" + (int) yaw + "°)", (int) x, (int) y, ARGB.WHITE);
    }
}
