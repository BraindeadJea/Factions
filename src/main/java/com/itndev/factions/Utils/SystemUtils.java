package com.itndev.factions.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class SystemUtils {

    @Deprecated
    public static void warning(String Message) {
        Bukkit.broadcastMessage(colorize("&c&lERROR &7" + Message));
    }

    public static void sendmessage(Player p, String Message) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Message));
    }

    public static void sendfactionmessage(Player p, String Message) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&o&l[ &r&f국가 &a&o&l] &r&f" + Message));
    }

    @Deprecated
    public static void sendUUIDmessage(String UUID, String Message) {
        OfflinePlayer op = Bukkit.getOfflinePlayer(UserInfoUtils.getPlayerName(UUID));
        if(op.isOnline()) {
            Player p = (Player) op;
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Message));
        }
    }

    public static String loc2string(Location loc) {
        String a = String.valueOf(Math.round(loc.getPitch()));
        String b = String.valueOf(Math.round(loc.getYaw()));
        String c = String.valueOf(loc.getWorld().getName());
        String d = String.valueOf(loc.getX());
        String e = String.valueOf(loc.getY());
        String f = String.valueOf(loc.getZ());
        return a + "=" + b + "=" + c + "=" + d + "=" + e + "=" + f;
    }

    public static Location string2loc(String coords) {
        if(coords.contains("=")) {
            String[] skadis = coords.split("=");
            if(skadis.length == 6) {
                int a = Integer.valueOf(skadis[0]);
                int b = Integer.valueOf(skadis[1]);
                String c = skadis[2];
                double d = Double.valueOf(skadis[3]);
                double e = Double.valueOf(skadis[4]);
                double f = Double.valueOf(skadis[5]);
                Location loc = new Location(Bukkit.getWorld(c), d, e, f);
                loc.setPitch(a);
                loc.setYaw(b);
                return loc;
            }
        }
        return null;
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
