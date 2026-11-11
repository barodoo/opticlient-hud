package com.optihud.module;

import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.ARGB;

/**
 * COMBAT HUD module: displays the count of a configurable item (default:
 * golden apples, ender pearls, arrows).
 */
public class ItemCounterModule extends Module {
    private String targetItem = "golden apple";
    private int count = 0;

    public ItemCounterModule() {
        super("ITEM COUNTER", "COMBAT", "Shows how many of a configurable item are in the inventory.");
        str("item", "golden apple", "The item to count (e.g. golden apple, ender pearl, arrow)");
    }

    public String getTargetItem() { return targetItem; }
    public int getCount() { return count; }

    @Override
    protected void onRender(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        if (bool("hide").getValue()) return;
        MoonRenderHelper.drawString(graphics, "ITEMS : " + targetItem + " x" + count, (int) x, (int) y, ARGB.WHITE);
    }
}
