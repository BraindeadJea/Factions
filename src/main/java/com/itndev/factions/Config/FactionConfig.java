package com.itndev.factions.Config;

import com.itndev.factions.AdminCommands.AdminMainCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class FactionConfig {



    public static void saveConfig() {
        YamlConfig factionconf = new YamlConfig();
        factionconf.SetTargetName("FactionConfig");
        FileConfiguration conf = factionconf.getStorage();
        /*try {
            Location loc = AdminMainCommand.getCopyLocation();
            if(loc == null) {
                loc = new Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0);
            }
            conf.set("FactionOutPost.OustPostLocation.World", loc.getWorld().getName());
            conf.set("FactionOutPost.OustPostLocation.X", loc.getBlockX());
            conf.set("FactionOutPost.OustPostLocation.Y", loc.getBlockY());
            conf.set("FactionOutPost.OustPostLocation.Z", loc.getBlockZ());
        } catch (Exception e){
            e.printStackTrace();
        }*/
        factionconf.saveStorage();
    }

    public static void readConfig() {
        YamlConfig factionconf = new YamlConfig();
        factionconf.SetTargetName("FactionConfig");
        factionconf.CreateStorage();
        FileConfiguration conf = factionconf.getStorage();
        /*setDefaults(conf);
        String worldname = conf.getString("FactionOutPost.OustPostLocation.World");
        double X = conf.getDouble("FactionOutPost.OustPostLocation.X");
        double Y = conf.getDouble("FactionOutPost.OustPostLocation.Y");
        double Z = conf.getDouble("FactionOutPost.OustPostLocation.Z");
        Location loc = new Location(Bukkit.getWorld(worldname), X, Y, Z);
        AdminMainCommand.setCopyLocation(loc);*/
    }

    public static void setDefaults(FileConfiguration conf) {
        conf.addDefault("FactionOutPost.OustPostLocation.World", "world");
        conf.addDefault("FactionOutPost.OustPostLocation.X", 0.0);
        conf.addDefault("FactionOutPost.OustPostLocation.Y", 0.0);
        conf.addDefault("FactionOutPost.OustPostLocation.Z", 0.0);
        conf.options().copyDefaults(true);
    }
}
