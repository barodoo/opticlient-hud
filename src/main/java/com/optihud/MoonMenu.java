package com.optihud;

import com.optihud.module.Module;
import com.optihud.module.ModuleManager;
import com.optihud.module.ModuleSetting;
import com.optihud.module.NumberSetting;
import com.optihud.module.ColorSetting;
import com.optihud.module.ModeSetting;
import com.optihud.module.BooleanSetting;
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
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.ARGB;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The main mod menu screen.
 *
 * <p>Features:
 * <ul>
 *   <li>category tab strip (HUD | COMBAT | MOVEMENT | VISUAL | UTILITY)</li>
 *   <li>a search bar at the top</li>
 *   <li>one ON/OFF toggle plus a settings button per module</li>
 *   <li>a HUD EDITOR button that opens the drag-and-drop HUD editor</li>
 * </ul>
 *
 * <p>All screens are actual Screen subclasses so they participate in the
 * Fabric screen event system.</p>
 */
public class MoonMenu extends Screen {
    private final List<Module> filtered = new ArrayList<>();
    private final List<Module> shown = new ArrayList<>();

    // tabs
    private int selectedTab = 0;
    private final String[] tabs = {"HUD", "COMBAT", "MOVEMENT", "VISUAL", "UTILITY"};

    // search
    private String search = "";

    // scroll
    private int scroll = 0;

    // mouse
    private int mouseX = 0;
    private int mouseY = 0;

    private String updateResponse = "";

    public MoonMenu() {
        super(I18n.get("lunarhud.menu.title"));
    }

    @Override
    public void init() {
        super.init();
        LunarHudClient.hotkeyFont = Minecraft.getInstance().font;
        rebuild();
    }

    @Override
    public void tick() {
        super.tick();
        mouseX = mouseY = 0;
    }

    @Override
    public void render(GuiGraphicsExtractor context, DeltaTracker delta) {
        background(context, 40, 40, 240, 200, ARGB.BLACK);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        // Top bar.
        MoonRenderHelper.drawString(context, "  LunarHUD", 120, 12, ARGB.WHITE);
        context.horizontalLine(12, width - 24, 12, ARGB.WHITE);

        // Search bar.
        MoonRenderHelper.drawString(context, "  Search: ", 16, 36, ARGB.GRAY);
        if (search.length() > 0) {
            MoonRenderHelper.drawString(context, search, 112, 34, ARGB.WHITE);
        }
        context.fill(108, 32, 150, 18, ARGB.BLACK);
        drawTextbox(context, 112, 32, 150, 18, search);

        // Tab strip.
        int tabY = 58;
        int tabW = 100;
        int tabH = 22;
        for (int i = 0; i < tabs.length; i++) {
            int x = 16 + i * (tabW + 6);
            if (i == selectedTab) {
                context.fill(x, tabY, tabW, tabH, ARGB.BLACK);
                MoonRenderHelper.drawString(context, " " + tabs[i] + " ", x + 10, tabY + 5, ARGB.WHITE);
                context.outline(x, tabY, tabW, tabH, ARGB.WHITE);
            } else {
                context.fill(x, tabY, tabW, tabH, ARGB.BLACK);
                MoonRenderHelper.drawString(context, " " + tabs[i] + " ", x + 10, tabY + 5, ARGB.WHITE);
                context.outline(x, tabY, tabW, tabH, ARGB.WHITE);
            }
        }

        // Module list area.
        int listTop = 88;
        int listLeft = 16;
        int listW = width - 32;
        int listH = 40 * 6;

        context.fill(listLeft, listTop, listW, listH, ARGB.BLACK);
        context.outline(listLeft, listTop, listW, listH, ARGB.WHITE);

        // Draw module rows.
        int rowY = listTop + 8;
        int rowH = 20;
        for (Module m : shown) {
            int x = listLeft + 4;
            int y = rowY + scroll;
            int w = listW - 12;

            int tint = (int) ((rowY + scroll) % 2 == 0 ? ARGB.BGMASK : ARGB.BGMASK / 2);
            if (m.isHidden()) tint = ARGB.BLACK;
            context.fill(x, y, w, rowH, tint);

            // Toggle.
            int toggleX = x + 4;
            boolean on = m.isEnabled();
            if (on) {
                context.fill(toggleX, y + 4, 12, 12, ARGB.GREEN);
            } else {
                context.fill(toggleX, y + 4, 12, 12, ARGB.RED);
            }
            context.outline(toggleX, y + 4, 12, 12, ARGB.WHITE);

            // Name + description.
            MoonRenderHelper.drawString(context, "  " + m.displayName(), toggleX + 18, y + 3, ARGB.WHITE);
            MoonRenderHelper.drawString(context, "    " + m.getDescription(), toggleX + 18, y + 15, ARGB.GRAY);

            // Settings button.
            int setX = x + w - 70;
            int setY = y + 3;
            context.fill(setX, setY, 56, 14, ARGB.BLUE);
            MoonRenderHelper.drawString(context, "Settings", setX + 8, setY + 6, ARGB.WHITE);
            context.outline(setX, setY, 56, 14, ARGB.WHITE);

            rowY += rowH;
        }

        // HUD EDITOR button.
        int edX = 16;
        int edY = listTop + listH - 52;
        context.fill(edX, edY, 300, 32, ARGB.BLUE);
        MoonRenderHelper.drawString(context, "HUD EDITOR", edX + 10, edY + 9, ARGB.WHITE);
        context.outline(edX, edY, 300, 32, ARGB.WHITE);

        if (rowY > listTop + listH) {
            MoonRenderHelper.drawString(context, "  ... more modules (use search to find them)", 16, listTop + listH - 20, ARGB.GRAY);
        }
    }

