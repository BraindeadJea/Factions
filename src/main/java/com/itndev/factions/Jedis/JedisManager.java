package com.itndev.factions.Jedis;

import com.itndev.factions.Listener.PlayerListener;
import com.itndev.factions.Main;
import com.itndev.factions.Storage.CachedStorage;
import com.itndev.factions.Storage.FactionStorage;
import com.itndev.factions.Storage.UserInfoStorage;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.JedisUtils;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

public class JedisManager {

    public static Long JEDISSYNCCLOCK = 1L;

    public static void StartUpJedis() {
        JedisFactory123();
    }

    private static JedisPool jedisPool;
    public static void JedisFactory123() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        jedisPool = new JedisPool(poolConfig, "127.0.0.1", 6614, 300000, "54rg46ujhy7ju57wujndt35ytgryeutwefer4rt34rd34rsfg6hdf43truhgfwgr348yfgcs", false);
    }

    public static JedisPool getJedisPool() {
        return jedisPool;
    }



    @Deprecated
    public static void JedisPubSub() {

        new BukkitRunnable() {

            @Override
            public void run() {
                String[] channels = {"client1", "client2", "client3", "client4", "client5"};
                JedisPubSub jedisPubSub3 = new JedisPubSub() {
                    public void onMessage(String channel, String message) {
                        JedisUtils.HashMapUpdate(JedisUtils.String2HashMap(message), channel);
                    }
                };
                getJedisPool().getResource().subscribe(jedisPubSub3, channels);
                getJedisPool().getResource().sync();
            }
        }.runTaskAsynchronously(Main.getInstance());
    }


    @Deprecated
    public static void jedisTest() {
        JedisFactory123();


        //Jedis jedis = new Jedis(main.getInstance().dbaddress, 6614, 30000, main.getInstance().password, false);
        //jedis.auth();
        //jedis.close();


        //ArrayList<String> keynames = new ArrayList<>();


        JedisPoolConfig poolconfig = new JedisPoolConfig();
        //poolconfig.setMaxIdle(8);
        //poolconfig.setMaxTotal(32);






        new BukkitRunnable() {



            /*HashMap<String, String> commandlist1 = new HashMap<>();
            HashMap<String, String> commandlist2 = new HashMap<>();
            HashMap<String, String> commandlist3 = new HashMap<>();
            HashMap<String, String> commandlist4 = new HashMap<>();
            HashMap<String, String> chatmsg1 = new HashMap<>();
            HashMap<String, String> chatmsg2 = new HashMap<>();
            HashMap<String, String> chatmsg3 = new HashMap<>();
            HashMap<String, String> chatmsg4 = new HashMap<>();*/



            @Override
            public void run(){


                //Pipeline pipeline = jedis.pipelined();


                HashMap<String, String> commandlist1 = new HashMap<>();
                HashMap<String, String> commandlist2 = new HashMap<>();
                HashMap<String, String> commandlist3 = new HashMap<>();
                HashMap<String, String> commandlist4 = new HashMap<>();
                HashMap<String, String> commandlist5 = new HashMap<>(); //not used rn

                HashMap<String, String> chatmsg1 = new HashMap<>();
                HashMap<String, String> chatmsg2 = new HashMap<>();
                HashMap<String, String> chatmsg3 = new HashMap<>();
                HashMap<String, String> chatmsg4 = new HashMap<>();
                HashMap<String, String> chatmsg5 = new HashMap<>(); //not used rn

                HashMap<String, String> bungeeinfo1 = new HashMap<>();
                try(Jedis jedis1 = getJedisPool().getResource()) {
                    if (jedis1.isConnected()) {
                        /*if(jedis1.exists(Main.getInstance().clientname + "-forcesync")) {
                            String id = Main.getInstance().clientname + "-forcesync";
                            //Pipeline pipeline = jedissync.pipelined();
                            ConcurrentHashMap<String, String> teamrank = new ConcurrentHashMap<>();
                            ConcurrentHashMap<String, String> teampvp = new ConcurrentHashMap<>();
                            ConcurrentHashMap<String, String> teams = new ConcurrentHashMap<>();
                            ConcurrentHashMap<String, String> tempteammember = new ConcurrentHashMap<>();
                            //HashMap<String, String> fuckubich = new HashMap<>();
                            ConcurrentHashMap<String, ArrayList<String>> teammember = new ConcurrentHashMap<>();
                            ConcurrentHashMap<String, String> namename = new ConcurrentHashMap<>();
                            ConcurrentHashMap<String, String> nameuuid = new ConcurrentHashMap<>();
                            ConcurrentHashMap<String, String> uuidname = new ConcurrentHashMap<>();
                            teamrank = (ConcurrentHashMap<String, String>) jedis1.hgetAll(id + ":teamrank");
                            teampvp = (ConcurrentHashMap<String, String>) jedis1.hgetAll(id + ":teampvp");
                            teams = (ConcurrentHashMap<String, String>) jedis1.hgetAll(id + ":teams");
                            //fuckubich = (HashMap<String, String>) jedissync.hgetAll(id + ":fuckubich");
                            tempteammember = (ConcurrentHashMap<String, String>) jedis1.hgetAll(id + ":teammember");
                            for (String keys : tempteammember.keySet()) {
                                ArrayList<String> memlist = String2Array(tempteammember.get(keys));
                                teammember.put(keys, memlist);
                            }
                            namename = (ConcurrentHashMap<String, String>) jedis1.hgetAll(id + ":namename");
                            nameuuid = (ConcurrentHashMap<String, String>) jedis1.hgetAll(id + ":nameuuid");
                            uuidname = (ConcurrentHashMap<String, String>) jedis1.hgetAll(id + ":uuidname");
                        ArrayList<HashMap<String, String>> listofhmtoclear = new ArrayList<>();
                        listofhmtoclear.add(storage.teamrank);
                        listofhmtoclear.add(storage.teampvp);
                        listofhmtoclear.add(storage.teams);
                        listofhmtoclear.add(storage.teammember);
                            storage.teamrank.clear();
                            storage.teampvp.clear();
                            storage.teams.clear();
                            storage.teammember.clear();
                            listener.namename.clear();
                            listener.nameuuid.clear();
                            listener.uuidname.clear();
                            storage.teamrank = teamrank;
                            storage.teampvp = teampvp;
                            storage.teams = teams;
                            storage.teammember = teammember;
                            listener.namename = namename;
                            listener.nameuuid = nameuuid;
                            listener.uuidname = uuidname;
                            //.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l[!] &f성공적으로 SYNC 테스크 완료"));
                            //storage.teamrank.clear();
                            //pipeline.sync();
                            jedis1.del(Main.getInstance().clientname + "-forcesync");
                        }*/
                        //=======><><><><=======

                        commandlist1 = (HashMap<String, String>) jedis1.hgetAll("client1");
                        commandlist2 = (HashMap<String, String>) jedis1.hgetAll("client2");
                        commandlist3 = (HashMap<String, String>) jedis1.hgetAll("client3");
                        commandlist4 = (HashMap<String, String>) jedis1.hgetAll("client4");
                        commandlist5 = (HashMap<String, String>) jedis1.hgetAll("client5");

                        chatmsg1 = (HashMap<String, String>) jedis1.hgetAll("client1chatmsg");
                        chatmsg2 = (HashMap<String, String>) jedis1.hgetAll("client2chatmsg");
                        chatmsg3 = (HashMap<String, String>) jedis1.hgetAll("client3chatmsg");
                        chatmsg4 = (HashMap<String, String>) jedis1.hgetAll("client4chatmsg");
                        chatmsg5 = (HashMap<String, String>) jedis1.hgetAll("client5chatmsg");

                        bungeeinfo1 = (HashMap<String, String>) jedis1.hgetAll("bungee1");

                        ConcurrentHashMap<String, String> redisupdateqtemp = JedisTempStorage.TempCommandQueue;
                        ConcurrentHashMap<String, String> redischatsyncqtemp = JedisTempStorage.TempChatQueue;

                        jedis1.set("ping-" + Main.ServerName, "ping");
                        jedis1.expire("ping-" + Main.ServerName, 10);
                        if(!redisupdateqtemp.isEmpty()) {
                            jedis1.del(String.valueOf(Main.ServerName));
                            jedis1.hmset(String.valueOf(Main.ServerName), redisupdateqtemp);
                            //jedis.expire(String.valueOf(main.getInstance().clientname) + "", 1);
                            jedis1.expireAt(String.valueOf(Main.ServerName), System.currentTimeMillis() + 200);
                            redisupdateqtemp.clear();
                            for (String key : redisupdateqtemp.keySet()) {
                                JedisTempStorage.TempCommandQueue.remove(key);
                            }
                            //010101001001Main
                        } else {
                            jedis1.del(String.valueOf(Main.ServerName));
                        }
                        if (!redischatsyncqtemp.isEmpty()) {
                            jedis1.del(String.valueOf(Main.ServerName + "chatmsg"));
                            //jedis.
                            jedis1.hmset(String.valueOf(Main.ServerName + "chatmsg"), redischatsyncqtemp);
                            jedis1.expireAt(String.valueOf(Main.ServerName + "chatmsg"), System.currentTimeMillis() + 200);
                            redischatsyncqtemp.clear();
                            for (String key : redischatsyncqtemp.keySet()) {
                                JedisTempStorage.TempChatQueue.remove(key);
                            }
                        } else {
                            jedis1.del(String.valueOf(Main.ServerName + "chatmsg"));
                        }


                    } else {

                        System.out.println("Not connected to any Redis Server!!!");
                    }
                } catch (Exception e) {

                    System.out.println("[WARNING] " + e.getMessage());
                }


                if (!commandlist1.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(commandlist1.get("MAXAMOUNT")); c++) {
                        updatehashmap(commandlist1.get(String.valueOf(c)), "client1");
                    }
                }
                commandlist1.clear();
                if (!commandlist2.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(commandlist2.get("MAXAMOUNT")); c++) {
                        updatehashmap(commandlist2.get(String.valueOf(c)), "client2");
                    }
                }
                commandlist2.clear();
                if (!commandlist3.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(commandlist3.get("MAXAMOUNT")); c++) {
                        updatehashmap(commandlist3.get(String.valueOf(c)), "client3");
                    }
                }
                commandlist3.clear();
                if (!commandlist4.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(commandlist4.get("MAXAMOUNT")); c++) {
                        updatehashmap(commandlist4.get(String.valueOf(c)), "client4");
                    }
                }
                commandlist4.clear();
                if(!commandlist5.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(commandlist5.get("MAXAMOUNT")); c++) {
                        updatehashmap(commandlist5.get(String.valueOf(c)), "client5");
                    }
                }
                commandlist5.clear();






                //Chat
                if (!chatmsg1.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(chatmsg1.get("MAXAMOUNT")); c++) {
                        updatehashmap(chatmsg1.get(String.valueOf(c)), "client1");
                    }
                }
                chatmsg1.clear();
                if (!chatmsg2.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(chatmsg2.get("MAXAMOUNT")); c++) {
                        updatehashmap(chatmsg2.get(String.valueOf(c)), "client2");
                    }
                }
                chatmsg2.clear();
                if (!chatmsg3.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(chatmsg3.get("MAXAMOUNT")); c++) {
                        updatehashmap(chatmsg3.get(String.valueOf(c)), "client3");
                    }
                }
                chatmsg3.clear();
                if (!chatmsg4.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(chatmsg4.get("MAXAMOUNT")); c++) {
                        updatehashmap(chatmsg4.get(String.valueOf(c)), "client4");
                    }
                }
                chatmsg4.clear();
                if(!chatmsg5.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(chatmsg5.get("MAXAMOUNT")); c++) {
                        updatehashmap(chatmsg5.get(String.valueOf(c)), "client5");
                    }
                }


                //jedis.hmset(main.clientname, RedisUpdateQ);
                    /*if (!RedisUpdateQ.isEmpty()) {
                        jedis.del(String.valueOf(main.getInstance().clientname).toString() + "");
                        jedis.hmset(String.valueOf(main.getInstance().clientname).toString() + "", RedisUpdateQ);
                        //jedis.expire(String.valueOf(main.getInstance().clientname) + "", 1);
                        jedis.expire(String.valueOf(main.getInstance().clientname.toString()).toString(), 1);
                        RedisUpdateQ.clear();
                    } else {
                        jedis.del(String.valueOf(main.getInstance().clientname) + "");
                        RedisUpdateQ.clear();
                    }
                    if(!RedisChatSyncQ.isEmpty()) {
                        jedis.del(String.valueOf(main.getInstance().clientname + "chatmsg").toString());
                        //jedis.
                        jedis.hmset(String.valueOf(main.getInstance().clientname + "chatmsg").toString(), RedisChatSyncQ);
                        jedis.expire(String.valueOf(main.getInstance().clientname + "chatmsg").toString(), 1);
                        RedisChatSyncQ.clear();
                    } else {
                        jedis.del(String.valueOf(main.getInstance().clientname + "chatmsg").toString());
                    }*/
                    /*try {
                        jedis.del(main.getInstance().clientname);
                        jedis.del(main.getInstance().clientname + "chatmsg");
                    } catch (Throwable e) {
                        System.out.println(e.getMessage());
                    }*/
                new Thread( () -> {
                    HashMap<String, String> templands = FactionStorage.LandToFaction;
                    for(String key : templands.keySet()) {
                        FactionStorage.AsyncLandToFaction.put(key, templands.get(key));
                    }
                    HashMap<String, String> tempoutposts = FactionStorage.OutPostToFaction;
                    for(String key : tempoutposts.keySet()) {
                        FactionStorage.AsyncOutPostToFaction.put(key, tempoutposts.get(key));
                    }
                }).start();


                JEDISSYNCCLOCK++;

            }

        }.runTaskTimerAsynchronously(Main.getInstance(), 1L, 1L);//runTaskTimer(main.getInstance(), 5L, 5L);




    }

    @Deprecated
    public static void updatehashmap(String k, String ServerName) {
        if(k.equalsIgnoreCase("-buffer-")) {
            return;
        }
        double c = 1000;
        if(c > 600) {
            String[] args = k.split(":=:");
            if(args[0].equalsIgnoreCase("update")) {
                if(args[1].equalsIgnoreCase("FactionToLand")
                        || args[1].equalsIgnoreCase("LandToFaction")
                        || args[1].equalsIgnoreCase("FactionRank")
                        || args[1].equalsIgnoreCase("PlayerFaction")
                        || args[1].equalsIgnoreCase("FactionMember")
                        || args[1].equalsIgnoreCase("FactionNameToFactionName")
                        || args[1].equalsIgnoreCase("FactionNameToFactionUUID")
                        || args[1].equalsIgnoreCase("FactionUUIDToFactionName")
                        || args[1].equalsIgnoreCase("FactionInviteQueue")
                        || args[1].equalsIgnoreCase("FactionDTR")
                        || args[1].equalsIgnoreCase("FactionInfo")
                        || args[1].equalsIgnoreCase("Timeout2")
                        || args[1].equalsIgnoreCase("Timeout2info")
                        || args[1].equalsIgnoreCase("FactionOutPost")
                        || args[1].equalsIgnoreCase("FactionOutPostList")
                        || args[1].equalsIgnoreCase("FactionToOutPost")
                        || args[1].equalsIgnoreCase("OutPostToFaction")) {
                    FactionStorage.FactionStorageUpdateHandler(args, ServerName);
                } else if(args[1].equalsIgnoreCase("namename")
                        || args[1].equalsIgnoreCase("nameuuid")
                        || args[1].equalsIgnoreCase("uuidname")) {
                    UserInfoStorage.UserInfoStorageUpdateHandler(args);
                } else if(args[1].equalsIgnoreCase("cachedDTR")
                        || args[1].equalsIgnoreCase("cachedBank")) {
                    CachedStorage.JedisCacheSync(args);
                }
            } else if(args[0].equalsIgnoreCase("ping")) {
                //jedis.c = Math.toIntExact(c + 1);
                if(c == 4) {
                    //jedis.c = 0;
                    System.out.println("Pinged from Redis Database");
                }
            } else if(args[0].equalsIgnoreCase("chat")) {
                String playeruuid = args[1];
                String message = args[2];
                FactionUtils.FactionChat(playeruuid, message);
                //utils.teamchat(playeruuid, message);
            } else if(args[0].equalsIgnoreCase("proxyuserupdate")) {

                //String playeruuid = args[1];
                //if(args[0].equalsIgnoreCase())
                //String targetuuid = args[2];
                //String message = args[3];
                //String trueorfalse = args[4];
                //utils.teamnotify(playeruuid, targetuuid, message, trueorfalse);
            } else if(args[0].equalsIgnoreCase("notify")) {

                String playeruuid = args[1];
                String targetuuid = args[2];
                String message = args[3];
                String trueorfalse = args[4];
                FactionUtils.FactionNotify(playeruuid, targetuuid, message, trueorfalse);
                //utils.teamnotify(playeruuid, targetuuid, message, trueorfalse);
            } else if(args[0].equalsIgnoreCase("warplocation")) {
                String targetuuid = args[1];
                String Server = args[2];
                String stringlocation = args[3];
                String expired = args[4];
                if(Main.ServerName.equalsIgnoreCase(Server)) {
                    if(!expired.equalsIgnoreCase("expired")) {
                        Location loc = SystemUtils.string2loc(stringlocation);
                        PlayerListener.onJoinWarp.put(targetuuid, loc);
                    } else {
                        PlayerListener.onJoinWarp.remove(targetuuid);
                    }
                }
            } else {
                System.out.println("[WARNING (REDIS)] WRONG COMMAND USAGE FROM REDIS" + " ( '" + k + "' )");
            }


        } else if(c <= 600) {
            String warn = "data update failed... trying to update data that should already been processed or update has been duplicated / processed delayed (" + String.valueOf(c) + ")";
            SystemUtils.warning(warn);
            //utils.broadcastwarn(warn);
        }
        //example command "update:=:hashmap1~4:=:add/remove:=:key:=:add/remove/(앞에 remove일 경우 여기랑 이 뒤는 쓸 필요 없다):=:value"


    }

}

