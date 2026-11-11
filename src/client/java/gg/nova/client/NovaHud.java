package gg.nova.client;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public final class NovaHud {
    private NovaHud() {}

    public static void register() {
        HudElementRegistry.addLast(
            ResourceLocation.fromNamespaceAndPath("nova", "client_hud"),
            NovaHud::render
        );
    }

    private static void render(net.minecraft.client.gui.GuiGraphics graphics,
                               net.minecraft.client.DeltaTracker tickCounter) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null || client.options.hideGui) return;

        int x = 8;
        int y = 8;
        int rows = 0;

        if (ModuleManager.isEnabled("FPS Counter")) {
            rows = drawRow(graphics, client, "FPS", Integer.toString(client.getFps()), x, y, rows);
        }
        if (ModuleManager.isEnabled("Ping Display") && client.getConnection() != null) {
            var info = client.getConnection().getPlayerInfo(client.player.getUUID());
            if (info != null) rows = drawRow(graphics, client, "PING", info.getLatency() + " ms", x, y, rows);
        }
        if (ModuleManager.isEnabled("Coordinates")) {
            String coords = client.player.getBlockX() + "  "
                + client.player.getBlockY() + "  " + client.player.getBlockZ();
            rows = drawRow(graphics, client, "XYZ", coords, x, y, rows);
        }
        if (ModuleManager.isEnabled("Direction")) {
            String[] directions = {"South", "West", "North", "East"};
            int index = Math.floorMod(Math.round(client.player.getYRot() / 90.0f), 4);
            drawRow(graphics, client, "FACING", directions[index], x, y, rows);
        }
    }

    private static int drawRow(net.minecraft.client.gui.GuiGraphics graphics,
                               Minecraft client, String label, String value,
                               int x, int y, int row) {
        int top = y + row * 18;
        int width = Math.max(104, client.font.width(label + "  " + value) + 16);
        graphics.fill(x, top, x + width, top + 16, 0xB8071225);
        graphics.fill(x, top, x + 2, top + 16, NovaTheme.BLUE);
        graphics.drawString(client.font, label, x + 8, top + 4, NovaTheme.CYAN, false);
        graphics.drawString(client.font, value, x + 48, top + 4, NovaTheme.TEXT, false);
        return row + 1;
    }
}