    @Override
    public void close() {
        super.close();
        // Save module states to the config when the menu closes.
        ModuleManager.saveConfig(ConfigManager.configFile().toString());
        LunarHudClient.menuOpen = false;
        LunarHudClient.currentScreen = null;
    }

    // ---------------------------------------------------------------- input

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        this.mouseX = (int) mx;
        this.mouseY = (int) my;
        if (button != 0) return false;
        for (Module m : filtered) {
            if (isMouseOverModule(m, mouseX, mouseY)) {
                if (mouseX >= listLeft + 4 && mouseX <= listLeft + listW - 70 &&
                        mouseY >= rowY(m) && mouseY <= rowY(m) + 20) {
                    // toggle area
                    return toggleModule(m);
                } else {
                    // settings button
                    openSettings(m);
                    return true;
                }
            }
        }
        if (isMouseOverEditor()) {
            openEditor();
            return true;
        }
        return super.mouseClicked(mx, my, button);
    }

    private boolean toggleModule(Module m) {
        m.enabled = !m.enabled;
        if (m.isEnabled()) m.onToggle();
        updateResponse = m.displayName() + (m.isEnabled() ? " ON" : " OFF");
        rebuild();
        return true;
    }

    private void openSettings(Module m) {
        // For brevity we use a simple popup overlay.
        String[] settingNames = new String[m.getSettingCount()];
        for (int i = 0; i < m.getSettingCount(); i++) {
            settingNames[i] = m.getSettings().get(i).key;
        }
        String t = "Module: " + m.displayName() + "\n\n";
        for (String s : settingNames) t += s + "\n";
        Minecraft.getInstance().gui.setScreen(new MoonSettingsScreen(m, t));
        LunarHudClient.menuOpen = false;
    }

    private void openEditor() {
        LunarHudClient.editorOpen = true;
        LunarHudClient.editorScreen = new MoonHudEditor();
        LunarHudClient.currentScreen = LunarHudClient.editorScreen;
        LunarHudClient.menuOpen = false;
    }

    private boolean isMouseOverEditor() {
        int ex = 16;
        int ey = LunarHudClient.editorScreen == null ? 0 : 40;
        return mouseX >= ex && mouseX <= ex + 300 &&
                mouseY >= ey && mouseY <= ey + 32;
    }

    private boolean isMouseOverModule(Module m, double mx, double my) {
        // Approximate: the module occupies a row in the list.
        int idx = shown.indexOf(m);
        if (idx < 0) return false;
        int y = 88 + 8 + idx * 20;
        return mx >= 4 && mx <= width - 8 && my >= y && my <= y + 20;
    }

    private int rowY(Module m) {
        int idx = shown.indexOf(m);
        if (idx < 0) return 0;
        return 88 + 8 + idx * 20;
    }

    private void rebuild() {
        filtered.clear();
        shown.clear();
        for (Module m : ModuleManager.modules.values()) {
            if (m.getCategory().equals(tabs[selectedTab]) &&
                    m.displayName().toLowerCase().contains(search.toLowerCase())) {
                filtered.add(m);
            }
        }
        int len = Math.min(filtered.size(), 40);
        for (int i = 0; i < len; i++) shown.add(filtered.get(i));
        scroll = 0;
    }

    // ------------------------------------------------------------------ text helpers

    private void drawTextbox(GuiGraphicsExtractor g, int x, int y, int w, int h, String text) {
        // Simple single-line text drawing (no native textbox widget here).
        MoonRenderHelper.drawString(g, text, x, y, ARGB.WHITE);
    }
}
