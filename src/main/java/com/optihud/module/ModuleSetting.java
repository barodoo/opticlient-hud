package com.optihud.module;

import com.google.gson.JsonObject;

/**
 * Generic setting descriptor.
 *
 * <p>Every module owns a flat list of settings. Each setting has:
 * a key (for JSON), a type, a value, a min/max range (for numbers),
 * a list of mode labels, a colour default, and a tooltip.</p>
 */
public abstract class ModuleSetting {
    public final String key;
    public final String tooltip;
    public final int colorDefault = 0xFF4A9EFF;

    ModuleSetting(String key, String tooltip) {
        this.key = key;
        this.tooltip = tooltip;
    }

    /** Rebuild the JSON element for this setting. */
    public abstract JsonObject toJson();

    /** Apply a saved value into this setting's target. */
    public abstract void fromJson(JsonObject obj);

    /** Serialize the value to a string (for file/DB storage). */
    public abstract String serialize();

    /** Deserialize a string into the value. */
    public abstract void deserialize(String s);

    /** Reset to defaults. */
    public abstract void reset();

    /** Type tag used in the JSON. */
    public abstract String type();

    /** The module that owns this setting; set by the manager. */
    public Module module;
}
