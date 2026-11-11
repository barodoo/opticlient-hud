package com.optihud.module;

import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.ARGB;

/**
 * COMBAT HUD module: displays equipped armor and held item with durability.
 *
 * <p>Options: number or bar durability, show item icon.</p>
 */
public class ArmorStatusModule extends Module {
    private boolean showItemIcon = true;
    private boolean durabilityBar = false;
    private boolean showHeldItem = true;

    public ArmorStatusModule() {
        super("ARMOR STATUS", "COMBAT", "Displays equipped armor and held item durability.");
        bool("icon", true, "Show the item icon");
        bool("bar", false, "Show durability as a bar");
        bool("held", true, "Show the held item");
    }

    public boolean isShowItemIcon() { return showItemIcon; }
    public boolean isDurabilityBar() { return durabilityBar; }
    public boolean isShowHeldItem() { return showHeldItem; }

    @Override
    protected void onRender(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        if (bool("hide").getValue()) return;
        MoonRenderHelper.drawString(graphics, "ARMOR : ", (int) x, (int) y, ARGB.WHITE);
    }
}
