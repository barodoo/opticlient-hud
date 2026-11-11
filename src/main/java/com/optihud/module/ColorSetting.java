package com.optihud.module;

import com.google.gson.JsonObject;

import java.awt.Color;

/**
 * A colour picker setting, stored as a JSON integer (0xRRGGBB).
 */
public class ColorSetting extends ModuleSetting {
    private int value;

    public ColorSetting(String key, int defaultValue, String tooltip) {
        super(key, tooltip);
        this.value = defaultValue;
    }

    public int getValue() { return value; }

    @Override
    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("key", key);
        o.addProperty("type", "color");
        o.addProperty("value", value);
        return o;
    }

    @Override
    public void fromJson(JsonObject obj) {
        if (obj.has("value")) value = obj.get("value").getAsInt();
    }

    @Override
    public String serialize() { return String.valueOf(value); }

    @Override
    public void deserialize(String s) {
        try { value = Integer.parseInt(s); } catch (NumberFormatException ignored) {}
    }

    @Override
    public void reset() { value = 0xFF4A9EFF; }

    @Override
    public String type() { return "color"; }
}
