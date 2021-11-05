package com.itndev.factions.Commands.FactionsCommands;

import com.itndev.factions.Main;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

public class FactionTimeOut {

    public static HashMap<String, Integer> Timeout1 = new HashMap<>();

    public static void TimeoutManager() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(String k : Timeout1.keySet()) {
                    int temp = Timeout1.get(k) - 1;
                    Timeout1.put(k, temp - 1);
                    if(temp <= 0) {
                        Timeout1.remove(k);
                        String[] parts = k.split(":=:");

                        String FactionUUID = parts[0];

                        String playeruuid = parts[1];

                        FactionUtils.SendFactionMessage(playeruuid, playeruuid, "single", "&r&c" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + "&r&f 에 대한 해체수락이 만료되었습니다");
                    }
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0L, 20L);
    }

    public static void DeleteFactionTEMP(String FactionUUID, Player p) {

        //메세지
        SystemUtils.sendmessage(p, "");

        Timeout1.put(FactionUUID + ":=:" + p.getUniqueId(), 30);
    }

    public static void DeleteFaction() {

    }

}
