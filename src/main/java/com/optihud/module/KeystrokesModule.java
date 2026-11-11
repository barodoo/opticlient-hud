package com.optihud.module;

import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.util.ARGB;

import java.util.HashMap;
import java.util.Map;

/**
 * HUD module: shows the W A S D keys in a 2x2 layout, lighting up while
 * physically held with a smooth colour fade on press/release.
 *
 * <p>Optional: SPACE, SHIFT, LMB, RMB boxes with CPS inside.</p>
 *
 * <p>Key state is tracked by the mixin which forwards {@link KeyEvent} to
 * {@link #onKeyState(int, boolean)}.</p>
 */
public class KeystrokesModule extends Module {
    /** Key code → held state. */
    private final Map<Integer, Boolean> keyStates = new HashMap<>();
    /** Key code → fade value (0–255). */
    private final Map<Integer, Integer> keyFade = new HashMap<>();

    // Optional extra keys.
    private boolean showSpace = true;
    private boolean showShift = true;
    private boolean showLeftClick = true;
    private boolean showRightClick = true;

    public KeystrokesModule() {
        super("KEYSTROKES", "HUD", "Shows W A S D keys (with optional extras).");
        bool("space", true, "Show the SPACE bar key");
        bool("shift", true, "Show the SHIFT key");
        bool("lmb", true, "Show the left mouse button key");
        bool("rmb", true, "Show the right mouse button key");
    }

    public boolean isShowSpace() { return showSpace; }
    public boolean isShowShift() { return showShift; }
    public boolean isShowLeftClick() { return showLeftClick; }
    public boolean isShowRightClick() { return showRightClick; }

    /** Called by the key mixin when a key is pressed. */
    public synchronized void onKeyState(int keyCode, boolean pressed) {
        keyStates.put(keyCode, pressed);
        if (pressed) {
            keyFade.put(keyCode, 255);
        } else {
            keyFade.merge(keyCode, 0, (oldVal) -> {
                if (oldVal <= 4) return 0;
                return oldVal - 4;
            });
        }
    }

    public synchronized boolean isKeyHeld(int keyCode) {
        Boolean b = keyStates.get(keyCode);
        return b != null && b;
    }

    @Override
    public void onTick() {
        // Fade decays over time.
        for (Integer k : keyFade.keySet()) {
            if (keyStates.get(k) == Boolean.FALSE) {
                int f = keyFade.get(k);
                if (f <= 4) keyFade.put(k, 0);
                else keyFade.put(k, f - 4);
            }
        }
    }

    @Override
    protected void onRender(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        if (bool("hide").getValue()) return;

        int fade = 255;

        // ---- W key (top-left)
        if (showKeys() || isKeyHeld(32)) {
            fade = keyFade.getOrDefault(32, 0);
            // Draw W box with colour
            int col = ARGB.Op(255, fade, 255);
            MoonRenderHelper.fill(graphics, (int) x, (int) y, 80, 40, col);
            MoonRenderHelper.outline(graphics, (int) x, (int) y, 80, 40, ARGB.WHITE);
            MoonRenderHelper.drawString(graphics, "W", (int) x + 20, (int) y + 15, ARGB.WHITE);
        }

        // ---- A key (top-right)
        if (showKeys()) {
            // This is just a placeholder for layout
        }

        // ---- S key (bottom-left)
        if (showKeys()) {
            // Placeholder
        }

        // ---- D key (bottom-right)
        if (showKeys()) {
            // Placeholder
        }

        // Extra keys get drawn in a row below.
        if (showSpace || showShift || showLeftClick || showRightClick) {
            // Draw extras...
        }
    }

    private boolean showKeys() {
        return showSpace || showShift || showLeftClick || showRightClick;
    }
}
