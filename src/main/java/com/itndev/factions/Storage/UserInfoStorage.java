package com.itndev.factions.Storage;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Jedis.JedisManager;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.UserInfoUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

public class UserInfoStorage {

    public static ConcurrentHashMap<String, String> uuidname = new ConcurrentHashMap<>(); // uuid -> lowercase name
    public static ConcurrentHashMap<String, String> nameuuid = new ConcurrentHashMap<>(); // lowercase name -> uuid
    public static ConcurrentHashMap<String, String> namename = new ConcurrentHashMap<>();

    public static void onPlayerJoinEvent(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        //p.sendMessage("반갑습니다");
        String uuid = p.getUniqueId().toString();
        //p.setGameMode(GameMode.ADVENTURE);
        /*if(storage.proxyonline.containsKey(uuid)) {
            String cmd = "update the player location";
        } else {
            String cmd = "update the player location";
            jedis.RedisChatSyncQ.put("", "");
            String cmd2 = "notifiy team members that the player has joined the network";
            jedis.RedisChatSyncQ.put("notify:=:" + p.getUniqueId().toString() + ":=:" + p.getUniqueId().toString() + ":=:" + p.getName() + "님이 서버에 접속하셨습니다" + ":=:" + "true", "notify:=:" + p.getUniqueId().toString() + ":=:" + p.getUniqueId().toString() + ":=:" + p.getName() + "님이 서버에 접속하셨습니다" + ":=:" + "true");

        }*/
        if(UserInfoStorage.uuidname.isEmpty()) {
            System.out.println("====UUIDNAME IS EMPTY====");
        }
        if(UserInfoStorage.nameuuid.isEmpty()) {
            System.out.println("====NAMEUUID IS EMPTY====");
        }
        if(UserInfoStorage.namename.isEmpty()) {
            System.out.println("====NAMENAME IS EMPTY====");
        }
        if(Main.ServerName.equalsIgnoreCase("client1")) { //main.getInstance().clientname.equalsIgnoreCase("client1")
            if (!FactionStorage.PlayerFaction.containsKey(uuid)) {
                //storage.teamrank.put(e.getPlayer().getUniqueId().toString(), "nomad");
                JedisTempStorage.AddCommandToQueueFix("update:=:FactionRank:=:add:=:" + uuid + ":=:add:=:" + "nomad", "update:=:FactionRank:=:add:=:" + uuid + ":=:add:=:" + "nomad");
            }
            if(FactionStorage.FactionRank.containsKey(uuid) && FactionStorage.FactionRank.get(uuid).equalsIgnoreCase(Config.Nomad)) {
                if(FactionStorage.PlayerFaction.containsKey(uuid)) {
                    String teamname = FactionStorage.PlayerFaction.get(uuid);
                    JedisTempStorage.AddCommandToQueueFix("update:=:PlayerFaction:=:remove:=:" + uuid + ":=:add:=:" + "nomad", "update:=:PlayerFaction:=:remove:=:" + uuid + ":=:add:=:" + "nomad");
                    JedisTempStorage.AddCommandToQueueFix("update:=:FactionRank:=:add:=:" + uuid + ":=:add:=:" + "nomad", "update:=:FactionRank:=:add:=:" + uuid + ":=:add:=:" + "nomad");
                    //jedis.RedisUpdateQ.put("update:=:teammember:=:remove:=:" + uuid + ":=:add:=:" + "nomad", "update:=:teammember:=:remove:=:" + uuid + ":=:add:=:" + "nomad");

                }

            }
            if (uuidname.containsKey(uuid) /*e.getPlayer().hasPlayedBefore()*/) {
                if (!FactionStorage.FactionRank.containsKey(uuid)) {
                    //storage.teamrank.put(e.getPlayer().getUniqueId().toString(), "nomad");
                    JedisTempStorage.AddCommandToQueueFix("update:=:FactionRank:=:add:=:" + uuid + ":=:add:=:" + "nomad", "update:=:FactionRank:=:add:=:" + uuid + ":=:add:=:" + "nomad");
                }
                if (uuidname.containsKey(uuid)) {
                    String originname = uuidname.get(uuid);
                    if (!originname.equals(e.getPlayer().getName().toLowerCase(Locale.ROOT))) {


                        //uuidname.put(e.getPlayer().getUniqueId().toString(), e.getPlayer().getName().toLowerCase(Locale.ROOT));
                        JedisTempStorage.AddCommandToQueueFix("update:=:uuidname:=:add:=:" + uuid + ":=:add:=:" + e.getPlayer().getName().toLowerCase(Locale.ROOT), "update:=:uuidname:=:add:=:" + uuid + ":=:add:=:" + e.getPlayer().getName().toLowerCase(Locale.ROOT));

                        //nameuuid.put(e.getPlayer().getName().toLowerCase(Locale.ROOT), e.getPlayer().getUniqueId().toString());
                        JedisTempStorage.AddCommandToQueueFix("update:=:nameuuid:=:add:=:" + e.getPlayer().getName().toLowerCase(Locale.ROOT) + ":=:add:=:" + uuid, "update:=:nameuuid:=:add:=:" + e.getPlayer().getName().toLowerCase(Locale.ROOT) + ":=:add:=:" + uuid);

                        //nameuuid.remove(originname);
                        JedisTempStorage.AddCommandToQueueFix("update:=:nameuuid:=:remove:=:" + originname + ":=:add:=:" + "nomad", "update:=:nameuuid:=:remove:=:" + originname + ":=:add:=:" + "nomad");

                        //namename.put(e.getPlayer().getName().toLowerCase(Locale.ROOT), e.getPlayer().getName());
                        JedisTempStorage.AddCommandToQueueFix("update:=:namename:=:add:=:" + e.getPlayer().getName().toLowerCase(Locale.ROOT) + ":=:add:=:" + e.getPlayer().getName(), "update:=:namename:=:add:=:" + e.getPlayer().getName().toLowerCase(Locale.ROOT) + ":=:add:=:" + e.getPlayer().getName());

                    }
                } else {
                    //uuidname.put(e.getPlayer().getUniqueId().toString(), e.getPlayer().getName().toLowerCase(Locale.ROOT));
                    JedisTempStorage.AddCommandToQueueFix("update:=:uuidname:=:add:=:" + uuid + ":=:add:=:" + e.getPlayer().getName().toLowerCase(Locale.ROOT), "update:=:uuidname:=:add:=:" + uuid + ":=:add:=:" + e.getPlayer().getName().toLowerCase(Locale.ROOT));

                    //nameuuid.put(e.getPlayer().getName().toLowerCase(Locale.ROOT), e.getPlayer().getUniqueId().toString());
                    JedisTempStorage.AddCommandToQueueFix("update:=:nameuuid:=:add:=:" + e.getPlayer().getName().toLowerCase(Locale.ROOT) + ":=:add:=:" + uuid, "update:=:nameuuid:=:add:=:" + e.getPlayer().getName().toLowerCase(Locale.ROOT) + ":=:add:=:" + uuid);

                    //namename.put(e.getPlayer().getName().toLowerCase(Locale.ROOT), e.getPlayer().getName());
                    JedisTempStorage.AddCommandToQueueFix("update:=:namename:=:add:=:" + e.getPlayer().getName().toLowerCase(Locale.ROOT) + ":=:add:=:" + e.getPlayer().getName(), "update:=:namename:=:add:=:" + e.getPlayer().getName().toLowerCase(Locale.ROOT) + ":=:add:=:" + e.getPlayer().getName());
                }
            } else {
                //storage.teamrank.put(e.getPlayer().getUniqueId().toString(), "nomad");
                JedisTempStorage.AddCommandToQueueFix("update:=:FactionRank:=:add:=:" + uuid + ":=:add:=:" + "nomad", "update:=:FactionRank:=:add:=:" + uuid + ":=:add:=:" + "nomad");

                //uuidname.put(e.getPlayer().getUniqueId().toString(), e.getPlayer().getName().toLowerCase(Locale.ROOT));
                JedisTempStorage.AddCommandToQueueFix("update:=:uuidname:=:add:=:" + uuid + ":=:add:=:" + e.getPlayer().getName().toLowerCase(Locale.ROOT), "update:=:uuidname:=:add:=:" + uuid + ":=:add:=:" + e.getPlayer().getName().toLowerCase(Locale.ROOT));

                //nameuuid.put(e.getPlayer().getName().toLowerCase(Locale.ROOT), e.getPlayer().getUniqueId().toString());
                JedisTempStorage.AddCommandToQueueFix("update:=:nameuuid:=:add:=:" + e.getPlayer().getName().toLowerCase(Locale.ROOT) + ":=:add:=:" + uuid, "update:=:nameuuid:=:add:=:" + e.getPlayer().getName().toLowerCase(Locale.ROOT) + ":=:add:=:" + uuid);

                //namename.put(e.getPlayer().getName().toLowerCase(Locale.ROOT), e.getPlayer().getName());
                JedisTempStorage.AddCommandToQueueFix("update:=:namename:=:add:=:" + e.getPlayer().getName().toLowerCase(Locale.ROOT) + ":=:add:=:" + e.getPlayer().getName(), "update:=:namename:=:add:=:" + e.getPlayer().getName().toLowerCase(Locale.ROOT) + ":=:add:=:" + e.getPlayer().getName());
            }
        }
    }

