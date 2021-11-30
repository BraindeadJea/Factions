package com.itndev.factions.Utils.FactionList;

import com.itndev.factions.Faction;
import com.itndev.factions.Main;
import com.itndev.factions.Storage.FactionStorage;
import com.itndev.factions.Utils.CacheUtils;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

public class FactionList {

    public static ConcurrentHashMap<Integer, String> FactionTop = new ConcurrentHashMap<>();
    public static HashMap<String, Double> FactionBalTop = new HashMap<>();
    public static LinkedHashMap<String, Double> LinkedFactionBalTop = new LinkedHashMap<>();
    public static Integer FactionTopSize = 0;
    public static Long FactionTopLastRefresh = System.currentTimeMillis();

    public static void FactionTopExecute() {
        new Thread( () -> {
            /*
            if(FactionTopSize == 0) {
                new Thread( () -> {
                    try {
                        if(BuildFactionTop().get()) {
                            //Send Top
                            SendFactionTop(p, page);
                        } else {
                            System.out.println("=================================실패");
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
                        if(BuildFactionTop().get()) {
                            //Send Top
                            SendFactionTop(p, page);
                        } else {
                            System.out.println("=================================실패");
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                SendFactionTop(p, page);
            }
            */
            try {
                ResultSet rs = Main.hikariCP.getHikariConnection().prepareStatement("SELECT * FROM FactionBank").executeQuery();
                while (rs.next()) {
                    FactionBalTop.put(
                            rs.getString("FactionUUID"),
                            Double.parseDouble(rs.getString("FactionBank"))
                    );
                    CacheUtils.UpdateCachedBank(rs.getString("FactionUUID"), Double.parseDouble(rs.getString("FactionBank")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void SendFactionTop(Player p, int temppage) {
        new Thread( () -> {
            int maxpage = FactionTopSize/10 + 1;
            int page = temppage;
            if(maxpage < page) {
                page = maxpage;
            }
            int scan = (page * 10) - 9;
            String temp = "";
            for(int i = scan; i <= scan + 9; i++) {
                if(!FactionTop.containsKey(i)) {
                    break;
                }
                temp = temp + "&r&f" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionTop.get(i))) + " &8&l- &7" + FactionBalTop.get(FactionTop.get(i)) + "원\n";
            }
            SystemUtils.sendmessage(p,"&r&m&l------------&r&3&l[ &f&o국가 목록 &3&l]&r&m&l------------\n" +
                    temp + "&8 - &f" + page + "&7/&f" + maxpage + "&8 -&r \n&r&m&l------------&r&3&l[ &f&o국가 목록 &3&l]&r&m&l------------\n");
        }).start();
    }


    public static void BuildFactionTop() {
        new BukkitRunnable() {
            @Override
            public void run() {
                sortByValue(FactionBalTop);

                /*HashMap<Integer, String> FactionToptemp = new HashMap<>();
                HashMap<String, Double> FactionBalToptemp = FactionBalTop;
                Integer FactionTopSizeTemp = 0;
                for(String FactionUUID2 : FactionBalToptemp.keySet()) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(FactionToptemp.isEmpty()) {
                        FactionToptemp.put(1, FactionUUID2);
                        FactionTopSizeTemp = 1;
                    } else {
                        for (int k = 1; k <= FactionTopSizeTemp; k++) {
                            Double bank1 = FactionBalToptemp.get(FactionToptemp.get(k));
                            Double bank2 = FactionBalToptemp.get(FactionUUID2);
                            if (bank1 < bank2) {
                                HashMap<Integer, String> temp32u48 = FactionToptemp;
                                for (int v = k; v <= FactionTopSizeTemp; v++) {
                                    FactionToptemp.put(v + 1, temp32u48.get(v));
                                }
                                FactionToptemp.put(k, FactionUUID2);
                            } else {
                                FactionToptemp.put(FactionTopSizeTemp + 1, FactionUUID2);
                            }
                            FactionTopSizeTemp = FactionTopSizeTemp + 1;
                        }
                    }
                }
                for(int temp = 1; temp <= FactionTopSizeTemp; temp++) {
                    FactionTop.put(temp, FactionToptemp.get(temp));
                }
                FactionTopSize = FactionTopSizeTemp;
                FactionTopLastRefresh = System.currentTimeMillis();*/


            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public static void sortByValue(HashMap<String, Double> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Double> > list =
                new LinkedList<Map.Entry<String, Double> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Double> >() {
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        FactionTopSize = 0;
        Integer FactionTopSizeTemp = list.size();
        for (Map.Entry<String, Double> aa : list) {
            FactionTop.put(FactionTopSizeTemp - FactionTopSize, aa.getKey());
            FactionTopSize = FactionTopSize + 1;
        }
    }

}
