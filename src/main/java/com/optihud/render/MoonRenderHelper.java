package com.optihud.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.ARGB;

/**
 * Small helpers for drawing styled text and rectangles inside the HUD layer.
 *
 * <p>Text colour changed from RGB to ARGB in 1.21.6+, so we always pass opaque
 * 32-bit ARGB values.</p>
 */
public final class MoonRenderHelper {
    private MoonRenderHelper() {}

    public static void drawString(GuiGraphicsExtractor g, String text, int x, int y, int color) {
        g.text(Minecraft.getInstance().font, text, x, y, color, false);
    }

    public static void drawCentered(GuiGraphicsExtractor g, String text, int x, int y, int color) {
        Font font = Minecraft.getInstance().font;
        int w = font.width(text);
        g.text(font, text, x - w / 2, y, color, false);
    }

    public static void fill(GuiGraphicsExtractor g, int x, int y, int w, int h, int color) {
        g.fill(x, y, x + w, y + h, color);
    }

    public static void outline(GuiGraphicsExtractor g, int x, int y, int w, int h, int color) {
        g.outline(x, y, x + w, y + h, color);
    }

    public static void horizonLine(GuiGraphicsExtractor g, int x0, int x1, int y, int color) {
        g.horizontalLine(x0, x1, y, color);
    }

    public static void verticalLine(GuiGraphicsExtractor g, int x, int y0, int y1, int color) {
        g.verticalLine(x, y0, y1, color);
    }
}
