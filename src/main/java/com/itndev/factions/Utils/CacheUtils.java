package com.itndev.factions.Utils;

import com.itndev.factions.Jedis.JedisManager;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import com.itndev.factions.Storage.CachedStorage;

import java.util.concurrent.CompletableFuture;
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

    public static CompletableFuture<Double> getCachedDTR(String FactionUUID) {
        CompletableFuture<Double> FinalDTR = new CompletableFuture<>();
        if(CachedStorage.CachedDTR.containsKey(FactionUUID)) {
            FinalDTR.complete(CachedStorage.CachedDTR.get(FactionUUID));
        } else {
            FinalDTR = Main.database.GetFactionDTR(FactionUUID);
        }
        return FinalDTR;
    }

    public static CompletableFuture<Double> getCachedBank(String FactionUUID) {
        CompletableFuture<Double> FinalBank = new CompletableFuture<>();
        if(CachedStorage.CachedBank.containsKey(FactionUUID)) {
            FinalBank.complete(CachedStorage.CachedBank.get(FactionUUID));
        } else {
            FinalBank = Main.database.GetFactionBank(FactionUUID);
        }
        return FinalBank;
    }
}
