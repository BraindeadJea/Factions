package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Faction.FactionOutpost;
import com.itndev.factions.Main;
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

public class FactionOutPostClaim {

    public static void FactionOutPostClaimCommand(Player sender, String UUID, String[] args) {
        if(args.length < 2) {
            SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 전초기지점령 &7(전초기지이름)\n");
            return;
        }
        new Thread( () -> {
            try {
                if (FactionUtils.HigherThenorSameRank(UUID, Config.CoLeader)) {
                    Location loc = sender.getLocation();
                    String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                    ItemStack item = sender.getInventory().getItemInMainHand();
                    if(args[1].length() > 20) {
                        SystemUtils.sendfactionmessage(sender, "&r&f전초기지 이름이 너무 깁니다. 20자 미만으로 설정해주시기 바랍니다\n");
                        return;
                    }
                    if (!ValidChecker.ValidCheck(args[1]).get()) {
                        SystemUtils.sendfactionmessage(sender, "&r&f사용 불가능한 전초기지 이릅입니다\n");
                        return;
                    }
                    if(FactionUtils.isExistingOutPost(FactionUUID, args[1].toLowerCase(Locale.ROOT))) {
                        SystemUtils.sendfactionmessage(sender, "&r&f이미 존재하는 전초기지 이릅입니다\n");
                        return;
                    }
                    String OutPostName = args[1].toLowerCase(Locale.ROOT);
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
}
