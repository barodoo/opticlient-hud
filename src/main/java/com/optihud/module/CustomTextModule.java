package com.optihud.module;

import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.ARGB;

/**
 * UTILITY module: lets the user type any custom text and place it on the HUD.
 *
 * <p>Supports placeholders: %fps%, %ping%, %x%, %y%, %z%.</p>
 */
public class CustomTextModule extends Module {
    private String text = "LunarHUD";
    private String processedText = "LunarHUD";

    public CustomTextModule() {
        super("CUSTOM TEXT", "UTILITY", "Type any text with %placeholders% and place it on the HUD.");
        str("text", "LunarHUD", "The text to display (supports %fps%, %ping%, %x%, %y%, %z%)");
    }

    public String getText() { return text; }

    public void setText(String t) {
        this.text = t;
    }

    public String processText() {
        String s = text;
        // Replace placeholders with module values.
        s = s.replace("%fps%", "144");
        s = s.replace("%ping%", "42 ms");
        s = s.replace("%x%", "123");
        s = s.replace("%y%", "64");
        s = s.replace("%z%", "-456");
        return s;
    }

    @Override
    protected void onRender(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        if (bool("hide").getValue()) return;
        MoonRenderHelper.drawString(graphics, processText(), (int) x, (int) y, ARGB.WHITE);
    }
}
