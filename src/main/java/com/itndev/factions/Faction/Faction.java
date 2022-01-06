package com.itndev.factions.Faction;

import com.itndev.factions.Config.Lang;
import com.itndev.factions.Main;
import com.itndev.factions.Storage.FactionStorage;
import com.itndev.factions.Utils.CacheUtils;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.UserInfoUtils;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Faction {

    private Boolean isBuilt = false;

    private String FactionName = null;
    private String FactionCapName = null;
    private String FactionUUID = null;

    private ArrayList<String> FactionMembers = null;
    private Double Bank = null;
    private Double DTR = null;
    private Integer ClaimLand = null;

    public Faction(String factionUUID2) {
        FactionUUID = factionUUID2;
        try {
            BuildFactionInfo(null);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void BuildFactionInfo(String FactionUUID2) throws ExecutionException, InterruptedException, TimeoutException {
        if(FactionUUID2 == null) {
            FactionUUID2 = FactionUUID;
        }
        CompletableFuture<Double> FutureBank = CacheUtils.getCachedBank(FactionUUID2);
        CompletableFuture<Double> FutureDTR = CacheUtils.getCachedDTR(FactionUUID2);

        FactionName = FactionUtils.getFactionName(FactionUUID2);
        FactionUUID = FactionUUID2;
        FactionCapName = FactionUtils.getCappedFactionName(FactionName);

        FactionMembers = FactionUtils.getFactionMember(FactionUUID);
        Bank = FutureBank.get(40, TimeUnit.MILLISECONDS);
        DTR = FutureDTR.get(40, TimeUnit.MILLISECONDS);
        if(FactionStorage.FactionToLand.containsKey(FactionUUID2)) {
            ClaimLand = FactionStorage.FactionToLand.get(FactionUUID2).size();
        } else {
            ClaimLand = 0;
        }
        isBuilt = true;
    }

    public void DangerousBuildFactionInfo(String FactionUUID2) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<Double> FutureBank = CacheUtils.getCachedBank(FactionUUID2);
        CompletableFuture<Double> FutureDTR = CacheUtils.getCachedDTR(FactionUUID2);

        FactionName = FactionUtils.getFactionName(FactionUUID2);
        FactionUUID = FactionUUID2;
        FactionCapName = FactionUtils.getCappedFactionName(FactionName);

        FactionMembers = FactionUtils.getFactionMember(FactionUUID);
        Bank = FutureBank.get();
        DTR = FutureDTR.get();
        if(FactionStorage.FactionToLand.containsKey(FactionUUID2)) {
            ClaimLand = FactionStorage.FactionToLand.get(FactionUUID2).size();
        } else {
            ClaimLand = 0;
        }
        isBuilt = true;
    }

    public String getFactionName() {
        if(!isBuilt) {
            return null;
        }
        return FactionName;
    }

    public String getFactionUUID() {
        if(!isBuilt) {
            return null;
        }
        return FactionUUID;
    }

    public String getFactionCapName() {
        if(!isBuilt) {
            return null;
        }
        return FactionCapName;
    }

    public Double getBank() {
        if(!isBuilt) {
            return null;
        }
        return Bank;
    }

    public Double getDTR() {
        if(!isBuilt) {
            return null;
        }
        return DTR;
    }

    public Integer getClaimLand() {
        if(!isBuilt) {
            return null;
        }
        return ClaimLand;
    }

    public String getFormattedMembers(String Rank) {
        if(!isBuilt) {
            return null;
        }
        String Formatted = "";
        int num = 0;
        //) {
        ArrayList<String> RankedMemebers = new ArrayList<>();
        for(String uuids : FactionMembers) {
            if(FactionUtils.getPlayerRank(uuids).equalsIgnoreCase(Rank)) {
                RankedMemebers.add(uuids);
            }

        }
        for(String uuids : RankedMemebers) {
            num = num + 1;
            if (FactionMembers.size() != num) {
                Formatted = Formatted + "&7" + UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(uuids)) + "&8&l, &r";
            } else {
                Formatted = Formatted + "&7" + UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(uuids)) + "&r";
            }
        }
        return Formatted;
    }


    public String getFactionDesc() {
        if(!isBuilt) {
            return null;
        }
        if(FactionUtils.FactionDescExists(FactionUUID)) {
            return FactionUtils.GetFactionDesc(FactionUUID);
        } else {
            return Lang.FACTION_DEFAULT_DESC;
        }
    }

    public ArrayList<String> getFactionMembers() {
        if(!isBuilt) {
            return null;
        }
        return FactionMembers;
    }
}