    public static void UserInfoStorageUpdateHandler(String[] args) {
        if(args[1].equalsIgnoreCase("namename")) {

            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값

                if(args[4].equalsIgnoreCase("add")) {

                    if(UserInfoStorage.namename.containsKey(key)) {
                        UserInfoStorage.namename.remove(key);
                        UserInfoStorage.namename.put(key, value);
                    } else {
                        UserInfoStorage.namename.put(key, value);
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {

                    if(UserInfoStorage.namename.containsKey(key)) {

                        UserInfoStorage.namename.remove(key);

                        //할거 없다
                    } else {
                        //할거 없다
                    }
                }

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                if(UserInfoStorage.namename.containsKey(key)) {
                    UserInfoStorage.namename.remove(key);
                }
            }

        } else if(args[1].equalsIgnoreCase("nameuuid")) {

            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값

                if(args[4].equalsIgnoreCase("add")) {

                    if(UserInfoStorage.nameuuid.containsKey(key)) {
                        UserInfoStorage.nameuuid.remove(key);
                        UserInfoStorage.nameuuid.put(key, value);
                    } else {
                        UserInfoStorage.nameuuid.put(key, value);
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {

                    if(UserInfoStorage.nameuuid.containsKey(key)) {

                        UserInfoStorage.nameuuid.remove(key);

                        //할거 없다
                    } else {
                        //할거 없다
                    }
                }

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                if(UserInfoStorage.nameuuid.containsKey(key)) {
                    UserInfoStorage.nameuuid.remove(key);
                }
            }

        } else if(args[1].equalsIgnoreCase("uuidname")) {

            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값

                if(args[4].equalsIgnoreCase("add")) {

                    if(UserInfoStorage.uuidname.containsKey(key)) {
                        UserInfoStorage.uuidname.remove(key);
                        UserInfoStorage.uuidname.put(key, value);
                    } else {
                        UserInfoStorage.uuidname.put(key, value);
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {

                    if(UserInfoStorage.uuidname.containsKey(key)) {

                        UserInfoStorage.uuidname.remove(key);

                        //할거 없다
                    } else {
                        //할거 없다
                    }
                }

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                if(UserInfoStorage.uuidname.containsKey(key)) {
                    UserInfoStorage.uuidname.remove(key);
                }
            }

        }
    }

}
