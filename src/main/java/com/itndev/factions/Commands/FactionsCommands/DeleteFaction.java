package com.itndev.factions.Commands.FactionsCommands;

import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.entity.Player;

public class DeleteFaction {
    public static void DeteleFaction(Player p, String FactionName) {
        SystemUtils.sendmessage(p, "&a&o&l[ &r&f국가 &a&o&l] &r&f국가 &c" + FactionName + " 을(를) 해체했습니다");
        JedisTempStorage.AddCommandToQueue("notify:=:" + p.getUniqueId() + ":=:" + "all" + ":=:" + "&r&f새 국가 &c" + FactionName + " (이)가 새워졌습니다" + ":=:" + "true");
        String FactionUUID = FactionUtils.getPlayerFactionUUID(p.getUniqueId().toString());
        FactionUtils.DeleteFaction(FactionUtils.getPlayerFactionUUID(p.getUniqueId().toString()));
        Main.database.DeleteFactionName(FactionUUID);
    }
}
