package com.itndev.factions.PlaceHolder;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Utils.FactionUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

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
        return "0.4.1";
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
                return "&a" + FactionUtils.getFactionName(FactionUtils.getPlayerFactionUUID(UUID));
            }
        }
        return null;
    }
}