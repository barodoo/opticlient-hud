package com.optihud.module;

import com.google.gson.JsonObject;

/**
 * A true/false setting, stored as a JSON boolean.
 */
public class BooleanSetting extends ModuleSetting {
    private boolean value;

    public BooleanSetting(String key, boolean defaultValue, String tooltip) {
        super(key, tooltip);
        this.value = defaultValue;
    }

    public boolean getValue() { return value; }

    @Override
    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("key", key);
        o.addProperty("type", "boolean");
        o.addProperty("value", value);
        return o;
    }

    @Override
    public void fromJson(JsonObject obj) {
        if (obj.has("value")) value = obj.get("value").getAsBoolean();
    }

    @Override
    public String serialize() { return String.valueOf(value); }

    @Override
    public void deserialize(String s) { value = Boolean.parseBoolean(s); }

    @Override
    public void reset() { value = false; }

    @Override
    public String type() { return "boolean"; }
}
