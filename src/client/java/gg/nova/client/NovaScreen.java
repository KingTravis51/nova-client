package gg.nova.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.List;

public final class NovaScreen extends Screen {
    private static final int SIDEBAR_WIDTH = 164;
    private static final int CARD_HEIGHT = 76;
    private static final int GAP = 9;

    private NovaModule.Category selected = NovaModule.Category.PVP;
    private int left, top, panelWidth, panelHeight, contentX, contentWidth, cardWidth;

    public NovaScreen() { this(NovaModule.Category.PVP); }

    public NovaScreen(NovaModule.Category initialCategory) {
        super(Component.literal("Nova Client"));
        this.selected = initialCategory == null ? NovaModule.Category.PVP : initialCategory;
    }

    private void layout() {
        panelWidth = Math.min(1120, Math.max(700, width - 32));
        panelHeight = Math.min(690, Math.max(390, height - 32));
        left = (width - panelWidth) / 2;
        top = (height - panelHeight) / 2;
        contentX = left + SIDEBAR_WIDTH + 22;
        contentWidth = panelWidth - SIDEBAR_WIDTH - 44;
        cardWidth = (contentWidth - GAP * 2) / 3;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        layout();
        g.fill(0, 0, width, height, 0xC9071020);
        g.fill(left - 1, top - 1, left + panelWidth + 1, top + panelHeight + 1, 0xFF263D5C);
        g.fill(left, top, left + panelWidth, top + panelHeight, NovaTheme.NAVY);
        g.fill(left, top, left + panelWidth, top + 3, NovaTheme.BLUE);
        g.fill(left + SIDEBAR_WIDTH, top + 3, left + SIDEBAR_WIDTH + 1, top + panelHeight, NovaTheme.DIVIDER);
        renderSidebar(g);
        renderHeader(g);
        renderCards(g, mouseX, mouseY);
        renderFooter(g);
        super.render(g, mouseX, mouseY, delta);
    }

    private void renderSidebar(GuiGraphics g) {
        g.fill(left, top + 3, left + SIDEBAR_WIDTH, top + panelHeight, NovaTheme.SIDEBAR);
        g.fill(left + 18, top + 20, left + 21, top + 62, NovaTheme.BLUE);
        g.drawString(font, "N O V A", left + 31, top + 24, NovaTheme.CYAN);
        g.drawString(font, "CLIENT  /  1.1.1", left + 31, top + 44, NovaTheme.MUTED);
        g.fill(left + 17, top + 79, left + SIDEBAR_WIDTH - 17, top + 80, NovaTheme.DIVIDER);
        g.drawString(font, "WORKSPACE", left + 19, top + 96, 0xFF7189A8);

        int y = top + 119;
        for (NovaModule.Category category : NovaModule.Category.values()) {
            boolean active = selected == category;
            int h = 31;
            g.fill(left + 11, y - 6, left + SIDEBAR_WIDTH - 11, y + h - 6,
                active ? 0xFF193653 : NovaTheme.SIDEBAR);
            if (active) {
                g.fill(left + 11, y - 6, left + 14, y + h - 6, NovaTheme.BLUE);
                g.fill(left + 15, y + h - 7, left + SIDEBAR_WIDTH - 12, y + h - 6, 0x6639B8FF);
            }
            g.drawString(font, categoryLabel(category), left + 25, y + 3,
                active ? NovaTheme.CYAN : NovaTheme.TEXT);
            y += 39;
        }

        g.fill(left + 17, top + panelHeight - 58, left + SIDEBAR_WIDTH - 17,
            top + panelHeight - 57, NovaTheme.DIVIDER);
        g.drawString(font, "ADVANCED EDITION", left + 19, top + panelHeight - 43, NovaTheme.PURPLE);
        g.drawString(font, "PLAY SMARTER.", left + 19, top + panelHeight - 25, NovaTheme.MUTED);
    }

    private String categoryLabel(NovaModule.Category category) {
        return switch (category) {
            case PVP -> "PvP";
            case PERFORMANCE -> "Performance";
            case HUD -> "HUD Studio";
            case VISUALS -> "Visual Studio";
            case COSMETICS -> "Cosmetics";
            case UTILITY -> "Utilities";
        };
    }

    private void renderHeader(GuiGraphics g) {
        g.drawString(font, "NOVA  /  WORKSPACE", contentX, top + 17, NovaTheme.PURPLE);
        g.drawString(font, categoryLabel(selected).toUpperCase() + " MODULES",
            contentX, top + 36, NovaTheme.TEXT);
        g.drawString(font, "Personalise your Minecraft experience",
            contentX, top + 55, NovaTheme.MUTED);
        String count = ModuleManager.getModules(selected).size() + " FEATURES";
        g.drawString(font, count, left + panelWidth - 112, top + 37, NovaTheme.CYAN);
        g.fill(contentX, top + 78, left + panelWidth - 20, top + 79, NovaTheme.DIVIDER);
    }

