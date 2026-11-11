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

    private final int navy = 0xFF091326;
    private final int sidebar = 0xFF0D192D;
    private final int card = 0xFF142641;
    private final int hover = 0xFF20385B;
    private final int blue = 0xFF35AFFF;
    private final int cyan = 0xFF65DFFF;
    private final int purple = 0xFF9B7CFF;
    private final int muted = 0xFF91A6C0;

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
        graphics.fill(0, 0, width, height, 0xC9000816);
        graphics.fill(left, top, left + PANEL_WIDTH, top + PANEL_HEIGHT, navy);
        graphics.fill(left, top, left + PANEL_WIDTH, top + 3, blue);
        renderSidebar(graphics);
        renderHeader(graphics);
        renderModuleCards(graphics, mouseX, mouseY);
        renderFooter(graphics);
        super.render(graphics, mouseX, mouseY, delta);
    }

    private void renderSidebar(GuiGraphics graphics) {
        graphics.fill(left, top + 3, left + 174, top + PANEL_HEIGHT, sidebar);
        graphics.drawString(font, "NOVA", left + 22, top + 22, cyan);
        graphics.drawString(font, "CLIENT 1.1.0", left + 22, top + 43, muted);
        graphics.drawString(font, "WORKSPACE", left + 20, top + 90, 0xFF637C9C);

        int y = top + 112;
        for (NovaModule.Category category : NovaModule.Category.values()) {
            boolean active = selected == category;
            graphics.fill(left + 12, y - 6, left + 160, y + 21, active ? hover : sidebar);
            if (active) graphics.fill(left + 12, y - 6, left + 15, y + 21, blue);
            graphics.drawString(font, category.name(), left + 24, y + 2,
                active ? cyan : 0xFFD0DCEC);
            y += 39;
        }

        graphics.drawString(font, "ADVANCED EDITION", left + 20, top + 385, purple);
        graphics.drawString(font, "NOVA 1.1.0", left + 20, top + 411, muted);
    }

    private void renderHeader(GuiGraphics graphics) {
        graphics.drawString(font, selected.name() + " MODULES", left + 194, top + 24, 0xFFFFFFFF);
        graphics.drawString(font, "Customise your Minecraft experience", left + 194, top + 44, muted);
        graphics.drawString(font, "RIGHT SHIFT TO CLOSE", left + 545, top + 27, muted);
        graphics.fill(left + 190, top + 69, left + 710, top + 70, 0xFF263D5C);
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
            graphics.fill(x, y, x + 3, y + cardHeight, module.isEnabled() ? purple : blue);
            graphics.drawString(font, module.getName(), x + 12, y + 12, 0xFFFFFFFF);
            graphics.drawString(font, module.getDescription(), x + 12, y + 31, muted);
            int toggleColor = module.isEnabled() ? 0xFF176D9B : 0xFF35435A;
            graphics.fill(x + 189, y + 18, x + 239, y + 43, toggleColor);
            graphics.drawCenteredString(font, module.isEnabled() ? "ON" : "OFF",
                x + 214, y + 26, 0xFFFFFFFF);
        }

        if (modules.isEmpty()) {
            graphics.drawString(font, "No modules registered.", left + 205, top + 100, muted);
        }
    }

    private void renderFooter(GuiGraphics graphics) {
        graphics.fill(left + 190, top + 412, left + 710, top + 413, 0xFF263D5C);
        graphics.drawString(font, "NOVA CLIENT  |  MINECRAFT 1.21.11", left + 194, top + 426, muted);
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
