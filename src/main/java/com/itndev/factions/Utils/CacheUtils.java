package com.itndev.factions.Utils;

import com.itndev.factions.Jedis.JedisManager;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import com.itndev.factions.Storage.CachedStorage;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CacheUtils {

    public static void UpdateCachedDTR(String FactionUUID, Double DTR) {
        JedisTempStorage.AddCommandToQueue("update:=:CachedDTR:=:add:=:" + FactionUUID + ":=:add:=:" + String.valueOf(DTR) + ":=:" + Main.ServerName);
        CachedStorage.CachedDTR.put(FactionUUID, DTR);
    }

    public static void UpdateCachedBank(String FactionUUID, Double Bank) {
        JedisTempStorage.AddCommandToQueue("update:=:CachedBank:=:add:=:" + FactionUUID + ":=:add:=:" + String.valueOf(Bank) + ":=:" + Main.ServerName);
        CachedStorage.CachedBank.put(FactionUUID, Bank);
    }

    public static void removeCachedDTR(String FactionUUID) {
        JedisTempStorage.AddCommandToQueue("update:=:CachedDTR:=:remove:=:" + FactionUUID + ":=:add:=:" + String.valueOf(0) + ":=:" + Main.ServerName);
        CachedStorage.CachedDTR.remove(FactionUUID);
    }

    public static void removeCachedBank(String FactionUUID) {
        JedisTempStorage.AddCommandToQueue("update:=:CachedBank:=:remove:=:" + FactionUUID + ":=:add:=:" + String.valueOf(0) + ":=:" + Main.ServerName);
        CachedStorage.CachedBank.remove(FactionUUID);
    }

    public static Double getCachedDTR(String FactionUUID) {
        if(CachedStorage.CachedDTR.containsKey(FactionUUID)) {
            return CachedStorage.CachedDTR.get(FactionUUID);
        } else {
            try {
                Double FinalDTR = Main.database.GetFactionDTR(FactionUUID).get(40, TimeUnit.MILLISECONDS);
                //CachedStorage.CachedDTR.put(FactionUUID, FinalDTR);
                UpdateCachedDTR(FactionUUID, FinalDTR);
                return FinalDTR;
            } catch (InterruptedException | ExecutionException | TimeoutException e ){
                e.printStackTrace();
                return null;
            }

        }
    }

    public static Double getCachedBank(String FactionUUID) {
        if(CachedStorage.CachedBank.containsKey(FactionUUID)) {
            return CachedStorage.CachedBank.get(FactionUUID);
        } else {
            try {
                Double FinalBank = Main.database.GetFactionBank(FactionUUID).get(40, TimeUnit.MILLISECONDS);
                UpdateCachedBank(FactionUUID, FinalBank);
                return FinalBank;
            } catch (InterruptedException | ExecutionException | TimeoutException e ){
                e.printStackTrace();
                return null;
            }
        }
    }
}
