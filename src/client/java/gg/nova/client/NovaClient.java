package gg.nova.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public final class NovaClient implements ClientModInitializer {
    private static KeyMapping openNova;

    @Override
    public void onInitializeClient() {
        ModuleManager.registerDefaults();
        NovaConfig.load();
        NovaHud.register();

        openNova = KeyBindingHelper.registerKeyBinding(
            new KeyMapping("key.nova.open", InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT, KeyMapping.Category.MISC)
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openNova.consumeClick()) {
                if (client.screen instanceof NovaScreen) client.setScreen(null);
                else if (client.screen == null) client.setScreen(new NovaScreen());
            }
            NovaCosmetics.tick(client);
        });
    }
}
