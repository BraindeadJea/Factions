package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Main;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FactionSpawn {


    public static void WarpFactionSpawn(Player sender) {

    }

    public static void SetFactionSpawn(Player sender) {
        String UUID = sender.getUniqueId().toString();
        String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
        CompletableFuture<Double> bal = Main.database.GetFactionBank(FactionUUID);
        if(FactionUtils.HigherThenorSameRank(UUID, Config.CoLeader)) {

            try {
                if (bal.get(40, TimeUnit.MILLISECONDS) > 500) {
                    CompletableFuture<Double> finalbank = Main.database.AddFactionBank(FactionUUID, 500);
                    double finalbankget = finalbank.get(20, TimeUnit.MILLISECONDS);
                    if (finalbankget > -1) {
                        Location loc = sender.getLocation();
                        String convertedloc = SystemUtils.loc2string(loc);

                        FactionUtils.SetFactionSpawn(FactionUUID, convertedloc);
                        SystemUtils.sendfactionmessage(sender, "&r&c성공! &r&f해당 위치로 국가 스폰을 설정했습니다.");

                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f스폰을 재설정하려면 적어도 국가 금고에 500원 초과가 있어야 합니다");
                    }
                } else {
                    SystemUtils.sendfactionmessage(sender, "&r&f스폰을 재설정하려면 적어도 국가 금고에 500원 초과가 있어야 합니다");
                }
            } catch (TimeoutException | ExecutionException | InterruptedException e) {
                SystemUtils.warning(e.getMessage());
            }
        } else {
            SystemUtils.sendfactionmessage(sender, "&r&f권한이 없습니다. &r&c" + Config.CoLeader_Lang + " &r&f등급 이상부터 사용이 가능합니다");
        }
    }

    public void onFactionSpawn(String FactionUUID, String UUID, Boolean isMyFaction) {


    }

    public void onJoinFactionSpawn() {

    }
}
