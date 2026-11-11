package com.optihud;

import com.optihud.module.Module;
import com.optihud.module.ModuleManager;
import com.optihud.module.ModuleSetting;
import com.optihud.module.NumberSetting;
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
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.ARGB;
import net.minecraft.util.math.MathHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * The drag-and-drop HUD editor screen.
 *
 * <p>The user can:</p>
 * <ul>
 *   <li>drag enabled HUD elements around the screen</li>
 *   <li>use crosshair guides (snap to edges / centre)</li>
 *   <li>nudge with arrow keys</li>
 *   <li>scale each element with a slider</li>
 *   <li>right click to open quick settings</li>
 *   <li>ESC to save and exit</li>
 * </ul>
 *
 * <p>All positions are written to the same config file used by the rest of the
 * mod on close.</p>
 */
public class MoonHudEditor extends Screen {
    private final List<MoonHudElement> elements = new ArrayList<>();
    private final Map<Module, MoonHudElement> elementMap = new HashMap<>();

    private MoonHudElement selected = null;
    private int dragStartX, dragStartY;
    private int dragOffsetX, dragOffsetY;

    // nudge
    private int nudgeX = 0, nudgeY = 0;
    private boolean snapping = false;

    public MoonHudEditor() {
        super(I18n.get("lunarhud.editor.title"));
    }

    @Override
    public void init() {
        super.init();
        buildElements();
    }

    private void buildElements() {
        elements.clear();
        elementMap.clear();
        for (Module m : ModuleManager.modules.values()) {
            if (!m.isHidden()) {
                MoonHudElement e = new MoonHudElement(m);
                elements.add(e);
                elementMap.put(m, e);
            }
        }
        loadPositions();
    }

