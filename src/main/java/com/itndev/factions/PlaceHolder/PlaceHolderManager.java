package com.itndev.factions.PlaceHolder;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Utils.CacheUtils;
import com.itndev.factions.Utils.FactionUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PlaceHolderManager extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "itndevfactions";
    }

    @Override
    public String getAuthor() {
        return "Itndev";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        if(p == null) {
            return "";
        }
        String UUID = p.getUniqueId().toString();
        if(params.equalsIgnoreCase("factionname")) {
            if(FactionUtils.isInFaction(UUID)) {
                return FactionUtils.getFactionName(FactionUtils.getPlayerFactionUUID(UUID));
            } else {
                return "NOT_IN_FACTION";
            }
        } else if(params.equalsIgnoreCase("rank")) {
            return FactionUtils.getPlayerLangRank(UUID);
        } else if(params.equalsIgnoreCase("rawrank")) {
            return FactionUtils.getPlayerRank(UUID);
        } else if(params.equalsIgnoreCase("colorizedfactionname")) {
            if(FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Nomad)) {
                return "&7무소속";
            } else {
                return "&a" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUtils.getPlayerFactionUUID(UUID)));
            }
        } else if(params.equalsIgnoreCase("formatfactionname")) {
            if(FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Nomad)) {
                return "";
            } else {
                return "&f[ &r&a" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUtils.getPlayerFactionUUID(UUID))) + " &r&f] ";
            }
        } else if(params.equalsIgnoreCase("formatrank")) {
            if(FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Nomad)) {
                return "";
            } else {
                return "&r&7" + FactionUtils.getPlayerLangRank(UUID) + " ";
            }
        } else if(params.equalsIgnoreCase("factiondtr")) {
            if(FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Nomad)) {
                return "";
            } else {
                try {
                    return "" + CacheUtils.getCachedDTR(FactionUtils.getPlayerFactionUUID(UUID)).get(40, TimeUnit.MILLISECONDS);
                } catch (TimeoutException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } else if(params.equalsIgnoreCase("factionbank")) {
            if(FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Nomad)) {
                return "";
            } else {
                try {
                    return "" + CacheUtils.getCachedBank(FactionUtils.getPlayerFactionUUID(UUID)).get(40, TimeUnit.MILLISECONDS);
                } catch (TimeoutException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}