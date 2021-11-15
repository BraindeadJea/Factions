package com.itndev.factions.Storage;

import com.itndev.factions.Main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.*;

public class CachedStorage {

    public static ConcurrentHashMap<String, Double> CachedDTR = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Double> CachedBank = new ConcurrentHashMap<>();



    public static void JedisCacheSync(String[] args) {
        if(args[6].equalsIgnoreCase(Main.ServerName)) {
            return;
        }
        if(args[1].equalsIgnoreCase("CachedDTR")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값

                if(args[4].equalsIgnoreCase("add")) {

                    if(CachedStorage.CachedDTR.containsKey(key)) {
                        CachedStorage.CachedDTR.remove(key);
                        CachedStorage.CachedDTR.put(key, Double.parseDouble(value));
                    } else {
                        CachedStorage.CachedDTR.put(key, Double.parseDouble(value));
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {

                    if(CachedStorage.CachedDTR.containsKey(key)) {

                        CachedStorage.CachedDTR.remove(key);

                        //할거 없다
                    } else {
                        //할거 없다
                    }
                }

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                if(CachedStorage.CachedDTR.containsKey(key)) {
                    CachedStorage.CachedDTR.remove(key);
                }
            }
        } else if(args[1].equalsIgnoreCase("CachedBank")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값

                if(args[4].equalsIgnoreCase("add")) {

                    if(CachedStorage.CachedBank.containsKey(key)) {
                        CachedStorage.CachedBank.remove(key);
                        CachedStorage.CachedBank.put(key, Double.parseDouble(value));
                    } else {
                        CachedStorage.CachedBank.put(key, Double.parseDouble(value));
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {

                    if(CachedStorage.CachedBank.containsKey(key)) {

                        CachedStorage.CachedBank.remove(key);

                        //할거 없다
                    } else {
                        //할거 없다
                    }
                }

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                if(CachedStorage.CachedBank.containsKey(key)) {
                    CachedStorage.CachedBank.remove(key);
                }
            }
        }
    }
}
