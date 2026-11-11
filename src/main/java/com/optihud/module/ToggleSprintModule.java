package com.optihud.module;

import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.ARGB;

/**
 * MOVEMENT module: toggle sprint with an on-screen status text.
 *
 * <p>The on-screen text shows <tt>[Sprinting (Toggled)]</tt> when sprint is
 * active.</p>
 */
public class ToggleSprintModule extends ToggleModule {
    private boolean showStatus = true;

    public ToggleSprintModule() {
        super("TOGGLE SPRINT", "MOVEMENT", "Toggle sprint with an on-screen status.");
        bool("status", true, "Show the on-screen sprint status");
    }

    public boolean isShowStatus() { return showStatus; }

    @Override
    public void onToggle() {
        // The actual sprint toggle is handled by a mixin hooking into the key
        // binding's toggle logic.
        if (isShowStatus()) {
            setStatusText(enabled ? "[Sprinting (Toggled)]" : "[Sprint Off]");
        }
    }

    @Override
    protected void onRender(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        if (bool("hide").getValue()) return;
        if (showStatus && getStatusText().length() > 0) {
            MoonRenderHelper.drawString(graphics, " " + getStatusText(), (int) x, (int) y, ARGB.WHITE);
        }
    }
}
