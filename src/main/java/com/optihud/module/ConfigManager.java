package com.optihud.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lunarhud.LunarHudClient;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Loads and saves the module config as pretty-printed JSON.
 *
 * <p>Path resolves to: <tt>~/.minecraft/config/lunarhud/config.json</tt>.</p>
 */
public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "config.json";

    /** Return the config file path. */
    public static Path configFile() {
        return Minecraft.getInstance().runDirectory.toPath()
                .resolve("config").resolve("lunarhud").resolve(FILE_NAME);
    }

    /** Load the file if it exists. */
    public static void load(String path) {
        Path file = configFile();
        if (Files.notExists(file)) {
            save(path);
            return;
        }
        try {
            String json = Files.readString(file, StandardCharsets.UTF_8);
            ConfigManager m = GSON.fromJson(json, ConfigManager.class);
            if (m != null) {
                apply(m);
            }
        } catch (IOException e) {
            LunarHudClient.info("Failed to load config: " + e.getMessage());
        }
    }

    private static void apply(ConfigManager m) {
        for (Module mdl : ModuleManager.modules.values()) {
            // Re-seed defaults first so missing config fields are sane.
            for (ModuleSetting s : mdl.getSettings()) s.reset();
            JsonObject node = m.modules.getAsJsonObject(mdl.getName());
            if (node == null) continue;
            if (node.has("enabled")) mdl.enabled = node.get("enabled").getAsBoolean();
            if (node.has("hidden")) mdl.hidden = node.get("hidden").getAsBoolean();
            if (node.has("x")) mdl.x = node.get("x").getAsFloat();
            if (node.has("y")) mdl.y = node.get("y").getAsFloat();
            if (node.has("scale")) mdl.scale = node.get("scale").getAsFloat();
            if (node.has("color")) mdl.color = node.get("color").getAsInt();

            JsonArray st = node.getAsJsonArray("settings");
            if (st != null) {
                for (com.google.gson.JsonElement e : st) {
                    JsonObject o = e.getAsJsonObject();
                    ModuleSetting set = findSetting(mdl, o.get("key").getAsString());
                    if (set != null) set.fromJson(o);
                }
            }
        }
    }

    private static ModuleSetting findSetting(Module mdl, String key) {
        for (ModuleSetting s : mdl.getSettings()) {
            if (s.key.equals(key)) return s;
        }
        return null;
    }

    /** Persist the current module list to disk. */
    public static void save(String path) {
        Path file = configFile();
        try {
            Files.createDirectories(file.getParent());
            JsonObject root = new JsonObject();
            JsonArray modules = new JsonArray();
            for (Module mdl : ModuleManager.modules.values()) {
                modules.add(mdl.toJson());
            }
            root.add("modules", modules);
            String out = GSON.toJson(root);
            Files.writeString(file, out, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LunarHudClient.info("Failed to save config: " + e.getMessage());
        }
    }

    /** Apply a parsed config document onto the module list. */
    public static void applyTo(java.util.Map<String, Module> modules) {
        // Already applied in load()
    }
}
