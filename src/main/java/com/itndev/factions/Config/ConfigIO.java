package com.itndev.factions.Config;

public class ConfigIO {

    public static void read() {
        FactionConfig.readConfig();
        Config.readConfig();
    }

    public static void save() {
        FactionConfig.saveConfig();
        Config.saveConfig();
    }
}
