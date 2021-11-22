package com.itndev.factions.Listener;

import com.itndev.factions.Storage.TempStorage;
import com.itndev.factions.Storage.UserInfoStorage;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.TeleportInvisFix;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;

public class PlayerListener implements Listener {

    public static HashMap<String, Location> onJoinWarp = new HashMap<>();

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncChatEvent e) {

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent e) {
        new Thread( () -> {
            Location from = e.getFrom();
            Location to = e.getTo();
            if(from.getZ() != to.getZ()  ||  from.getX() != to.getX()  ||  from.getY() != to.getY()) {
                TempStorage.TeleportLocation.remove(e.getPlayer().getUniqueId().toString());
                if(!from.getChunk().equals(to.getChunk())) {
                    if(!FactionUtils.isSameClaimFaction(from, to)) {
                        String fromname = FactionUtils.GetClaimFaction(from);
                        String toname = FactionUtils.GetClaimFaction(to);
                        SystemUtils.sendfactionmessage(e.getPlayer(), "&r&f이동 &7: &r" + fromname + " &8->&r&f " + toname);
                    }
                }
            }
        }).start();
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if(!p.getGameMode().equals(GameMode.CREATIVE)) {
            Location loc = e.getBlock().getLocation();
            String UUID = p.getUniqueId().toString();
            if(FactionUtils.isClaimed(loc)) {
                if(!FactionUtils.getPlayerFactionUUID(UUID).equalsIgnoreCase(FactionUtils.AsyncWhosClaim(loc))) {
                    e.setCancelled(true);
                    return;
                }
                /*
                * 서버 1 -> 맵 1 -> 점령시 -> 그 서버에만 저장
                * 서버 2 -> 맵 2 ^ <- 점령불가
                * 갯수 -> 서버 2 -> 서버1의 영토갯수 알고싶다 -> 서버 1 물어보기
                * 해당 맵에 점령한 영토갯수 -> 좀 이상하니까
                * 채팅 중복 ->
                * 벌크 업데이트 -> 업데이트 -> 순서대로 작동 틱 단위로 함 50ms
                * 중복이 별로 일어나지 않거나 일어나도 위험하지 않은 정보 -> 레디스 캐시 -> 모든 서버에게 데이터 복사본 나눠줌
                * ---> MYSQL 데이터베이스 갯수만 저장 <- 데이터 중복 방지 (이름 , 금고 금액, DTR 등등) <- 리얼타임
                * 국가 전용 UUID
                * */
            }/* else if() {
                if(e.getBlock().getType().equals(Material.BEACON)) {

                }
            }*/

        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPVP(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player attacker = (Player) e.getDamager();
            Player victem = (Player) e.getEntity();
            if(FactionUtils.isSameFaction(attacker.getUniqueId().toString(), victem.getUniqueId().toString())) {
                e.setCancelled(true);
                SystemUtils.sendmessage(attacker, "&4&l⚠ &r&c국가원들끼리의 전투는 금지되어 있습니다");
                return;
            } else {
                TempStorage.TeleportLocation.remove(attacker.getUniqueId().toString());
                TempStorage.TeleportLocation.remove(victem.getUniqueId().toString());
            }
        }
    }

    @EventHandler
    public void OnJoinWarp(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String uuid = p.getUniqueId().toString();
        UserInfoStorage.onPlayerJoinEvent(e);
        if(onJoinWarp.containsKey(uuid)) {
            p.teleportAsync(onJoinWarp.get(uuid));
            onJoinWarp.remove(uuid);
            SystemUtils.sendfactionmessage(p, "&r&f국가 스폰으로 이동합니다");
        }
    }

    //@EventHandler
    /*public void onTeleport(PlayerTeleportEvent e) {
        TeleportInvisFix.onTeleport(e);
    }*/
}
