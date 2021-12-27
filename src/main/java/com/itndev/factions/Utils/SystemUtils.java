package com.itndev.factions.Utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.itndev.factions.Main;
import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.Style;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Unmodifiable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class SystemUtils {

    public static SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    @Deprecated
    public static void warning(String Message) {
        Bukkit.broadcast(Convert(colorize("&c&lERROR &7" + Message)));
    }

    public static void sendmessage(Player p, String Message) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Message));
    }

    public static void sendfactionmessage(Player p, String Message) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&o&l[ &r&f국가 &a&o&l] &r&f" + Message));
    }

    public static void sendrawfactionmessage(Player p, String Message) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&o&l[ &r&f국가 &a&o&l] &r&f") + Message);
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

    public static String getDate(Long timedata) {
        return timeformat.format(new Date(timedata));
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

    public static void SendtoServer(Player p, String ServerName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(ServerName);
        p.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static TextComponent Convert(String message) {
        TextComponent text = new TextComponent(message);
        return text;
    }
}
