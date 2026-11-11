package com.optihud;

import com.optihud.module.*;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.option.KeyBindingWithTooltip;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

import java.util.ArrayList;
import java.util.List;

/**
 * Loader entrypoint.
 *
 * <p>Registers the key binding, starts the config manager, builds the module
 * list, and installs the HUD layer renderer.</p>
 */
public class LunarHudClient implements ClientModInitializer {
    public static final String MOD_ID = "optihud";
    public static final Identifier RESOURCE_ID = Identifier.of(MOD_ID, "textures/gui/background.png");

    // The mod's main toggle key (RIGHT SHIFT by default).
    public static KeyBindingWithTooltip toggleKey;

    // UI state.
    public static boolean menuOpen;
    public static boolean editorOpen;
    public static MoonMenu menuScreen;
    public static MoonHudEditor editorScreen;
    public static MoonMenu currentScreen;

    // Shared game fonts used across screens and HUD drawing.
    public static Font font;
    public static Font hotkeyFont;

    @Override
    public void onInitializeClient() {
        InitScreens.init();

        // Create the mod's key category.
        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.of(MOD_ID, "mod_category"));

        // The toggle key (RIGHT SHIFT by default).
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBindingWithTooltip(
                MOD_ID + ".toggle_menu",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_RSHIFT,
                category
        ));

        // Listen to the end-of-tick so we refresh per-frame HUD data.
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (menuOpen || editorOpen) {
                if (menuOpen && menuScreen != null) {
                    menuScreen.tick();
                }
                if (editorOpen && editorScreen != null) {
                    editorScreen.tick();
                }
            }
        });

        // Set up screen switching hooks for the HUD editor.
        ClientLifecycleEvents.BEFORE_CLIENT_LEVEL_CHANGE.register(client -> {
            if (editorOpen && currentScreen != null) {
                editorOpen = false;
                editorScreen = null;
                currentScreen = null;
            }
        });

        // Register the HUD layer once.
        registerHudLayer();

        // Initialize all modules.
        initModules();

        // Load saved config.
        ModuleManager.loadConfig(ConfigManager.configFile().toString());
        LunarHudClient.info("Modules loaded: " + ModuleManager.modules.size());
    }

    private void registerHudLayer() {
        // Register a single HUD layer that routes to the active screen.
        HudElementRegistry.addFirst(
                Identifier.of(MOD_ID, "main"),
                (graphics, deltaTracker) -> {
                    if (menuOpen && menuScreen != null) {
                        menuScreen.render(graphics, deltaTracker);
                    } else if (editorOpen && editorScreen != null) {
                        editorScreen.render(graphics, deltaTracker);
                    }
                }
        );
    }

    private void initModules() {
        // HUD modules.
        ModuleManager.register(new FPSCounter());
        ModuleManager.register(new CPSCounter());
        ModuleManager.register(new KeystrokesModule());
        ModuleManager.register(new PingModule());
        ModuleManager.register(new CoordinatesModule());
        ModuleManager.register(new DirectionModule());
        ModuleManager.register(new ClockModule());
        ModuleManager.register(new MemoryModule());
        ModuleManager.register(new SpeedModule());
        ModuleManager.register(new ServerIpModule());
        ModuleManager.register(new BiomeModule());
        ModuleManager.register(new PlayerCountModule());

        // Combat modules.
        ModuleManager.register(new ArmorStatusModule());
        ModuleManager.register(new PotionStatusModule());
        ModuleManager.register(new ItemCounterModule());

        // Movement module.
        ModuleManager.register(new ToggleSprintModule());

        // Visual module.
        ModuleManager.register(new ZoomModule());

        // Utility module.
        ModuleManager.register(new CustomTextModule());

        LunarHudClient.info("All modules registered: " + ModuleManager.modules.size());
    }

    public static void info(String msg) {
        System.out.println("[LunarHUD] " + msg);
    }
}
