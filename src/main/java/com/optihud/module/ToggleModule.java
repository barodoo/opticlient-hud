package com.optihud.module;

/**
 * Base class for toggleable modules (sprint/sneak, zoom, etc).
 *
 * <p>Wraps a module in this so the menu can show a one-click toggle and a
 * transient on-screen status string. The sub-class implements the functionality.</p>
 */
public abstract class ToggleModule extends Module {
    protected boolean toggledOn = false;
    private String statusText = "";

    public ToggleModule(String name, String category, String description) {
        super(name, category, description);
    }

    /** Toggle the module on/off and persist it. */
    public void toggle() {
        enabled = !enabled;
        if (enabled) {
            toggledOn = true;
            onToggle();
        }
        onToggle();
    }

    /** Override to do something when the module is enabled/disabled. */
    public void onToggle() {}

    public boolean isToggledOn() { return toggledOn; }
    public void setToggledOn(boolean on) { toggledOn = on; }
    public String getStatusText() { return statusText; }
    public void setStatusText(String s) { statusText = s; }
}
