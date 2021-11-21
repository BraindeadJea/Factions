package com.itndev.factions.Config;

import com.itndev.factions.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YamlConfig {

    public File file = null;
    public FileConfiguration customlocalstorage;
    public String FlatFileName = null;

    public void SetTargetName(String FlatFileName2) {
        FlatFileName = FlatFileName2;
    }

    public void CreateStorage() {
        //file = new File(main.getInstance().getDataFolder(), "LocalStorage.yml");
        file = new File(Main.getInstance().getDataFolder().getPath() + System.getProperty("file.separator") + "FlatFile", FlatFileName + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        customlocalstorage = YamlConfiguration.loadConfiguration(file);
    }

    public void resetlocalstorage() throws IOException {
        //file = new File(main.getInstance().getDataFolder(), "LocalStorage.yml");
        //file.delete();
        //file.createNewFile();
        customlocalstorage.getKeys(false).forEach(key ->{
            customlocalstorage.set(key, null);
        });
        saveStorage();
    }

    public void saveStorage() {

        try {
            customlocalstorage.save(file);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public FileConfiguration getStorage() {
        return customlocalstorage;
    }

    public void reloadStorage() {
        customlocalstorage = YamlConfiguration.loadConfiguration(file);
    }
}