/*public class JedisManager {

    private static Long JEDISSYNCCLOCK = 1L;

    private static Long JEDISSYNCCLOCK_CLIENT1 = 1L;
    private static Long JEDISSYNCCLOCK_CLIENT2 = 1L;
    private static Long JEDISSYNCCLOCK_CLIENT3 = 1L;
    private static Long JEDISSYNCCLOCK_CLIENT4 = 1L;
    private static Long JEDISSYNCCLOCK_CLIENT5 = 1L;


    public static Long GETCLOCK() {
        return JEDISSYNCCLOCK;
    }

    public static void StartUpJedis() {
        JedisFactory123();
    }

    public static void ReSetIFBiggerThen(int clientnumber, Long jedistime) {
        if(clientnumber == 1) {
            if(JEDISSYNCCLOCK_CLIENT1 > jedistime) {
                JEDISSYNCCLOCK_CLIENT1 = 1L;
            }
        } else if(clientnumber == 2) {
            if(JEDISSYNCCLOCK_CLIENT2 > jedistime) {
                JEDISSYNCCLOCK_CLIENT2 = 1L;
            }
        } else if(clientnumber == 3) {
            if(JEDISSYNCCLOCK_CLIENT3 > jedistime) {
                JEDISSYNCCLOCK_CLIENT3 = 1L;
            }
        } else if(clientnumber == 4) {
            if(JEDISSYNCCLOCK_CLIENT4 > jedistime) {
                JEDISSYNCCLOCK_CLIENT4 = 1L;
            }
        } else if(clientnumber == 5) {
            if(JEDISSYNCCLOCK_CLIENT5 > jedistime) {
                JEDISSYNCCLOCK_CLIENT5 = 1L;
            }
        }
    }

    public static void SETCLOCK(int clientnumber, Long jedistime) {
        if(clientnumber == 1) {
            JEDISSYNCCLOCK_CLIENT1 = jedistime;
        } else if(clientnumber == 2) {
            JEDISSYNCCLOCK_CLIENT2 = jedistime;
        } else if(clientnumber == 3) {
            JEDISSYNCCLOCK_CLIENT3 = jedistime;
        } else if(clientnumber == 4) {
            JEDISSYNCCLOCK_CLIENT4 = jedistime;
        } else if(clientnumber == 5) {
            JEDISSYNCCLOCK_CLIENT5 = jedistime;
        }
    }

    public static Long GETCLOCK(int clientnumber) {
        if(clientnumber == 1) {
            return JEDISSYNCCLOCK_CLIENT1;
        } else if(clientnumber == 2) {
            return JEDISSYNCCLOCK_CLIENT2;
        } else if(clientnumber == 3) {
            return JEDISSYNCCLOCK_CLIENT3;
        } else if(clientnumber == 4) {
            return JEDISSYNCCLOCK_CLIENT4;
        } else if(clientnumber == 5) {
            return JEDISSYNCCLOCK_CLIENT5;
        } else {
            return null;
        }
    }

    private static JedisPool jedisPool;
    public static void JedisFactory123() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        jedisPool = new JedisPool(poolConfig, "127.0.0.1", 6614, 300000, "54rg46ujhy7ju57wujndt35ytgryeutwefer4rt34rd34rsfg6hdf43truhgfwgr348yfgcs", false);
    }

    public static JedisPool getJedisPool() {
        return jedisPool;
    }

    @Deprecated
    public static void jedisTest() {
        JedisFactory123();


        //Jedis jedis = new Jedis(main.getInstance().dbaddress, 6614, 30000, main.getInstance().password, false);
        //jedis.auth();
        //jedis.close();


        //ArrayList<String> keynames = new ArrayList<>();


        JedisPoolConfig poolconfig = new JedisPoolConfig();
        //poolconfig.setMaxIdle(8);
        //poolconfig.setMaxTotal(32);



        new BukkitRunnable() {
            @Override
            public void run() {
                JedisPubSub jedisPubSub = new JedisPubSub() {
                    public void onMessage(String channel, String message) {
                        if (channel.equalsIgnoreCase("client1")) {
                            JedisUtils.HashMapUpdate(JedisUtils.String2HashMap(message), "client1");
                        }
                    }
                };
                getJedisPool().getResource().subscribe(jedisPubSub, new String[] { "client1" });
                JedisPubSub jedisPubSub1 = new JedisPubSub() {
                    public void onMessage(String channel, String message) {
                        if (channel.equalsIgnoreCase("client2")) {
                            JedisUtils.HashMapUpdate(JedisUtils.String2HashMap(message), "client2");
                        }
                    }
                };
                getJedisPool().getResource().subscribe(jedisPubSub1, new String[] { "client2" });
                JedisPubSub jedisPubSub2 = new JedisPubSub() {
                    public void onMessage(String channel, String message) {
                        if (channel.equalsIgnoreCase("client3")) {
                            //updatehashmap(message, "client3");
                            JedisUtils.HashMapUpdate(JedisUtils.String2HashMap(message), "client3");
                        }
                    }
                };
                getJedisPool().getResource().subscribe(jedisPubSub2, new String[] { "client3" });
                JedisPubSub jedisPubSub3 = new JedisPubSub() {
                    public void onMessage(String channel, String message) {
                        if (channel.equalsIgnoreCase("client4")) {
                            JedisUtils.HashMapUpdate(JedisUtils.String2HashMap(message), "client4");
                        }
                    }
                };
                getJedisPool().getResource().subscribe(jedisPubSub3, new String[] { "client4" });
            }
        }.runTaskAsynchronously(Main.getInstance());



        new BukkitRunnable() {



            HashMap<String, String> commandlist1 = new HashMap<>();
            HashMap<String, String> commandlist2 = new HashMap<>();
            HashMap<String, String> commandlist3 = new HashMap<>();
            HashMap<String, String> commandlist4 = new HashMap<>();

            HashMap<String, String> chatmsg1 = new HashMap<>();
            HashMap<String, String> chatmsg2 = new HashMap<>();
            HashMap<String, String> chatmsg3 = new HashMap<>();
            HashMap<String, String> chatmsg4 = new HashMap<>();



            @Override
            public void run(){


                //Pipeline pipeline = jedis.pipelined();


                HashMap<String, String> commandlist1 = new HashMap<>();
                HashMap<String, String> commandlist2 = new HashMap<>();
                HashMap<String, String> commandlist3 = new HashMap<>();
                HashMap<String, String> commandlist4 = new HashMap<>();
                HashMap<String, String> commandlist5 = new HashMap<>(); //not used rn

                HashMap<String, String> chatmsg1 = new HashMap<>();
                HashMap<String, String> chatmsg2 = new HashMap<>();
                HashMap<String, String> chatmsg3 = new HashMap<>();
                HashMap<String, String> chatmsg4 = new HashMap<>();
                HashMap<String, String> chatmsg5 = new HashMap<>(); //not used rn

                HashMap<String, String> bungeeinfo1 = new HashMap<>();
                try(Jedis jedis1 = getJedisPool().getResource()) {
                    if (jedis1.isConnected()) {
                        if(jedis1.exists(Main.getInstance().clientname + "-forcesync")) {
                            String id = Main.getInstance().clientname + "-forcesync";
                            //Pipeline pipeline = jedissync.pipelined();
                            ConcurrentHashMap<String, String> teamrank = new ConcurrentHashMap<>();
                            ConcurrentHashMap<String, String> teampvp = new ConcurrentHashMap<>();
                            ConcurrentHashMap<String, String> teams = new ConcurrentHashMap<>();
                            ConcurrentHashMap<String, String> tempteammember = new ConcurrentHashMap<>();
                            //HashMap<String, String> fuckubich = new HashMap<>();
                            ConcurrentHashMap<String, ArrayList<String>> teammember = new ConcurrentHashMap<>();

                            ConcurrentHashMap<String, String> namename = new ConcurrentHashMap<>();
                            ConcurrentHashMap<String, String> nameuuid = new ConcurrentHashMap<>();
                            ConcurrentHashMap<String, String> uuidname = new ConcurrentHashMap<>();
                            teamrank = (ConcurrentHashMap<String, String>) jedis1.hgetAll(id + ":teamrank");
                            teampvp = (ConcurrentHashMap<String, String>) jedis1.hgetAll(id + ":teampvp");
                            teams = (ConcurrentHashMap<String, String>) jedis1.hgetAll(id + ":teams");
                            //fuckubich = (HashMap<String, String>) jedissync.hgetAll(id + ":fuckubich");
                            tempteammember = (ConcurrentHashMap<String, String>) jedis1.hgetAll(id + ":teammember");
                            for (String keys : tempteammember.keySet()) {
                                ArrayList<String> memlist = String2Array(tempteammember.get(keys));
                                teammember.put(keys, memlist);
                            }

                            namename = (ConcurrentHashMap<String, String>) jedis1.hgetAll(id + ":namename");
                            nameuuid = (ConcurrentHashMap<String, String>) jedis1.hgetAll(id + ":nameuuid");
                            uuidname = (ConcurrentHashMap<String, String>) jedis1.hgetAll(id + ":uuidname");

                        ArrayList<HashMap<String, String>> listofhmtoclear = new ArrayList<>();
                        listofhmtoclear.add(storage.teamrank);
                        listofhmtoclear.add(storage.teampvp);
                        listofhmtoclear.add(storage.teams);
                        listofhmtoclear.add(storage.teammember);

                            storage.teamrank.clear();
                            storage.teampvp.clear();
                            storage.teams.clear();
                            storage.teammember.clear();

                            listener.namename.clear();
                            listener.nameuuid.clear();
                            listener.uuidname.clear();

                            storage.teamrank = teamrank;
                            storage.teampvp = teampvp;
                            storage.teams = teams;
                            storage.teammember = teammember;

                            listener.namename = namename;
                            listener.nameuuid = nameuuid;
                            listener.uuidname = uuidname;
                            //.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&l[!] &f성공적으로 SYNC 테스크 완료"));


                            //storage.teamrank.clear();


                            //pipeline.sync();


                            jedis1.del(Main.getInstance().clientname + "-forcesync");
                        }
                        //=======><><><><=======


                        commandlist1 = (HashMap<String, String>) jedis1.hgetAll("client1");
                        commandlist2 = (HashMap<String, String>) jedis1.hgetAll("client2");
                        commandlist3 = (HashMap<String, String>) jedis1.hgetAll("client3");
                        commandlist4 = (HashMap<String, String>) jedis1.hgetAll("client4");
                        commandlist5 = (HashMap<String, String>) jedis1.hgetAll("client5");

                        chatmsg1 = (HashMap<String, String>) jedis1.hgetAll("client1chatmsg");
                        chatmsg2 = (HashMap<String, String>) jedis1.hgetAll("client2chatmsg");
                        chatmsg3 = (HashMap<String, String>) jedis1.hgetAll("client3chatmsg");
                        chatmsg4 = (HashMap<String, String>) jedis1.hgetAll("client4chatmsg");
                        chatmsg5 = (HashMap<String, String>) jedis1.hgetAll("client5chatmsg");

                        bungeeinfo1 = (HashMap<String, String>) jedis1.hgetAll("bungee1");
                        ConcurrentHashMap<String, String> redisupdateqtemp;
                        synchronized (JedisTempStorage.TempCommandQueue) {
                            redisupdateqtemp = JedisTempStorage.TempCommandQueue;
                            JedisTempStorage.TempCommandQueue.clear();
                        }

                        ConcurrentHashMap<String, String> redischatsyncqtemp;
                        synchronized (JedisTempStorage.TempChatQueue) {
                            redischatsyncqtemp = JedisTempStorage.TempChatQueue;
                            JedisTempStorage.TempChatQueue.clear();
                        }
                        jedis1.set("ping-" + Main.ServerName, "ping");
                        jedis1.expire("ping-" + Main.ServerName, 10);
                        if(!redisupdateqtemp.isEmpty()) {
                            jedis1.del(String.valueOf(Main.ServerName));
                            jedis1.hmset(String.valueOf(Main.ServerName), redisupdateqtemp);
                            //jedis.expire(String.valueOf(main.getInstance().clientname) + "", 1);
                            jedis1.expireAt(String.valueOf(Main.ServerName), System.currentTimeMillis() + 200);
                            redisupdateqtemp.clear();
                        } else {
                            jedis1.del(String.valueOf(Main.ServerName));
                        }
                        if (!redischatsyncqtemp.isEmpty()) {
                            jedis1.del(String.valueOf(Main.ServerName + "chatmsg"));
                            //jedis.
                            jedis1.hmset(String.valueOf(Main.ServerName + "chatmsg"), redischatsyncqtemp);
                            jedis1.expireAt(String.valueOf(Main.ServerName + "chatmsg"), System.currentTimeMillis() + 200);
                            redischatsyncqtemp.clear();
                        } else {
                            jedis1.del(String.valueOf(Main.ServerName + "chatmsg"));
                        }
                        if(false) {

                            jedis1.set("synctime-" + Main.ServerName, String.valueOf(JEDISSYNCCLOCK));
                            for(int x = 1; x <= 5; x++) {
                                String wfjfe = jedis1.get("synctime-client" + x);

                                if(wfjfe == null) {

                                } else {
                                    Long c = Long.parseLong(wfjfe);
                                    if (c <= 10L) {
                                        ReSetIFBiggerThen(x, 10L);
                                        for (int timesynckey = 1; timesynckey <= c + 1; timesynckey++) {
                                            HashMap<String, String> templist = (HashMap<String, String>) jedis1.hgetAll("client" + x + "-" + timesynckey);
                                            if (!templist.isEmpty()) {
                                                SETCLOCK(x, Long.parseLong(String.valueOf(timesynckey)));
                                                for (int px = 1; px <= Integer.valueOf(templist.get("MAXAMOUNT")); px++) {
                                                    updatehashmap(templist.get(String.valueOf(c)), "client" + x);
                                                }
                                            }
                                        }
                                    }
                                    if (GETCLOCK(x) == 1L) {
                                        Long temp = c + 1;
                                        HashMap<String, String> templist = (HashMap<String, String>) jedis1.hgetAll("client" + x + "-" + c);
                                        HashMap<String, String> templist2 = (HashMap<String, String>) jedis1.hgetAll("client" + x + "-" + temp);
                                        if (!templist.isEmpty()) {
                                            SETCLOCK(x, c);
                                            for (int px = 1; px <= Integer.valueOf(templist.get("MAXAMOUNT")); px++) {
                                                updatehashmap(templist.get(String.valueOf(c)), "client" + x);
                                            }
                                        }
                                        if (!templist2.isEmpty()) {
                                            SETCLOCK(x, temp);
                                            for (int px = 1; px <= Integer.valueOf(templist2.get("MAXAMOUNT")); px++) {
                                                updatehashmap(templist2.get(String.valueOf(c)), "client" + x);
                                            }
                                        }
                                    }
                                    while (c > GETCLOCK(x)) {
                                        HashMap<String, String> MAINLIST = (HashMap<String, String>) jedis1.hgetAll("client" + x + "-" + (GETCLOCK(x)));
                                        HashMap<String, String> MAINCHATLIST = (HashMap<String, String>) jedis1.hgetAll("client" + x + "chatmsg-" + (GETCLOCK(x)));
                                        SETCLOCK(x, (GETCLOCK(x) + 1));
                                        if (!MAINLIST.isEmpty()) {
                                            for (int px = 1; px <= Integer.valueOf(MAINLIST.get("MAXAMOUNT")); px++) {
                                                updatehashmap(MAINLIST.get(String.valueOf(c)), "client" + x);
                                            }
                                        }
                                        if (!MAINCHATLIST.isEmpty()) {
                                            for (int px = 1; px <= Integer.valueOf(MAINCHATLIST.get("MAXAMOUNT")); px++) {
                                                updatehashmap(MAINCHATLIST.get(String.valueOf(c)), "client" + x);
                                            }
                                        }
                                    }
                                }
                            }
                            JedisTempStorage.TempCommandQueue.put("D", "d");
                            ConcurrentHashMap<String, String> redisupdateqtemp = JedisTempStorage.TempCommandQueue;
                            ConcurrentHashMap<String, String> redischatsyncqtemp = JedisTempStorage.TempChatQueue;
                            JedisTempStorage.TempCommandQueue.clear();
                            JedisTempStorage.TempChatQueue.clear();
                            jedis1.set("ping-" + Main.ServerName, "ping");
                            jedis1.expire("ping-" + Main.ServerName, 10);
                            if(!redisupdateqtemp.isEmpty()) {
                                jedis1.hmset(String.valueOf(Main.ServerName) + "-" + JEDISSYNCCLOCK, redisupdateqtemp);
                                //jedis.expire(String.valueOf(main.getInstance().clientname) + "", 1);
                                jedis1.expireAt(String.valueOf(Main.ServerName) + "-" + JEDISSYNCCLOCK, System.currentTimeMillis() + 60000);
                                redisupdateqtemp.clear();

                                //010101001001Main
                            } else {
                                //jedis1.del(String.valueOf(Main.ServerName));
                            }
                            if (!redischatsyncqtemp.isEmpty()) {
                                //jedis1.del(String.valueOf(Main.ServerName + "chatmsg" + "-" + JEDISSYNCCLOCK));
                                //jedis.
                                jedis1.hmset(String.valueOf(Main.ServerName + "chatmsg" + "-" + JEDISSYNCCLOCK), redischatsyncqtemp);
                                jedis1.expireAt(String.valueOf(Main.ServerName + "chatmsg" + "-" + JEDISSYNCCLOCK), System.currentTimeMillis() + 200);
                                redischatsyncqtemp.clear();
                            } else {
                                //jedis1.del(String.valueOf(Main.ServerName + "chatmsg"));
                            }
                        } else {


                        }







                    } else {

                        System.out.println("Not connected to any Redis Server!!!");
                    }
                } catch (Exception e) {

                    System.out.println("[WARNING] " + e.getMessage());
                    e.printStackTrace();
                }


                if (!commandlist1.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(commandlist1.get("MAXAMOUNT")); c++) {
                        updatehashmap(commandlist1.get(String.valueOf(c)), "client1");
                    }
                }
                commandlist1.clear();
                if (!commandlist2.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(commandlist2.get("MAXAMOUNT")); c++) {
                        updatehashmap(commandlist2.get(String.valueOf(c)), "client2");
                    }
                }
                commandlist2.clear();
                if (!commandlist3.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(commandlist3.get("MAXAMOUNT")); c++) {
                        updatehashmap(commandlist3.get(String.valueOf(c)), "client3");
                    }
                }
                commandlist3.clear();
                if (!commandlist4.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(commandlist4.get("MAXAMOUNT")); c++) {
                        updatehashmap(commandlist4.get(String.valueOf(c)), "client4");
                    }
                }
                commandlist4.clear();
                if(!commandlist5.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(commandlist5.get("MAXAMOUNT")); c++) {
                        updatehashmap(commandlist5.get(String.valueOf(c)), "client5");
                    }
                }
                commandlist5.clear();






                //Chat
                if (!chatmsg1.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(chatmsg1.get("MAXAMOUNT")); c++) {
                        updatehashmap(chatmsg1.get(String.valueOf(c)), "client1");
                    }
                }
                chatmsg1.clear();
                if (!chatmsg2.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(chatmsg2.get("MAXAMOUNT")); c++) {
                        updatehashmap(chatmsg2.get(String.valueOf(c)), "client2");
                    }
                }
                chatmsg2.clear();
                if (!chatmsg3.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(chatmsg3.get("MAXAMOUNT")); c++) {
                        updatehashmap(chatmsg3.get(String.valueOf(c)), "client3");
                    }
                }
                chatmsg3.clear();
                if (!chatmsg4.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(chatmsg4.get("MAXAMOUNT")); c++) {
                        updatehashmap(chatmsg4.get(String.valueOf(c)), "client4");
                    }
                }
                chatmsg4.clear();
                if(!chatmsg5.isEmpty()) {
                    for(int c = 1; c <= Integer.valueOf(chatmsg5.get("MAXAMOUNT")); c++) {
                        updatehashmap(chatmsg5.get(String.valueOf(c)), "client5");
                    }
                }


                //jedis.hmset(main.clientname, RedisUpdateQ);
                    if (!RedisUpdateQ.isEmpty()) {
                        jedis.del(String.valueOf(main.getInstance().clientname).toString() + "");
                        jedis.hmset(String.valueOf(main.getInstance().clientname).toString() + "", RedisUpdateQ);
                        //jedis.expire(String.valueOf(main.getInstance().clientname) + "", 1);
                        jedis.expire(String.valueOf(main.getInstance().clientname.toString()).toString(), 1);
                        RedisUpdateQ.clear();
                    } else {
                        jedis.del(String.valueOf(main.getInstance().clientname) + "");
                        RedisUpdateQ.clear();
                    }
                    if(!RedisChatSyncQ.isEmpty()) {
                        jedis.del(String.valueOf(main.getInstance().clientname + "chatmsg").toString());
                        //jedis.
                        jedis.hmset(String.valueOf(main.getInstance().clientname + "chatmsg").toString(), RedisChatSyncQ);
                        jedis.expire(String.valueOf(main.getInstance().clientname + "chatmsg").toString(), 1);
                        RedisChatSyncQ.clear();
                    } else {
                        jedis.del(String.valueOf(main.getInstance().clientname + "chatmsg").toString());
                    }
                    try {
                        jedis.del(main.getInstance().clientname);
                        jedis.del(main.getInstance().clientname + "chatmsg");
                    } catch (Throwable e) {
                        System.out.println(e.getMessage());

                    }
                new Thread( () -> {
                    HashMap<String, String> templands = FactionStorage.LandToFaction;
                    for(String key : templands.keySet()) {
                        FactionStorage.AsyncLandToFaction.put(key, templands.get(key));
                    }
                    HashMap<String, String> tempoutposts = FactionStorage.OutPostToFaction;
                    for(String key : tempoutposts.keySet()) {
                        FactionStorage.AsyncOutPostToFaction.put(key, tempoutposts.get(key));
                    }
                }).start();



                JEDISSYNCCLOCK++;
            }

        }.runTaskTimerAsynchronously(Main.getInstance(), 1L, 1L);//runTaskTimer(main.getInstance(), 5L, 5L);




    }

    @Deprecated
    public static void updatehashmap(String k, String ServerName) {
        double c = 1000;
        if(c > 600) {
            String[] args = k.split(":=:");
            if(args[0].equalsIgnoreCase("update")) {
                if(args[1].equalsIgnoreCase("FactionToLand")
                        || args[1].equalsIgnoreCase("LandToFaction")
                        || args[1].equalsIgnoreCase("FactionRank")
                        || args[1].equalsIgnoreCase("PlayerFaction")
                        || args[1].equalsIgnoreCase("FactionMember")
                        || args[1].equalsIgnoreCase("FactionNameToFactionName")
                        || args[1].equalsIgnoreCase("FactionNameToFactionUUID")
                        || args[1].equalsIgnoreCase("FactionUUIDToFactionName")
                        || args[1].equalsIgnoreCase("FactionInviteQueue")
                        || args[1].equalsIgnoreCase("FactionDTR")
                        || args[1].equalsIgnoreCase("FactionInfo")
                        || args[1].equalsIgnoreCase("Timeout2")
                        || args[1].equalsIgnoreCase("Timeout2info")
                        || args[1].equalsIgnoreCase("FactionOutPost")
                        || args[1].equalsIgnoreCase("FactionOutPostList")
                        || args[1].equalsIgnoreCase("FactionToOutPost")
                        || args[1].equalsIgnoreCase("OutPostToFaction")) {
                    FactionStorage.FactionStorageUpdateHandler(args, ServerName);
                } else if(args[1].equalsIgnoreCase("namename")
                        || args[1].equalsIgnoreCase("nameuuid")
                        || args[1].equalsIgnoreCase("uuidname")) {
                    UserInfoStorage.UserInfoStorageUpdateHandler(args);
                } else if(args[1].equalsIgnoreCase("cachedDTR")
                        || args[1].equalsIgnoreCase("cachedBank")) {
                    CachedStorage.JedisCacheSync(args);
                }
            } else if(args[0].equalsIgnoreCase("ping")) {
                //jedis.c = Math.toIntExact(c + 1);
                if(c == 4) {
                    //jedis.c = 0;
                    System.out.println("Pinged from Redis Database");
                }
            } else if(args[0].equalsIgnoreCase("chat")) {
                String playeruuid = args[1];
                String message = args[2];
                FactionUtils.FactionChat(playeruuid, message);
                //utils.teamchat(playeruuid, message);
            } else if(args[0].equalsIgnoreCase("proxyuserupdate")) {

                //String playeruuid = args[1];
                //if(args[0].equalsIgnoreCase())
                //String targetuuid = args[2];
                //String message = args[3];
                //String trueorfalse = args[4];
                //utils.teamnotify(playeruuid, targetuuid, message, trueorfalse);
            } else if(args[0].equalsIgnoreCase("notify")) {

                String playeruuid = args[1];
                String targetuuid = args[2];
                String message = args[3];
                String trueorfalse = args[4];
                FactionUtils.FactionNotify(playeruuid, targetuuid, message, trueorfalse);
                //utils.teamnotify(playeruuid, targetuuid, message, trueorfalse);
            } else if(args[0].equalsIgnoreCase("warplocation")) {
                String targetuuid = args[1];
                String Server = args[2];
                String stringlocation = args[3];
                String expired = args[4];
                if(Main.ServerName.equalsIgnoreCase(Server)) {
                    if(!expired.equalsIgnoreCase("expired")) {
                        Location loc = SystemUtils.string2loc(stringlocation);
                        PlayerListener.onJoinWarp.put(targetuuid, loc);
                    } else {
                        PlayerListener.onJoinWarp.remove(targetuuid);
                    }
                }
            } else {
                System.out.println("[WARNING (REDIS)] WRONG COMMAND USAGE FROM REDIS" + " ( '" + k + "' )");
            }


        } else if(c <= 600) {
            String warn = "data update failed... trying to update data that should already been processed or update has been duplicated / processed delayed (" + String.valueOf(c) + ")";
            SystemUtils.warning(warn);
            //utils.broadcastwarn(warn);
        }
        //example command "update:=:hashmap1~4:=:add/remove:=:key:=:add/remove/(앞에 remove일 경우 여기랑 이 뒤는 쓸 필요 없다):=:value"


    }

}*/
