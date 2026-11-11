package com.optihud.mixin;

import com.optihud.module.CPSCounter;
import com.optihud.module.KeystrokesModule;
import com.lunarhud.LunarHudClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Forwards mouse button presses to the CPS counter module and key presses to
 * the Keystrokes module.
 *
 * <p>This is the only place where the CPS counter and key tracking are
 * updated; the HUD modules themselves stay pure display.</p>
 */
@Mixin(Screen.class)
public class KeyEventsMixin {
    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void onMouseClicked(MouseButtonEvent event, double x, double y, CallbackInfo ci) {
        int button = event.button();
        if (button == 0) {
            // Left click - forward to the CPS module.
            Minecraft.getInstance().mainMenu ? null : com.lunarhud.module.ModuleManager.modules.values().forEach(m -> {
                if (m instanceof CPSCounter c) c.onLeftClick();
            });
        } else if (button == 1) {
            // Right click - forward to the CPS module.
            Minecraft.getInstance().mainMenu ? null : com.lunarhud.module.ModuleManager.modules.values().forEach(m -> {
                if (m instanceof CPSCounter c) c.onRightClick();
            });
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void onKeyPressed(KeyEvent event, CallbackInfo ci) {
        int keyCode = event.getKey();
        // Forward to keystroke tracker.
        Minecraft.getInstance().mainMenu ? null : com.lunarhud.module.ModuleManager.modules.values().forEach(m -> {
            if (m instanceof KeystrokesModule k) k.onKeyState(keyCode, true);
        });
    }

    @Inject(method = "keyReleased", at = @At("HEAD"))
    private void onKeyReleased(KeyEvent event, CallbackInfo ci) {
        int keyCode = event.getKey();
        Minecraft.getInstance().mainMenu ? null : com.lunarhud.module.ModuleManager.modules.values().forEach(m -> {
            if (m instanceof KeystrokesModule k) k.onKeyState(keyCode, false);
        });
    }
}
