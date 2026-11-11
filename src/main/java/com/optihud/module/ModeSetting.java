package com.optihud.module;

import com.google.gson.JsonObject;

/**
 * An option selector (dropdown / paginated choice), stored as a JSON string index.
 */
public class ModeSetting extends ModuleSetting {
    private final String[] options;
    private int index = 0;

    public ModeSetting(String key, String[] options, String defaultValue, String tooltip) {
        super(key, tooltip);
        this.options = options;
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(defaultValue)) { index = i; break; }
        }
    }

    public int getIndex() { return index; }
    public String[] getOptions() { return options; }
    public String getValueText() { return options[index]; }

    @Override
    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("key", key);
        o.addProperty("type", "mode");
        o.addProperty("options", String.join("|", options));
        o.addProperty("value", index);
        return o;
    }

    @Override
    public void fromJson(JsonObject obj) {
        if (obj.has("options")) {
            String[] def = obj.get("options").getAsString().split("\\|");
            if (def.length != options.length) {
                options = new String[def.length];
                System.arraycopy(def, 0, options, 0, def.length);
            }
        }
        if (obj.has("value")) index = obj.get("value").getAsInt();
    }

    @Override
    public String serialize() { return String.valueOf(index); }

    @Override
    public void deserialize(String s) {
        try {
            int i = Integer.parseInt(s);
            if (i >= 0 && i < options.length) index = i;
        } catch (NumberFormatException ignored) {}
    }

    @Override
    public void reset() { index = 0; }

    @Override
    public String type() { return "mode"; }
}
