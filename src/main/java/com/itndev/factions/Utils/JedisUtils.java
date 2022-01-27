package com.itndev.factions.Utils;

import com.itndev.factions.Jedis.JedisManager;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class JedisUtils {
    private static String splitter = "/=&C&:&G&:&1&=/";
    private static String buffer = "-buffer-";

    @Deprecated
    public static Boolean HashMapUpdate(HashMap<String, String> map, String ServerName) {
        if (!map.isEmpty()) {
            for(int c = 1; c <= Integer.valueOf(map.get("MAXAMOUNT")); c++) {
                JedisManager.updatehashmap(map.get(String.valueOf(c)), ServerName);
            }
            return true;
        }
        return false;
    }

    public static String HashMap2String(ConcurrentHashMap<String, String> map) {
        String finalbuildstring = buffer + splitter;
        if(!map.isEmpty()) {
            int maxamount = Integer.valueOf(map.get("MAXAMOUNT"));
            for (int c = 1; c <= maxamount; c++) {
                if (c == maxamount) {
                    finalbuildstring = finalbuildstring + map.get(String.valueOf(c)) + splitter + buffer;
                } else {
                    finalbuildstring = finalbuildstring + map.get(String.valueOf(c)) + splitter;
                }
            }
        } else {
            return null;
        }
        return finalbuildstring;
    }

    public static HashMap<String, String> String2HashMap(String info) {
        HashMap<String, String> finalmap = new HashMap<>();
        if(!info.contains(splitter)) {
            if(info.length() > 0) {
                finalmap.put("1", info);
                finalmap.put("MAXAMOUNT", "1");
            }
            return finalmap;
        }
        String[] info_args = info.split(splitter);
        finalmap.put("MAXAMOUNT", String.valueOf(info_args.length - 1));
        for(int c = 0; c < info_args.length; c++) {
            finalmap.put(String.valueOf(c + 1), info_args[c]);
        }
        return finalmap;
    }
}
