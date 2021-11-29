package com.itndev.factions.Listener;

import com.itndev.factions.Commands.FactionsCommands.FactionChatToggle;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import com.itndev.factions.Storage.TempStorage;
import com.itndev.factions.Storage.UserInfoStorage;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.TeleportInvisFix;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.leoko.advancedban.Universal;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.leoko.advancedban.Universal;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Door;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.SortedMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerListener implements Listener {

    public static HashMap<String, Location> onJoinWarp = new HashMap<>();

    public static HashMap<String, Long> chatevent = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent e) {
        new Thread( () -> {
            Location from = e.getFrom();
            Location to = e.getTo();
            if(from.getZ() != to.getZ()  ||  from.getX() != to.getX()  ||  from.getY() != to.getY()) {
                TempStorage.TeleportLocation.remove(e.getPlayer().getUniqueId().toString());
                if(!from.getChunk().equals(to.getChunk())) {
                    if(!FactionUtils.isSameClaimFaction(from, to)) {
                        String fromname = FactionUtils.GetClaimFaction(from);
                        String toname = FactionUtils.GetClaimFaction(to);
                        SystemUtils.sendfactionmessage(e.getPlayer(), "&r&f이동 &7: &r" + fromname + " &8->&r&f " + toname);
                    }
                }
            }
        }).start();
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if(!p.getGameMode().equals(GameMode.CREATIVE)) {
            Location loc = e.getBlock().getLocation();
            String UUID = p.getUniqueId().toString();
            if(FactionUtils.isClaimed(loc)) {
                if(!FactionUtils.getPlayerFactionUUID(UUID).equalsIgnoreCase(FactionUtils.AsyncWhosClaim(loc))) {
                    e.setCancelled(true);
                    return;
                }
                /*
                * 서버 1 -> 맵 1 -> 점령시 -> 그 서버에만 저장
                * 서버 2 -> 맵 2 ^ <- 점령불가
                * 갯수 -> 서버 2 -> 서버1의 영토갯수 알고싶다 -> 서버 1 물어보기
                * 해당 맵에 점령한 영토갯수 -> 좀 이상하니까
                * 채팅 중복 ->
                * 벌크 업데이트 -> 업데이트 -> 순서대로 작동 틱 단위로 함 50ms
                * 중복이 별로 일어나지 않거나 일어나도 위험하지 않은 정보 -> 레디스 캐시 -> 모든 서버에게 데이터 복사본 나눠줌
                * ---> MYSQL 데이터베이스 갯수만 저장 <- 데이터 중복 방지 (이름 , 금고 금액, DTR 등등) <- 리얼타임
                * 국가 전용 UUID
                * */
            }/* else if() {
                if(e.getBlock().getType().equals(Material.BEACON)) {

                }
            }*/

        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPVP(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player attacker = (Player) e.getDamager();
            Player victem = (Player) e.getEntity();
            if(FactionUtils.isSameFaction(attacker.getUniqueId().toString(), victem.getUniqueId().toString())) {
                e.setCancelled(true);
                SystemUtils.sendmessage(attacker, "&4&l⚠ &r&c국가원들끼리의 전투는 금지되어 있습니다");
                return;
            } else {
                TempStorage.TeleportLocation.remove(attacker.getUniqueId().toString());
                TempStorage.TeleportLocation.remove(victem.getUniqueId().toString());
            }
        }
    }

    @EventHandler
    public void OnJoinWarp(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String uuid = p.getUniqueId().toString();
        UserInfoStorage.onPlayerJoinEvent(e);
        if(onJoinWarp.containsKey(uuid)) {
            p.teleportAsync(onJoinWarp.get(uuid));
            onJoinWarp.remove(uuid);
            SystemUtils.sendfactionmessage(p, "&r&f국가 스폰으로 이동합니다");
        }
    }


    @Deprecated
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
        String k = e.getMessage();
        Player p = e.getPlayer();
        String UUID = p.getUniqueId().toString();
        e.setCancelled(true);
            /*try {
                if (main.getInstance().ess.getUser(p.getUniqueId()).isMuted()) {
                    utils.sendmsg(p, "&4&l(!) &f&l당신은 뮤트됬습니다");
                    e.setCancelled(true);
                    return;
                }
            } catch (NullPointerException nulle) {
                System.out.println(nulle.getMessage());
            }*/
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
            if(FactionChatToggle.FactionChatToggled.containsKey(p)) {
                if(FactionUtils.isInFaction(UUID)) {

                    //utils.teamchat(e.getPlayer().getUniqueId().toString(), k);
                    JedisTempStorage.AddCommandToQueueFix("chat" + ":=:" + p.getUniqueId().toString() + ":=:" + k, "chat" + ":=:" + p.getUniqueId().toString() + ":=:" + k);
                } else {

                    SystemUtils.sendmessage(p, "&c&l(!) &7소속된 팀이 없어서 팀 채팅에서 강제적으로 퇴장당했습니다");
                    FactionChatToggle.FactionChatToggled.remove(p);

                }
            } else if(!p.hasPermission("faxcore.chatbypass") && k.length() > 64){
                SystemUtils.sendmessage(p, "&c&l(!) &f&l메시지가 너무 깁니다. 64자 미만으로 줄여주세요");

            } else {
                try {
                    if (Universal.get().getMethods().callChat(e.getPlayer())) {
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
                String tagonfront = "&7[ " + String.valueOf(closeplayer.size()) + "명 ]&r ";

                String Dformat = colorize(tagonfront) + colorize(format.replace("{message}", (player.hasPermission("lpc.colorcodes") && player.hasPermission("lpc.rgbcodes")) ?
                        translateHexColorCodes(colorize(message)) : (player.hasPermission("lpc.colorcodes") ? colorize(message) : (player.hasPermission("lpc.rgbcodes") ?
                        translateHexColorCodes(message) : message))).replace("%itndevfactions_formatfactionname%", FactionUtils.getFormattedFaction(UUID) + " ").replace("%itndevfaction_formatrank%", FactionUtils.getFormattedRank(UUID) + " "));
                for(Player finalonlinecloseplayers : closeplayer) {
                    finalonlinecloseplayers.sendMessage(Dformat);
                }
            }

        }).start();

    }


    private String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
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

    //@EventHandler
    /*public void onTeleport(PlayerTeleportEvent e) {
        TeleportInvisFix.onTeleport(e);
    }*/
}
