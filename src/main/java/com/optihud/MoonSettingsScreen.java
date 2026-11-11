package com.optihud;

import com.optihud.module.Module;
import com.optihud.module.ModuleSetting;
import com.optihud.module.BooleanSetting;
import com.optihud.module.NumberSetting;
import com.optihud.module.ColorSetting;
import com.optihud.module.ModeSetting;
import com.optihud.module.StringSetting;
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

/**
 * Simple settings overlay shown for a single module.
 *
 * <p>Lists the module's settings and provides a "Reset to defaults" button.</p>
 */
public class MoonSettingsScreen extends Screen {
    private final Module module;
    private String content;

    public MoonSettingsScreen(Module m, String content) {
        super(I18n.get("lunarhud.settings.title"));
        this.module = m;
        this.content = content;
    }

    @Override
    public void init() {
        super.init();
        // Refresh font reference.
        LunarHudClient.hotkeyFont = Minecraft.getInstance().font;
    }

    @Override
    public void render(GuiGraphicsExtractor context, DeltaTracker delta) {
        background(context, 60, 60, 280, 260, ARGB.BLACK);
        MoonRenderHelper.drawString(context, "  " + module.displayName(), width / 2, 30, ARGB.WHITE);
        context.horizontalLine(20, width - 20, 32, ARGB.WHITE);

        String[] lines = content.split("\n");
        for (int i = 0; i < lines.length; i++) {
            MoonRenderHelper.drawString(context, "  " + lines[i], 24, 60 + i * 24, ARGB.GRAY);
        }

        // Reset button.
        int bx = 24;
        int by = 60 + lines.length * 24 + 10;
        context.fill(bx, by, 200, 26, ARGB.RED);
        MoonRenderHelper.drawString(context, "Reset to defaults", bx + 10, by + 8, ARGB.WHITE);
        context.outline(bx, by, 200, 26, ARGB.WHITE);

        // Back button.
        int bxx = width - 140;
        int byy = 20;
        context.fill(bxx, byy, 120, 22, ARGB.BLUE);
        MoonRenderHelper.drawString(context, "Back", bxx + 10, byy + 7, ARGB.WHITE);
        context.outline(bxx, byy, 120, 22, ARGB.WHITE);
    }

    @Override
    public void close() {
        super.close();
        // Re-open the menu.
        LunarHudClient.menuOpen = true;
        LunarHudClient.menuScreen = this;
        LunarHudClient.currentScreen = LunarHudClient.menuScreen;
        // Save after settings change.
        ModuleManager.saveConfig(ConfigManager.configFile().toString());
    }
}