    private void loadPositions() {
        Path p = Path.of(ConfigManager.configFile().getParent().resolve("module-positions.json"));
        if (Files.notExists(p)) return;
        try {
            String json = Files.readString(p);
            if (json.isEmpty()) return;
            // Simple JSON parse for positions.
            String[] parts = json.split("}");
            for (String part : parts) {
                if (part.indexOf("\"x\":") < 0) continue;
                String name = part.split("\"name\"")[1].replace("\"", "").trim();
                if (name.isEmpty()) continue;
                Module m = ModuleManager.modules.get(name);
                if (m == null) {
                    // fallback search
                    for (Module mm : ModuleManager.modules.values()) {
                        if (mm.getName().equals(name)) m = mm;
                    }
                }
                if (m == null) continue;
                int ix = part.indexOf("\"x\":");
                int iy = part.indexOf("\"y\":");
                int is = part.indexOf("\"scale\":");
                try {
                    double dx = 10, dy = 10, s = 1f;
                    if (ix >= 0) {
                        String v = part.substring(ix + 4, part.indexOf(",", ix));
                        dx = Double.parseDouble(v);
                    }
                    if (iy >= 0) {
                        String v = part.substring(iy + 4, part.indexOf(",", iy));
                        dy = Double.parseDouble(v);
                    }
                    if (is >= 0) {
                        String v = part.substring(is + 7);
                        s = Float.parseFloat(v);
                    }
                    m.x = (float) dx;
                    m.y = (float) dy;
                    m.scale = (float) s;
                } catch (NumberFormatException ignored) {}
            }
        } catch (IOException e) {
            LunarHudClient.info("Failed to load editor positions: " + e.getMessage());
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (nudgeX != 0 || nudgeY != 0) {
            if (selected != null) {
                selected.x += nudgeX;
                selected.y += nudgeY;
            }
            nudgeX = nudgeY = 0;
        }
    }

    @Override
    public void render(GuiGraphicsExtractor context, DeltaTracker delta) {
        // Background.
        context.fill(0, 0, width, height, ARGB.BLACK);

        // Draw enabled HUD elements.
        for (MoonHudElement e : elements) {
            Module m = e.module;
            if (m == null || !m.isEnabled()) continue;
            e.render(context, delta);
        }

        // Guides.
        if (selected != null && snapping) {
            drawGuides(context);
        }

        // Selection outline.
        if (selected != null && selected.drawOutline) {
            RenderSystem.enableDepthTest();
            RenderSystem.setShaderColor(1f, 1f, 1f, 0.3f);
            context.fill(selected.x - 6, selected.y - 6, 12, 12, ARGB.BLACK);
            context.outline(selected.x - 6, selected.y - 6, 12, 12, ARGB.WHITE);
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        }

        // Editor controls.
        drawEditorControls(context);
    }

    private void drawGuides(GuiGraphicsExtractor g) {
        int cx = width / 2;
        int cy = height / 2;
        int edgeX0 = 0, edgeX1 = width, edgeY0 = 0, edgeY1 = height;
        int selX = selected.x, selY = selected.y;

        // Horizontal edge lines.
        g.horizontalLine(edgeX0, edgeX1, selY, ARGB.BLUE);
        g.horizontalLine(edgeX0, edgeX1, selY + 2, ARGB.BLUE);
        // Vertical edge lines.
        g.verticalLine(selX, edgeY0, edgeY1, ARGB.BLUE);
        g.verticalLine(selX + 2, edgeY0, edgeY1, ARGB.BLUE);

        // Centre crosshair.
        g.horizontalLine(cx - 3, cx + 3, cy, ARGB.GREEN);
        g.verticalLine(cx, cy - 3, cy + 3, ARGB.GREEN);

        // Snap text.
        g.text(Minecraft.getInstance().font, "SNAP: edges + centre", 12, 12, ARGB.RED, false);
    }

    private void drawEditorControls(GuiGraphicsExtractor g) {
        int bx = 16, by = 0;
        g.fill(bx, by, 300, 36, ARGB.BLACK);
        g.outline(bx, by, 300, 36, ARGB.WHITE);

        MoonRenderHelper.drawString(g, "  [ESC] Save & exit  |  [R] Reset positions  |  [N] Cycle nudge", bx + 10, by + 8, ARGB.WHITE);

        // Scale slider for the selected element.
        if (selected != null && selected.setting != null) {
            ModuleSetting ms = selected.setting;
            if (ms instanceof NumberSetting ns) {
                float val = ns.getValue();
                int track = 280, thumb = 14;
                int px = bx + 14, py = by + 22, pw = track, ph = 8;
                g.fill(px, py, pw, ph, ARGB.BLUE);
                float t = clamp((val - ns.getMin()) / (ns.getMax() - ns.getMin()));
                int tx = px + (int) (t * (pw - thumb));
                g.fill(tx, py, thumb, ph, ARGB.WHITE);
                MoonRenderHelper.drawString(g, " " + val + "x", px + 8, py + 2, ARGB.WHITE);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        mouseX = (int) mx;
        mouseY = (int) my;
        if (button != 0) return false;

        if (isKeyDown(32)) { // space
            return false;
        }

        // Pick an element under the cursor.
        MoonHudElement picked = null;
        for (MoonHudElement e : elements) {
            if (e.hit(mouseX, mouseY)) { picked = e; break; }
        }

        if (picked != null) {
            // Right click opens quick settings.
            if (button == 1) {
                openQuickSettings(picked.module);
                return true;
            }
            selected = picked;
            dragStartX = mouseX;
            dragStartY = mouseY;
            dragOffsetX = (int) (picked.x - mouseX);
            dragOffsetY = (int) (picked.y - mouseY);
            snapping = true;
        } else {
            selected = null;
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double deltaX, double deltaY) {
        if (selected != null && button == 0) {
            int dx = (int) deltaX;
            int dy = (int) deltaY;
            int nx = (int) mx + dragOffsetX;
            int ny = (int) my + dragOffsetY;
            // Snap to nearest edge / centre.
            if (snapping && (Math.abs(dx) > 4 || Math.abs(dy) > 4)) {
                nx = snapToEdgeOrCenter(nx, ny);
                ny = snapToEdgeOrCenter(ny, nx);
            }
            selected.x = (float) nx;
            selected.y = (float) ny;
            // Update the module too.
            Module m = selected.module;
            if (m != null) { m.x = selected.x; m.y = selected.y; }
        }
        mouseX = (int) mx;
        mouseY = (int) my;
        return true;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        int key = event.getKey();
        if (key == 27) { // ESC = save and exit
            savePositions();
            LunarHudClient.editorOpen = false;
            LunarHudClient.editorScreen = null;
            LunarHudClient.currentScreen = null;
            return true;
        }
        if (key == 26) { // R = reset positions
            for (Module m : ModuleManager.modules.values()) {
                m.x = 10f;
                m.y = 10f;
                m.scale = 1f;
            }
            savePositions();
            LunarHudClient.info("Editor positions reset.");
            return true;
        }
        if (key == 14) { // N = cycle nudge
            if (selected != null) {
                nudgeX = 1; nudgeY = 1;
                return true;
            }
        }
        // Arrow keys nudge the selected element.
        if (selected != null) {
            int dx = 0, dy = 0;
            switch (key) {
                case 263: dy = -1; break; // up
                case 262: dy = 1; break;  // down
                case 265: dx = -1; break; // left
                case 264: dx = 1; break;  // right
            }
            if (dx != 0 || dy != 0) {
                selected.x += dx;
                selected.y += dy;
                Module m = selected.module;
                if (m != null) { m.x = selected.x; m.y = selected.y; }
                return true;
            }
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean keyReleased(KeyEvent event) {
        if (event.getKey() == 14) { // N
            nudgeX = 0; nudgeY = 0;
            return true;
        }
        return super.keyReleased(event);
    }

    @Override
    public void close() {
        super.close();
        savePositions();
    }

    private void savePositions() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        boolean first = true;
        for (Module m : ModuleManager.modules.values()) {
            if (m.isHidden()) continue;
            if (!first) sb.append(",\n");
            first = false;
            sb.append("  \"").append(escape(m.getName())).append("\": {\n");
            sb.append("    \"name\": \"").append(escape(m.getName())).append("\",\n");
            sb.append("    \"x\": ").append(m.getX()).append(",\n");
            sb.append("    \"y\": ").append(m.getY()).append(",\n");
            sb.append("    \"scale\": ").append(m.getScale()).append("\n");
            sb.append("  }");
        }
        sb.append("\n}");
        Path p = Path.of(ConfigManager.configFile().getParent().resolve("module-positions.json"));
        try {
            Files.createDirectories(p.getParent());
            Files.writeString(p, sb.toString());
        } catch (IOException e) {
            LunarHudClient.info("Failed to save editor positions: " + e.getMessage());
        }
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private int snapToEdgeOrCenter(int val, int other) {
        int edgeX0 = 0, edgeX1 = width, edgeY0 = 0, edgeY1 = height;
        int cx = width / 2, cy = height / 2;
        int sel = val;

        int[] dists = new int[5];
        dists[0] = Math.abs(val - edgeX0);
        dists[1] = Math.abs(val - edgeX1);
        dists[2] = Math.abs(val - edgeY0);
        dists[3] = Math.abs(val - edgeY1);
        dists[4] = Math.abs(val - (width / 2));

        int min = 1000000, mi = 0;
        for (int i = 0; i < 5; i++) {
            if (dists[i] < min) { min = dists[i]; mi = i; }
        }
        if (mi == 0) return edgeX0;
        if (mi == 1) return edgeX1;
        if (mi == 2) return edgeY0;
        if (mi == 3) return edgeY1;
        if (mi == 4) return width / 2;
        return sel;
    }

    private float clamp(float v) { return v < 0 ? 0 : (v > 1 ? 1 : v); }

    private void openQuickSettings(Module m) {
        // Quick settings overlay for the selected element.
        Minecraft.getInstance().gui.setScreen(new MoonQuickSettingsScreen(m, selected));
    }
}
