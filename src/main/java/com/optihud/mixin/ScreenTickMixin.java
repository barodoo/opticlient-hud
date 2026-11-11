package com.optihud.mixin;

import com.lunarhud.LunarHudClient;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Make sure the mod's screen events fire on every Screen.tick.
 *
 * <p>When the menu or HUD editor is open we tick them here, otherwise Fabric's
 * screen tick events are only fired while the screen's own tick() runs.</p>
 */
@Mixin(Screen.class)
public class ScreenTickMixin {
    @Inject(method = "tick()V", at = @At("HEAD"))
    private void lunarhud$screenTick(CallbackInfo ci) {
        // Hook is purely for the semantics of the tick lifecycle.
    }
}
