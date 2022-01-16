package com.itndev.factions.Storage.StorageIO;

import com.itndev.factions.Config.YamlConfig;
import com.itndev.factions.Storage.FactionStorage;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FactionStorageIOManager {

    public static void restoreFactionInfo() {
        YamlConfig factionstorage = new YamlConfig();
        factionstorage.SetTargetName("FactionStorage");
        try {
            factionstorage.CreateStorage();
            FileConfiguration Storage = factionstorage.getStorage();

            restoreFactionNameToFactionName(Storage);
            restoreFactionNameToFactionUUID(Storage);
            restoreFactionUUIDToFactionName(Storage);
            restoreFactionMember(Storage);
            restoreFactionRank(Storage);
            restorePlayerFaction(Storage);
            restoreFactionOutPost(Storage);
            restoreFactionOutPostList(Storage);
            restoreFactionToLand(Storage);
            restoreFactionToOutPost(Storage);
            restoreFactionWarpLocations(Storage);
            restoreLandToFaction(Storage);
            restoreOutPostToFaction(Storage);
            restoreFactionInfo(Storage);
            restoreFactionInfoList(Storage);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void SaveFactionInfo() {
        YamlConfig factionstorage = new YamlConfig();
        factionstorage.SetTargetName("FactionStorage");
        try {
            factionstorage.CreateStorage();
            factionstorage.resetlocalstorage();

            FileConfiguration Storage = factionstorage.getStorage();

            saveFactionNameToFactionName(Storage);
            saveFactionNameToFactionUUID(Storage);
            saveFactionUUIDToFactionName(Storage);
            saveFactionMember(Storage);
            saveFactionRank(Storage);
            savePlayerFaction(Storage);
            saveFactionOutPost(Storage);
            saveFactionOutPostList(Storage);
            saveFactionToLand(Storage);
            saveFactionToOutPost(Storage);
            saveFactionWarpLocations(Storage);
            saveLandToFaction(Storage);
            saveOutPostToFaction(Storage);
            saveFactionInfo(Storage);
            saveFactionInfoList(Storage);
            factionstorage.saveStorage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveFactionNameToFactionUUID(FileConfiguration Storage) {
        if(FactionStorage.FactionNameToFactionUUID.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : FactionStorage.FactionNameToFactionUUID.entrySet()) {
            Storage.set("FactionNameToFactionUUID." + (String)entry.getKey(), entry.getValue());
        }
    }
    public static void restoreFactionNameToFactionUUID(FileConfiguration Storage) {
        if(!Storage.contains("FactionNameToFactionUUID.")) {
            return;
        }
        Storage.getConfigurationSection("FactionNameToFactionUUID.").getKeys(false).forEach(key -> {
            String v = Storage.get("FactionNameToFactionUUID." + key).toString();
            FactionStorage.FactionNameToFactionUUID.put(key, v);
        });
    }
    public static void saveFactionNameToFactionName(FileConfiguration Storage) {
        if(FactionStorage.FactionNameToFactionName.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : FactionStorage.FactionNameToFactionName.entrySet()) {
            Storage.set("FactionNameToFactionName." + (String)entry.getKey(), entry.getValue());
        }
    }
    public static void restoreFactionNameToFactionName(FileConfiguration Storage) {
        if(!Storage.contains("FactionNameToFactionName.")) {
            return;
        }
        Storage.getConfigurationSection("FactionNameToFactionName.").getKeys(false).forEach(key -> {
            String v = Storage.get("FactionNameToFactionName." + key).toString();
            FactionStorage.FactionNameToFactionName.put(key, v);
        });
    }
    public static void saveFactionUUIDToFactionName(FileConfiguration Storage) {
        if(FactionStorage.FactionUUIDToFactionName.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : FactionStorage.FactionUUIDToFactionName.entrySet()) {
            Storage.set("FactionUUIDToFactionName." + (String)entry.getKey(), entry.getValue());
        }
    }
    public static void restoreFactionUUIDToFactionName(FileConfiguration Storage) {
        if(!Storage.contains("FactionUUIDToFactionName.")) {
            return;
        }
        Storage.getConfigurationSection("FactionUUIDToFactionName.").getKeys(false).forEach(key -> {
            String v = Storage.get("FactionUUIDToFactionName." + key).toString();
            FactionStorage.FactionUUIDToFactionName.put(key, v);
        });
    }
    public static void savePlayerFaction(FileConfiguration Storage) {
        if(FactionStorage.PlayerFaction.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : FactionStorage.PlayerFaction.entrySet()) {
            Storage.set("PlayerFaction." + (String)entry.getKey(), entry.getValue());
        }
    }
    public static void restorePlayerFaction(FileConfiguration Storage) {
        if(!Storage.contains("PlayerFaction.")) {
            return;
        }
        Storage.getConfigurationSection("PlayerFaction.").getKeys(false).forEach(key -> {
            String v = Storage.get("PlayerFaction." + key).toString();
            FactionStorage.PlayerFaction.put(key, v);
        });
    }
    public static void saveFactionRank(FileConfiguration Storage) {
        if(FactionStorage.FactionRank.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : FactionStorage.FactionRank.entrySet()) {
            Storage.set("FactionRank." + (String)entry.getKey(), entry.getValue());
        }
    }
    public static void restoreFactionRank(FileConfiguration Storage) {
        if(!Storage.contains("FactionRank.")) {
            return;
        }
        Storage.getConfigurationSection("FactionRank.").getKeys(false).forEach(key -> {
            String v = Storage.get("FactionRank." + key).toString();
            FactionStorage.FactionRank.put(key, v);
        });
    }
    public static void saveFactionMember(FileConfiguration Storage) {
        if(FactionStorage.FactionMember.isEmpty()) {
            return;
        }
        for (Map.Entry<String, ArrayList<String>> entry : FactionStorage.FactionMember.entrySet()) {
            Storage.set("FactionMember." + (String) entry.getKey(), entry.getValue());
        }
    }
    public static void restoreFactionMember(FileConfiguration Storage) {
        if(!Storage.contains("FactionMember.")) {
            return;
        }
        Storage.getConfigurationSection("FactionMember.").getKeys(false).forEach(key -> {
            ArrayList<String> v = (ArrayList<String>)Storage.get("FactionMember." + key);
            FactionStorage.FactionMember.put(key, v);
        });
    }
    public static void saveFactionToLand(FileConfiguration Storage) {
        if(FactionStorage.FactionToLand.isEmpty()) {
            return;
        }
        for (Map.Entry<String, ArrayList<String>> entry : FactionStorage.FactionToLand.entrySet()) {
            Storage.set("FactionToLand." + (String) entry.getKey(), entry.getValue());
        }
    }
    public static void restoreFactionToLand(FileConfiguration Storage) {
        if(!Storage.contains("FactionToLand.")) {
            return;
        }
        Storage.getConfigurationSection("FactionToLand.").getKeys(false).forEach(key -> {
            ArrayList<String> v = (ArrayList<String>)Storage.get("FactionToLand." + key);
            FactionStorage.FactionToLand.put(key, v);
        });
    }
    public static void saveLandToFaction(FileConfiguration Storage) {
        if(FactionStorage.LandToFaction.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : FactionStorage.LandToFaction.entrySet()) {
            Storage.set("LandToFaction." + (String)entry.getKey(), entry.getValue());
        }
    }
    public static void restoreLandToFaction(FileConfiguration Storage) {
        if(!Storage.contains("LandToFaction.")) {
            return;
        }
        Storage.getConfigurationSection("LandToFaction.").getKeys(false).forEach(key -> {
            String v = Storage.get("LandToFaction." + key).toString();
            FactionStorage.LandToFaction.put(key, v);
        });
    }
    public static void saveFactionToOutPost(FileConfiguration Storage) {
        if(FactionStorage.FactionToOutPost.isEmpty()) {
            return;
        }
        for (Map.Entry<String, ArrayList<String>> entry : FactionStorage.FactionToOutPost.entrySet()) {
            Storage.set("FactionToOutPost." + (String) entry.getKey(), entry.getValue());
        }
    }
    public static void restoreFactionToOutPost(FileConfiguration Storage) {
        if(!Storage.contains("FactionToOutPost.")) {
            return;
        }
        Storage.getConfigurationSection("FactionToOutPost.").getKeys(false).forEach(key -> {
            ArrayList<String> v = (ArrayList<String>)Storage.get("FactionToOutPost." + key);
            FactionStorage.FactionToOutPost.put(key, v);
        });
    }
    public static void saveOutPostToFaction(FileConfiguration Storage) {
        if(FactionStorage.OutPostToFaction.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : FactionStorage.OutPostToFaction.entrySet()) {
            Storage.set("OutPostToFaction." + (String)entry.getKey(), entry.getValue());
        }
    }
    public static void restoreOutPostToFaction(FileConfiguration Storage) {
        if(!Storage.contains("OutPostToFaction.")) {
            return;
        }
        Storage.getConfigurationSection("OutPostToFaction.").getKeys(false).forEach(key -> {
            String v = Storage.get("OutPostToFaction." + key).toString();
            FactionStorage.OutPostToFaction.put(key, v);
        });
    }
    public static void saveFactionOutPost(FileConfiguration Storage) {
        if(FactionStorage.FactionOutPost.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : FactionStorage.FactionOutPost.entrySet()) {
            Storage.set("FactionOutPost." + (String)entry.getKey(), entry.getValue());
        }
    }
    public static void restoreFactionOutPost(FileConfiguration Storage) {
        if(!Storage.contains("FactionOutPost.")) {
            return;
        }
        Storage.getConfigurationSection("FactionOutPost.").getKeys(false).forEach(key -> {
            String v = Storage.get("FactionOutPost." + key).toString();
            FactionStorage.FactionOutPost.put(key, v);
        });
    }
    public static void saveFactionOutPostList(FileConfiguration Storage) {
        if(FactionStorage.FactionOutPostList.isEmpty()) {
            return;
        }
        for (Map.Entry<String, ArrayList<String>> entry : FactionStorage.FactionOutPostList.entrySet()) {
            Storage.set("FactionOutPostList." + (String) entry.getKey(), entry.getValue());
        }
    }
    public static void restoreFactionOutPostList(FileConfiguration Storage) {
        if(!Storage.contains("FactionOutPostList.")) {
            return;
        }
        Storage.getConfigurationSection("FactionOutPostList.").getKeys(false).forEach(key -> {
            ArrayList<String> v = (ArrayList<String>)Storage.get("FactionOutPostList." + key);
            FactionStorage.FactionOutPostList.put(key, v);
        });
    }
    public static void saveFactionWarpLocations(FileConfiguration Storage) {
        if(FactionStorage.FactionWarpLocations.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : FactionStorage.FactionWarpLocations.entrySet()) {
            Storage.set("FactionWarpLocations." + (String)entry.getKey(), entry.getValue());
        }
    }
    public static void restoreFactionWarpLocations(FileConfiguration Storage) {
        if(!Storage.contains("FactionWarpLocations.")) {
            return;
        }
        Storage.getConfigurationSection("FactionWarpLocations.").getKeys(false).forEach(key -> {
            String v = Storage.get("FactionWarpLocations." + key).toString();
            FactionStorage.FactionWarpLocations.put(key, v);
        });
    }
    public static void saveFactionInfo(FileConfiguration Storage) {
        if(FactionStorage.FactionInfo.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : FactionStorage.FactionInfo.entrySet()) {
            Storage.set("FactionInfo." + (String)entry.getKey(), entry.getValue());
        }
    }
    public static void restoreFactionInfo(FileConfiguration Storage) {
        if(!Storage.contains("FactionInfo.")) {
            return;
        }
        Storage.getConfigurationSection("FactionInfo.").getKeys(false).forEach(key -> {
            String v = Storage.get("FactionInfo." + key).toString();
            FactionStorage.FactionInfo.put(key, v);
        });
    }
    public static void saveFactionInfoList(FileConfiguration Storage) {
        if(FactionStorage.FactionInfoList.isEmpty()) {
            return;
        }
        for (Map.Entry<String, ArrayList<String>> entry : FactionStorage.FactionInfoList.entrySet()) {
            Storage.set("FactionInfoList." + (String) entry.getKey(), entry.getValue());
        }
    }
    public static void restoreFactionInfoList(FileConfiguration Storage) {
        if (!Storage.contains("FactionInfoList.")) {
            return;
        }
        Storage.getConfigurationSection("FactionInfoList.").getKeys(false).forEach(key -> {
            ArrayList<String> v = (ArrayList<String>) Storage.get("FactionInfoList." + key);
            FactionStorage.FactionInfoList.put(key, v);
        });
    }
}

