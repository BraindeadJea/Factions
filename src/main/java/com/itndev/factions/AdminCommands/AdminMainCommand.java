package com.itndev.factions.AdminCommands;

import com.itndev.factions.Faction.FactionOutpost;
import com.itndev.factions.Jedis.JedisManager;
import com.itndev.factions.Storage.FactionStorage;
import com.itndev.factions.Utils.FactionList.FactionList;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class AdminMainCommand implements CommandExecutor {

    private static Location loc = null;

    public static Location getCopyLocation() {
        return loc;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] strings) {
        if(!sender.hasPermission("itndevfactions.admin")) {
            sender.sendMessage(SystemUtils.colorize("&c&lERROR &r&7권한이 없습니다"));
            return false;
        }
        if(label.equalsIgnoreCase("factionadmin")) {
            RunAdminCommand((Player) sender, strings);
        }
        return false;
    }

    public void RunAdminCommand(Player p, String[] args) {
        if(args.length < 1) {
            SystemUtils.sendmessage(p, SystemUtils.colorize("&c&lERROR &r&f존재하지 않는 명령어"));
            return;
        }
        if(args[0].equalsIgnoreCase("reloadfactionbaltop")) {
            FactionList.FactionTopExecute();
            SystemUtils.sendmessage(p, "&c&lSUCESS &r&7국가 금고 정보 로컬 데이터 리프레시 중...");
        } else if(args[0].equalsIgnoreCase("buildfactionbaltop")) {
            FactionList.BuildFactionTop();
            SystemUtils.sendmessage(p, "&c&lSUCESS &r&7국가 목록 빌드 중...");
        } else if(args[0].equalsIgnoreCase("setlocation")) {
            loc = p.getLocation();
            SystemUtils.sendmessage(p, "&c&lSUCESS &r&7복사 위치 설정완료");
        } else if(args[0].equalsIgnoreCase("pastechunk")) {
            if(loc != null) {
                Location loc2 = SystemUtils.ReplaceChunk(loc, p.getLocation());
                SystemUtils.sendmessage(p, "&c&lSUCESS &r&7청크 붙여넣는중");
                if(loc2 == null) {
                    SystemUtils.sendmessage(p, "&c&lINFO &r&7BEACON 위치 확인 불가");
                } else {
                    String k = "X:" + loc2.getBlockX() + " Y:" + loc2.getBlockY() + " Z:" + loc2.getBlockZ();
                    SystemUtils.sendfactionmessage(p, "&r&f참 신호기 위치 &r&7: &r&c" + k);
                }
            } else {
                SystemUtils.sendmessage(p, "&c&lERROR &r&7설정된 위치가 존재하지 않음");
            }
        } else if(args[0].equalsIgnoreCase("tryclaimoutpost")) {
            Location loc = p.getLocation();
            FactionOutpost.TryClaimOutPost(p, loc, UUID.randomUUID().toString());
        } else if(args[0].equalsIgnoreCase("radiusclaimed")) {
            new Thread(() -> {
                try {
                    if(FactionUtils.AsyncNearByChunksAreOwned5(p.getLocation()).get()) {
                        p.sendMessage("yes");
                    } else {
                        p.sendMessage("no");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }).start();
        } else if(args[0].equalsIgnoreCase("checkfactionoutposthashmap")) {
            new Thread( () -> {
                p.sendMessage("==============");
                for(String key : FactionStorage.FactionOutPost.keySet()) {
                    p.sendMessage(key + " - " + FactionStorage.FactionOutPost.get(key));
                }
                p.sendMessage("==============");
                for(String key : FactionStorage.FactionOutPostList.keySet()) {
                    p.sendMessage(key + " - " + FactionStorage.FactionOutPostList.get(key).toString());
                }
                p.sendMessage("==============");
            }).start();

        } else if(args[0].equalsIgnoreCase("getclock")) {
            SystemUtils.sendmessage(p, "&c&lINFO &7JEDIS SYNC CLOCK TIME : " + JedisManager.JEDISSYNCCLOCK);
        }
     }

}
