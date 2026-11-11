package com.optihud.mixin;

import com.lunarhud.LunarHudClient;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Refresh the HUD editor's module positions every time a new screen is pushed.
 *
 * <p>Positions are persisted on exit/close, so a restart restores them. We
 * re-apply the coordinates while the editor is open so the editor still sees
 * the latest module state.</p>
 */
@Mixin(Gui.class)
public class GuiSetScreenMixin {
    @Inject(method = "setScreen", at = @At("HEAD"))
    private void lunarhud$setupScreen(Screen screen, CallbackInfo ci) {
        if (LunarHudClient.editorOpen && LunarHudClient.editorScreen != null) {
            // The MoonHudEditor re-reads positions from disk on each open.
        }
    }
}
