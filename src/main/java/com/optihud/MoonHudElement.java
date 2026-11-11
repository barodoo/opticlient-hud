package com.optihud;

import com.optihud.module.Module;
import com.optihud.module.NumberSetting;
import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.ARGB;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents one enabled HUD element at runtime.
 *
 * <p>The menu / editor only creates these after a module is enabled. The element
 * is rendered by the HUD layer, and the editor uses it to drag / scale the
 * module.</p>
 */
public class MoonHudElement {
    public final Module module;
    public float x, y;
    public float scale = 1f;
    public boolean drawOutline = false;
    public int outlineColor = 0xFF4A9EFF;

    public MoonHudElement(Module m) {
        this.module = m;
        this.x = m.getX();
        this.y = m.getY();
        this.scale = m.getScale();
    }

    public void render(GuiGraphicsExtractor g, DeltaTracker delta) {
        if (!module.isEnabled()) return;
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.pushMatrix();
        g.pushPose();
        g.translate((int) x, (int) y, 0);
        g.scale(scale, scale, 1f);
        module.onRender(g, delta);
        g.popPose();
        RenderSystem.popMatrix();
        RenderSystem.disableBlend();
    }

    public boolean hit(int px, int py) {
        return px >= (int) x && px <= (int) (x + 200) &&
                py >= (int) y && py <= (int) (y + 60);
    }
}
