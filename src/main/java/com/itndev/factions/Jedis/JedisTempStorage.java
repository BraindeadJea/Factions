package com.itndev.factions.Jedis;

import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class JedisTempStorage {

    public static final ConcurrentHashMap<String, String> TempCommandQueue = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, String> TempChatQueue = new ConcurrentHashMap<>();


    public static void AddCommandToQueue(String command) {
        if(!TempCommandQueue.containsKey("MAXAMOUNT")) {
            TempCommandQueue.put("MAXAMOUNT", "1");
            TempCommandQueue.put("1", command);
        } else {
            int num = Integer.valueOf(TempCommandQueue.get("MAXAMOUNT"));
            TempCommandQueue.put("MAXAMOUNT", String.valueOf(num + 1));
            TempCommandQueue.put(String.valueOf(num + 1), command);
        }
    }

    public static void AddCommandToQueueFix(String command, String nothing) {
        if(!TempCommandQueue.containsKey("MAXAMOUNT")) {
            TempCommandQueue.put("MAXAMOUNT", "1");
            TempCommandQueue.put("1", command);
        } else {
            int num = Integer.valueOf(TempCommandQueue.get("MAXAMOUNT"));
            TempCommandQueue.put("MAXAMOUNT", String.valueOf(num + 1));
            TempCommandQueue.put(String.valueOf(num + 1), command);
        }
    }


    public static void RemovePlayerFromFaction(Player p, String FactionUUID) {

    }

}
