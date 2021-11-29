package com.itndev.factions.Utils.FactionList;

import com.itndev.factions.Faction;
import com.itndev.factions.Storage.FactionStorage;
import com.itndev.factions.Utils.CacheUtils;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.concurrent.*;

public class FactionList {

    public static ConcurrentHashMap<Integer, Faction> FactionTop = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Double> FactionBalCashTop = new ConcurrentHashMap<>();
    public static Integer FactionTopSize = 0;
    public static Long FactionTopLastRefresh = System.currentTimeMillis();

    public static void FactionTopExecute(Player p, int page) {
        if(FactionTopSize == 0) {
            new Thread( () -> {
                try {
                    if(BuildFactionTop().get(10000, TimeUnit.MILLISECONDS)) {
                        //Send Top
                        SendFactionTop(p, page);
                    }
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }
            }).start();
            return;
        }
        int time = (int) (System.currentTimeMillis()/1000);
        int lastrefresh = (int) (FactionTopLastRefresh/1000);
        if(time - lastrefresh > 3600) {
            new Thread( () -> {
                try {
                    if(BuildFactionTop().get(10000, TimeUnit.MILLISECONDS)) {
                        //Send Top
                        SendFactionTop(p, page);
                    }
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            SendFactionTop(p, page);
        }

    }

    public static void SendFactionTop(Player p, int temppage) {
        new Thread( () -> {
            int page = temppage;
            int scan = (page * 10) - 9;
            String temp = "";
            int maxpage = FactionTopSize/10 + 1;
            if(maxpage < page) {
                page = maxpage;
            }
            for(int b = scan; b < scan + 100; b++) {
                Faction faction = FactionTop.get(b);
                temp = temp + "&r&f" + faction.getFactionCapName() + " &8&l- &7" + FactionBalCashTop.get(faction.getFactionUUID()) + "원\n";
            }
            SystemUtils.sendmessage(p,"&r&m&l------------&r&3&l[ &f&o국가 목록 &3&l]&r&m&l------------\n" +
                    temp + "&8 - &f" + page + "&7/&f" + maxpage + "&8 -&r \n&r&m&l------------&r&3&l[ &f&o국가 목록 &3&l]&r&m&l------------\n");
        }).start();


    }


    public static CompletableFuture<Boolean> BuildFactionTop() {
        CompletableFuture<Boolean> isFinished = new CompletableFuture<>();
        new Thread( () -> {
            HashMap<String, Double> FactionBalTop = new HashMap<>();
            for(String FactionUUID : FactionStorage.FactionUUIDToFactionName.keySet()) {
                try {
                    Double tempcash = CacheUtils.getCachedBank(FactionUUID).get();
                    FactionBalTop.put(FactionUUID, tempcash);
                    FactionBalCashTop.put(FactionUUID, tempcash);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    isFinished.complete(false);
                }
            }
            for(String FactionUUID2 : FactionBalTop.keySet()) {
                Faction faction = new Faction();
                try {
                    faction.BuildFactionInfo(FactionUUID2);
                } catch (ExecutionException | InterruptedException | TimeoutException e) {
                    e.printStackTrace();
                    isFinished.complete(false);
                }
                if(FactionTop.isEmpty()) {
                    FactionTop.put(1, faction);
                    FactionTopSize = 1;
                }
                for(int k : FactionTop.keySet()) {
                    if(FactionBalTop.get(FactionTop.get(k).getFactionUUID()) < FactionBalTop.get(FactionUUID2)) {
                        for(int v = k; v <= FactionTopSize; v++) {
                            FactionTop.put(v + 1, FactionTop.get(v));
                        }
                        FactionTop.put(k, faction);
                        FactionTopSize = FactionTopSize + 1;
                    } else {
                        FactionTop.put(FactionTopSize + 1, faction);
                        FactionTopSize = FactionTopSize + 1;
                    }
                }
            }
            FactionTopLastRefresh = System.currentTimeMillis();
            isFinished.complete(true);
        }).start();
        return isFinished;
    }

}
