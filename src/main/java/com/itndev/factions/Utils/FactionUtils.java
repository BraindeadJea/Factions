package com.itndev.factions.Utils;

import com.itndev.factions.Commands.FactionsCommands.ClaimLand;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import com.itndev.factions.Storage.FactionStorage;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class FactionUtils {

    public static String newFactionUUID() {
        String uuid;
        Long time = System.currentTimeMillis();
        UUID uuid2 = UUID.randomUUID();
        uuid = String.valueOf(time) + "=" + String.valueOf(uuid2);
        return uuid;
    }

    public static void SetPlayerRank(String UUID, String Rank) {
        if(Rank == null) {
            JedisTempStorage.AddCommandToQueue("update:=:FactionRank:=:remove:=:" + UUID + ":=:add:=:" + Rank.toLowerCase(Locale.ROOT));
        } else {
            JedisTempStorage.AddCommandToQueue("update:=:FactionRank:=:add:=:" + UUID + ":=:add:=:" + Rank.toLowerCase(Locale.ROOT));
        }
    }

    public static void SetPlayerFaction(String UUID, String FactionUUID) {
        if(FactionUUID == null) {
            JedisTempStorage.AddCommandToQueue("update:=:PlayerFaction:=:remove:=:" + UUID + ":=:add:=:ddd");
        } else {
            JedisTempStorage.AddCommandToQueue("update:=:PlayerFaction:=:add:=:" + UUID + ":=:add:=:" + FactionUUID);
        }
    }

    public static void SetFactionMember(String UUID, String FactionUUID, Boolean Remove) {
        if(FactionUUID == null) {
            return;
        }
        if(!Remove) {
            JedisTempStorage.AddCommandToQueue("update:=:FactionMember:=:add:=:" + FactionUUID + ":=:add:=:" + UUID);
        } else {
            JedisTempStorage.AddCommandToQueue("update:=:FactionMember:=:add:=:" + FactionUUID + ":=:remove:=:" + UUID);
        }
    }

    public static void AddFactionDTR(String FactionUUID, double DTR, Boolean RemoveFaction) {
        if(!RemoveFaction) {
            JedisTempStorage.AddCommandToQueue("update:=:FactionDTR:=:add:=:" + FactionUUID + ":=:add:=:" + DTR);
        } else {
            JedisTempStorage.AddCommandToQueue("update:=:FactionDTR:=:remove:=:" + FactionUUID + ":=:remove:=:" + "D");
        }

    }

    public static void SetFactionName(String FactionUUID, String NewFactionName) {
        //Faction name update
        String oldFactionName = getFactionName(FactionUUID);
        if(oldFactionName.equalsIgnoreCase(NewFactionName)) {
            JedisTempStorage.AddCommandToQueue("update:=:FactionNameToFactionName:=:add:=:" + NewFactionName.toLowerCase(Locale.ROOT) + ":=:add:=:" + NewFactionName);
            //JedisTempStorage.AddCommandToQueue("update:=:FactionNameToFactionUUID:=:remove:=:" + oldFactionName.toLowerCase(Locale.ROOT) + ":=:add:=:" + NewFactionName);
            JedisTempStorage.AddCommandToQueue("update:=:FactionNameToFactionUUID:=:add:=:" + NewFactionName.toLowerCase(Locale.ROOT) + ":=:add:=:" + FactionUUID);
            JedisTempStorage.AddCommandToQueue("update:=:FactionNameToFactionUUID:=:add:=:" + FactionUUID + ":=:add:=:" + NewFactionName.toLowerCase(Locale.ROOT));
        } else {
            JedisTempStorage.AddCommandToQueue("update:=:FactionNameToFactionName:=:add:=:" + NewFactionName.toLowerCase(Locale.ROOT) + ":=:add:=:" + NewFactionName);
            JedisTempStorage.AddCommandToQueue("update:=:FactionNameToFactionUUID:=:remove:=:" + oldFactionName.toLowerCase(Locale.ROOT) + ":=:add:=:" + NewFactionName);
            JedisTempStorage.AddCommandToQueue("update:=:FactionNameToFactionUUID:=:add:=:" + NewFactionName.toLowerCase(Locale.ROOT) + ":=:add:=:" + FactionUUID);
            JedisTempStorage.AddCommandToQueue("update:=:FactionUUIDToFactionName:=:add:=:" + FactionUUID + ":=:add:=:" + NewFactionName.toLowerCase(Locale.ROOT));
        }
    }

    public static void CreateFaction(String LeaderUUID, String FactionUUID, String FactionOrginName) {
        String FactionName = FactionOrginName.toLowerCase(Locale.ROOT);
        JedisTempStorage.AddCommandToQueue("update:=:FactionMember:=:add:=:" + FactionUUID + ":=:add:=:" + LeaderUUID);
        JedisTempStorage.AddCommandToQueue("update:=:FactionNameToFactionUUID:=:add:=:" + FactionName + ":=:add:=:" + FactionUUID);
        JedisTempStorage.AddCommandToQueue("update:=:FactionNameToFactionName:=:add:=:" + FactionName + ":=:add:=:" + FactionOrginName);
        JedisTempStorage.AddCommandToQueue("update:=:FactionUUIDToFactionName:=:add:=:" + FactionUUID + ":=:add:=:" + FactionName);
        JedisTempStorage.AddCommandToQueue("update:=:FactionRank:=:add:=:" + LeaderUUID + ":=:add:=:leader");
        JedisTempStorage.AddCommandToQueue("update:=:PlayerFaction:=:add:=:" + LeaderUUID + ":=:add:=:" + FactionName);
    }

    public static void DeleteFaction(String FactionUUID) {
        String FactionName = getFactionName(FactionUUID).toLowerCase(Locale.ROOT);
        JedisTempStorage.AddCommandToQueue("update:=:FactionMember:=:remove:=:" + FactionUUID + ":=:remove:=:" + "UUID");
        JedisTempStorage.AddCommandToQueue("update:=:FactionNameToFactionUUID:=:remove:=:" + FactionName + ":=:add:=:" + "nothing");
        JedisTempStorage.AddCommandToQueue("update:=:FactionNameToFactionName:=:remove:=:" + FactionName + ":=:add:=:" + "nothing");
        JedisTempStorage.AddCommandToQueue("update:=:FactionUUIDToFactionName:=:remove:=:" + FactionUUID + ":=:add:=:" + "nothing");
        if(FactionStorage.FactionMember.containsKey(FactionUUID)) {
            for(String PlayerUUID : FactionStorage.FactionMember.get(FactionUUID)) {
                JedisTempStorage.AddCommandToQueue("update:=:FactionRank:=:add:=:" + PlayerUUID + ":=:add:=:nomad");
                JedisTempStorage.AddCommandToQueue("update:=:PlayerFaction:=:remove:=:" + PlayerUUID + ":=:add:=:ddd");
            }
        }
    }

    public static void SendFactionMessage(String playeruuid, String targetuuid, String type, String message) {
        if(type.equalsIgnoreCase("single")) {
            //type : SIBAL, TeamChat, all
            JedisTempStorage.AddCommandToQueue("notify:=:" + playeruuid + ":=:" + targetuuid + ":=:" + message + ":=:" + "false");
        } else {
            JedisTempStorage.AddCommandToQueue("notify:=:" + playeruuid + ":=:" + type + ":=:" + message + ":=:" + "true");
        }

    }

    @Deprecated
    public static void FactionUUIDNotify(String FactionUUID, String Message) {
        for(String UUID : FactionUtils.getFactionMember(FactionUUID)) {
            OfflinePlayer op = Bukkit.getOfflinePlayer(UserInfoUtils.getPlayerName(UUID));
            if(op.isOnline()) {
                Player p = (Player) op;
                SystemUtils.sendmessage(p, Message);
            }
        }
    }

    @Deprecated
    public static void FactionNotify(String playeruuid, String targetuuid, String message, String trueorfalse) {
        if(trueorfalse.equalsIgnoreCase("true")) {
            if(targetuuid.equalsIgnoreCase("all")) {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    SystemUtils.sendmessage(p, message);
                }
            } else if (targetuuid.equalsIgnoreCase("SIBAL")) {
                for (String playeruuids : FactionUtils.getFactionMember(FactionUtils.getPlayerFactionUUID(playeruuid))) {
                    if (playeruuids != targetuuid) {
                        SystemUtils.sendUUIDmessage(playeruuids, "&a&o&l[ &r&f국가 &a&o&l] &r&f" + message);
                    }
                }

            } else if(targetuuid.equalsIgnoreCase("TeamChat")){
                for(String playeruuids : FactionUtils.getFactionMember(FactionUtils.getPlayerFactionUUID(playeruuid))) {
                    SystemUtils.sendUUIDmessage(playeruuids, "&a&o&l[ &r&f국가 &a&o&l] &r&f" + message);
                }
            } else {
                for (String playeruuids : FactionUtils.getFactionMember(FactionUtils.getPlayerFactionUUID(playeruuid))) {
                    if (playeruuids != targetuuid) {
                        SystemUtils.sendUUIDmessage(playeruuids, "&a&o&l[ &r&f국가 &a&o&l] &r&f" + message);
                    }
                }
                SystemUtils.sendUUIDmessage(targetuuid, "&a&o&l[ &r&f국가 &a&o&l] &r&f" + message);
                //Bukkit.getPlayer(listener.uuid2name(targetuuid)).sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&o&l[ &r&f팀 &a&o&l] &r&f" + message));
            }
        } else {

            SystemUtils.sendUUIDmessage(targetuuid, "&a&o&l[ &r&f국가 &a&o&l] &r&f" + message);
            //Bukkit.getPlayer(listener.uuid2name(targetuuid)).sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&o&l[ &r&f팀 &a&o&l] &r&f" + message));
        }
    }

    @Deprecated
    public static void FactionChat(String UUID, String message) {
        for(String UUIDs : FactionUtils.getFactionMember(FactionUtils.getPlayerFactionUUID(UUID))) {
            OfflinePlayer op = Bukkit.getOfflinePlayer(UserInfoUtils.getPlayerName(UUIDs));
            if(op.isOnline()) {
                Player p = (Player) op;
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&o&l[ &f국가채팅 &a&o&l] &r&f" + UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(UUID)) + " &7: &r&3") + message);
            }
        }

    }

    public static String getFactionByClaim(String ChunkKey) {
        if(FactionStorage.LandToFaction.containsKey(ChunkKey)) {
            return FactionStorage.LandToFaction.get(ChunkKey);
        }
        return null;
    }

    public static String getPlayerFactionUUID(String UUID) {
        String finalUUID = null;
        if(FactionStorage.PlayerFaction.containsKey(UUID)) {
            finalUUID = FactionStorage.PlayerFaction.get(UUID);
        }
        return finalUUID;
    }

    public static String getFactionName(String FactionUUID) {
        String finalname = null;
        if(FactionStorage.FactionUUIDToFactionName.containsKey(FactionUUID)) {
            finalname = FactionStorage.FactionUUIDToFactionName.get(FactionUUID);
        }
        return finalname;
    }

    public static String getFactionUUID(String FactionName) {
        String finalUUID = null;
        if(FactionStorage.FactionNameToFactionUUID.containsKey(FactionName.toLowerCase(Locale.ROOT))) {
            finalUUID = FactionStorage.FactionNameToFactionUUID.get(FactionName.toLowerCase(Locale.ROOT));
        }
        return finalUUID;
    }

    public static String getCappedFactionName(String FactionName) {
        String finalname = null;
        if(FactionStorage.FactionNameToFactionName.containsKey(FactionName)) {
            finalname = FactionStorage.FactionNameToFactionName.get(FactionName);
        }
        return finalname;
    }

    public static ArrayList<String> getFactionMember(String FactionUUID) {
        ArrayList<String> finallist = new ArrayList<>();
        if(FactionStorage.FactionMember.containsKey(FactionUUID)) {
            finallist = FactionStorage.FactionMember.get(FactionUUID);
        }
        return finallist;
    }

    public static String getPlayerRank(String UUID) {
        if(FactionStorage.FactionRank.containsKey(UUID)) {
            return FactionStorage.FactionRank.get(UUID);
        } else {
            return "nomad";
        }

    }

    public static String getChunkKey(Location loc) {
        return Main.ServerName + "=" + loc.getWorld().getName() + "=" + loc.getChunk().getChunkKey();
    }

    public static Boolean isClaimed(Location loc) {
        return FactionStorage.LandToFaction.containsKey(getChunkKey(loc));
    }

    public static Boolean isInFaction(String UUID) {
        return FactionStorage.PlayerFaction.containsKey(UUID);
    }

    public static Boolean isSameFaction(String UUID, String UUID2) {
        if(isInFaction(UUID) && isInFaction(UUID2)) {
            return (getPlayerFactionUUID(UUID).equalsIgnoreCase(getPlayerFactionUUID(UUID2)));
        }
        return false;
    }

    public static String WhosClaim(Location loc) {
        if(FactionStorage.LandToFaction.containsKey(getChunkKey(loc))) {
            return FactionStorage.LandToFaction.get(getChunkKey(loc));
        }
        return null;
    }

}
