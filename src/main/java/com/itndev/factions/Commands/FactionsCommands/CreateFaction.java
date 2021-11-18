package com.itndev.factions.Commands.FactionsCommands;

import com.itndev.factions.Jedis.JedisManager;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import com.itndev.factions.MySQL.MySQLUtils;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.entity.Player;

import java.util.Locale;

public class CreateFaction {

    public static MySQLUtils sqlUtils = new MySQLUtils();

    public static void CreateFaction(Player p, String newFactionUUID, String FactionName) {
        SystemUtils.sendmessage(p, "&a&o&l[ &r&f국가 &a&o&l] &r&f새 국가 &c" + FactionName + " 을(를) 새웠습니다");
        JedisTempStorage.AddCommandToQueue("notify:=:" + p.getUniqueId() + ":=:" + "all" + ":=:" + "&a&o&l[ &r&f국가 &a&o&l] &r&f" + "&r&f새 국가 &c" + FactionName + " (이)가 새워졌습니다" + ":=:" + "true");
        FactionUtils.CreateFaction(p.getName(), newFactionUUID, FactionName);
        //Main.database.AddNewFactionName(FactionName, newFactionUUID);
        Main.database.CreateNewDTR(newFactionUUID, FactionName.toLowerCase(Locale.ROOT));
        Main.database.CreateNewBank(newFactionUUID, FactionName.toLowerCase(Locale.ROOT));
    }
}
