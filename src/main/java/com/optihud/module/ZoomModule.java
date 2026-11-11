package com.optihud.module;

import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.ARGB;

/**
 * VISUAL module: zoom the screen with a hold keybind (default C) + scroll
 * wheel to change zoom level.
 *
 * <p>Options: smooth zoom animation, smooth camera.</p>
 */
public class ZoomModule extends ToggleModule {
    private float zoomLevel = 0;
    private boolean smoothZoom = false;
    private boolean smoothCamera = false;

    public ZoomModule() {
        super("ZOOM", "VISUAL", "Zoom the screen with a keybind.");
        bool("smooth zoom", false, "Enable smooth zoom animation");
        bool("smooth camera", false, "Enable smooth camera following");
        bool("hide", false, "Hide the module");
    }

    public boolean isSmoothZoom() { return smoothZoom; }
    public boolean isSmoothCamera() { return smoothCamera; }

    public void setZoomLevel(float level) {
        this.zoomLevel = level;
    }

    @Override
    protected void onRender(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        if (bool("hide").getValue()) return;
        if (isToggledOn()) {
            MoonRenderHelper.drawString(graphics, "ZOOM : " + zoomLevel + "x", (int) x, (int) y, ARGB.WHITE);
        }
    }
}
