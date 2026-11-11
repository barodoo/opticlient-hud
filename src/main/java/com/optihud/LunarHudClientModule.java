package com.optihud;

import com.optihud.module.ConfigManager;
import com.optihud.module.Module;
import com.optihud.module.ModuleManager;

/**
 * Client-side entrypoint that wires the config and module manager.
 *
 * <p>Initialises the config manager at the correct path, registers all module
 * instances, and starts the mod.</p>
 */
public class LunarHudClientModule implements com.lunarhud.module.ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Load any previously saved config.
        ModuleManager.loadConfig(ConfigManager.configFile().toString());
        LunarHudClient.info("Modules loaded: " + ModuleManager.modules.size());
    }
}
