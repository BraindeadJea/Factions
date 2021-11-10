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


    public static HashMap<String, String> Timeout1info = new HashMap<>();
    public static HashMap<String, Integer> Timeout1 = new HashMap<>();

    public static HashMap<String, ArrayList<String>> Timeout2info = new HashMap<>();
    public static HashMap<String, Integer> Timeout2 = new HashMap<>();

    public static void TimeoutManager() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(String k : Timeout1.keySet()) {
                    int temp = Timeout1.get(k) - 1;
                    Timeout1.put(k, temp);
                    if(temp <= 0) {
                        Timeout1.remove(k);

                        String[] parts = k.split("%");

                        String FactionUUID = parts[0];

                        Timeout1info.remove(FactionUUID);

                        String playeruuid = parts[1];

                        FactionUtils.SendFactionMessage(playeruuid, playeruuid, "single", "&r&c" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + "&r&f 에 대한 해체수락이 만료되었습니다");
                    }
                }
                for(String k : Timeout2.keySet()) {
                    int temp = Timeout2.get(k) - 1;
                    Timeout2.put(k, temp);
                    if(temp <= 0) {
                        Timeout2.remove(k);

                        String parts[] = k.split("%");

                        String PlayerUUID = parts[0];
                        String FactionUUID = parts[1];

                        ArrayList<String> templist = Timeout2info.get(PlayerUUID);
                        if(!templist.isEmpty()) {
                            if(templist.contains(FactionUUID)) {
                                templist.remove(FactionUUID);
                                FactionUtils.SendFactionMessage(PlayerUUID, PlayerUUID, "single", "&r&c" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + "&r&f 에서 보낸 초대가 만료되었습니다");
                                if(templist.isEmpty()) {
                                    Timeout2info.remove(PlayerUUID);
                                } else {
                                    Timeout2info.put(PlayerUUID, templist);
                                }
                            }

                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0L, 20L);
    }

    public static void DeleteFactionTEMP(String FactionUUID, Player p) {

        //메세지
        //SystemUtils.sendmessage(p, "");

        Timeout1.put(FactionUUID + "%" + p.getUniqueId().toString(), 20);
        Timeout1info.put(FactionUUID, p.getUniqueId().toString());
    }

    public static void DeleteFaction() {


    }

}
