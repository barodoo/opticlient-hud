package com.optihud.module;

import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.ARGB;

/**
 * HUD module: displays the current ping.
 *
 * <p>Format: <tt>PING : 42 ms</tt>, colour changes green/yellow/red.</p>
 */
public class PingModule extends Module {
    private int ping = 0;
    private int pingColor = 0xFFAAAAAA;

    public PingModule() {
        super("PING", "HUD", "Shows the current server ping.");
        bool("hide", false, "Hide the module");
    }

    /** Called by the server-ping mixin or by the HUD layer when the player updates. */
    public void setPing(int ping) {
        this.ping = ping;
        // Green: < 80, Yellow: 80-150, Red: > 150
        if (ping < 80) pingColor = 0xFF00FF00;
        else if (ping < 150) pingColor = 0xFFAAAA00;
        else pingColor = 0xFFFF0000;
    }

    public int getPing() { return ping; }

    @Override
    protected void onRender(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        if (bool("hide").getValue()) return;
        MoonRenderHelper.drawString(graphics, "PING : " + ping + " ms", (int) x, (int) y, pingColor);
    }
}
