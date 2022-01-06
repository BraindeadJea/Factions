package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Main;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.ValidChecker;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class FactionBank {
    private static DecimalFormat df = new DecimalFormat("0.00");

    public static void FactionBank(Player sender, String UUID, String[] args) {
        new Thread( () -> {
            try {
                if (FactionUtils.isInFaction(UUID)) {
                    if (!FactionUtils.HigherThenorSameRank(UUID, Config.VipMember)) {
                        SystemUtils.sendfactionmessage(sender, "&r&f권한이 없습니다. &r&c" + Config.VipMember_Lang + " &r&f등급 이상부터 사용이 가능합니다");
                        return;
                    }

                    if (args.length < 3) {
                        SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 금고 &7(입금/출금) &7(금액)\n");
                        return;
                    }

                    Boolean Take = null;

                    if (args[1].equalsIgnoreCase("입금")) {
                        Take = false;
                    } else if (args[1].equalsIgnoreCase("출금")) {
                        Take = true;
                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 금고 &7(입금/출금) &7(금액)\n");
                        return;
                    }

                    if (args[2].length() > 20) {
                        SystemUtils.sendfactionmessage(sender, "&r&c오류 ! &7명령어가 너무 깁니다... 확인 불가\n");
                        return;
                    }

                    if (!ValidChecker.instanceofNumber(args[2])) {
                        SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 금고 &7(입금/출금) &7(금액)\n" +
                                "&7> &f금액에는 숫자를 입력해야 합니다.");
                        return;
                    }
                    double bal = Main.getEconomy().getBalance(Bukkit.getOfflinePlayer(sender.getUniqueId()));
                    double amount = Double.parseDouble(args[2]);
                    if (!Take) {

                        if (bal <= amount) {
                            SystemUtils.sendfactionmessage(sender, "&r&f국가 금고에 해당 금액을 넣기에는 돈이 부족합니다.\n");
                            return;
                        }
                        amount = amount * -1;
                    } else {
                        if (bal + amount > 9000000000000D) {
                            SystemUtils.sendfactionmessage(sender, "&r&f출금시 최대 금액 한도를 초과하므로 자동으로 해당 출금요청을 취소합니다\n");
                            return;
                        }
                    }

                    String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);

                    CompletableFuture<Double> futurebank = Main.database.AddFactionBank(FactionUUID, amount);

                    OfflinePlayer op = Bukkit.getOfflinePlayer(sender.getUniqueId());

                    double finalbank = futurebank.get();
                    if (finalbank < 0) {
                        SystemUtils.sendfactionmessage(sender, "&r&f국가 금고에서 해당 금액만큼을 출금하기에는 돈이 부족합니다\n");
                        return;
                    }

                    String TakeorGet = null;

                    if (Take) {
                        TakeorGet = "&a출금";
                        Main.econ.depositPlayer(op, amount);
                    } else {
                        TakeorGet = "&a입금";
                        Main.econ.withdrawPlayer(op, amount * -1);
                    }

                    SystemUtils.sendfactionmessage(sender, "&r&f성공적으로 해당 금액만큼을 국가 금고에서 " + TakeorGet + " &r했습니다. \n" +
                            "&r&7(남은금액 : " + df.format(finalbank) + "원)");

                } else {
                    SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
                }
            } catch (ExecutionException | InterruptedException e) {
                SystemUtils.sendmessage(sender, "&c&lERROR &7오류 발생 : 오류코드 DB-D03 (시스템시간:" + SystemUtils.getDate(System.currentTimeMillis()) + ")");
                e.printStackTrace();
            }
        }).start();

    }
}
