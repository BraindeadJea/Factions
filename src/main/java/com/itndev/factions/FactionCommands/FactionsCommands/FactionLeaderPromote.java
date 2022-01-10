package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.UserInfoUtils;
import org.bukkit.entity.Player;

import java.util.Locale;

public class FactionLeaderPromote {

    public static void FactionLeaderPromote(Player sender, String UUID, String[] args) {
        new Thread( () -> {
            if(args.length < 2) {
                SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 소속 &7(이름)\n");
                return;
            }
            String name = args[1];
            if(!FactionUtils.isInFaction(UUID)) {
                SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
                return;
            }

            if(!FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Leader)) {
                SystemUtils.sendfactionmessage(sender, "&r&f국가의 " + Config.Leader_Lang + " 만 이 명령어를 사용할수 있습니다\n");
                return;
            }

            if(!UserInfoUtils.hasJoined(args[1].toLowerCase(Locale.ROOT))) {
                SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + name + "(은)는 서버에 접속한 적이 없습니다");
                return;
            }

            String TargetUUID = UserInfoUtils.getPlayerUUID(name.toLowerCase(Locale.ROOT));
            if(!FactionUtils.isSameFaction(UUID, TargetUUID)) {
                SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(TargetUUID)) + "(은)는 당신의 국가 소속이 아닙니다");
                return;
            }

            FactionUtils.SetPlayerRank(TargetUUID, Config.Leader);
            FactionUtils.SetPlayerRank(UUID, Config.CoLeader);
            SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(TargetUUID)) + " 에게 국가의 소유권을 양도하였습니다\n" +
                    "변경된 당신의 등급 &7&l: &r&f" + Config.CoLeader_Lang);
            FactionUtils.SendFactionMessage(TargetUUID, TargetUUID, "single", "&r&f국가의 " + Config.Leader_Lang + "인 " + sender.getName() + " 이가 당신에게 국가의 소유권을 양도하였습니다\n" +
                    "변경된 당신의 등급 &7&l: &r&f" + Config.Leader_Lang);
            JedisTempStorage.AddCommandToQueue("notify:=:" + UUID + ":=:" + "SIBAL" + ":=:" + "&r&f" + sender.getName() + " 이가 국가의 소유권을 " + UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(TargetUUID)) + " 에게 넘겨주었습니다" + ":=:" + "true");
        }).start();
    }

}
