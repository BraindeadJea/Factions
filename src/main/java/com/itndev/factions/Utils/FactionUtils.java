package com.itndev.factions.Utils;

import com.itndev.factions.AdminCommands.AdminMainCommand;
import com.itndev.factions.Config.Config;
import com.itndev.factions.Config.Lang;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import com.itndev.factions.Storage.FactionStorage;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    public static String getFormattedFaction(String UUID) {
        if(FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Nomad)) {
            return "";
        } else {
            return "&f[ &r&a" + FactionUtils.getFactionName(FactionUtils.getPlayerFactionUUID(UUID)) + " &r&f]";
        }
    }

    public static String getFormattedRank(String UUID) {
        if(FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Nomad)) {
            return "";
        } else {
            return "&r&7" + FactionUtils.getPlayerLangRank(UUID);
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
            UnClaimLand(FactionUUID, Chunkkey);
        }
        ClearFactionInfo(FactionUUID);
        for(String Chunkkey : FactionStorage.FactionToOutPost.keySet()) {
            UnClaimOutPost(FactionUUID, Chunkkey);
        }
        JedisTempStorage.AddCommandToQueue("update:=:FactionToLand:=:remove:=:" + FactionUUID + ":=:remove:=:" + FactionUUID + ":=:bulk");
        JedisTempStorage.AddCommandToQueue("update:=:FactionToLand:=:remove:=:" + FactionUUID + ":=:add:=:" + "nothing");
        JedisTempStorage.AddCommandToQueue("update:=:FactionOutPost:=:remove:=:" + FactionUUID + ":=:add:=:" + "nothing");
        JedisTempStorage.AddCommandToQueue("update:=:FactionWarpLocations:=:remove:=:" + FactionUUID + ":=:add:=:" + "nothing");
    }

    public static CompletableFuture<Boolean> AsyncNearByChunksAreOwned5(Location loc) {
        CompletableFuture<Boolean> Finalbool = new CompletableFuture<>();
        new Thread( () -> {
            ArrayList<Location> checklist = new ArrayList<>();
            ArrayList<CompletableFuture<Boolean>> checkfinishlist = new ArrayList<>();
            int x = loc.getBlockX();
            int z = loc.getBlockZ();
            int amount = 48;
            Location temploc1 = loc;
            temploc1.setX(x + amount);
            temploc1.setZ(z + amount);
            checklist.add(temploc1);
            Location temploc2 = loc;
            temploc2.setX(x - amount);
            temploc2.setZ(z - amount);
            checklist.add(temploc2);
            Location temploc3 = loc;
            temploc3.setX(x + amount);
            temploc3.setZ(z - amount);
            checklist.add(temploc3);
            Location temploc4 = loc;
            temploc4.setX(x - amount);
            temploc4.setZ(z + amount);
            checklist.add(temploc4);
            Location temploc5 = loc;
            temploc5.setX(x + amount);
            checklist.add(temploc5);
            Location temploc6 = loc;
            temploc6.setX(x - amount);
            checklist.add(temploc6);
            Location temploc7 = loc;
            temploc7.setZ(z + amount);
            checklist.add(temploc7);
            Location temploc8 = loc;
            temploc8.setZ(z - amount);
            checklist.add(temploc8);
            Location temploc9 = loc;
            checklist.add(temploc9);
            for(Location temploc : checklist) {
                checkfinishlist.add(AsyncNearByChunksAreOwned(temploc));
            }
            Boolean isfinished = false;
            for(CompletableFuture<Boolean> finalboolean : checkfinishlist) {
                try {
                    if(!finalboolean.get()) {
                        Finalbool.complete(false);
                        isfinished = true;
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    Finalbool.complete(false);
                    isfinished = true;
                }
            }
            if(!isfinished) {
                Finalbool.complete(true);
            }

        }).start();
        return Finalbool;
    }

    public static Boolean AreNearByPlayersEnemies(Player p, double Radius) {
        for(Player near : p.getLocation().getNearbyPlayers(Radius)) {
            if(!isSameFaction(p.getUniqueId().toString(), near.getUniqueId().toString())) {
                return true;
            }
        }
        return false;
    }

    public static Boolean isExistingOutPost(String FactionUUID, String OutPostName) {
        return FactionStorage.FactionOutPostList.get(FactionUUID).contains(OutPostName.toLowerCase(Locale.ROOT));
    }

    public static Boolean NearByChunksAreOwned(Location loc) {
        Location temploc1 = loc;
        Location temploc2 = loc;
        Location temploc3 = loc;
        Location temploc4 = loc;
        Location templocC1 = loc;
        Location templocC2 = loc;
        Location templocC3 = loc;
        Location templocC4 = loc;

        temploc1.setX(loc.getX() + 16);

        temploc2.setZ(loc.getZ() + 16);

        temploc3.setX(loc.getX() - 16);

        temploc2.setZ(loc.getZ() - 16);

        templocC1.setX(loc.getX() + 16);
        templocC1.setZ(loc.getZ() + 16);

        templocC2.setX(loc.getX() - 16);
        templocC2.setZ(loc.getZ() + 16);

        templocC3.setX(loc.getX() + 16);
        templocC3.setZ(loc.getZ() - 16);

        templocC4.setX(loc.getX() - 16);
        templocC4.setZ(loc.getZ() - 16);

        if(FactionUtils.isClaimed(temploc1)) {
            return false;
        } else if(FactionUtils.isClaimed(temploc2)) {
            return false;
        } else if(FactionUtils.isClaimed(temploc3)) {
            return false;
        } else if(FactionUtils.isClaimed(temploc4)) {
            return false;
        } else if(FactionUtils.isClaimed(templocC1)) {
            return false;
        } else if(FactionUtils.isClaimed(templocC2)) {
            return false;
        } else if(FactionUtils.isClaimed(templocC3)) {
            return false;
        } else if(FactionUtils.isClaimed(templocC4)) {
            return false;
        } else {
            return true;
        }

    }

    public static CompletableFuture<Boolean> AsyncNearByChunksAreOwned(Location loc) {
        CompletableFuture<Boolean> Finalbool = new CompletableFuture<>();
        new Thread( () -> {
            Location temploc1 = loc;
            Location temploc2 = loc;
            Location temploc3 = loc;
            Location temploc4 = loc;
            Location templocC1 = loc;
            Location templocC2 = loc;
            Location templocC3 = loc;
            Location templocC4 = loc;

            temploc1.setX(loc.getX() + 16);

            temploc2.setZ(loc.getZ() + 16);

            temploc3.setX(loc.getX() - 16);

            temploc2.setZ(loc.getZ() - 16);

            templocC1.setX(loc.getX() + 16);
            templocC1.setZ(loc.getZ() + 16);

            templocC2.setX(loc.getX() - 16);
            templocC2.setZ(loc.getZ() + 16);

            templocC3.setX(loc.getX() + 16);
            templocC3.setZ(loc.getZ() - 16);

            templocC4.setX(loc.getX() - 16);
            templocC4.setZ(loc.getZ() - 16);

            if(FactionUtils.isClaimed(temploc1)) {
                Finalbool.complete(false);
            } else if(FactionUtils.isClaimed(temploc2)) {
                Finalbool.complete(false);
            } else if(FactionUtils.isClaimed(temploc3)) {
                Finalbool.complete(false);
            } else if(FactionUtils.isClaimed(temploc4)) {
                Finalbool.complete(false);
            } else if(FactionUtils.isClaimed(templocC1)) {
                Finalbool.complete(false);
            } else if(FactionUtils.isClaimed(templocC2)) {
                Finalbool.complete(false);
            } else if(FactionUtils.isClaimed(templocC3)) {
                Finalbool.complete(false);
            } else if(FactionUtils.isClaimed(templocC4)) {
                Finalbool.complete(false);
            } else {
                Finalbool.complete(true);
            }
        }).start();

        return Finalbool;

    }

    public static void SendFactionMessage(String playeruuid, String targetuuid, String type, String message) {
        if(type.equalsIgnoreCase("single")) {
            //type : SIBAL, TeamChat, all
            JedisTempStorage.AddCommandToQueue("notify:=:" + playeruuid + ":=:" + targetuuid + ":=:" + message + ":=:" + "false");
        } else {
            JedisTempStorage.AddCommandToQueue("notify:=:" + playeruuid + ":=:" + type + ":=:" + message + ":=:" + "true");
        }
    }

    public static void ClaimLand(String FactionUUID, String Chunkkey) {
        FactionStorage.LandToFaction.put(Chunkkey, FactionUUID);
        JedisTempStorage.AddCommandToQueue("update:=:LandToFaction:=:add:=:" + Chunkkey + ":=:add:=:" + FactionUUID + ":=:" + Main.ServerName);
        ArrayList<String> updatelist = new ArrayList<>();
        if(FactionStorage.FactionToLand.containsKey(FactionUUID)) {
            updatelist = FactionStorage.FactionToLand.get(FactionUUID);
        }
        updatelist.add(Chunkkey);
        FactionStorage.FactionToLand.put(FactionUUID, updatelist);
        JedisTempStorage.AddCommandToQueue("update:=:FactionToLand:=:add:=:" + FactionUUID + ":=:add:=:" + Chunkkey + ":=:" + Main.ServerName);
    }

    public static void UnClaimLand(String FactionUUID, String Chunkkey) {
        FactionStorage.LandToFaction.remove(Chunkkey);
        JedisTempStorage.AddCommandToQueue("update:=:LandToFaction:=:remove:=:" + Chunkkey + ":=:remove:=:" + FactionUUID + ":=:" + Main.ServerName);
        ArrayList<String> updatelist = FactionStorage.FactionToLand.get(FactionUUID);
        updatelist.remove(Chunkkey);
        if(updatelist.isEmpty()) {
            FactionStorage.FactionToLand.remove(FactionUUID);
        } else {
            FactionStorage.FactionToLand.put(FactionUUID, updatelist);
        }

        JedisTempStorage.AddCommandToQueue("update:=:FactionToLand:=:add:=:" + FactionUUID + ":=:remove:=:" + Chunkkey + ":=:" + Main.ServerName);
    }

    public static void ClaimOutPost(String FactionUUID, String Chunkkey) {
        FactionStorage.OutPostToFaction.put(Chunkkey, FactionUUID);
        JedisTempStorage.AddCommandToQueue("update:=:OutPostToFaction:=:add:=:" + Chunkkey + ":=:add:=:" + FactionUUID + ":=:" + Main.ServerName);
        ArrayList<String> updatelist = new ArrayList<>();
        if(FactionStorage.FactionToOutPost.containsKey(FactionUUID)) {
            updatelist = FactionStorage.FactionToOutPost.get(FactionUUID);
        }
        updatelist.add(Chunkkey);
        FactionStorage.FactionToOutPost.put(FactionUUID, updatelist);
        JedisTempStorage.AddCommandToQueue("update:=:FactionToOutPost:=:add:=:" + FactionUUID + ":=:add:=:" + Chunkkey + ":=:" + Main.ServerName);
    }

    public static void UnClaimOutPost(String FactionUUID, String Chunkkey) {
        FactionStorage.OutPostToFaction.remove(Chunkkey);
        JedisTempStorage.AddCommandToQueue("update:=:OutPostToFaction:=:remove:=:" + Chunkkey + ":=:remove:=:" + FactionUUID + ":=:" + Main.ServerName);
        ArrayList<String> updatelist = FactionStorage.FactionToOutPost.get(FactionUUID);
        updatelist.remove(Chunkkey);
        if(updatelist.isEmpty()) {
            FactionStorage.FactionToOutPost.remove(FactionUUID);
        } else {
            FactionStorage.FactionToOutPost.put(FactionUUID, updatelist);
        }
        JedisTempStorage.AddCommandToQueue("update:=:FactionToOutPost:=:add:=:" + FactionUUID + ":=:remove:=:" + Chunkkey + ":=:" + Main.ServerName);
    }

    public static Boolean isOutPost(Location loc) {
        String Chunkkey = FactionUtils.getChunkKey(loc);
        return FactionStorage.OutPostToFaction.containsKey(Chunkkey);
    }

    public static Boolean isMyOutPost(String FactionUUID, Location loc) {
        return FactionStorage.AsyncOutPostToFaction.get(FactionUtils.getChunkKey(loc)).equals(FactionUUID);
    }

    public static Boolean isSameClaimFaction(Location loc1, Location loc2) {
        if(!isClaimed(loc1) && !isClaimed(loc2)) {
            return true;
        } else if(Objects.equals(FactionUtils.AsyncWhosClaim(loc1), FactionUtils.AsyncWhosClaim(loc2))) {
            return true;
        } else {
            return false;
        }
    }

    public static String GetClaimFaction(Location loc) {
        if(!isClaimed(loc)) {
            return "&2야생";
        } else {
            return "&a" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUtils.AsyncWhosClaim(loc)));
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
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&o&l[ &f국가채팅 &a&o&l] &r&f" + UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(UUID)) + " &7: &r&7") + message);
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

    public static String FactionStatusConvert(String Status) {
        if(Status.equalsIgnoreCase(Config.Ally_Lang)) {
            return Config.Ally;
        } else if(Status.equalsIgnoreCase(Config.Enemy_Lang)) {
            return Config.Enemy;
        } else if(Status.equalsIgnoreCase(Config.Neutral_Lang)) {
            return Config.Neutral;
        } else {
            return null;
        }
    }

    public static String FactionLangStatusConvert(String Status) {
        if(Status.equalsIgnoreCase(Config.Ally)) {
            return Config.Ally_Lang;
        } else if(Status.equalsIgnoreCase(Config.Enemy)) {
            return Config.Enemy_Lang;
        } else if(Status.equalsIgnoreCase(Config.Neutral)) {
            return Config.Neutral_Lang;
        } else {
            return null;
        }
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
        JedisTempStorage.AddCommandToQueue("update:=:FactionInfo:=:add:=:" + FactionUUID + "=spawn" + ":=:add:=:" + Main.ServerName + "===" + ConvertLoc);
        RegisterFactionInfo(FactionUUID, "spawn");
    }

    public static Boolean FactionSpawnExists(String FactionUUID) {
        return FactionStorage.FactionInfo.containsKey(FactionUUID + "=spawn");
    }

    public static String getFactionSpawn(String FactionUUID) {
        return FactionStorage.FactionInfo.get(FactionUUID + "=spawn");
    }

    public static void ClearFactionInfo(String FactionUUID) {
        for(String key : FactionStorage.FactionInfoList.get(FactionUUID)) {
            JedisTempStorage.AddCommandToQueue("update:=:FactionInfo:=:remove:=:" + FactionUUID + "=" + key  + ":=:remove:=:");
        }
        JedisTempStorage.AddCommandToQueue("update:=:FactionInfoList:=:remove:=:" + FactionUUID + ":=:remove:=:");
    }

    public static void RemoveFactionSpawn(String FactionUUID) {
        JedisTempStorage.AddCommandToQueue("update:=:FactionInfo:=:remove:=:" + FactionUUID + "=spawn" + ":=:add:=:" + Main.ServerName);
        JedisTempStorage.AddCommandToQueue("update:=:FactionInfoList:=:add:=:" + FactionUUID + "" + ":=:remove:=:" + "spawn");
    }

    public static void SetFactionNotice(String FactionUUID, String factionnotice) {
        JedisTempStorage.AddCommandToQueue("update:=:FactionInfo:=:add:=:" + FactionUUID + "=notice" + ":=:add:=:" + factionnotice);
        RegisterFactionInfo(FactionUUID, "notice");
    }

    public static Boolean FactionNoticeExists(String FactionUUID) {
        return FactionStorage.FactionInfo.containsKey(FactionUUID + "=notice");
    }

    public static String GetFactionNotice(String FactionUUID) {
        if(!FactionStorage.FactionInfo.containsKey(FactionUUID + "=notice")) {
            return Lang.FACTION_DEFAULT_NOTICE;
        } else {
            return FactionStorage.FactionInfo.get(FactionUUID + "=notice");
        }
    }

    public static void SetFactionOutPostWarpLocation(String FactionUUID, String Chunkkey, Location loc, String OutPostName) {
        String location = SystemUtils.loc2string(loc);
        SetFactionOutPostName(FactionUUID, Chunkkey, OutPostName);
        JedisTempStorage.AddCommandToQueue("update:=:FactionInfo:=:add:=:" + FactionUUID + "=" + OutPostName + ":=:add:=:" + Main.ServerName + "===" + location);
    }

    public static void SetFactionOutPostName(String FactionUUID, String Chunkkey, String OutPostName) {
        JedisTempStorage.AddCommandToQueue("update:=:FactionInfo:=:add:=:" + FactionUUID + "=" + Chunkkey + ":=:add:=:" + OutPostName);
        RegisterFactionInfo(FactionUUID, Chunkkey);
    }

    public static void RemoveFactionNotice(String FactionUUID) {
        JedisTempStorage.AddCommandToQueue("update:=:FactionInfo:=:remove:=:" + FactionUUID + "=notice" + ":=:add:=:" + "D");
    }

    public static void SetFactionDesc(String FactionUUID, String factionDesc) {
        JedisTempStorage.AddCommandToQueue("update:=:FactionInfo:=:add:=:" + FactionUUID + "=desc" + ":=:add:=:" + factionDesc);
        RegisterFactionInfo(FactionUUID, "desc");
    }

    public static Boolean FactionDescExists(String FactionUUID) {
        return FactionStorage.FactionInfo.containsKey(FactionUUID + "=desc");
    }

    public static String GetFactionDesc(String FactionUUID) {
        if(!FactionStorage.FactionInfo.containsKey(FactionUUID + "=desc")) {
            return Lang.FACTION_DEFAULT_DESC;
        } else {
            return FactionStorage.FactionInfo.get(FactionUUID + "=desc");
        }
    }

    public static void RemoveFactionDesc(String FactionUUID) {
        JedisTempStorage.AddCommandToQueue("update:=:FactionInfo:=:remove:=:" + FactionUUID + "=desc" + ":=:add:=:" + "D");
    }

    public static void WarpLocation(String UUID, String ServerName, String Location, Boolean isExpire) {
        if(!isExpire) {
            JedisTempStorage.AddCommandToQueue("warplocation:=:" + UUID + ":=:" + ServerName + ":=:" + Location + ":=:notexpired");
        } else {
            JedisTempStorage.AddCommandToQueue("warplocation:=:" + UUID + ":=:" + ServerName + ":=:" + Location + ":=:expired");
        }
    }

    public static void RegisterFactionInfo(String FactionUUID, String type) {
        JedisTempStorage.AddCommandToQueue("update:=:FactionInfoList:=:add:=:" + FactionUUID + ":=:add:=:" + type);
    }
}
