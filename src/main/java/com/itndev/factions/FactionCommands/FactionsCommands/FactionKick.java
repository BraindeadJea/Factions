package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.UserInfoUtils;
import org.bukkit.entity.Player;

import java.util.Locale;

public class FactionKick {
    public static void FactionKick(Player sender, String UUID, String[] args) {
        if(args.length < 2) {
            SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 추방 &7(이름)");
            return;
        }

        if (FactionUtils.isInFaction(UUID)) {
            if (FactionUtils.HigherThenorSameRank(UUID, Config.VipMember)) {
                String name = args[1];
                if(UserInfoUtils.hasJoined(args[1].toLowerCase(Locale.ROOT))) {
                    String KICKUUID = UserInfoUtils.getPlayerUUID(args[1].toLowerCase(Locale.ROOT));
                    String OriginName = UserInfoUtils.getPlayerOrginName(args[1].toLowerCase(Locale.ROOT));
                    if(FactionUtils.isInFaction(KICKUUID) && FactionUtils.isSameFaction(UUID, KICKUUID)) {
                        if(FactionUtils.HigherThenRank(UUID, FactionUtils.getPlayerRank(KICKUUID))) {
                            String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                            SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + OriginName + " 이를 국가에서 추방했습니다.");
                            FactionUtils.SetPlayerFaction(KICKUUID, null);
                            FactionUtils.SetFactionMember(KICKUUID, FactionUUID, true);
                            FactionUtils.SetPlayerRank(KICKUUID, Config.Nomad);
                            FactionUtils.SendFactionMessage(KICKUUID, KICKUUID, "single", "&r&f당신은 " + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + " 에서 추방했습니다.");
                            FactionUtils.SendFactionMessage(UUID, UUID, UUID, "&r&f" + sender.getName() + " 이가 " + OriginName + " 이를 국가에서 추방했습니다.");
                        } else {
                            SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + OriginName + "(은)는 당신보다 등급이 높은 &r&c" + FactionUtils.getPlayerRank(KICKUUID) + " &r&f입니다. 자신보다 높은 등급인 멤버를 추방할수 없습니다");
                        }
                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + OriginName + "(은)는 당신의 팀이 아닙니다");
                    }
                } else {
                    SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + name + "(은)는 서버에 접속한 적이 없습니다");
                }
            } else {
                SystemUtils.sendfactionmessage(sender, "&r&f권한이 없습니다. &r&c" + Config.VipMember_Lang + " &r&f랭크 이상부터 사용이 가능합니다");
            }
        } else {
            SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
        }
    }
}
