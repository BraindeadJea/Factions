package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Main;
import org.bukkit.entity.Player;

public class ClaimLand {

    public static void CmdClaimLand(Player p, String FactionUUID) {
        String ChunkKey = Main.ServerName + "=" + p.getLocation().getWorld().getName() + "=" + p.getLocation().getChunk().getChunkKey();

    }

}
