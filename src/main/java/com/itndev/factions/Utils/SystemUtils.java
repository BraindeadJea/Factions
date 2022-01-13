package com.itndev.factions.Utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.itndev.factions.Config.Config;
import com.itndev.factions.FactionCommands.FactionsCommands.FactionChatToggle;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import io.papermc.paper.text.PaperComponents;
import me.leoko.advancedban.Universal;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.Style;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.graalvm.compiler.lir.alloc.lsra.LinearScan;
import org.jetbrains.annotations.Unmodifiable;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static void SendErrorMessage(String Error, Player p) {
        if(p != null) {
            SystemUtils.sendmessage(p, "&c&lERROR &r&7오류가 발생한듯 합니다. 커뮤니티에 오류를 제보해주시기 바랍니다\n" +
                    "&r&c&lINFO " + "&f&l오류 ID &r&8&l: &r&7" + Error + "\n" +
                    "&r&c&lINFO " + "&f&l디스코드 &r&8&l: &r&7" + Config.DiscordLink + "\n");
        }
        System.out.println("[ERROR] 오류 발생 (대상유저:" + getPlayerName(p) + "),(오류ID:" + Error + ")");
    }

    public static String getPlayerName(Player p) {
        if(p == null) {
            return "null";
        } else {
            return p.getName();
        }
    }

    public static TextComponent Convert(String message) {
        TextComponent text = new TextComponent(message);
        return text;
    }

    public static Location ReplaceChunk(Location From, Location To) {
        ChunkSnapshot SnapShot = From.getChunk().getChunkSnapshot();
        int height = 0;
        To.getChunk().getBlock(8, height, 8).getLocation();
        Random random = new Random();
        int c = random.nextInt(8) + 1;
        int amount = 0;

        for(int x = 0; x <= 15; x++) {
            for(int z = 0; z <= 15; z++) {
                for(int y = 1; y <= 255; y++) {
                    BlockData block = SnapShot.getBlockData(x, y, z);
                    Boolean cu = false;
                    if(block.getMaterial() == Material.BEACON) {
                        amount = amount++;
                        if(amount == c) {
                            cu = true;
                        }
                    }
                    To.getChunk().getBlock(x, y, z).setBlockData(block);
                    if(cu) {
                        return To.getChunk().getBlock(x, y, z).getLocation();
                    }
                }
            }
        }
        return null;
    }

    public static void RemoveHeldItem(Player p, int Amount) {
        ItemStack item = p.getInventory().getItemInMainHand();
        item.setAmount(item.getAmount() - Amount);
    }

    public static HashMap<String, Long> chatevent = new HashMap<>();

    public void MainChatProcced(Player p, String UUID, String k) {
        new Thread( () -> {
            if(!p.hasPermission("faxcore.chatbypass")) {
                if (FactionChatToggle.FactionChatToggled.containsKey(p.getName())) {
                    int cooldownTime = 2;
                    long secondsLeft = ((Long) chatevent.get(p.getName())).longValue() / 1000L + cooldownTime - System.currentTimeMillis() / 1000L;
                    if (secondsLeft > 0L) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l(!) &r&f채팅은 2초마다 한번 칠수 있습니다."));
                        return;
                    }
                }
                chatevent.put(p.getName(), Long.valueOf(System.currentTimeMillis()));
            }
            if(k.startsWith("@")) {
                if(FactionUtils.isInFaction(UUID)) {
                    JedisTempStorage.AddCommandToQueueFix("chat" + ":=:" + p.getUniqueId().toString() + ":=:" + k.replaceFirst("@", ""), "chat" + ":=:" + p.getUniqueId().toString() + ":=:" + k.replaceFirst("@", ""));
                } else {
                    SystemUtils.sendmessage(p, "&c&l(!) &7소속된 팀이 없습니다");
                }
            } else if(FactionChatToggle.FactionChatToggled.containsKey(p)) {
                if(FactionUtils.isInFaction(UUID)) {
                    JedisTempStorage.AddCommandToQueueFix("chat" + ":=:" + p.getUniqueId().toString() + ":=:" + k, "chat" + ":=:" + p.getUniqueId().toString() + ":=:" + k);
                } else {
                    SystemUtils.sendmessage(p, "&c&l(!) &7소속된 팀이 없어서 팀 채팅에서 강제적으로 퇴장당했습니다");
                    FactionChatToggle.FactionChatToggled.remove(p);
                }
            } else if(!p.hasPermission("faxcore.chatbypass") && k.length() > 64){
                SystemUtils.sendmessage(p, "&c&l(!) &f&l메시지가 너무 깁니다. 64자 미만으로 줄여주세요");
            } else {
                try {
                    if (Universal.get().getMethods().callChat(p)) {
                        return;
                    }
                } catch (NullPointerException nulle) {
                    System.out.println(nulle.getMessage());
                }
                if(Main.chattoggle.equals(true) && !p.hasPermission("faxcore.chatmutebypass")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l(!) &f현재 채팅이 잠겨있습니다"));
                    return;
                }
                Player player = p;
                String group = loadUser(player).getPrimaryGroup();
                String message = k;
                String format = (String) Objects.<String>requireNonNull("%itndevfactions_formatfactionname%%itndevfaction_formatrank%{prefix}{name} {suffix}&8:: &r{message}".replace("{world}", player.getWorld().getName()).replace("{prefix}", getPrefix(player)).replace("{prefixes}", getPrefixes(player)).replace("{name}", player.getName()).replace("{suffix}", getSuffix(player)).replace("{suffixes}", getSuffixes(player)).replace("{username-color}", (playerMeta(player).getMetaValue("username-color") != null) ? playerMeta(player).getMetaValue("username-color") : ((groupMeta(group).getMetaValue("username-color") != null) ? groupMeta(group).getMetaValue("username-color") : "")).replace("{message-color}", (playerMeta(player).getMetaValue("message-color") != null) ?
                        playerMeta(player).getMetaValue("message-color") : ((groupMeta(group).getMetaValue("message-color") != null) ?
                        groupMeta(group).getMetaValue("message-color") : "")));
                ArrayList<Player> closeplayer = new ArrayList<>();
                for(Player online : Bukkit.getOnlinePlayers()) {
                    if(online.getLocation().getWorld().getName().equals(p.getLocation().getWorld().getName()) && online.getLocation().distanceSquared(p.getLocation()) <= 300 * 300) {
                        closeplayer.add(online);
                    }
                }
                String tagonfront = "&7[ " + String.valueOf(closeplayer.size()) + "명 ]&r";
                String Dformat = colorize(tagonfront) + colorize(format.replace("{message}", (player.hasPermission("lpc.colorcodes") && player.hasPermission("lpc.rgbcodes")) ?
                        translateHexColorCodes(colorize(message)) : (player.hasPermission("lpc.colorcodes") ? colorize(message) : (player.hasPermission("lpc.rgbcodes") ?
                        translateHexColorCodes(message) : message))).replace("%itndevfactions_formatfactionname%",  " " + FactionUtils.getFormattedFaction(UUID) + " ").replace("%itndevfaction_formatrank%", FactionUtils.getFormattedRank(UUID) + " "));
                for(Player finalonlinecloseplayers : closeplayer) {
                    finalonlinecloseplayers.sendMessage(Dformat);
                }
            }
        }).start();
    }

    private String translateHexColorCodes(String message) {
        Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
        char colorChar = '§';
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 32);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, "§x§" + group
                    .charAt(0) + '§' + group.charAt(1) + '§' + group
                    .charAt(2) + '§' + group.charAt(3) + '§' + group
                    .charAt(4) + '§' + group.charAt(5));
        }
        return matcher.appendTail(buffer).toString();
    }

    private String getPrefix(Player player) {
        String prefix = playerMeta(player).getPrefix();
        return (prefix != null) ? prefix : "";
    }

    private String getSuffix(Player player) {
        String suffix = playerMeta(player).getSuffix();
        return (suffix != null) ? suffix : "";
    }

    private String getPrefixes(Player player) {
        SortedMap<Integer, String> map = playerMeta(player).getPrefixes();
        StringBuilder prefixes = new StringBuilder();
        for (String prefix : map.values())
            prefixes.append(prefix);
        return prefixes.toString();
    }

    private String getSuffixes(Player player) {
        SortedMap<Integer, String> map = playerMeta(player).getSuffixes();
        StringBuilder suffixes = new StringBuilder();
        for (String prefix : map.values())
            suffixes.append(prefix);
        return suffixes.toString();
    }

    private CachedMetaData playerMeta(Player player) {
        return loadUser(player).getCachedData().getMetaData(getApi().getContextManager().getQueryOptions(player));
    }

    private CachedMetaData groupMeta(String group) {
        return loadGroup(group).getCachedData().getMetaData(getApi().getContextManager().getStaticQueryOptions());
    }

    private User loadUser(Player player) {
        if (!player.isOnline())
            throw new IllegalStateException("Player is offline!");
        return getApi().getUserManager().getUser(player.getUniqueId());
    }

    private Group loadGroup(String group) {
        return getApi().getGroupManager().getGroup(group);
    }

    private LuckPerms getApi() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServer().getServicesManager().getRegistration(LuckPerms.class);
        Validate.notNull(provider);
        return (LuckPerms)provider.getProvider();
    }

    private boolean isPlaceholderAPIEnabled() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }
}
