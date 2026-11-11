package com.optihud.module;

import com.google.gson.JsonObject;

/**
 * A numeric slider setting, stored as a JSON number.
 */
public class NumberSetting extends ModuleSetting {
    private float min, max, value;

    public NumberSetting(String key, float min, float max, float defaultValue, String tooltip) {
        super(key, tooltip);
        this.min = min;
        this.max = max;
        this.value = defaultValue;
    }

    public float getValue() { return value; }
    public float getMin() { return min; }
    public float getMax() { return max; }

    @Override
    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("key", key);
        o.addProperty("type", "number");
        o.addProperty("min", min);
        o.addProperty("max", max);
        o.addProperty("value", value);
        return o;
    }

    @Override
    public void fromJson(JsonObject obj) {
        if (obj.has("min")) min = obj.get("min").getAsFloat();
        if (obj.has("max")) max = obj.get("max").getAsFloat();
        if (obj.has("value")) value = obj.get("value").getAsFloat();
    }

    @Override
    public String serialize() { return String.valueOf(value); }

    @Override
    public void deserialize(String s) {
        try { value = Float.parseFloat(s); } catch (NumberFormatException ignored) {}
    }

    @Override
    public void reset() { value = min; }

    @Override
    public String type() { return "number"; }
}
