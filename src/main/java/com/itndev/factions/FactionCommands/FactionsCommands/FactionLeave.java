package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.entity.Player;

public class FactionLeave {

    public static void FactionLeave(Player sender, String UUID, String[] args) {
        new Thread( () -> {
            if(!FactionUtils.isInFaction(UUID)) {
                SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
                return;
            }
            if(FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Leader)) {
                SystemUtils.sendfactionmessage(sender, "&r&f국가의 " + Config.Leader_Lang + " 은 국가를 나갈 수 없습니다. 국가의 소유권을 양도하거나 해체해야 합니다.\n" +
                        "&f(&7/국가 양도 (이름)&8, &7/국가 해체&f)");
                return;
            }
            String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
            FactionUtils.SetFactionMember(UUID, FactionUUID, true);
            FactionUtils.SetPlayerFaction(UUID, null);
            FactionUtils.SetPlayerRank(UUID, Config.Nomad);
            SystemUtils.sendfactionmessage(sender, "&r&f성공적으로 국가 " + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + " 에서 나왔습니다.");
            JedisTempStorage.AddCommandToQueue("notify:=:" + FactionUtils.getFactionLeader(FactionUUID) + ":=:" + "SIBAL" + ":=:" + "&r&f" + sender.getName() + " 이가 당신의 국가에서 나갔습니다" + ":=:" + "true");
        }).start();
    }
}
