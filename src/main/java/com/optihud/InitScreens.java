package com.optihud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

/**
 * Shared static screens must be initialized before the main client class
 * references them (especially the hotkey font injector used in the mixin).
 */
public final class InitScreens {
    public static void init() {
        LunarHudClient.font = Minecraft.getInstance().font;
    }
}
