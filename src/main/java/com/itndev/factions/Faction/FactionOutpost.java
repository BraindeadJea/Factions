package com.itndev.factions.Faction;

import com.itndev.factions.AdminCommands.AdminMainCommand;
import com.itndev.factions.Main;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FactionOutpost {

    public static void getFactionOutPostLocation(String FactionUUID) {
        FactionUUID factionUUID = new FactionUUID(FactionUUID);
    }

    public static void TryClaimOutPost(Player p, Location loc, String OutPostName) {
        //Location loc = p.getLocation();
        FactionUtils.ClaimOutPost(FactionUtils.getPlayerFactionUUID(p.getUniqueId().toString()), FactionUtils.getChunkKey(loc));
        SystemUtils.RemoveHeldItem(p, 1);
        ItemStack YES = p.getInventory().getItemInMainHand().clone();
        String FactionUUID = FactionUtils.getPlayerFactionUUID(p.getUniqueId().toString());
        new Thread( () -> {
            CompletableFuture<Boolean> AreNotOwned = FactionUtils.AsyncNearByChunksAreOwned5(loc.clone());
            String Chunkkey = FactionUtils.getChunkKey(loc);
            try {
                if(!AreNotOwned.get()) {
                    /*Location beaconloc = SystemUtils.ReplaceChunk(AdminMainCommand.getCopyLocation(), loc);
                    String k = "X:" + beaconloc.getBlockX() + " Y:" + beaconloc.getBlockY() + " Z:" + beaconloc.getBlockZ();
                    SystemUtils.sendfactionmessage(p, "&r&f참 신호기 위치 &r&7: &r&c" + k);
                    Location loc2 = loc.getChunk().getBlock(7, 7, 68).getLocation();
                    loc2.setX(loc2.getX() + 0.5);
                    loc2.setZ(loc2.getZ() + 0.5);
                    SystemUtils.sendmessage(p, "&c&lINFO &r&7Location:" + loc2.getX() + ":" + loc2.getY() + ":" + loc2.getZ());
                    FactionUtils.*/
                    ArrayList<Player> teleportlist = new ArrayList<>();
                    for(Entity entity : loc.getChunk().getEntities()) {
                        if(entity instanceof Player) {
                            teleportlist.add((Player) entity);
                        }
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Location beaconloc = SystemUtils.ReplaceChunk(AdminMainCommand.getCopyLocation(), loc);
                            String k = "X:" + beaconloc.getBlockX() + " Y:" + beaconloc.getBlockY() + " Z:" + beaconloc.getBlockZ();
                            //FactionUtils.SetFactionOutPostWarpLocation(FactionUUID, Chunkkey, loc, OutPostName);
                            SystemUtils.sendfactionmessage(p, "&r&f참 신호기 위치 &r&7: &r&c" + k);
                            Location loc2 = loc.getChunk().getBlock(7, 68, 7).getLocation();
                            loc2.setX(loc2.getX() - 1);
                            loc2.setZ(loc2.getZ() + 1);
                            SystemUtils.sendmessage(p, "&c&lINFO &r&7Location:" + loc2.getX() + ":" + loc2.getY() + ":" + loc2.getZ());
                            FactionUtils.SetFactionOutPostWarpLocation(FactionUUID, Chunkkey, loc2, OutPostName);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    SystemUtils.ReplaceChunk(AdminMainCommand.getCopyLocation(), loc2);
                                    for(Player ktmf :teleportlist) {
                                        ktmf.teleportAsync(loc2);
                                    }
                                    FactionUtils.SetFactionOutPostWarpLocation(FactionUUID, Chunkkey, loc2, OutPostName);
                                }
                            }.runTaskLater(Main.getInstance(), 2L);
                        }
                    }.runTaskLater(Main.getInstance(), 1L);
                } else {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            YES.setAmount(1);
                            p.getInventory().addItem(YES);
                            FactionUtils.UnClaimOutPost(FactionUtils.getPlayerFactionUUID(p.getUniqueId().toString()), FactionUtils.getChunkKey(loc));
                        }
                    }.runTask(Main.getInstance());
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void TryBreakOutPost(Player p, Location loc) {
        //Location loc = p.getLocation();
        //FactionUtils.ClaimOutPost(FactionUtils.getPlayerFactionUUID(p.getUniqueId().toString()), FactionUtils.getChunkKey(loc));
        //SystemUtils.RemoveHeldItem(p, 1);
        FactionUtils.UnClaimOutPost(FactionUtils.getPlayerFactionUUID(p.getUniqueId().toString()), FactionUtils.getChunkKey(loc));
        //ItemStack YES = p.getInventory().getItemInMainHand().clone();
        new Thread( () -> {
            //CompletableFuture<Boolean> AreNotOwned = FactionUtils.AsyncNearByChunksAreOwned5(loc.clone());

        }).start();
    }
}
