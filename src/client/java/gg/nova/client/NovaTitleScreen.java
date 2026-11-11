package gg.nova.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public final class NovaTitleScreen extends Screen {
    private int sidebarW, left, top, panelW, panelH, mainX, mainW;
    private int[] buttonY = new int[5];
    private int buttonH = 34;

    public NovaTitleScreen() { super(Component.literal("Nova")); }

    private void layout() {
        sidebarW = Math.min(190, Math.max(150, width / 7));
        panelW = Math.min(1180, width - 36);
        panelH = Math.min(700, height - 30);
        left = (width - panelW) / 2;
        top = (height - panelH) / 2;
        mainX = left + sidebarW + 24;
        mainW = panelW - sidebarW - 48;
        int baseY = top + 142;
        for (int i = 0; i < buttonY.length; i++) buttonY[i] = baseY + i * (buttonH + 8);
    }

    @Override
    protected void init() { layout(); }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        layout();
        renderBackdrop(g);
        g.fill(left - 1, top - 1, left + panelW + 1, top + panelH + 1, 0xFF28415F);
        g.fill(left, top, left + panelW, top + panelH, 0xF7071225);
        g.fill(left, top, left + panelW, top + 3, NovaTheme.BLUE);
        renderSidebar(g, mouseX, mouseY);
        renderHero(g);
        renderFeatureCards(g);
        renderStatus(g);
        super.render(g, mouseX, mouseY, delta);
    }

    private void renderBackdrop(GuiGraphics g) {
        g.fill(0, 0, width, height, 0xFF050B18);
        for (int i = 0; i < 28; i++) {
            int x = i * width / 28;
            int r = 5 + i / 3;
            int b = 18 + i;
            g.fill(x, 0, x + width / 28 + 1, height,
                0x30000000 | (r << 16) | (Math.min(28, r + 5) << 8) | Math.min(55, b));
        }
        // Abstract crystal shards and ambient lights; all drawn locally without external assets.
        long tick = System.currentTimeMillis();
        for (int i = 0; i < 14; i++) {
            int x = (int)((i * 173L + tick / (18 + i)) % Math.max(1, width));
            int y = (int)((i * 97L + tick / (25 + i)) % Math.max(1, height));
            int s = 3 + i % 5;
            g.fill(x, y, x + s, y + s * 3, i % 2 == 0 ? 0x5539B8FF : 0x559B7CFF);
        }
        g.fill(0, height - 4, width / 3, height, NovaTheme.BLUE);
        g.fill(width / 3, height - 4, width * 2 / 3, height, NovaTheme.PURPLE);
        g.fill(width * 2 / 3, height - 4, width, height, NovaTheme.CYAN);
    }

    private void renderSidebar(GuiGraphics g, int mouseX, int mouseY) {
        g.fill(left, top + 3, left + sidebarW, top + panelH, 0xFF0B192D);
        g.fill(left + 20, top + 25, left + 24, top + 72, NovaTheme.BLUE);
        g.drawString(font, "N O V A", left + 35, top + 29, NovaTheme.CYAN);
        g.drawString(font, "PLAY SMARTER.", left + 36, top + 49, NovaTheme.MUTED);
        g.drawString(font, "CLIENT 1.1.2", left + 36, top + 65, NovaTheme.PURPLE);
        g.fill(left + 18, top + 91, left + sidebarW - 18, top + 92, NovaTheme.DIVIDER);
        g.drawString(font, "NAVIGATION", left + 20, top + 108, 0xFF7189A8);

        String[] labels = {"Home", "Modules", "Visual Studio", "Cosmetics", "Settings"};
        for (int i = 0; i < labels.length; i++) {
            int y = buttonY[i];
            boolean hover = inside(mouseX, mouseY, left + 13, y, sidebarW - 26, buttonH);
            g.fill(left + 13, y, left + sidebarW - 13, y + buttonH,
                hover ? 0xFF1B3654 : (i == 0 ? 0xFF142A45 : 0x00000000));
            if (i == 0 || hover) g.fill(left + 13, y, left + 16, y + buttonH, i == 0 ? NovaTheme.BLUE : NovaTheme.PURPLE);
            g.drawString(font, labels[i], left + 27, y + 12,
                hover || i == 0 ? NovaTheme.CYAN : NovaTheme.TEXT);
        }

        int footY = top + panelH - 66;
        g.fill(left + 17, footY, left + sidebarW - 17, footY + 1, NovaTheme.DIVIDER);
        g.drawString(font, "ADVANCED EDITION", left + 20, footY + 16, NovaTheme.PURPLE);
        g.drawString(font, "FABRIC 1.21.11", left + 20, footY + 34, NovaTheme.MUTED);
    }

    private void renderHero(GuiGraphics g) {
        int y = top + 28;
        g.drawString(font, "NOVA  /  YOUR MINECRAFT", mainX, y, NovaTheme.PURPLE);
        g.drawString(font, "Make your world yours.", mainX, y + 24, NovaTheme.TEXT);
        g.drawString(font, "A cleaner PvP experience. Your style. Your settings.",
            mainX, y + 44, NovaTheme.MUTED);

        int heroY = top + 88;
        int heroH = Math.min(230, panelH / 2);
        g.fill(mainX - 1, heroY - 1, mainX + mainW - 1, heroY + heroH + 1, 0xFF304A6B);
        g.fill(mainX, heroY, mainX + mainW - 2, heroY + heroH, 0xE90B1830);
        g.fill(mainX, heroY, mainX + 4, heroY + heroH, NovaTheme.PURPLE);
        g.drawString(font, "NOVA", mainX + 25, heroY + 24, NovaTheme.CYAN);
        g.drawString(font, "MODERN GLASS EXPERIENCE", mainX + 25, heroY + 47, NovaTheme.PURPLE);
        g.drawString(font, "Modules, performance tools and player cosmetics",
            mainX + 25, heroY + 76, NovaTheme.TEXT);
        g.drawString(font, "designed around one consistent interface.",
            mainX + 25, heroY + 92, NovaTheme.MUTED);

        int actionY = heroY + heroH - 52;
        drawAction(g, mainX + 25, actionY, 150, 34, "OPEN MODULES", true);
        drawAction(g, mainX + 186, actionY, 130, 34, "COSMETICS", false);
        drawAction(g, mainX + 327, actionY, 120, 34, "VISUALS", false);

        int rightW = Math.min(220, mainW / 3);
        int rightX = mainX + mainW - rightW - 18;
        g.fill(rightX, heroY + 17, rightX + rightW, heroY + heroH - 17, 0xCC101F38);
        g.drawString(font, "YOUR NOVA SETUP", rightX + 13, heroY + 30, NovaTheme.CYAN);
        String[] stats = {
            "MODULES  " + ModuleManager.getModules().size(),
            "ACTIVE  " + ModuleManager.getModules().stream().filter(NovaModule::isEnabled).count(),
            "COSMETICS  6",
            "PROFILE  DEFAULT"
        };
        int sy = heroY + 58;
        for (String stat : stats) {
            g.fill(rightX + 12, sy - 4, rightX + rightW - 12, sy - 3, NovaTheme.DIVIDER);
            g.drawString(font, stat, rightX + 13, sy + 4, NovaTheme.TEXT);
            sy += 31;
        }
    }

    private void renderFeatureCards(GuiGraphics g) {
        int y = top + Math.min(340, panelH - 125);
        int gap = 12;
        int w = (mainW - gap * 2) / 3;
        String[] titles = {"PVP WORKSPACE", "VISUAL STUDIO", "PLAYER COSMETICS"};
        String[] desc = {
            "Useful tools and clean combat visuals",
            "Tune particles, effects and interface style",
            "Wings, auras and trails visible in third person"
        };
        for (int i = 0; i < 3; i++) {
            int x = mainX + i * (w + gap);
            g.fill(x - 1, y - 1, x + w + 1, y + 65, 0xFF263D5C);
            g.fill(x, y, x + w, y + 64, 0xFF12243D);
            g.fill(x, y, x + 3, y + 64, i == 1 ? NovaTheme.PURPLE : NovaTheme.BLUE);
            g.drawString(font, titles[i], x + 12, y + 12, NovaTheme.CYAN);
            g.drawString(font, desc[i], x + 12, y + 34, NovaTheme.MUTED);
        }
    }

    private void renderStatus(GuiGraphics g) {
        int y = top + panelH - 25;
        g.fill(mainX, y - 7, left + panelW - 20, y - 6, NovaTheme.DIVIDER);
        g.fill(mainX, y + 1, mainX + 7, y + 8, 0xFF48D597);
        g.drawString(font, "NOVA CLIENT  •  READY", mainX + 14, y, NovaTheme.MUTED);
        g.drawString(font, "RIGHT SHIFT  •  IN-GAME MODULES", left + panelW - 205, y, NovaTheme.PURPLE);
    }

    private void drawAction(GuiGraphics g, int x, int y, int w, int h, String text, boolean primary) {
        g.fill(x - 1, y - 1, x + w + 1, y + h + 1, primary ? NovaTheme.CYAN : NovaTheme.DIVIDER);
        g.fill(x, y, x + w, y + h, primary ? 0xFF164B78 : 0xFF142641);
        g.drawCenteredString(font, text, x + w / 2, y + 12, NovaTheme.TEXT);
    }

    private boolean inside(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        layout();
        if (event.button() != 0) return super.mouseClicked(event, doubleClick);
        double mx = event.x(), my = event.y();

        for (int i = 0; i < buttonY.length; i++) {
            if (inside(mx, my, left + 13, buttonY[i], sidebarW - 26, buttonH)) {
                openDestination(i);
                return true;
            }
        }
        int heroY = top + 88;
        int heroH = Math.min(230, panelH / 2);
        int actionY = heroY + heroH - 52;
        if (inside(mx, my, mainX + 25, actionY, 150, 34)) {
            Minecraft.getInstance().setScreen(new NovaScreen());
            return true;
        }
        if (inside(mx, my, mainX + 186, actionY, 130, 34)) {
            selectedCosmetics();
            return true;
        }
        if (inside(mx, my, mainX + 327, actionY, 120, 34)) {
            Minecraft.getInstance().setScreen(new NovaScreen());
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    private void openDestination(int i) {
        Minecraft mc = Minecraft.getInstance();
        switch (i) {
            case 0 -> { }
            case 1, 2, 3 -> mc.setScreen(new NovaScreen());
            case 4 -> mc.setScreen(new OptionsScreen(this, mc.options));
            default -> { }
        }
    }

    private void selectedCosmetics() {
        Minecraft.getInstance().setScreen(new NovaScreen());
    }

    @Override
    public void onClose() { }
    @Override
    public boolean isPauseScreen() { return false; }
}
