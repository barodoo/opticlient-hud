package com.optihud.module;

import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.ARGB;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/**
 * HUD module: displays the Java memory usage.
 *
 * <p>Format: <tt>MEM : 1.2 GB / 4 GB</tt> with percent option.</p>
 */
public class MemoryModule extends Module {
    private int usedGB = 0;
    private int totalGB = 0;
    private String usedText = "0 MB";
    private String totalText = "0 MB";
    private boolean showPercent = false;
    private float percent = 0;

    public MemoryModule() {
        super("MEMORY", "HUD", "Shows the Java memory usage.");
        bool("percent", false, "Show percentage");
        bool("hide", false, "Hide the module");
    }

    public void tick() {
        MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memBean.getHeapMemoryUsage();
        long used = heap.getUsed();
        long total = heap.getMax();

        usedGB = (int) (used / (1024 * 1024));
        totalGB = (int) (total / (1024 * 1024));
        percent = total > 0 ? (used / (float) total) * 100 : 0;

        if (usedGB < 1024) {
            usedText = usedGB + " MB";
        } else {
            usedText = String.format("%.1f GB", usedGB / 1024.0);
        }
        if (totalGB < 1024) {
            totalText = totalGB + " MB";
        } else {
            totalText = String.format("%.1f GB", totalGB / 1024.0);
        }
    }

    @Override
    protected void onRender(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        if (bool("hide").getValue()) return;
        if (showPercent) {
            MoonRenderHelper.drawString(graphics, "MEM : " + usedText + " / " + totalText + " (" + (int) percent + "%)", (int) x, (int) y, ARGB.WHITE);
        } else {
            MoonRenderHelper.drawString(graphics, "MEM : " + usedText + " / " + totalText, (int) x, (int) y, ARGB.WHITE);
        }
    }

    public boolean isShowPercent() { return showPercent; }
    public float getPercent() { return percent; }
}
