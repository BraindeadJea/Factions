package com.itndev.factions.Listener;

import com.itndev.factions.FactionCommands.FactionsCommands.FactionChatToggle;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import com.itndev.factions.Storage.FactionStorage;
import com.itndev.factions.Storage.TempStorage;
import com.itndev.factions.Storage.UserInfoStorage;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import me.leoko.advancedban.Universal;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.apache.commons.lang.Validate;
import org.bukkit.scheduler.BukkitRunnable;

import javax.security.auth.Subject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.SortedMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerListener implements Listener {

    public static HashMap<String, Location> onJoinWarp = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent e) {
        new Thread( () -> {
            Location from = e.getFrom();
            Location to = e.getTo();
            if(from.getZ() != to.getZ()  ||  from.getX() != to.getX()  ||  from.getY() != to.getY()) {
                TempStorage.TeleportLocation.remove(e.getPlayer().getUniqueId().toString());
                if(!from.getChunk().equals(to.getChunk())) {
                    String fromname = FactionUtils.GetClaimFaction(from);
                    String toname = FactionUtils.GetClaimFaction(to);
                    if(!fromname.equals(toname)) {
                        SystemUtils.sendfactionmessage(e.getPlayer(), "&r&f이동 &7: &r" + fromname + " &8->&r&f " + toname);
                    }
                }
            }
        }).start();
    }

    @Deprecated
    @EventHandler(ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if(!p.getGameMode().equals(GameMode.CREATIVE)) {
            Location loc = e.getBlock().getLocation();
            ItemStack holding = p.getInventory().getItemInMainHand();
            String UUID = p.getUniqueId().toString();
            Boolean tempwefjw = FactionUtils.isOutPost(loc);
            if (holding.getType() == Material.BEACON) {
                e.setCancelled(true);
                return;
            }
            if(FactionUtils.isClaimed(loc)) {
                if(!FactionUtils.getPlayerFactionUUID(UUID).equalsIgnoreCase(FactionUtils.AsyncWhosClaim(loc))) {
                    e.setCancelled(true);
                }
            } else if(tempwefjw) {
                SystemUtils.sendfactionmessage(p, "&r&f전초기지에서는 해당 액션을 취하실수 없습니다");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if(!p.getGameMode().equals(GameMode.CREATIVE)) {
            Location loc = e.getBlock().getLocation();
            Material m = e.getBlock().getType();
            String UUID = p.getUniqueId().toString();
            Boolean isOutPost = FactionUtils.isOutPost(loc);
            String LocalFactionUUID = FactionUtils.AsyncWhosClaim(loc);
            String PlayerFactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
            if(LocalFactionUUID != null) {
                if(PlayerFactionUUID == null || !PlayerFactionUUID.equalsIgnoreCase(LocalFactionUUID)) {
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
            } else if(isOutPost) {
                e.setCancelled(true);
                new Thread(() -> {
                    String OutPostFactionUUID = FactionUtils.GetOutPostOwner(loc);
                    if(PlayerFactionUUID != null && PlayerFactionUUID.equals(OutPostFactionUUID)) {
                        SystemUtils.sendfactionmessage(p, "&r&f전초기지에서는 해당 액션을 취하실수 없습니다");
                    } else if(PlayerFactionUUID != null) {
                        if(m == Material.BEACON) {
                            String Chunkkey = FactionUtils.getChunkKey(loc);
                            String FactionOutPostName = FactionUtils.GetFactionOutPostName(Chunkkey);
                            if(FactionUtils.GetBeaconLocation(OutPostFactionUUID, FactionOutPostName) == SystemUtils.loc2string(loc)) {
                                SystemUtils.sendfactionmessage(p, "&r&c진짜&r&f 신호기를 파괴했습니다 >.<");
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        //DESTROYOUTPOST

                                    }
                                }.runTask(Main.getInstance());
                            } else {
                                SystemUtils.sendfactionmessage(p, "&r&c가짜&r&f 신호기를 파괴했습니다 >.<");
                            }
                        }
                    } else {

                    }


                }).start();
            }

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


    @Deprecated
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
        String k = e.getMessage();
        Player p = e.getPlayer();
        String UUID = p.getUniqueId().toString();
        e.setCancelled(true);

        Main.sysutils.MainChatProcced(p , UUID, k);
            /*try {
                if (main.getInstance().ess.getUser(p.getUniqueId()).isMuted()) {
                    utils.sendmsg(p, "&4&l(!) &f&l당신은 뮤트됬습니다");
                    e.setCancelled(true);
                    return;
                }
            } catch (NullPointerException nulle) {
                System.out.println(nulle.getMessage());
            }*/


    }

    //@EventHandler
    /*public void onTeleport(PlayerTeleportEvent e) {
        TeleportInvisFix.onTeleport(e);
    }*/
}
