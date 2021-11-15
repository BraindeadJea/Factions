package com.itndev.factions.Utils;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportInvisFix {

    public static void onTeleport(PlayerTeleportEvent e) {
        Location loc = e.getTo();
        new Thread( () ->{
            int sq = 150 * 150;
            for (Player p : loc.getWorld().getPlayers()) {
                if (p.getLocation().distanceSquared(loc) <= sq) {
                    ProtocolManager manager = ProtocolLibrary.getProtocolManager();
                    boolean d;
                    PacketContainer teleportpacket = manager.createPacket(PacketType.Play.Server.ENTITY_TELEPORT, true);
                    teleportpacket.setMeta("EntityID", e.getPlayer().getUniqueId());
                }
            }
        }).start();
    }
}
