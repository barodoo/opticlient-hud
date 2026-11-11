package com.optihud.module;

import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.ARGB;

/**
 * HUD module: displays the number of players online on the current server.
 *
 * <p>In singleplayer or when the count is not available, shows the status
 * (e.g. "Singleplayer" or "Online").</p>
 */
public class PlayerCountModule extends Module {
    private int playerCount = 0;
    private String playerStatus = "Offline";

    public PlayerCountModule() {
        super("PLAYER COUNT", "HUD", "Shows the number of players online.");
        bool("hide", false, "Hide the module");
    }

    public void setPlayerCount(int count) {
        this.playerCount = count;
        if (count > 0) {
            this.playerStatus = count + " online";
        } else {
            this.playerStatus = "Singleplayer";
        }
    }

    @Override
    protected void onRender(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        if (bool("hide").getValue()) return;
        MoonRenderHelper.drawString(graphics, "PLAYERS : " + playerStatus, (int) x, (int) y, ARGB.WHITE);
    }
}
