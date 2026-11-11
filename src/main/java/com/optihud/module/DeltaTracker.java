package com.optihud.module;

/**
 * Tiny stand-in for Fabric's DeltaTracker.
 *
 * <p>The mod draws HUD elements through the real Fabric {@code GuiGraphicsExtractor}
 * layer API, which gives us a tick delta. We only need the raw delta for
 * smooth animation, so we implement the minimum interface.</p>
 */
public class DeltaTracker {
    /** Seconds since the last render call. */
    public final float partialTicks;

    public DeltaTracker(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    /** Seconds for a full interpolation step (used by visual modules). */
    public float getGameTimeDeltaPartialTick(boolean secondHalf) {
        return partialTicks;
    }
}
