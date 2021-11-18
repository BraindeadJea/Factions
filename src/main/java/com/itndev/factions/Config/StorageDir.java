package com.itndev.factions.Config;

import com.itndev.factions.Main;

import java.io.File;
import java.io.IOException;

public class StorageDir {

    public static File SubStorage = null;

    public static File MainConfig = null;

    public static void SetupStorage() {
        MainConfig = new File(Main.getInstance().getDataFolder(), "Config.yml");
        if(!MainConfig.exists()) {
            try {
                MainConfig.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SubStorage = new File(Main.getInstance().getDataFolder().getPath() + System.getProperty("file.separator") + "FlatFile", "test.yml");
        if(!SubStorage.exists()) {
            try {
                SubStorage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
