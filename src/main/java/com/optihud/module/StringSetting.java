package com.optihud.module;

import com.google.gson.JsonObject;

/**
 * A text field setting, stored as a JSON string.
 */
public class StringSetting extends ModuleSetting {
    private String value;

    public StringSetting(String key, String defaultValue, String tooltip) {
        super(key, tooltip);
        this.value = defaultValue;
    }

    public String getValue() { return value; }

    @Override
    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("key", key);
        o.addProperty("type", "string");
        o.addProperty("value", value);
        return o;
    }

    @Override
    public void fromJson(JsonObject obj) {
        if (obj.has("value")) value = obj.get("value").getAsString();
    }

    @Override
    public String serialize() { return value; }

    @Override
    public void deserialize(String s) { value = s; }

    @Override
    public void reset() { value = ""; }

    @Override
    public String type() { return "string"; }
}
