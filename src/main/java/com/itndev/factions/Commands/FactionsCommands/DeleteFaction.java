package com.itndev.factions.Commands.FactionsCommands;

import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.entity.Player;

public class DeleteFaction {
    public static void DeteleFaction(Player p, String FactionUUID) {
        String FactionName = FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID));
        SystemUtils.sendmessage(p, "&a&o&l[ &r&f국가 &a&o&l] &r&f국가 &c" + FactionName + " 을(를) 해체했습니다");
        JedisTempStorage.AddCommandToQueue("notify:=:" + p.getUniqueId() + ":=:" + "SIBAL" + ":=:" + "&r&f당신의 국가가 &c몰락&r&f했습니다" + ":=:" + "true");
        JedisTempStorage.AddCommandToQueue("notify:=:" + p.getUniqueId() + ":=:" + "all" + ":=:" + "&r&f국가 &c" + FactionName + " (이)가 &c몰락r&f했습니다" + ":=:" + "true");
        FactionUtils.DeleteFaction(FactionUtils.getPlayerFactionUUID(p.getUniqueId().toString()));
        Main.database.DeleteFactionName(FactionUUID);
        Main.database.DeleteFactionBank(FactionUUID);
        Main.database.DeleteFactionDTR(FactionUUID);
    }
}
