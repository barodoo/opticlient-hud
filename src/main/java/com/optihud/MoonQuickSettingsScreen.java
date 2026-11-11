package com.optihud;

import com.optihud.module.Module;
import com.optihud.module.ModuleSetting;
import com.optihud.module.NumberSetting;
import com.optihud.module.ColorSetting;
import com.optihud.module.ModeSetting;
import com.optihud.module.StringSetting;
import com.optihud.module.ConfigManager;
import com.optihud.module.ToggleModule;
import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.Narratable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.option.Config;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.ARGB;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Quick settings panel for an element (scale, colour, per-element controls).
 */
public class MoonQuickSettingsScreen extends Screen {
    private final Module module;
    private final com.lunarhud.MoonHudElement element;

    public MoonQuickSettingsScreen(Module m, com.lunarhud.MoonHudElement e) {
        super(I18n.get("lunarhud.quicksettings.title"));
        this.module = m;
        this.element = e;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void render(GuiGraphicsExtractor context, DeltaTracker delta) {
        background(context, 80, 80, 300, 240, ARGB.BLACK);
        MoonRenderHelper.drawString(context, "  " + module.displayName() + " — Quick", width / 2, 30, ARGB.WHITE);
        context.horizontalLine(20, width - 20, 32, ARGB.WHITE);

        // Scale slider.
        int sx = 24;
        if (module.getSettingCount() > 0) {
            ModuleSetting ms = module.getSettings().get(0);
            if (ms instanceof NumberSetting ns) {
                float val = ns.getValue();
                MoonRenderHelper.drawString(context, "  Scale: " + val + "x", sx, 60, ARGB.GRAY);
                int track = 260, thumb = 14;
                int px = sx, py = 80, pw = track, ph = 8;
                context.fill(px, py, pw, ph, ARGB.BLUE);
                float t = clamp((val - ns.getMin()) / (ns.getMax() - ns.getMin()));
                int tx = px + (int) (t * (pw - thumb));
                context.fill(tx, py, thumb, ph, ARGB.WHITE);
            }
        }

        // Colour setting.
        int ci = 0;
        if (module.getSettingCount() > 1) {
            ModuleSetting ms = module.getSettings().get(1);
            if (ms instanceof ColorSetting cs) {
                ci = cs.getValue();
            }
        }
        context.fill(width / 2 - 50, 110, 100, 20, ci);
        context.outline(width / 2 - 50, 110, 100, 20, ARGB.WHITE);
        MoonRenderHelper.drawString(context, "  Colour", width / 2 - 40, 114, ARGB.WHITE);

        // Reset button.
        int bx = 24;
        int by = 160;
        context.fill(bx, by, 260, 26, ARGB.RED);
        MoonRenderHelper.drawString(context, "Reset to defaults", bx + 10, by + 8, ARGB.WHITE);
        context.outline(bx, by, 260, 26, ARGB.WHITE);

        // Back.
        int bxx = width - 140;
        int byy = 20;
        context.fill(bxx, byy, 120, 22, ARGB.BLUE);
        MoonRenderHelper.drawString(context, "Back", bxx + 10, byy + 7, ARGB.WHITE);
        context.outline(bxx, byy, 120, 22, ARGB.WHITE);
    }

    @Override
    public void close() {
        super.close();
        LunarHudClient.menuOpen = true;
        LunarHudClient.menuScreen = this;
        LunarHudClient.currentScreen = LunarHudClient.menuScreen;
        ModuleManager.saveConfig(ConfigManager.configFile().toString());
    }
}
