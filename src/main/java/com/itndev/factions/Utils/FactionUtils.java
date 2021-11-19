package com.itndev.factions.Utils;

import com.itndev.factions.Config.Config;
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

    public static String getFactionLeader(String FactionUUID) {
        for(String UUID : FactionUtils.getFactionMember(FactionUUID)) {
            if(FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Leader)) {
                return UUID;
            }
        }
        return null;
    }

    public static Boolean isExistingFaction(String FactionName) {
        return FactionStorage.FactionNameToFactionUUID.containsKey(FactionName.toLowerCase(Locale.ROOT));
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
        JedisTempStorage.AddCommandToQueue("update:=:FactionRank:=:add:=:" + LeaderUUID + ":=:add:=:" + Config.Leader);
        JedisTempStorage.AddCommandToQueue("update:=:PlayerFaction:=:add:=:" + LeaderUUID + ":=:add:=:" + FactionUUID);
    }

    public static void DeleteFaction(String FactionUUID) {
        String FactionName = getFactionName(FactionUUID).toLowerCase(Locale.ROOT);
        JedisTempStorage.AddCommandToQueue("update:=:FactionMember:=:remove:=:" + FactionUUID + ":=:remove:=:" + "UUID");
        JedisTempStorage.AddCommandToQueue("update:=:FactionNameToFactionUUID:=:remove:=:" + FactionName + ":=:add:=:" + "nothing");
        JedisTempStorage.AddCommandToQueue("update:=:FactionNameToFactionName:=:remove:=:" + FactionName + ":=:add:=:" + "nothing");
        JedisTempStorage.AddCommandToQueue("update:=:FactionUUIDToFactionName:=:remove:=:" + FactionUUID + ":=:add:=:" + "nothing");
        if(FactionStorage.FactionMember.containsKey(FactionUUID)) {
            for(String PlayerUUID : FactionStorage.FactionMember.get(FactionUUID)) {
                JedisTempStorage.AddCommandToQueue("update:=:FactionRank:=:add:=:" + PlayerUUID + ":=:add:=:" + Config.Nomad);
                JedisTempStorage.AddCommandToQueue("update:=:PlayerFaction:=:remove:=:" + PlayerUUID + ":=:add:=:ddd");
            }
        }
        for(String Chunkkey : FactionStorage.FactionToLand.keySet()) {
            FactionUtils.UnClaimLand(FactionUUID, Chunkkey);
        }
        JedisTempStorage.AddCommandToQueue("update:=:FactionToLand:=:remove:=:" + FactionUUID + ":=:add:=:" + "nothing");
        JedisTempStorage.AddCommandToQueue("update:=:FactionOutPost:=:remove:=:" + FactionUUID + ":=:add:=:" + "nothing");
        JedisTempStorage.AddCommandToQueue("update:=:FactionWarpLocations:=:remove:=:" + FactionUUID + ":=:add:=:" + "nothing");
    }

    public static void SendFactionMessage(String playeruuid, String targetuuid, String type, String message) {
        if(type.equalsIgnoreCase("single")) {
            //type : SIBAL, TeamChat, all
            JedisTempStorage.AddCommandToQueue("notify:=:" + playeruuid + ":=:" + targetuuid + ":=:" + message + ":=:" + "false");
        } else {
            JedisTempStorage.AddCommandToQueue("notify:=:" + playeruuid + ":=:" + type + ":=:" + message + ":=:" + "true");
        }

    }

    public static Boolean ClaimLand(String FactionUUID, String Chunkkey) {
        if(!FactionStorage.LandToFaction.containsKey(Chunkkey)) {
            FactionStorage.LandToFaction.put(Chunkkey, FactionUUID);
            JedisTempStorage.AddCommandToQueue("update:=:LandToFaction:=:add:=:" + Chunkkey + ":=:add:=:" + FactionUUID + ":=:" + Main.ServerName);
            ArrayList<String> updatelist = new ArrayList<>();
            if(FactionStorage.FactionToLand.containsKey(FactionUUID)) {
                updatelist = FactionStorage.FactionToLand.get(FactionUUID);
            }
            updatelist.add(Chunkkey);
            FactionStorage.FactionToLand.put(FactionUUID, updatelist);
            JedisTempStorage.AddCommandToQueue("update:=:LandToFaction:=:add:=:" + FactionUUID + ":=:add:=:" + Chunkkey + ":=:" + Main.ServerName);
            return true;
        } else {
            return false;
        }
    }

    public static Boolean UnClaimLand(String FactionUUID, String Chunkkey) {
        if(FactionStorage.LandToFaction.containsKey(Chunkkey)) {
            FactionStorage.LandToFaction.remove(Chunkkey);
            JedisTempStorage.AddCommandToQueue("update:=:LandToFaction:=:add:=:" + Chunkkey + ":=:remove:=:" + FactionUUID + ":=:" + Main.ServerName);
            ArrayList<String> updatelist = FactionStorage.FactionToLand.get(FactionUUID);
            updatelist.remove(Chunkkey);
            FactionStorage.FactionToLand.put(FactionUUID, updatelist);
            JedisTempStorage.AddCommandToQueue("update:=:LandToFaction:=:add:=:" + FactionUUID + ":=:remove:=:" + Chunkkey + ":=:" + Main.ServerName);
            return true;
        } else {
            return false;
        }
    }

    public static Boolean isAExistingLangRank(String LangRank) {
        String lowcaserank = LangRank.toLowerCase(Locale.ROOT);
        if(lowcaserank.equalsIgnoreCase(Config.Leader_Lang)) {
            return true;
        } else if(lowcaserank.equalsIgnoreCase(Config.CoLeader_Lang)) {
            return true;
        } else if(lowcaserank.equalsIgnoreCase(Config.VipMember_Lang)) {
            return true;
        } else if(lowcaserank.equalsIgnoreCase(Config.Warrior_Lang)) {
            return true;
        } else if(lowcaserank.equalsIgnoreCase(Config.Member_Lang)) {
            return true;
        } else {
            return false;
        }
    }


    @Deprecated
    public static void FactionUUIDNotify(String FactionUUID, String Message) {
        for(String UUID : FactionUtils.getFactionMember(FactionUUID)) {
            OfflinePlayer op = Bukkit.getOfflinePlayer(UserInfoUtils.getPlayerName(UUID));
            if(op.isOnline()) {
                Player p = (Player) op;
                SystemUtils.sendmessage(p, SystemUtils.colorize(Message));
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
        if(FactionStorage.FactionNameToFactionName.containsKey(FactionName.toLowerCase(Locale.ROOT))) {
            return FactionStorage.FactionNameToFactionName.get(FactionName.toLowerCase(Locale.ROOT));
        }
        return null;
    }

    public static Boolean FactionSpawnExists(String FactionUUID) {
        return FactionStorage.FactionInfo.containsKey(FactionUUID + "=spawn");
    }

    public static String getFactionSpawn(String FactionUUID) {
        return FactionStorage.FactionInfo.get(FactionUUID + "=spawn");
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
            return Config.Nomad;
        }
    }

    public static String LangRankConvert(String Rank) {
        if(Rank.equalsIgnoreCase(Config.Nomad)) {
            return Config.Nomad_Lang;
        } else
        if(Rank.equalsIgnoreCase(Config.Member)) {
            return Config.Member_Lang;
        } else
        if(Rank.equalsIgnoreCase(Config.Warrior)) {
            return Config.Warrior_Lang;
        } else
        if(Rank.equalsIgnoreCase(Config.VipMember)) {
            return Config.VipMember_Lang;
        } else
        if(Rank.equalsIgnoreCase(Config.CoLeader)) {
            return Config.CoLeader_Lang;
        } else
        if(Rank.equalsIgnoreCase(Config.Leader)) {
            return Config.Leader_Lang;
        }
        return null;
    }

    public static String RankConvert(String Rank) {
        if(Rank.equalsIgnoreCase(Config.Nomad_Lang)) {
            return Config.Nomad;
        } else
        if(Rank.equalsIgnoreCase(Config.Member_Lang)) {
            return Config.Member;
        } else
        if(Rank.equalsIgnoreCase(Config.Warrior_Lang)) {
            return Config.Warrior;
        } else
        if(Rank.equalsIgnoreCase(Config.VipMember_Lang)) {
            return Config.VipMember;
        } else
        if(Rank.equalsIgnoreCase(Config.CoLeader_Lang)) {
            return Config.CoLeader;
        } else
        if(Rank.equalsIgnoreCase(Config.Leader_Lang)) {
            return Config.Leader;
        }
        return null;
    }

    public static String getPlayerLangRank(String UUID) {
        if(FactionStorage.FactionRank.containsKey(UUID)) {
            return LangRankConvert(FactionStorage.FactionRank.get(UUID));
        } else {
            return LangRankConvert(Config.Nomad);
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

    public static String AsyncWhosClaim(Location loc) {
        if(FactionStorage.AsyncLandToFaction.containsKey(getChunkKey(loc))) {
            return FactionStorage.AsyncLandToFaction.get(getChunkKey(loc));
        }
        return null;
    }

    public static String WhosClaim(Location loc) {
        if(FactionStorage.LandToFaction.containsKey(getChunkKey(loc))) {
            return FactionStorage.LandToFaction.get(getChunkKey(loc));
        }
        return null;
    }

    public static Boolean HigherThenorSameRank(String UUID, String Rank) {
        String PlayerRank = FactionUtils.getPlayerRank(UUID);
        if(RankPrio(PlayerRank) > RankPrio(Rank)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean HigherThenRank(String UUID, String Rank) {
        String PlayerRank = FactionUtils.getPlayerRank(UUID);
        if(RankPrio(PlayerRank) >= RankPrio(Rank)) {
            return true;
        } else {
            return false;
        }
    }

    public static Integer RankPrio(String Rank) {
        if(Rank.equalsIgnoreCase(Config.Leader)) {
            return 6;
        } else if(Rank.equalsIgnoreCase(Config.CoLeader)) {
            return 5;
        } else if(Rank.equalsIgnoreCase(Config.VipMember)) {
            return 4;
        } else if(Rank.equalsIgnoreCase(Config.Warrior)) {
            return 3;
        } else if(Rank.equalsIgnoreCase(Config.Member)) {
            return 2;
        } else {
            return 1;
        }
    }

    public static void SetFactionSpawn(String FactionUUID, String ConvertLoc) {
        JedisTempStorage.AddCommandToQueue("update:=:FactionInfo:=:add:=:" + FactionUUID + "=spawn" + ":=:add:=:" + Main.ServerName + "=" + ConvertLoc);
    }

    public static void RemoveFactionSpawn(String FactionUUID) {
        JedisTempStorage.AddCommandToQueue("update:=:FactionInfo:=:remove:=:" + FactionUUID + "=spawn" + ":=:add:=:" + Main.ServerName);
    }
}
