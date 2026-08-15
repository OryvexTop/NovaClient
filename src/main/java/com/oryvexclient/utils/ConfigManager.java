package com.oryvexclient.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.oryvexclient.OryvexClient;
import com.oryvexclient.modules.Module;
import net.minecraft.client.Minecraft;

import java.io.*;

public class ConfigManager {
    private final File dir;
    private final File configFile;
    private final Gson gson;

    public ConfigManager() {
        dir = new File(Minecraft.getMinecraft().mcDataDir, "oryvexclient");
        if (!dir.exists()) dir.mkdirs();
        configFile = new File(dir, "config.json");
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void saveConfig() {
        try {
            JsonObject root = new JsonObject();
            JsonObject modulesJson = new JsonObject();
            for (Module module : OryvexClient.getInstance().getModuleManager().getModules()) {
                JsonObject moduleJson = new JsonObject();
                moduleJson.addProperty("toggled", module.isToggled());
                moduleJson.addProperty("keybind", module.getKeybind());
                modulesJson.add(module.getName(), moduleJson);
            }
            root.add("modules", modulesJson);

            FileWriter writer = new FileWriter(configFile);
            writer.write(gson.toJson(root));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        if (!configFile.exists()) {
            saveConfig();
            return;
        }
        try {
            FileReader reader = new FileReader(configFile);
            JsonObject root = new JsonParser().parse(reader).getAsJsonObject();
            if (root.has("modules")) {
                JsonObject modulesJson = root.getAsJsonObject("modules");
                for (Module module : OryvexClient.getInstance().getModuleManager().getModules()) {
                    if (modulesJson.has(module.getName())) {
                        JsonObject moduleJson = modulesJson.getAsJsonObject(module.getName());
                        if (moduleJson.has("toggled") && moduleJson.get("toggled").getAsBoolean()) {
                            module.setToggled(true);
                        }
                        if (moduleJson.has("keybind")) {
                            module.setKeybind(moduleJson.get("keybind").getAsInt());
                        }
                    }
                }
            }
            reader.close();
            OryvexClient.getInstance().getKeybindManager().updateBinds();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
