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
    private NovaModule settingsModule;
    private int left, top, panelWidth, panelHeight, contentX, contentWidth, cardWidth;
    private int profileButtonX, profileButtonY;

    public NovaScreen() { this(NovaModule.Category.PVP); }
    public NovaScreen(NovaModule.Category initialCategory) {
        super(Component.literal("Nova Client"));
        selected = initialCategory == null ? NovaModule.Category.PVP : initialCategory;
    }

    private void layout() {
        panelWidth = Math.min(1120, Math.max(700, width - 32));
        panelHeight = Math.min(690, Math.max(390, height - 32));
        left = (width - panelWidth) / 2; top = (height - panelHeight) / 2;
        contentX = left + SIDEBAR_WIDTH + 22;
        contentWidth = panelWidth - SIDEBAR_WIDTH - 44;
        cardWidth = (contentWidth - GAP * 2) / 3;
        profileButtonX = left + panelWidth - 128;
        profileButtonY = top + 48;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        layout();
        g.fill(0, 0, width, height, 0xC9071020);
        g.fill(left - 1, top - 1, left + panelWidth + 1, top + panelHeight + 1, NovaTheme.DIVIDER);
        g.fill(left, top, left + panelWidth, top + panelHeight, NovaTheme.NAVY);
        g.fill(left, top, left + panelWidth, top + 3, NovaTheme.BLUE);
        g.fill(left + SIDEBAR_WIDTH, top + 3, left + SIDEBAR_WIDTH + 1, top + panelHeight, NovaTheme.DIVIDER);
        renderSidebar(g); renderHeader(g); renderCards(g, mouseX, mouseY); renderFooter(g);
        if (settingsModule != null) renderSettings(g, mouseX, mouseY);
        super.render(g, mouseX, mouseY, delta);
    }

    private void renderSidebar(GuiGraphics g) {
        g.fill(left, top + 3, left + SIDEBAR_WIDTH, top + panelHeight, NovaTheme.SIDEBAR);
        g.fill(left + 18, top + 20, left + 21, top + 62, NovaTheme.BLUE);
        g.drawString(font, "N O V A", left + 31, top + 24, NovaTheme.CYAN);
        g.drawString(font, "CLIENT  /  1.2.0", left + 31, top + 44, NovaTheme.MUTED);
        g.fill(left + 17, top + 79, left + SIDEBAR_WIDTH - 17, top + 80, NovaTheme.DIVIDER);
        g.drawString(font, "WORKSPACE", left + 19, top + 96, 0xFF7189A8);
        int y = top + 119;
        for (NovaModule.Category category : NovaModule.Category.values()) {
            boolean active = selected == category;
            g.fill(left + 11, y - 6, left + SIDEBAR_WIDTH - 11, y + 25,
                active ? 0xFF193653 : NovaTheme.SIDEBAR);
            if (active) g.fill(left + 11, y - 6, left + 14, y + 25, NovaTheme.BLUE);
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
            case PVP -> "PvP"; case PERFORMANCE -> "Performance"; case HUD -> "HUD Studio";
            case VISUALS -> "Visual Studio"; case COSMETICS -> "Cosmetics"; case UTILITY -> "Utilities";
        };
    }

    private void renderHeader(GuiGraphics g) {
        g.drawString(font, "NOVA  /  WORKSPACE", contentX, top + 17, NovaTheme.PURPLE);
        g.drawString(font, categoryLabel(selected).toUpperCase() + " MODULES", contentX, top + 36, NovaTheme.TEXT);
        g.drawString(font, "Left click: toggle     Right click: module settings", contentX, top + 55, NovaTheme.MUTED);
        g.fill(profileButtonX, profileButtonY, profileButtonX + 108, profileButtonY + 21, 0xFF193653);
        g.drawString(font, "SAVE PROFILE", profileButtonX + 9, profileButtonY + 7, NovaTheme.CYAN);
        g.drawString(font, "PROFILE: " + NovaConfig.getActiveProfile(), contentX, top + 70, NovaTheme.PURPLE);
        g.fill(contentX, top + 84, left + panelWidth - 20, top + 85, NovaTheme.DIVIDER);
    }

    private void renderCards(GuiGraphics g, int mouseX, int mouseY) {
        List<NovaModule> modules = ModuleManager.getModules(selected);
        int startY = top + 98;
        int maxRows = Math.max(1, (panelHeight - 149) / (CARD_HEIGHT + GAP));
        for (int i = 0; i < modules.size() && i / 3 < maxRows; i++) {
            int x = contentX + (i % 3) * (cardWidth + GAP);
            int y = startY + (i / 3) * (CARD_HEIGHT + GAP);
            NovaModule module = modules.get(i);
            boolean hovered = mouseX >= x && mouseX <= x + cardWidth && mouseY >= y && mouseY <= y + CARD_HEIGHT;
            g.fill(x - 1, y - 1, x + cardWidth + 1, y + CARD_HEIGHT + 1, hovered ? NovaTheme.CYAN : NovaTheme.DIVIDER);
            g.fill(x, y, x + cardWidth, y + CARD_HEIGHT, hovered ? NovaTheme.HOVER : NovaTheme.CARD);
            g.fill(x, y, x + 3, y + CARD_HEIGHT, module.isEnabled() ? NovaTheme.PURPLE : NovaTheme.BLUE);
            g.drawString(font, module.getName(), x + 12, y + 11, NovaTheme.TEXT);
            g.drawString(font, module.getDescription(), x + 12, y + 31, NovaTheme.MUTED);
            g.drawString(font, module.isEnabled() ? "ENABLED" : "DISABLED", x + 12, y + 54,
                module.isEnabled() ? NovaTheme.CYAN : NovaTheme.MUTED);
            g.drawString(font, "⚙", x + cardWidth - 18, y + 11, NovaTheme.PURPLE);
        }
        if (modules.isEmpty()) g.drawString(font, "No features in this category yet.", contentX, startY + 12, NovaTheme.MUTED);
    }

    private void renderSettings(GuiGraphics g, int mouseX, int mouseY) {
        int w = 310, h = 174, x = left + (panelWidth - w) / 2, y = top + (panelHeight - h) / 2;
        g.fill(x - 2, y - 2, x + w + 2, y + h + 2, NovaTheme.CYAN);
        g.fill(x, y, x + w, y + h, NovaTheme.SIDEBAR);
        g.drawString(font, "MODULE SETTINGS", x + 16, y + 15, NovaTheme.PURPLE);
        g.drawString(font, settingsModule.getName(), x + 16, y + 34, NovaTheme.TEXT);
        g.drawString(font, "Intensity: " + settingsModule.getIntensity() + "%  (click to change)", x + 16, y + 62, NovaTheme.CYAN);
        g.drawString(font, "Scale: " + settingsModule.getScale() + "%  (click to change)", x + 16, y + 88, NovaTheme.CYAN);
        g.drawString(font, "Background: " + (settingsModule.hasBackground() ? "ON" : "OFF") + "  (click)", x + 16, y + 114, NovaTheme.CYAN);
        g.drawString(font, "DONE", x + w - 54, y + h - 23, NovaTheme.TEXT);
    }

    private void renderFooter(GuiGraphics g) {
        int y = top + panelHeight - 24;
        g.fill(contentX, y - 8, left + panelWidth - 20, y - 7, NovaTheme.DIVIDER);
        g.fill(contentX, y + 1, contentX + 7, y + 8, 0xFF48D597);
        g.drawString(font, "NOVA CLIENT  |  MINECRAFT 1.21.11", contentX + 14, y, NovaTheme.MUTED);
        g.drawString(font, "RIGHT SHIFT  •  CLOSE", left + panelWidth - 157, y, NovaTheme.PURPLE);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        layout();
        double mx = event.x(), my = event.y();
        if (event.button() == 0 && mx >= profileButtonX && mx <= profileButtonX + 108
            && my >= profileButtonY && my <= profileButtonY + 21) {
            NovaConfig.saveProfile("Profile 1");
            return true;
        }
        if (settingsModule != null) {
            int w = 310, h = 174, x = left + (panelWidth - w) / 2, y = top + (panelHeight - h) / 2;
            if (mx >= x && mx <= x + w && my >= y && my <= y + h) {
                if (my < y + 80) settingsModule.cycleIntensity();
                else if (my < y + 108) settingsModule.cycleScale();
                else if (my < y + 140) settingsModule.setBackground(!settingsModule.hasBackground());
                else settingsModule = null;
                NovaConfig.save();
                return true;
            }
            settingsModule = null;
            return true;
        }
        if (event.button() == 0) {
            int navY = top + 119;
            for (NovaModule.Category category : NovaModule.Category.values()) {
                if (mx >= left + 11 && mx <= left + SIDEBAR_WIDTH - 11 && my >= navY - 6 && my <= navY + 25) {
                    selected = category; return true;
                }
                navY += 39;
            }
        }
        List<NovaModule> modules = ModuleManager.getModules(selected);
        int startY = top + 98;
        int maxRows = Math.max(1, (panelHeight - 149) / (CARD_HEIGHT + GAP));
        for (int i = 0; i < modules.size() && i / 3 < maxRows; i++) {
            int x = contentX + (i % 3) * (cardWidth + GAP), y = startY + (i / 3) * (CARD_HEIGHT + GAP);
            if (mx >= x && mx <= x + cardWidth && my >= y && my <= y + CARD_HEIGHT) {
                NovaModule module = modules.get(i);
                if (event.button() == 1) settingsModule = module;
                else if (event.button() == 0) module.toggle();
                NovaConfig.save();
                return true;
            }
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override public void onClose() { NovaConfig.save(); Minecraft.getInstance().setScreen(null); }
    @Override public boolean isPauseScreen() { return false; }
}