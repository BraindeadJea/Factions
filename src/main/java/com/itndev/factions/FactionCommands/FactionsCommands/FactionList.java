package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.ValidChecker;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class FactionList {

    public static void FactionList(Player sender, String UUID, String[] args) {
        new Thread( () -> {
            if(args.length < 2) {
                com.itndev.factions.Utils.FactionList.FactionList.SendFactionTop(sender, 1);
                return;
            }
            if(!ValidChecker.instanceofNumber(args[1])) {
                SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 목록 &7(페이지번호)\n");
                return;
            }
            int page = Integer.parseInt(args[1]);
            com.itndev.factions.Utils.FactionList.FactionList.SendFactionTop(sender, page);
        }).start();
    }
}
