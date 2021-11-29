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

    public static ConcurrentHashMap<Integer, String> FactionTop = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Double> FactionBalCashTop = new ConcurrentHashMap<>();
    public static Integer FactionTopSize = 0;
    public static Long FactionTopLastRefresh = System.currentTimeMillis();

    public static void FactionTopExecute(Player p, int page) {
        if(FactionTopSize == 0) {
            new Thread( () -> {
                try {
                    if(BuildFactionTop().get()) {
                        //Send Top
                        SendFactionTop(p, page);
                    }
                } catch (InterruptedException | ExecutionException e) {
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
                if(FactionTop.containsKey(b)) {
                    break;
                }
                String factionuuid = FactionTop.get(b);
                temp = temp + "&r&f" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(factionuuid)) + " &8&l- &7" + FactionBalCashTop.get(factionuuid) + "원\n";
            }
            SystemUtils.sendmessage(p,"&r&m&l------------&r&3&l[ &f&o국가 목록 &3&l]&r&m&l------------\n" +
                    temp + "&8 - &f" + page + "&7/&f" + maxpage + "&8 -&r \n&r&m&l------------&r&3&l[ &f&o국가 목록 &3&l]&r&m&l------------\n");
        }).start();


    }


    public static CompletableFuture<Boolean> BuildFactionTop() {
        CompletableFuture<Boolean> isFinished = new CompletableFuture<>();
        new Thread( () -> {
            HashMap<String, Double> FactionBalTop = new HashMap<>();
            HashMap<Integer, String> FactionToptemp = new HashMap<>();
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
                if(FactionToptemp.isEmpty()) {
                    FactionToptemp.put(1, FactionUUID2);
                    FactionTopSize = 1;
                }
                for(int k : FactionToptemp.keySet()) {
                    Double bank1 = FactionBalTop.get(FactionToptemp.get(k));
                    Double bank2 = FactionBalTop.get(FactionUUID2);
                    if(bank1 < bank2) {
                        for(int v = k; v <= FactionTopSize; v++) {
                            FactionToptemp.put(v + 1, FactionToptemp.get(v));
                            FactionToptemp.remove(v);
                        }
                        FactionToptemp.put(k, FactionUUID2);
                    } else {
                        FactionToptemp.put(FactionTopSize + 1, FactionUUID2);
                    }
                    FactionTopSize = FactionTopSize + 1;
                }
            }
            for(int temp : FactionToptemp.keySet()) {
                FactionTop.put(temp, FactionToptemp.get(temp));
            }
            FactionTopLastRefresh = System.currentTimeMillis();
            isFinished.complete(true);
        }).start();
        return isFinished;
    }

}
