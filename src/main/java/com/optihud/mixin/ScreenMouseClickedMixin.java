package com.optihud.mixin;

import com.lunarhud.LunarHudClient;
import com.optihud.module.ConfigManager;
import com.optihud.module.ModuleManager;
import com.optihud.module.ModuleSetting;
import com.optihud.module.NumberSetting;
import com.optihud.module.ColorSetting;
import com.optihud.module.ModeSetting;
import com.optihud.module.StringSetting;
import com.optihud.render.MoonRenderHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.Narratable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.option.Config;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.ARGB;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Intercept mouse clicks and key presses on the menu and editor screens.
 *
 * <p>This lets the mod's menu handle clicks without waiting for the screen's
 * own logic, so the mod works reliably across all screen types.</p>
 */
@Mixin(Screen.class)
public class ScreenMouseClickedMixin {
    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void lunarhud$screenMouseClicked(double x, double y, int button, CallbackInfo ci) {
        // Handled in the screen classes themselves.
    }
}
