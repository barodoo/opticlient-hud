package com.optihud.module;

import com.google.gson.JsonObject;
import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.ARGB;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for every module in the mod.
 *
 * <p>Each module carries: its metadata (name, category, description), an
 * enabled flag (persisted), a screen position + scale (persisted, set in the
 * HUD editor), and a list of {@link ModuleSetting} options that the settings
 * screen renders automatically.</p>
 */
public abstract class Module {
    protected final String name;
    protected final String category;
    protected final String description;
    protected boolean enabled = true;
    protected boolean hidden = false;

    // Screen position and scale (persisted by ConfigManager).
    protected float x = 10f;
    protected float y = 10f;
    protected float scale = 1f;
    protected int color = Color.WHITE.getRGB();

    /** All settings this module exposes in the settings screen. */
    protected final List<ModuleSetting> settings = new ArrayList<>();

    public Module(String name, String category, String description) {
        this.name = name;
        this.category = category;
        this.description = description;
    }

    /** Register a setting onto this module. */
    protected <T extends ModuleSetting> T addSetting(T setting) {
        settings.add(setting);
        return setting;
    }

    // ------------------------------------------------------------------ getters

    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public boolean isEnabled() { return enabled; }
    public boolean isHidden() { return hidden; }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getScale() { return scale; }
    public int getColor() { return color; }

    public boolean isVisible() { return enabled && !hidden; }

    public List<ModuleSetting> getSettings() { return settings; }
    public int getSettingCount() { return settings.size(); }

    // ------------------------------------------------------------------ render

    /**
     * Render the module into the HUD layer.
     *
     * <p>The module is drawn inside its own PoseStack transform so it can be
     * dragged around the screen without affecting other HUD elements or the
     * world rendering. The color passed to the renderer is an ARGB value.</p>
     */
    public void render(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        if (!isVisible()) return;
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.pushMatrix();
        graphics.pushPose();
        graphics.translate((int) x, (int) y, 0);
        graphics.scale(scale, scale, 1f);
        onRender(graphics, delta);
        graphics.popPose();
        RenderSystem.popMatrix();
        RenderSystem.disableBlend();
    }

    /** Sub-class override for the actual drawing. */
    protected abstract void onRender(GuiGraphicsExtractor graphics, DeltaTracker delta);

    /** Per-tick data updates (FPS counter, click counters, etc). */
    public void onTick() {}

    /** Called when the module is enabled/disabled. */
    public void onToggle() {}

    // ------------------------------------------------------------------ settings helpers

    /** Boolean setting (ON/OFF). */
    public BooleanSetting bool(String key, boolean defaultValue, String tooltip) {
        return addSetting(new BooleanSetting(key, defaultValue, tooltip));
    }

    /** Number setting (slider). */
    public NumberSetting num(String key, float min, float max, float defaultValue, String tooltip) {
        return addSetting(new NumberSetting(key, min, max, defaultValue, tooltip));
    }

    /** Colour setting. */
    public ColorSetting color(String key, int defaultValue, String tooltip) {
        return addSetting(new ColorSetting(key, defaultValue, tooltip));
    }

    /** Mode setting (dropdown). */
    public ModeSetting mode(String key, String[] values, String defaultValue, String tooltip) {
        return addSetting(new ModeSetting(key, values, defaultValue, tooltip));
    }

    /** Text setting (multi-line). */
    public StringSetting text(String key, String defaultValue, String tooltip) {
        return addSetting(new StringSetting(key, defaultValue, tooltip));
    }

    /** Apply a saved setting value into this module. */
    public void applySetting(ModuleSetting setting) {
        if (setting instanceof BooleanSetting bs) {
            if (bs.key().equals("toggle") && this instanceof ToggleModule tm) {
                tm.toggle();
            }
            return;
        }
    }

    /** Serialize this module into a JSON object. */
    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("name", name);
        o.addProperty("category", category);
        o.addProperty("enabled", enabled);
        o.addProperty("hidden", hidden);
        o.addProperty("x", x);
        o.addProperty("y", y);
        o.addProperty("scale", scale);
        o.addProperty("color", color);
        JsonArray arr = new JsonArray();
        for (ModuleSetting s : settings) arr.add(s.toJson());
        o.add("settings", arr);
        return o;
    }
}
