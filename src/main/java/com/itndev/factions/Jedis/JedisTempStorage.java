package com.itndev.factions.Jedis;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class JedisTempStorage {

    public static ConcurrentHashMap<String, String> TempCommandQueue = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, String> TempChatQueue = new ConcurrentHashMap<>();


    public static void AddCommandToQueue(String command) {
        if(TempCommandQueue.isEmpty()) {
            TempCommandQueue.put("MAXAMOUNT", "1");
            TempCommandQueue.put("1", command);
        } else {
            double num = Double.valueOf(TempCommandQueue.get("MAXAMOUNT"));
            TempCommandQueue.put("MAXAMOUNT", String.valueOf(num));
            TempCommandQueue.put(String.valueOf(num), command);
        }
    }


    public static void RemovePlayerFromFaction(Player p, String FactionUUID) {

    }

}
