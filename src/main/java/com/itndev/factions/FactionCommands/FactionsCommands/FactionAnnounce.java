package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Config.Lang;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.entity.Player;

public class FactionAnnounce {

    public static void FactionAnnounce(Player sender, String UUID, String[] args) {
        new Thread( () -> {
            if(FactionUtils.isInFaction(UUID)) {
                String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                if(!FactionUtils.FactionNoticeExists(FactionUUID)) {
                    SystemUtils.sendfactionmessage(sender, "&a&o&l[ &f공지 &a&o&l] &r&f" + Lang.FACTION_DEFAULT_NOTICE);
                    return;
                }
                SystemUtils.sendrawfactionmessage(sender, SystemUtils.colorize("&a&o&l[ &r&f공지 &a&o&l] &r&f") + FactionUtils.GetFactionNotice(FactionUUID));
            } else {
                SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
            }
        }).start();
    }
}
