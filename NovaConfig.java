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

    private static final Gson GSON =
        new GsonBuilder().setPrettyPrinting().create();

    private static Path getFile() {
        return Minecraft.getInstance()
            .gameDirectory.toPath()
            .resolve("config")
            .resolve("nova-client.json");
    }

    private NovaConfig() {}

    public static void save() {
        try {
            Path file = getFile();
            Files.createDirectories(file.getParent());

            JsonObject root = new JsonObject();
            JsonObject modules = new JsonObject();

            for (NovaModule module : ModuleManager.getModules()) {
                modules.addProperty(
                    module.getName(),
                    module.isEnabled()
                );
            }

            root.add("modules", modules);

            try (Writer writer = Files.newBufferedWriter(file)) {
                GSON.toJson(root, writer);
            }

        } catch (Exception exception) {
            System.err.println("[Nova] Configuration save failed.");
            exception.printStackTrace();
        }
    }

    public static void load() {
        Path file = getFile();

        if (!Files.exists(file)) {
            save();
            return;
        }

        try (Reader reader = Files.newBufferedReader(file)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

            if (!root.has("modules")) {
                return;
            }

            JsonObject modules = root.getAsJsonObject("modules");

            for (NovaModule module : ModuleManager.getModules()) {
                if (modules.has(module.getName())) {
                    module.setEnabled(
                        modules.get(module.getName()).getAsBoolean()
                    );
                }
            }

        } catch (Exception exception) {
            System.err.println("[Nova] Configuration load failed.");
            exception.printStackTrace();
        }
    }
}