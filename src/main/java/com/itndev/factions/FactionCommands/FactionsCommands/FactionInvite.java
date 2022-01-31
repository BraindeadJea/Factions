package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.UserInfoUtils;
import org.bukkit.entity.Player;

import java.util.Locale;

public class FactionInvite {
    public static void FactionInvite(Player sender, String UUID, String[] args) {
        if(args.length < 2) {
            SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 초대 &7(이름)");
            return;
        }

        if (FactionUtils.isInFaction(UUID)) {
            if (FactionUtils.HigherThenorSameRank(UUID, Config.VipMember)) {
                if(UserInfoUtils.hasJoined(args[1])) {
                    String InviteUUID = UserInfoUtils.getPlayerUUID(args[1].toLowerCase(Locale.ROOT));
                    String CasedName = UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(InviteUUID));
                    if(!FactionUtils.isInFaction(InviteUUID)) {
                        FactionTimeOut.InvitePlayer(sender, FactionUtils.getPlayerFactionUUID(UUID), InviteUUID);
                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&c" + CasedName + "&r&f(은)는 이미 다른 국가에 소속되어 있습니다");
                    }
                } else {
                    SystemUtils.sendfactionmessage(sender,   "&r&c" + args[1] + "&r&f(은)는 존재하지 않는 유저입니다");
                }
            } else {
                SystemUtils.sendfactionmessage(sender, "&r&f권한이 없습니다. &c" + Config.VipMember_Lang + " &r&f랭크 이상부터 사용이 가능합니다");
            }
        } else {
            SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
        }
    }

    public static void FactionInviteCancel(Player sender, String UUID, String[] args) {
        if(args.length < 2) {
            SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 초대취소 &7(이름)");
            return;
        }

        if (FactionUtils.isInFaction(UUID)) {
            if (FactionUtils.HigherThenorSameRank(UUID, Config.VipMember)) {
                if (UserInfoUtils.hasJoined(args[1])) {
                    String InviteUUID = UserInfoUtils.getPlayerUUID(args[1].toLowerCase(Locale.ROOT));
                    String CasedName = UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(InviteUUID));
                    FactionTimeOut.cancelInvite(sender, FactionUtils.getPlayerFactionUUID(UUID), InviteUUID);
                } else {
                    SystemUtils.sendfactionmessage(sender, "&r&c" + args[1] + "&r&f(은)는 존재하지 않는 유저입니다");
                }
            } else {
                SystemUtils.sendfactionmessage(sender, "&r&f권한이 없습니다. &c" + Config.VipMember_Lang + " &r&f랭크 이상부터 사용이 가능합니다");
            }
        } else {
            SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
        }
    }

    public static void FactionInviteAccept(Player sender, String UUID, String[] args) {
        if(args.length < 2) {
            SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 수락 &7(국가이름)");
            return;
        }

        if(!FactionUtils.isInFaction(UUID)) {
            if(FactionUtils.isExistingFaction(args[1])) {
                String FactionUUID = FactionUtils.getFactionUUID(args[1].toLowerCase(Locale.ROOT));
                FactionTimeOut.AcceptInvite(sender, UUID, FactionUUID);
                FactionUtils.SetPlayerRank(UUID, Config.Member);
            } else {
                SystemUtils.sendfactionmessage(sender, "&r&f해당 국가 " + args[1] + " 은 존재하지 않습니다");
            }
        } else {
            SystemUtils.sendfactionmessage(sender, "&r&f당신은 이미 다른 국가에 소속되어 있습니다");
        }
    }
}
