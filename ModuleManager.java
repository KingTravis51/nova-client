package gg.nova.client;

import java.util.ArrayList;
import net.minecraft.client.input.MouseButtonEvent;
import java.util.List;

public final class ModuleManager {

    private static final List<NovaModule> MODULES = new ArrayList<>();

    private ModuleManager() {}

    public static void registerDefaults() {
        MODULES.clear();

        // CRYSTAL PVP
        register("Crystal Optimiser", "Crystal PvP optimisation settings", NovaModule.Category.PVP);
        register("Anchor Optimiser", "Anchor PvP optimisation settings", NovaModule.Category.PVP);
        register("Totem Optimiser", "Totem visual optimisation settings", NovaModule.Category.PVP);
        register("No Totem Particles", "Totem particle display option", NovaModule.Category.PVP);
        register("Explosion Optimiser", "Explosion effect settings", NovaModule.Category.PVP);
        register("Item Optimiser", "PvP item rendering settings", NovaModule.Category.PVP);

        // HUD
        register("Armour HUD", "Armour display settings", NovaModule.Category.HUD);
        register("Potion HUD", "Potion effect display settings", NovaModule.Category.HUD);
        register("Hole HUD", "Hole information display settings", NovaModule.Category.HUD);
        register("Ping Display", "Connection latency display settings", NovaModule.Category.HUD);
        register("Keystrokes", "Movement key display settings", NovaModule.Category.HUD);
        register("CPS Counter", "Clicks per second display settings", NovaModule.Category.HUD);
        register("Custom Crosshair", "Crosshair appearance settings", NovaModule.Category.HUD);

        // INFORMATION
        register("Surround Detector", "Surround information settings", NovaModule.Category.UTILITY);
        register("Damage Calculator", "Combat damage information settings", NovaModule.Category.UTILITY);
        register("Crystal Counter", "Crystal inventory counter", NovaModule.Category.UTILITY);
        register("Totem Counter", "Totem inventory counter", NovaModule.Category.UTILITY);
        register("Anchor Counter", "Anchor inventory counter", NovaModule.Category.UTILITY);
        register("Obsidian Counter", "Obsidian inventory counter", NovaModule.Category.UTILITY);
        register("Glowstone Counter", "Glowstone inventory counter", NovaModule.Category.UTILITY);
        register("Ender Pearl Counter", "Ender pearl inventory counter", NovaModule.Category.UTILITY);
    }

    private static void register(
        String name,
        String description,
        NovaModule.Category category
    ) {
        MODULES.add(new NovaModule(name, description, category));
    }

    public static List<NovaModule> getModules() {
        return List.copyOf(MODULES);
    }

    public static List<NovaModule> getModules(NovaModule.Category category) {
        return MODULES.stream()
            .filter(module -> module.getCategory() == category)
            .toList();
    }
}