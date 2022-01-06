package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Main;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.ValidChecker;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CreateFaction {
    public static void CreateFaction(Player sender, String UUID, String[] args) {
        new Thread( () -> {
            try {
                if(args.length < 2) {
                    SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 생성 &7(국가이름)");
                    return;
                }
                CompletableFuture<Boolean> FutureValidCheck = ValidChecker.ValidCheck(args[1]);
                if (FactionUtils.getPlayerRank(sender.getUniqueId().toString()).equalsIgnoreCase("nomad")) {
                    if (FutureValidCheck.get(40, TimeUnit.MILLISECONDS)) {
                        OfflinePlayer op = Bukkit.getOfflinePlayer(sender.getUniqueId());
                        double bal = Main.econ.getBalance(op);
                        if (bal > Config.FactionCreateBalance) {
                            if (args[1].length() <= 12) {
                                String newFactionUUID = FactionUtils.newFactionUUID();
                                if (Main.database.TryClaimName(args[1], newFactionUUID).get()) {//try creating a faction name) {
                                    Main.econ.withdrawPlayer(op, Config.FactionCreateBalance);
                                    com.itndev.factions.FactionCommands.FactionsCommands.FactionCD.CreateFactionUtils.CreateFaction(sender, newFactionUUID, args[1]);
                                } else {
                                    SystemUtils.sendfactionmessage(sender, "&r&f해당 국가가 이미 존재합니다");
                                }
                            } else {
                                SystemUtils.sendfactionmessage(sender, "&r&f국가 이름의 길이는 12자를 초과할수 없습니다");
                            }
                        } else {
                            SystemUtils.sendfactionmessage(sender, "&r&f국가를 생성하기 위한 비용이 부족합니다 &7( " + String.valueOf(Config.FactionCreateBalance - bal) + "원 )");
                        }
                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f해당 문자/단어는 국가 이름에 들어갈수 없습니다");
                    }
                } else {
                    SystemUtils.sendfactionmessage(sender, "&r&f당신은 이미 다른 국가에 소속되어 있습니다. 기존 국가를 나간후에 다시 시도해 주십시오");
                }

            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                SystemUtils.sendmessage(sender, "&c&lERROR &7오류 발생 : 오류코드 DB-D02 (시스템시간:" + SystemUtils.getDate(System.currentTimeMillis()) + ")");
                e.printStackTrace();
            }
        }).start();

    }
}
