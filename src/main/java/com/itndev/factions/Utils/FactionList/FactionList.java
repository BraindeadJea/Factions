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
