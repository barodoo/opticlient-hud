package com.optihud.module;

import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.ARGB;

/**
 * HUD module: displays the current server address (or "Singleplayer").
 */
public class ServerIpModule extends Module {
    private String serverAddress = "Singleplayer";

    public ServerIpModule() {
        super("SERVER IP", "HUD", "Shows the current server address.");
        bool("hide", false, "Hide the module");
    }

    public void setServerAddress(String address) {
        this.serverAddress = address;
    }

    @Override
    protected void onRender(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        if (bool("hide").getValue()) return;
        MoonRenderHelper.drawString(graphics, "SERVER IP : " + serverAddress, (int) x, (int) y, ARGB.WHITE);
    }
}
