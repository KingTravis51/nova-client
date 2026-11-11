package gg.nova.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.List;

public final class NovaScreen extends Screen {

    private static final int PANEL_WIDTH = 740;
    private static final int PANEL_HEIGHT = 440;

    private final int navy = NovaTheme.NAVY;
    private final int sidebar = NovaTheme.SIDEBAR;
    private final int card = NovaTheme.CARD;
    private final int hover = NovaTheme.HOVER;
    private final int blue = NovaTheme.BLUE;
    private final int cyan = NovaTheme.CYAN;
    private final int purple = NovaTheme.PURPLE;
    private final int muted = NovaTheme.MUTED;

    private NovaModule.Category selected = NovaModule.Category.PVP;
    private int left;
    private int top;

    public NovaScreen() {
        super(Component.literal("Nova Client"));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        left = Math.max(10, (width - PANEL_WIDTH) / 2);
        top = Math.max(10, (height - PANEL_HEIGHT) / 2);
        graphics.fill(0, 0, width, height, NovaTheme.BACKDROP);
        graphics.fill(left - 1, top - 1, left + PANEL_WIDTH + 1, top + PANEL_HEIGHT + 1, 0xFF263D5C);
        graphics.fill(left, top, left + PANEL_WIDTH, top + PANEL_HEIGHT, navy);
        graphics.fill(left, top, left + PANEL_WIDTH, top + 3, blue);
        graphics.fill(left + PANEL_WIDTH - 2, top + 3, left + PANEL_WIDTH, top + PANEL_HEIGHT, 0x5539B8FF);
        graphics.fill(left + 174, top + 3, left + 175, top + PANEL_HEIGHT - 2, NovaTheme.DIVIDER);
        renderSidebar(graphics);
        renderHeader(graphics);
        renderModuleCards(graphics, mouseX, mouseY);
        renderFooter(graphics);
        super.render(graphics, mouseX, mouseY, delta);
    }

    private void renderSidebar(GuiGraphics graphics) {
        graphics.fill(left, top + 3, left + 174, top + PANEL_HEIGHT, sidebar);
        graphics.fill(left + 18, top + 18, left + 21, top + 54, purple);
        graphics.drawString(font, "N O V A", left + 29, top + 22, cyan);
        graphics.drawString(font, "CLIENT 1.1.1", left + 29, top + 43, muted);
        graphics.fill(left + 18, top + 69, left + 156, top + 70, NovaTheme.DIVIDER);
        graphics.drawString(font, "WORKSPACE", left + 20, top + 90, 0xFF637C9C);

        int y = top + 112;
        for (NovaModule.Category category : NovaModule.Category.values()) {
            boolean active = selected == category;
            graphics.fill(left + 12, y - 6, left + 160, y + 21, active ? hover : sidebar);
            graphics.fill(left + 12, y - 6, left + 160, y - 5, active ? 0x6639B8FF : 0x00000000);
            if (active) {
                graphics.fill(left + 12, y - 6, left + 15, y + 21, blue);
                graphics.fill(left + 15, y + 20, left + 160, y + 21, 0x6639B8FF);
            }
            graphics.drawString(font, category.name(), left + 24, y + 2,
                active ? cyan : 0xFFD0DCEC);
            y += 39;
        }

        graphics.drawString(font, "ADVANCED EDITION", left + 20, top + 385, purple);
        graphics.drawString(font, "NOVA 1.1.1", left + 20, top + 411, muted);
    }

    private void renderHeader(GuiGraphics graphics) {
        graphics.drawString(font, "NOVA / MODULE WORKSPACE", left + 194, top + 13, purple);
        graphics.drawString(font, selected.name() + " MODULES", left + 194, top + 29, NovaTheme.TEXT);
        graphics.drawString(font, "Customise your Minecraft experience", left + 194, top + 48, muted);
        graphics.drawString(font, "RIGHT SHIFT  •  CLOSE", left + 545, top + 29, muted);
        graphics.fill(left + 190, top + 69, left + 710, top + 70, NovaTheme.DIVIDER);
    }

    private void renderModuleCards(GuiGraphics graphics, int mouseX, int mouseY) {
        List<NovaModule> modules = ModuleManager.getModules(selected);
        int startX = left + 190;
        int startY = top + 86;
        int cardWidth = 254;
        int cardHeight = 66;
        int gapX = 12;
        int gapY = 10;

        for (int i = 0; i < modules.size(); i++) {
            NovaModule module = modules.get(i);
            int column = i % 2;
            int row = i / 2;
            int x = startX + column * (cardWidth + gapX);
            int y = startY + row * (cardHeight + gapY);
            boolean isHovered = mouseX >= x && mouseX <= x + cardWidth
                && mouseY >= y && mouseY <= y + cardHeight;

            graphics.fill(x, y, x + cardWidth, y + cardHeight, isHovered ? hover : card);
            graphics.fill(x, y, x + cardWidth, y + 1, isHovered ? cyan : NovaTheme.DIVIDER);
            graphics.fill(x, y, x + 3, y + cardHeight, module.isEnabled() ? purple : blue);
            graphics.drawString(font, module.getName(), x + 12, y + 10, NovaTheme.TEXT);
            graphics.drawString(font, module.getDescription(), x + 12, y + 29, muted);
            graphics.drawString(font, "NOVA MODULE", x + 12, y + 49, 0xFF7189A8);
            int toggleColor = module.isEnabled() ? NovaTheme.TOGGLE_ON : NovaTheme.TOGGLE_OFF;
            graphics.fill(x + 189, y + 18, x + 239, y + 43, toggleColor);
            graphics.drawCenteredString(font, module.isEnabled() ? "ON" : "OFF",
                x + 214, y + 26, 0xFFFFFFFF);
        }

        if (modules.isEmpty()) {
            graphics.drawString(font, "No modules registered.", left + 205, top + 100, muted);
        }
    }

    private void renderFooter(GuiGraphics graphics) {
        graphics.fill(left + 190, top + 412, left + 710, top + 413, NovaTheme.DIVIDER);
        graphics.fill(left + 194, top + 426, left + 201, top + 433, 0xFF48D597);
        graphics.drawString(font, "NOVA CLIENT  |  MINECRAFT 1.21.11", left + 208, top + 426, muted);
        graphics.drawString(font, "ADVANCED", left + 640, top + 426, purple);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double mouseX = event.x();
        double mouseY = event.y();
        if (event.button() != 0) return super.mouseClicked(event, doubleClick);

        int navY = top + 112;
        for (NovaModule.Category category : NovaModule.Category.values()) {
            if (mouseX >= left + 12 && mouseX <= left + 160
                && mouseY >= navY - 6 && mouseY <= navY + 21) {
                selected = category;
                return true;
            }
            navY += 39;
        }

        List<NovaModule> modules = ModuleManager.getModules(selected);
        int cardWidth = 254;
        int cardHeight = 66;
        int gapX = 12;
        int gapY = 10;
        for (int i = 0; i < modules.size(); i++) {
            int column = i % 2;
            int row = i / 2;
            int x = left + 190 + column * (cardWidth + gapX);
            int y = top + 86 + row * (cardHeight + gapY);
            if (mouseX >= x && mouseX <= x + cardWidth && mouseY >= y && mouseY <= y + cardHeight) {
                modules.get(i).toggle();
                NovaConfig.save();
                return true;
            }
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public void onClose() {
        NovaConfig.save();
        Minecraft.getInstance().setScreen(null);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
