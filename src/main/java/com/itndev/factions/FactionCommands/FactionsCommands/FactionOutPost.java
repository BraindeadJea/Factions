package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Faction.FactionOutpost;
import com.itndev.factions.Main;
import com.itndev.factions.Storage.FactionStorage;
import com.itndev.factions.Storage.TempStorage;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.ValidChecker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class FactionOutPost {

    public static void FactionOutPostMainCommand(Player sender, String UUID, String[] args) {
        if(args.length < 2) {
            SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 전초기지 &7(점령/목록/워프)\n");
            return;
        }
        if(!FactionUtils.isInFaction(UUID)) {
            SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
            return;
        }
        if(args[1].equalsIgnoreCase("점령")) {
            FactionOutPostClaimCommand(sender, UUID, args);
        } else if(args[1].equalsIgnoreCase("목록")) {
            FactionOutPostListCommand(sender, UUID, args);
        } else if(args[1].equalsIgnoreCase("워프")) {
            FactionOutPostWarpCommand(sender, UUID, args);
        } else {
            SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 전초기지 &7(점령/목록/워프)\n");
        }
    }

    public static void FactionOutPostClaimCommand(Player sender, String UUID, String[] args) {
        if(args.length < 3) {
            SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 전초기지점령 &7(전초기지이름)\n");
            return;
        }
        new Thread( () -> {
            try {
                if (FactionUtils.HigherThenorSameRank(UUID, Config.CoLeader)) {
                    Location loc = sender.getLocation();
                    String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                    ItemStack item = sender.getInventory().getItemInMainHand();
                    if(args[2].length() > 20) {
                        SystemUtils.sendfactionmessage(sender, "&r&f전초기지 이름이 너무 깁니다. 20자 미만으로 설정해주시기 바랍니다\n");
                        return;
                    }
                    if (!ValidChecker.ValidCheck(args[2]).get()) {
                        SystemUtils.sendfactionmessage(sender, "&r&f사용 불가능한 전초기지 이릅입니다\n");
                        return;
                    }
                    if(FactionUtils.isExistingOutPost(FactionUUID, args[2].toLowerCase(Locale.ROOT))) {
                        SystemUtils.sendfactionmessage(sender, "&r&f이미 존재하는 전초기지 이릅입니다\n");
                        return;
                    }
                    String OutPostName = args[2].toLowerCase(Locale.ROOT);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (item != null && item.getType() == Material.BEACON && item.getItemMeta().hasEnchant(Enchantment.DURABILITY)) {
                                //
                                FactionOutpost.TryClaimOutPost(sender, loc, OutPostName);
                            } else {
                                SystemUtils.sendfactionmessage(sender, "&r&f전초기지 점령 신호기를 든 상태로 해당 명령어를 사용해 주세요");
                            }
                        }
                    }.runTask(Main.getInstance());
                } else {
                    SystemUtils.sendfactionmessage(sender, "&r&f권한이 없습니다. &r&c" + Config.CoLeader + " &r&f랭크 이상부터 사용이 가능합니다");
                }
            } catch (NullPointerException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void FactionOutPostListCommand(Player sender, String UUID, String[] args) {
        if(args.length < 2) {
            SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 전초기지 목록&\n");
            return;
        }
        new Thread( () -> {
            SystemUtils.sendmessage(sender, "&r&m&l------------&r&3&l[ &f&o국가 워프 &3&l]&r&m&l------------\n");
            String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
            if(FactionStorage.FactionOutPostList.containsKey(FactionUUID)) {
                for(String FactionOutPostName : FactionStorage.FactionOutPostList.get(FactionUUID)) {
                    SystemUtils.sendmessage(sender, "&r&7&l- &r&f" + FactionOutPostName);
                }
            } else {
                SystemUtils.sendmessage(sender, "&r&7&l- &r&fNONE EXIST");
            }
            SystemUtils.sendmessage(sender, "&r&m&l------------&r&3&l[ &f&o국가 워프 &3&l]&r&m&l------------\n");
        }).start();
    }

    public static void FactionOutPostWarpCommand(Player sender, String UUID, String[] args) {
        if(args.length < 3) {
            SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 전초기지 워프 &7(전초기지이름)\n");
            return;
        }
        String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
        String OutPostName = args[2].toLowerCase(Locale.ROOT);
        if(FactionUtils.isExistingOutPost(FactionUUID, OutPostName)) {
            String[] temp222 = FactionUtils.GetFactionOutPostWarpLocation(FactionUUID, OutPostName).split("===");
            String TargetServerName = temp222[0];
            Location loc = SystemUtils.string2loc(temp222[1]);

            if(TargetServerName.equalsIgnoreCase(Main.ServerName)) {

                //wait and teleport to location
                SystemUtils.sendfactionmessage(sender, "&r&c5초&r&f후 전초기지 " + OutPostName + "으로 이동합니다");
                Long time = System.currentTimeMillis();
                TempStorage.TeleportLocation.put(sender.getUniqueId().toString(), time);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(TempStorage.TeleportLocation.containsKey(UUID)) {
                            if(TempStorage.TeleportLocation.get(UUID).equals(time)) {
                                SystemUtils.sendfactionmessage(sender, "&r&f전초기지 " + OutPostName + "으로 이동합니다");
                                sender.teleport(loc);
                                return;
                            }
                        }
                        SystemUtils.sendfactionmessage(sender, "&r&f이동이 취소되었습니다");
                    }
                }.runTaskLater(Main.getInstance(), 100L);
            } else {
                SystemUtils.sendfactionmessage(sender, "&r&c5초&r&f후 전초기지 " + OutPostName + "으로 이동합니다");
                Long time = System.currentTimeMillis();
                TempStorage.TeleportLocation.put(sender.getUniqueId().toString(), time);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(TempStorage.TeleportLocation.containsKey(UUID)) {
                            if(TempStorage.TeleportLocation.get(UUID).equals(time)) {
                                SystemUtils.SendtoServer(sender, Main.ServerName);
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
            SystemUtils.sendfactionmessage(sender, "&r&f존재하지 않는 전초기지입니다\n");
        }

    }
}
