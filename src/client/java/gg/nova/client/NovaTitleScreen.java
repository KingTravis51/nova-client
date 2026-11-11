package gg.nova.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

/**
 * Nova's custom Minecraft title menu.
 * All menu geometry is calculated from the current window size.
 */
public final class NovaTitleScreen extends Screen {
    private static final int NAVY = NovaTheme.NAVY;
    private static final int PANEL = NovaTheme.PANEL;
    private static final int PANEL_HOVER = NovaTheme.HOVER;
    private static final int BLUE = NovaTheme.BLUE;
    private static final int CYAN = NovaTheme.CYAN;
    private static final int PURPLE = NovaTheme.PURPLE;
    private static final int MUTED = NovaTheme.MUTED;

    private int buttonX;
    private int buttonY;
    private int buttonWidth;
    private int buttonHeight;
    private int buttonGap;
    private int quickX;
    private int quickY;
    private int quickWidth;
    private int quickHeight;

    public NovaTitleScreen() {
        super(Component.literal("Nova"));
    }

    @Override
    protected void init() {
        layout();
    }

    private void layout() {
        buttonWidth = Math.min(290, Math.max(190, width / 3));
        buttonHeight = 38;
        buttonGap = 9;
        buttonX = Math.max(28, width / 12);
        buttonY = Math.max(178, height / 2 - 8);

        quickWidth = Math.min(360, Math.max(230, width / 3));
        quickHeight = 178;
        quickX = Math.max(buttonX + buttonWidth + 35, width - quickWidth - Math.max(34, width / 12));
        quickY = Math.max(110, height / 2 - 62);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        layout();
        renderBackdrop(graphics);
        renderBrand(graphics);
        renderButtons(graphics, mouseX, mouseY);
        renderQuickPlay(graphics, mouseX, mouseY);
        renderFooter(graphics);
    }

    private void renderBackdrop(GuiGraphics graphics) {
        graphics.fill(0, 0, width, height, NAVY);

        // Layered vertical colour bands create a subtle blue-to-purple atmosphere.
        for (int i = 0; i < 36; i++) {
            int x = i * width / 36;
            int next = (i + 1) * width / 36 + 1;
            int blue = 10 + (int) (8.0 * i / 35.0);
            int red = 7 + (int) (12.0 * i / 35.0);
            int green = 18 + (int) (5.0 * i / 35.0);
            graphics.fill(x, 0, next, height, 0x50000000 | (red << 16) | (green << 8) | blue);
        }

        // Decorative crystal silhouettes with a gentle animated glow.
        int pulse = (int) (Math.sin(System.currentTimeMillis() / 650.0) * 12.0);
        drawCrystal(graphics, width - 170, 100, 74, 0x4439B8FF + (pulse << 24));
        drawCrystal(graphics, width - 300, height - 150, 48, 0x339B7CFF);
        drawCrystal(graphics, Math.max(35, width / 2 - 20), 72, 34, 0x3373E5FF);

        graphics.fill(0, height - 2, width, height, 0xFF172C49);
        graphics.fill(0, height - 2, width / 3, height, BLUE);
    }

    private void drawCrystal(GuiGraphics graphics, int x, int y, int size, int color) {
        int half = size / 2;
        for (int row = 0; row < half; row += 3) {
            int inset = row * size / (half * 2);
            graphics.fill(x - half + inset, y + row, x + half - inset, y + row + 3, color);
            graphics.fill(x - half + inset, y - row, x + half - inset, y - row + 2, color);
        }
    }

    private void renderBrand(GuiGraphics graphics) {
        int brandY = Math.max(42, height / 5);
        graphics.fill(buttonX, brandY - 12, buttonX + 4, brandY + 49, BLUE);
        graphics.drawString(font, "N O V A", buttonX + 18, brandY - 8, CYAN);
        graphics.drawString(font, "MINECRAFT CLIENT", buttonX + 19, brandY + 12, MUTED);
        graphics.drawString(font, "Make your world yours.", buttonX + 19, brandY + 35, 0xFFE4EEFA);
        graphics.drawString(font, "ADVANCED EDITION  •  1.21.11", buttonX + 1, brandY + 59, PURPLE);
    }

