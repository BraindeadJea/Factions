package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Main;
import com.itndev.factions.Storage.FactionStorage;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FactionWarpMenu {

    @Deprecated
    public static void SendFactionWarpMenu(Player sender, String UUID, String[] args) {
        new Thread( () -> {
            if(!FactionUtils.isInFaction(UUID)) {
                SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
                return;
            }

            Inventory inv = Bukkit.createInventory(sender, 54, ChatColor.translateAlternateColorCodes('&', "&3&l[ &r&f국가 워프메뉴 &3&l]"));
            //ArrayList<Integer> exp = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
            //ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            //ItemMeta glassmeta = glass.getItemMeta();
            //glassmeta.setDisplayName("");
            //glass.setItemMeta(glassmeta);
            String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
            if(FactionStorage.FactionOutPostList.containsKey(FactionUUID)) {
                for(String FactionOutPostName : FactionStorage.FactionOutPostList.get(FactionUUID)) {
                    inv.addItem(getWarpClickItem(FactionOutPostName));
                }
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    sender.openInventory(inv);
                }
            }.runTask(Main.getInstance());

        }).start();

    }

    @Deprecated
    private static ItemStack getWarpClickItem(String FactionOutPostName) {
        ItemStack beaconwarp = new ItemStack(Material.BEACON);
        ItemMeta meta = beaconwarp.getItemMeta();
        meta.setDisplayName(SystemUtils.colorize("&3&l[&r&f " + FactionOutPostName + " &r&3&l]"));
        List<String> lore = new ArrayList<>();
        lore.add(SystemUtils.colorize("&r국가 전초기지 워프"));
        meta.setLore(lore);
        beaconwarp.setItemMeta(meta);
        return beaconwarp;
    }
}
