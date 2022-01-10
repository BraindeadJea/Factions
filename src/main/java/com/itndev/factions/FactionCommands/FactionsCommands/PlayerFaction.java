package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.UserInfoUtils;
import org.bukkit.entity.Player;

import java.util.Locale;

public class PlayerFaction {

    public static void PlayerFaction(Player sender, String UUID, String[] args) {
        new Thread( () -> {
            if(args.length < 2) {
                SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 소속 &7(이름)\n");
                return;
            }

            if(!UserInfoUtils.hasJoined(args[1])) {
                SystemUtils.sendfactionmessage(sender, "&r&f해당 유저는 서버에 접속한 적이 없습니다\n");
                return;
            }

            String TargetUUID = UserInfoUtils.getPlayerUUID(args[1].toLowerCase(Locale.ROOT));
            String TargetOriginName = UserInfoUtils.getPlayerOrginName(args[1].toLowerCase(Locale.ROOT));

            if(!FactionUtils.isInFaction(TargetUUID)) {
                SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + TargetOriginName + " (은)는 소속된 국가가 없습니다\n");
                return;
            }

            String TargetFactionName = FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUtils.getPlayerFactionUUID(TargetUUID)));

            SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + TargetOriginName + " 이의 소속국가 &7: &r&f" + TargetFactionName + " \n");
        }).start();
    }
}