    private void renderButtons(GuiGraphics graphics, int mouseX, int mouseY) {
        String[] labels = {"SINGLEPLAYER", "MULTIPLAYER", "NOVA MODULES", "COSMETICS", "SETTINGS", "QUIT GAME"};
        for (int i = 0; i < labels.length; i++) {
            int y = buttonY + i * (buttonHeight + buttonGap);
            boolean hovered = inside(mouseX, mouseY, buttonX, y, buttonWidth, buttonHeight);
            int background = hovered ? PANEL_HOVER : PANEL;
            graphics.fill(buttonX, y, buttonX + buttonWidth, y + buttonHeight, background);
            graphics.fill(buttonX, y, buttonX + 3, y + buttonHeight, i == 2 ? PURPLE : BLUE);
            graphics.fill(buttonX + 3, y, buttonX + buttonWidth, y + 1, hovered ? CYAN : 0x5539B8FF);
            graphics.drawString(font, labels[i], buttonX + 18, y + 14, hovered ? CYAN : 0xFFEAF3FF);
            graphics.drawString(font, "›", buttonX + buttonWidth - 20, y + 11, hovered ? CYAN : MUTED);
        }
    }

    private void renderQuickPlay(GuiGraphics graphics, int mouseX, int mouseY) {
        boolean hovered = inside(mouseX, mouseY, quickX, quickY + 105, quickWidth, 42);
        graphics.fill(quickX, quickY, quickX + quickWidth, quickY + quickHeight, PANEL);
        graphics.fill(quickX, quickY, quickX + quickWidth, quickY + 2, PURPLE);
        graphics.drawString(font, "QUICK PLAY", quickX + 18, quickY + 17, CYAN);
        graphics.drawString(font, "DONUT SMP", quickX + 18, quickY + 48, 0xFFFFFFFF);
        graphics.drawString(font, "Jump back into your favourite server.", quickX + 18, quickY + 66, MUTED);
        graphics.fill(quickX + 16, quickY + 91, quickX + quickWidth - 16, quickY + 92, 0x5539B8FF);
        graphics.fill(quickX + 16, quickY + 105, quickX + quickWidth - 16, quickY + 147,
            hovered ? PANEL_HOVER : 0xFF102844);
        graphics.fill(quickX + 16, quickY + 105, quickX + 19, quickY + 147, BLUE);
        graphics.drawCenteredString(font, "OPEN SERVER LIST", quickX + quickWidth / 2, quickY + 121,
            hovered ? CYAN : 0xFFEAF3FF);
    }

    private void renderFooter(GuiGraphics graphics) {
        graphics.drawString(font, "NOVA  •  CUSTOM CLIENT EXPERIENCE", 18, height - 18, MUTED);
        graphics.drawString(font, "RIGHT SHIFT  •  MODULES", Math.max(18, width - 190), height - 18, MUTED);
    }

    private boolean inside(double mouseX, double mouseY, int x, int y, int w, int h) {
        return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() != 0) return super.mouseClicked(event, doubleClick);

        double mouseX = event.x();
        double mouseY = event.y();

        for (int i = 0; i < 6; i++) {
            int y = buttonY + i * (buttonHeight + buttonGap);
            if (!inside(mouseX, mouseY, buttonX, y, buttonWidth, buttonHeight)) continue;

            Minecraft minecraft = Minecraft.getInstance();
            switch (i) {
                case 0 -> minecraft.setScreen(new SelectWorldScreen(this));
                case 1 -> minecraft.setScreen(new JoinMultiplayerScreen(this));
                case 2, 3 -> minecraft.setScreen(new NovaScreen());
                case 4 -> minecraft.setScreen(new OptionsScreen(this, minecraft.options));
                case 5 -> minecraft.stop();
                default -> { }
            }
            return true;
        }

        if (inside(mouseX, mouseY, quickX + 16, quickY + 105, quickWidth - 32, 42)) {
            Minecraft.getInstance().setScreen(new JoinMultiplayerScreen(this));
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public void onClose() {
        // Keep the title menu open when Escape is pressed.
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
