package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Faction.Faction;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class FactionInfo {
    private static DecimalFormat df = new DecimalFormat("0.00");

    public static void FactionInfo(Player sender, String UUID, String[] args) {
        new Thread( () -> {
            Boolean MyFaction = false;
            String TargetFactionUUID;
            if(args.length < 2) {
                MyFaction = true;
                if(!FactionUtils.isInFaction(UUID)) {
                    SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 정보 &7(국가이름)\n");
                    return;
                }
            }

            if(MyFaction) {
                TargetFactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
            } else {
                if(!FactionUtils.isExistingFaction(args[1])) {
                    SystemUtils.sendfactionmessage(sender,   "&r&f해당 국가 " + args[1] + " &r&f(은)는 존재하지 않는 국가입니다");
                    return;
                }
                TargetFactionUUID = FactionUtils.getFactionUUID(args[1]);
            }
            Faction faction = new Faction(TargetFactionUUID);
            SystemUtils.sendmessage(sender, "&r&f&m-----------------&r&a&o&l[ &r&f국가 &r&a&o&l]&r&f&m-----------------\n" +
                    "&r\n" +
                    "&r&7&l> &r 국가이름 &r&8&l: &f" + faction.getFactionCapName() + "\n" +
                    "&r\n" +
                    "&r&7&l> &r 설명 &r&8&l: &7" + faction.getFactionDesc() + "&r\n" +
                    "&r\n" +
                    "&r&7&l> &r 금고잔액 &r&8&l: &7" + df.format(faction.getBank()) + "\n" +
                    "&r&7&l> &r 남은파워 &r&8&l: &7" + faction.getDTR() + "&f/&7100.0\n" +
                    "&r&7&l> &r 설립일 &r&8&l: &7" + SystemUtils.FactionUUIDToDate(TargetFactionUUID) + "\n" +
                    "&r&7&l> &r 영토 &r&8&l: &7" + faction.getClaimLand() + " 청크\n" +
                    "&r\n" +
                    "&r&7&l> &r " + Config.Leader_Lang + " &r&8&l: &7" + faction.getFormattedMembers(Config.Leader) + "\n" +
                    "&r&7&l> &r " + Config.CoLeader_Lang + " &r&8&l: &7" + faction.getFormattedMembers(Config.CoLeader) + "\n" +
                    "&r&7&l> &r " + Config.VipMember_Lang + " &r&8&l: &7" + faction.getFormattedMembers(Config.VipMember) + "\n" +
                    "&r&7&l> &r " + Config.Warrior_Lang + " &r&8&l: &7" + faction.getFormattedMembers(Config.Warrior) + "\n" +
                    "&r&7&l> &r " + Config.Member_Lang + " &r&8&l: &7" + faction.getFormattedMembers(Config.Member) + "\n" +
                    "&r\n" +
                    "&r&f&m-----------------&r&a&o&l[ &r&f국가 &r&a&o&l]&r&f&m-----------------\n");
        }).start();
    }
}
