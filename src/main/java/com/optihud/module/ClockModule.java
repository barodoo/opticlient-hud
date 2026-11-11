package com.optihud.module;

import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.ARGB;

import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * HUD module: displays real-world time and in-game time/day counter.
 *
 * <p>Options: 12h / 24h format.</p>
 */
public class ClockModule extends Module {
    private String time12 = "--:--";
    private String time24 = "--:--";
    private int dayCounter = 0;
    private boolean format12 = true;

    public ClockModule() {
        super("CLOCK", "HUD", "Shows real-world time and in-game day counter.");
        mode("format", new String[]{"12h", "24h"}, "12h", "Time format");
        bool("hide", false, "Hide the module");
    }

    public boolean isFormat12() { return format12; }

    public void setFormat12(boolean format12) { this.format12 = format12; }

    public void tick() {
        LocalTime now = LocalTime.now();
        if (format12) {
            time12 = now.format(DateTimeFormatter.ofPattern("hh:mm a"));
            MoonRenderHelper.drawString(graphics, "CLOCK : " + time12, (int) x, (int) y, ARGB.WHITE);
        } else {
            time24 = now.format(DateTimeFormatter.ofPattern("HH:mm"));
            MoonRenderHelper.drawString(graphics, "CLOCK : " + time24, (int) x, (int) y, ARGB.WHITE);
        }
        MoonRenderHelper.drawString(graphics, "DAY : " + dayCounter, (int) x, (int) y + 12, ARGB.GRAY);
    }

    public void setDayCounter(int day) { dayCounter = day; }
}
