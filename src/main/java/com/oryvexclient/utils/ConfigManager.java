package com.oryvexclient.utils;

import com.oryvexclient.utils.OryvexClient;
import com.oryvexclient.utils.modules.Module;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;

import java.io.*;

public class ConfigManager {

    private static final String CONFIG_FILE = "oryvexclient_config.json";
    private final File configFile;

    public ConfigManager() {
        configFile = new File(Minecraft.getMinecraft().mcDataDir, CONFIG_FILE);
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
            writer.write(root.toString());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
