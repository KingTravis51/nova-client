package gg.nova.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class NovaConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String ACTIVE_PROFILE = "activeProfile";

    private static Path getFile() {
        return Minecraft.getInstance().gameDirectory.toPath()
            .resolve("config").resolve("nova-client.json");
    }

    private NovaConfig() {}

    public static void save() {
        try {
            Path file = getFile();
            Files.createDirectories(file.getParent());
            JsonObject root = Files.exists(file)
                ? JsonParser.parseString(Files.readString(file)).getAsJsonObject()
                : new JsonObject();
            root.add("modules", createModuleState());
            if (!root.has("profiles")) root.add("profiles", new JsonObject());
            try (Writer writer = Files.newBufferedWriter(file)) {
                GSON.toJson(root, writer);
            }
        } catch (Exception exception) {
            System.err.println("[Nova] Configuration save failed.");
            exception.printStackTrace();
        }
    }

    private static JsonObject createModuleState() {
        JsonObject modules = new JsonObject();
        for (NovaModule module : ModuleManager.getModules()) {
            JsonObject state = new JsonObject();
            state.addProperty("enabled", module.isEnabled());
            state.addProperty("intensity", module.getIntensity());
            state.addProperty("scale", module.getScale());
            state.addProperty("background", module.hasBackground());
            modules.add(module.getName(), state);
        }
        return modules;
    }

    public static void saveProfile(String name) {
        try {
            Path file = getFile();
            Files.createDirectories(file.getParent());
            JsonObject root = Files.exists(file)
                ? JsonParser.parseString(Files.readString(file)).getAsJsonObject()
                : new JsonObject();
            JsonObject profiles = root.has("profiles") ? root.getAsJsonObject("profiles") : new JsonObject();
            profiles.add(name, createModuleState());
            root.add("profiles", profiles);
            root.addProperty(ACTIVE_PROFILE, name);
            root.add("modules", createModuleState());
            try (Writer writer = Files.newBufferedWriter(file)) { GSON.toJson(root, writer); }
        } catch (Exception exception) {
            System.err.println("[Nova] Profile save failed.");
            exception.printStackTrace();
        }
    }

    public static boolean loadProfile(String name) {
        try {
            Path file = getFile();
            if (!Files.exists(file)) return false;
            JsonObject root = JsonParser.parseString(Files.readString(file)).getAsJsonObject();
            if (!root.has("profiles") || !root.getAsJsonObject("profiles").has(name)) return false;
            applyModuleState(root.getAsJsonObject("profiles").getAsJsonObject(name));
            root.add("modules", createModuleState());
            root.addProperty(ACTIVE_PROFILE, name);
            try (Writer writer = Files.newBufferedWriter(file)) { GSON.toJson(root, writer); }
            return true;
        } catch (Exception exception) {
            System.err.println("[Nova] Profile load failed.");
            exception.printStackTrace();
            return false;
        }
    }

    public static String getActiveProfile() {
        try {
            Path file = getFile();
            if (!Files.exists(file)) return "Default";
            JsonObject root = JsonParser.parseString(Files.readString(file)).getAsJsonObject();
            return root.has(ACTIVE_PROFILE) ? root.get(ACTIVE_PROFILE).getAsString() : "Default";
        } catch (Exception ignored) { return "Default"; }
    }

    public static void load() {
        Path file = getFile();
        if (!Files.exists(file)) { save(); return; }
        try (Reader reader = Files.newBufferedReader(file)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            if (root.has("modules")) applyModuleState(root.getAsJsonObject("modules"));
        } catch (Exception exception) {
            System.err.println("[Nova] Configuration load failed.");
            exception.printStackTrace();
        }
    }

    private static void applyModuleState(JsonObject modules) {
        for (NovaModule module : ModuleManager.getModules()) {
            if (!modules.has(module.getName())) continue;
            var element = modules.get(module.getName());
            if (element.isJsonPrimitive()) {
                module.setEnabled(element.getAsBoolean());
                continue;
            }
            JsonObject state = element.getAsJsonObject();
            if (state.has("enabled")) module.setEnabled(state.get("enabled").getAsBoolean());
            if (state.has("intensity")) module.setIntensity(state.get("intensity").getAsInt());
            if (state.has("scale")) module.setScale(state.get("scale").getAsInt());
            if (state.has("background")) module.setBackground(state.get("background").getAsBoolean());
        }
    }
}