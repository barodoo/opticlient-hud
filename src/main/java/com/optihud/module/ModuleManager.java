package com.optihud.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lunarhud.LunarHudClient;
import com.lunarhud.LunarHudClientModule;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds every module, instantiates it and applies the saved config.
 *
 * <p>The menu queries this for tabs, search filtering and settings screens.</p>
 */
public class ModuleManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /** All modules in the mod. */
    public static final Map<String, Module> modules = new HashMap<>();

    /** Known module types (name → class). */
    private static final Map<String, Class<? extends Module>> TYPES = new HashMap<>();

    static {
        // HUD category modules (added in Part 2).
        registerType("hud", "FPS");
        registerType("hud", "CPS");
        registerType("hud", "KEYSTROKES");
        registerType("hud", "PING");
        registerType("hud", "COORDINATES");
        registerType("hud", "DIRECTION");
        registerType("hud", "CLOCK");
        registerType("hud", "MEMORY");
        registerType("hud", "SPEED");
        registerType("hud", "SERVER_IP");
        registerType("hud", "BIOME");
        registerType("hud", "PLAYER_COUNT");

        // Combat.
        registerType("combat", "ARMOR_STATUS");
        registerType("combat", "POTION_STATUS");
        registerType("combat", "ITEM_COUNTER");

        // Movement.
        registerType("movement", "TOGGLE_SPRINT");

        // Visual.
        registerType("visual", "ZOOM");

        // Utility.
        registerType("utility", "CUSTOM_TEXT");
    }

    private static void registerType(String category, String name) {
        TYPES.put(name, null);
    }

    /** Register a module instance in the manager. */
    public static void register(Module m) {
        modules.put(m.getName(), m);
    }

    /** Load config from disk into the module list. */
    public static void loadConfig(String path) {
        ConfigManager.load(path);
        applyConfig();
    }

    /** Save the current module states to disk. */
    public static void saveConfig(String path) {
        ConfigManager.save(path);
    }

    private static void applyConfig() {
        for (Module mdl : modules.values()) {
            // Re-seed defaults first so any missing config fields are sane.
            for (ModuleSetting s : mdl.getSettings()) s.reset();
        }
        ConfigManager.applyTo(modules);
    }
}
