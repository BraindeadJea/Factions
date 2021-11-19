package com.itndev.factions.Storage.StorageIO;

import com.itndev.factions.Config.YamlConfig;
import com.itndev.factions.Storage.FactionStorage;
import com.itndev.factions.Storage.UserInfoStorage;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.Map;

public class UserInfoStorageIOManager {
    public static void restoreUserInfo() {
        YamlConfig UserInfostorage = new YamlConfig();
        UserInfostorage.SetTargetName("UserInfoStorage");
        try {
            UserInfostorage.CreateStorage();
            FileConfiguration Storage = UserInfostorage.getStorage();

            restorenameuuid(Storage);
            restoreuuidname(Storage);
            restorenamename(Storage);
            /*restoreFactionNameToFactionName(Storage);
            restoreFactionNameToFactionUUID(Storage);
            restoreFactionUUIDToFactionName(Storage);
            restoreFactionMember(Storage);
            restoreFactionRank(Storage);
            restorePlayerFaction(Storage);
            restoreFactionOutPost(Storage);
            restoreFactionToLand(Storage);
            restoreFactionWarpLocations(Storage);
            restoreLandToFaction(Storage);*/
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void SaveUserInfo() {
        YamlConfig userinfostorage = new YamlConfig();
        userinfostorage.SetTargetName("UserInfoStorage");
        try {
            userinfostorage.CreateStorage();
            userinfostorage.resetlocalstorage();

            FileConfiguration Storage = userinfostorage.getStorage();

            savenameuuid(Storage);
            saveuuidname(Storage);
            savenamename(Storage);

            userinfostorage.saveStorage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void savenameuuid(FileConfiguration Storage) {
        if(UserInfoStorage.nameuuid.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : UserInfoStorage.nameuuid.entrySet()) {
            Storage.set("nameuuid." + (String)entry.getKey(), entry.getValue());
        }
    }

    public static void restorenameuuid(FileConfiguration Storage) {
        if(!Storage.contains("nameuuid.")) {
            return;
        }
        Storage.getConfigurationSection("nameuuid.").getKeys(false).forEach(key -> {
            String v = Storage.get("nameuuid." + key).toString();
            UserInfoStorage.nameuuid.put(key, v);
        });
    }

    public static void saveuuidname(FileConfiguration Storage) {
        if(UserInfoStorage.uuidname.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : UserInfoStorage.uuidname.entrySet()) {
            Storage.set("uuidname." + (String)entry.getKey(), entry.getValue());
        }
    }

    public static void restoreuuidname(FileConfiguration Storage) {
        if(!Storage.contains("uuidname.")) {
            return;
        }
        Storage.getConfigurationSection("uuidname.").getKeys(false).forEach(key -> {
            String v = Storage.get("uuidname." + key).toString();
            UserInfoStorage.uuidname.put(key, v);
        });
    }

    public static void savenamename(FileConfiguration Storage) {
        if(UserInfoStorage.namename.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : UserInfoStorage.namename.entrySet()) {
            Storage.set("namename." + (String)entry.getKey(), entry.getValue());
        }
    }

    public static void restorenamename(FileConfiguration Storage) {
        if(!Storage.contains("namename.")) {
            return;
        }
        Storage.getConfigurationSection("namename.").getKeys(false).forEach(key -> {
            String v = Storage.get("namename." + key).toString();
            UserInfoStorage.namename.put(key, v);
        });
    }
}
