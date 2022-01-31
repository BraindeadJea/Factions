package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Main;
import com.itndev.factions.Storage.TempStorage;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FactionSpawn {


    public static void FactionSpawn(Player sender, String UUID, String[] args) {
        if(FactionUtils.isInFaction(UUID)) {
            String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
            if(FactionUtils.FactionSpawnExists(FactionUUID)) {
                String[] temp222 = FactionUtils.getFactionSpawn(FactionUUID).split("===");
                String TargetServerName = temp222[0];
                Location loc = SystemUtils.string2loc(temp222[1]);

                if(TargetServerName.equalsIgnoreCase(Main.ServerName)) {

                    //wait and teleport to location
                    SystemUtils.sendfactionmessage(sender, "&r&c5초&r&f후 국가 스폰으로 이동됩니다");
                    Long time = System.currentTimeMillis();
                    TempStorage.TeleportLocation.put(sender.getUniqueId().toString(), time);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(TempStorage.TeleportLocation.containsKey(UUID)) {
                                if(TempStorage.TeleportLocation.get(UUID).equals(time)) {
                                    SystemUtils.sendfactionmessage(sender, "&r&f국가 스폰으로 이동합니다");
                                    sender.teleport(loc);
                                    return;
                                }
                            }
                            SystemUtils.sendfactionmessage(sender, "&r&f이동이 취소되었습니다");
                        }
                    }.runTaskLater(Main.getInstance(), 100L);
                } else {
                    SystemUtils.sendfactionmessage(sender, "&r&c5초&r&f후 국가 스폰으로 이동됩니다");
                    Long time = System.currentTimeMillis();
                    TempStorage.TeleportLocation.put(sender.getUniqueId().toString(), time);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(TempStorage.TeleportLocation.containsKey(UUID)) {
                                if(TempStorage.TeleportLocation.get(UUID).equals(time)) {
                                    SystemUtils.SendtoServer(sender, TargetServerName);
                                    FactionUtils.WarpLocation(UUID, TargetServerName, temp222[1], false);
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            FactionUtils.WarpLocation(UUID, TargetServerName, temp222[1], true);
                                        }
                                    }.runTaskLaterAsynchronously(Main.getInstance(), 400L);
                                    return;
                                }
                            }
                            SystemUtils.sendfactionmessage(sender, "&r&f이동이 취소되었습니다");
                        }
                    }.runTaskLater(Main.getInstance(), 100L);

                    //
                    //서버로 이동
                    //그다음에 텔레포트
                }

            } else {
                SystemUtils.sendfactionmessage(sender, "&r&f국가 스폰이 설정되어 있지 않습니다");
            }
        } else {
            SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
        }
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