    private void renderCards(GuiGraphics g, int mouseX, int mouseY) {
        List<NovaModule> modules = ModuleManager.getModules(selected);
        int startY = top + 94;
        int maxRows = Math.max(1, (panelHeight - 145) / (CARD_HEIGHT + GAP));
        for (int i = 0; i < modules.size(); i++) {
            int col = i % 3;
            int row = i / 3;
            if (row >= maxRows) break;
            int x = contentX + col * (cardWidth + GAP);
            int y = startY + row * (CARD_HEIGHT + GAP);
            NovaModule module = modules.get(i);
            boolean hovered = mouseX >= x && mouseX <= x + cardWidth
                && mouseY >= y && mouseY <= y + CARD_HEIGHT;
            int bg = hovered ? NovaTheme.HOVER : NovaTheme.CARD;
            g.fill(x - 1, y - 1, x + cardWidth + 1, y + CARD_HEIGHT + 1,
                hovered ? NovaTheme.CYAN : NovaTheme.DIVIDER);
            g.fill(x, y, x + cardWidth, y + CARD_HEIGHT, bg);
            g.fill(x, y, x + 3, y + CARD_HEIGHT, module.isEnabled() ? NovaTheme.PURPLE : NovaTheme.BLUE);
            g.drawString(font, module.getName(), x + 12, y + 11, NovaTheme.TEXT);
            drawWrappedDescription(g, module.getDescription(), x + 12, y + 31, cardWidth - 24);
            int toggleX = x + cardWidth - 49;
            int toggleY = y + CARD_HEIGHT - 27;
            g.fill(toggleX, toggleY, toggleX + 38, toggleY + 18,
                module.isEnabled() ? NovaTheme.TOGGLE_ON : NovaTheme.TOGGLE_OFF);
            g.fill(toggleX + (module.isEnabled() ? 22 : 3), toggleY + 3,
                toggleX + (module.isEnabled() ? 34 : 15), toggleY + 15, 0xFFFFFFFF);
            g.drawString(font, module.isEnabled() ? "ENABLED" : "DISABLED",
                x + 12, y + CARD_HEIGHT - 19, module.isEnabled() ? NovaTheme.CYAN : NovaTheme.MUTED);
        }

        if (modules.isEmpty()) {
            g.drawString(font, "No features in this category yet.", contentX, startY + 12, NovaTheme.MUTED);
        } else if (modules.size() > maxRows * 3) {
            g.drawString(font, "More features coming in the next workspace update.",
                contentX, top + panelHeight - 77, NovaTheme.MUTED);
        }
    }

    private void drawWrappedDescription(GuiGraphics g, String text, int x, int y, int maxWidth) {
        String remaining = text;
        if (font.width(remaining) <= maxWidth) {
            g.drawString(font, remaining, x, y, NovaTheme.MUTED);
            return;
        }
        while (!remaining.isEmpty() && font.width(remaining) > maxWidth) {
            int cut = remaining.length();
            while (cut > 1 && font.width(remaining.substring(0, cut)) > maxWidth) cut--;
            int space = remaining.lastIndexOf(' ', cut);
            if (space > 0) cut = space;
            g.drawString(font, remaining.substring(0, cut), x, y, NovaTheme.MUTED);
            remaining = remaining.substring(cut).trim();
            y += 11;
            if (y > top + panelHeight - 45) break;
        }
        if (!remaining.isEmpty()) g.drawString(font, remaining, x, y, NovaTheme.MUTED);
    }

    private void renderFooter(GuiGraphics g) {
        int y = top + panelHeight - 24;
        g.fill(contentX, y - 8, left + panelWidth - 20, y - 7, NovaTheme.DIVIDER);
        g.fill(contentX, y + 1, contentX + 7, y + 8, 0xFF48D597);
        g.drawString(font, "NOVA CLIENT  |  MINECRAFT 1.21.11",
            contentX + 14, y, NovaTheme.MUTED);
        g.drawString(font, "RIGHT SHIFT  •  CLOSE",
            left + panelWidth - 157, y, NovaTheme.PURPLE);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        layout();
        if (event.button() != 0) return super.mouseClicked(event, doubleClick);
        double mx = event.x(), my = event.y();

        int navY = top + 119;
        for (NovaModule.Category category : NovaModule.Category.values()) {
            if (mx >= left + 11 && mx <= left + SIDEBAR_WIDTH - 11
                && my >= navY - 6 && my <= navY + 25) {
                selected = category;
                return true;
            }
            navY += 39;
        }

        List<NovaModule> modules = ModuleManager.getModules(selected);
        int startY = top + 94;
        int maxRows = Math.max(1, (panelHeight - 145) / (CARD_HEIGHT + GAP));
        for (int i = 0; i < modules.size() && i / 3 < maxRows; i++) {
            int x = contentX + (i % 3) * (cardWidth + GAP);
            int y = startY + (i / 3) * (CARD_HEIGHT + GAP);
            if (mx >= x && mx <= x + cardWidth && my >= y && my <= y + CARD_HEIGHT) {
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
    public boolean isPauseScreen() { return false; }
}
