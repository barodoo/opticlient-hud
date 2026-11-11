package com.optihud.module;

import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.util.ARGB;

import java.util.ArrayList;
import java.util.List;

/**
 * HUD module: displays LEFT and RIGHT clicks per second (CPS).
 *
 * <p>Uses a rolling 1-second window with per-frame tick counting. The actual
 * click detection is done in a mixin that forwards {@link KeyEvent} and
 * {@link MouseButtonEvent} into the module's handlers.</p>
 *
 * <p>Format: <tt>LEFT CPS | RIGHT CPS</tt> e.g. <tt>8 | 5</tt></p>
 */
public class CPSCounter extends Module {
    private int leftClicksThisSec = 0;
    private int rightClicksThisSec = 0;
    private int leftClicksLastSec = 0;
    private int rightClicksLastSec = 0;
    private int updateTimer = 0;

    private boolean leftHeld = false;
    private boolean rightHeld = false;

    public CPSCounter() {
        super("CPS", "HUD", "Shows left and right clicks per second.");
        bool("hide", false, "Hide the module");
    }

    @Override
    public void onTick() {
        updateTimer++;
        if (updateTimer >= 20) {
            leftClicksLastSec = leftClicksThisSec;
            rightClicksLastSec = rightClicksThisSec;
            leftClicksThisSec = 0;
            rightClicksThisSec = 0;
            updateTimer = 0;
        }
    }

    /** Called by the click-mixin on a left mouse button press. */
    public synchronized void onLeftClick() {
        leftClicksThisSec++;
    }

    /** Called by the click-mixin on a right mouse button press. */
    public synchronized void onRightClick() {
        rightClicksThisSec++;
    }

    /** Get the current left CPS value. */
    public synchronized int getLeftCPS() {
        return leftClicksLastSec;
    }

    /** Get the current right CPS value. */
    public synchronized int getRightCPS() {
        return rightClicksLastSec;
    }

    @Override
    protected void onRender(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        if (bool("hide").getValue()) return;
        MoonRenderHelper.drawString(
                graphics,
                "LEFT CPS | RIGHT CPS  (" + getLeftCPS() + " | " + getRightCPS() + ")",
                (int) x, (int) y, ARGB.WHITE);
    }
}
