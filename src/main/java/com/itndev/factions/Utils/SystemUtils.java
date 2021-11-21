package com.itndev.factions.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class SystemUtils {

    public static SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

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

    public static UUID Convert2UUID(String UUID2) {
        return UUID.fromString(UUID2);
    }

    @Deprecated
    public static void sendUUIDfactionmessage(String UUID, String Message) {
        OfflinePlayer op = Bukkit.getOfflinePlayer(UserInfoUtils.getPlayerName(UUID));
        if(op.isOnline()) {
            Player p = (Player) op;
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&o&l[ &r&f국가 &a&o&l] &r&f" + Message));
        }
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
            String[] skadi = coords.split("=");
            if(skadi.length == 6) {
                int a = Integer.valueOf(skadi[0]);
                int b = Integer.valueOf(skadi[1]);
                String c = skadi[2];
                double d = Double.valueOf(skadi[3]);
                double e = Double.valueOf(skadi[4]);
                double f = Double.valueOf(skadi[5]);
                Location loc = new Location(Bukkit.getWorld(c), d, e, f);
                loc.setPitch(a);
                loc.setYaw(b);
                return loc;
            }
        }
        return null;
    }

    public static String FactionUUIDToDate(String FactionUUID) {
        Long time = Long.valueOf(FactionUUID.split("=")[0]);
        return timeformat.format(new Date(time));
    }

    public static String Args2String(String[] args, int Start) {
        String FinalString = "";
        for(int k = Start; k < args.length; k++) {
            if(args[k] == null) {
                break;
            }
            if(k == args.length - 1) {
                FinalString = FinalString + args[k];
            } else {
                FinalString = FinalString + args[k] + " ";
            }

        }
        return FinalString;
    }



    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
