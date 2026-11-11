package gg.nova.client;

import java.util.ArrayList;
import java.util.List;

public final class ModuleManager {
    private static final List<NovaModule> MODULES = new ArrayList<>();

    private ModuleManager() {}

    public static void registerDefaults() {
        MODULES.clear();

        // PvP quality-of-life and visual controls. No combat automation.
        register("Crystal Optimiser", "Reduce distracting crystal visuals", NovaModule.Category.PVP);
        register("Anchor Optimiser", "Tune anchor visual effects", NovaModule.Category.PVP);
        register("Totem Optimiser", "Keep totem effects lightweight", NovaModule.Category.PVP);
        register("No Totem Particles", "Hide distracting totem particles", NovaModule.Category.PVP);
        register("Explosion Optimiser", "Reduce explosion visual clutter", NovaModule.Category.PVP);
        register("Item Optimiser", "Simplify selected item visuals", NovaModule.Category.PVP);
        register("Low Fire", "Reduce the on-screen fire overlay", NovaModule.Category.PVP);
        register("Clear Water", "Improve underwater visibility", NovaModule.Category.PVP);

        // Performance preferences.
        register("Particle Reducer", "Reduce ambient particle clutter", NovaModule.Category.PERFORMANCE);
        register("Weather Optimiser", "Reduce weather visual intensity", NovaModule.Category.PERFORMANCE);
        register("Entity Shadows", "Toggle entity shadow rendering", NovaModule.Category.PERFORMANCE);
        register("Minimal Explosions", "Use lighter explosion visuals", NovaModule.Category.PERFORMANCE);
        register("Dynamic FPS Display", "Show live frame-rate information", NovaModule.Category.PERFORMANCE);

        // HUD.
        register("Armour HUD", "Display equipped armour", NovaModule.Category.HUD);
        register("Potion HUD", "Display active potion effects", NovaModule.Category.HUD);
        register("Ping Display", "Display connection latency", NovaModule.Category.HUD);
        register("Keystrokes", "Show movement and mouse inputs", NovaModule.Category.HUD);
        register("CPS Counter", "Display clicks per second", NovaModule.Category.HUD);
        register("Custom Crosshair", "Use Nova crosshair styling", NovaModule.Category.HUD);
        register("Coordinates", "Display your current coordinates", NovaModule.Category.HUD);
        register("FPS Counter", "Display current frames per second", NovaModule.Category.HUD);

        // Visuals.
        register("Crystal Highlights", "Add a Nova crystal accent to menus", NovaModule.Category.VISUALS);
        register("Hit Particles", "Customise client-side hit particles", NovaModule.Category.VISUALS);
        register("Damage Tint", "Subtle damage feedback tint", NovaModule.Category.VISUALS);
        register("Sky Accents", "Enable supported Nova sky accents", NovaModule.Category.VISUALS);
        register("Minimal Totem Pop", "Use a cleaner totem pop visual", NovaModule.Category.VISUALS);

        // Third-person cosmetic particle effects.
        register("Crystal Wings", "Animated crystal wings around your player", NovaModule.Category.COSMETICS);
        register("Energy Wings", "Glowing energy wing particle effect", NovaModule.Category.COSMETICS);
        register("Crystal Aura", "Orbiting crystals around your player", NovaModule.Category.COSMETICS);
        register("Portal Aura", "A soft purple portal aura", NovaModule.Category.COSMETICS);
        register("Starlight Trail", "Leave a sparkling trail as you move", NovaModule.Category.COSMETICS);
        register("Ender Trail", "Leave a drifting portal trail", NovaModule.Category.COSMETICS);

        // Utility and information.
        register("Surround Detector", "Visual cue for nearby surround blocks", NovaModule.Category.UTILITY);
        register("Damage Calculator", "Combat damage information display", NovaModule.Category.UTILITY);
        register("Crystal Counter", "Count crystals in your inventory", NovaModule.Category.UTILITY);
        register("Totem Counter", "Count totems in your inventory", NovaModule.Category.UTILITY);
        register("Anchor Counter", "Count respawn anchors in your inventory", NovaModule.Category.UTILITY);
        register("Obsidian Counter", "Count obsidian in your inventory", NovaModule.Category.UTILITY);
        register("Glowstone Counter", "Count glowstone in your inventory", NovaModule.Category.UTILITY);
        register("Ender Pearl Counter", "Count ender pearls in your inventory", NovaModule.Category.UTILITY);
    }

    private static void register(String name, String description, NovaModule.Category category) {
        MODULES.add(new NovaModule(name, description, category));
    }

    public static List<NovaModule> getModules() { return List.copyOf(MODULES); }

    public static List<NovaModule> getModules(NovaModule.Category category) {
        return MODULES.stream().filter(module -> module.getCategory() == category).toList();
    }

    public static boolean isEnabled(String name) {
        return MODULES.stream().anyMatch(module -> module.getName().equals(name) && module.isEnabled());
    }
}
