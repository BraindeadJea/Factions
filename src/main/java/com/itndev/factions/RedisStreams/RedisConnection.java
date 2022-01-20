package com.itndev.factions.RedisStreams;

import com.itndev.factions.Jedis.JedisManager;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import com.itndev.factions.Utils.JedisUtils;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.StreamMessage;
import io.lettuce.core.XReadArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStreamCommands;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedisConnection {

    private static RedisClient client = null;
    private static StatefulRedisConnection<String, String> connection = null;
    private static RedisStreamCommands<String, String> commands = null;
    private static String lastSeenMessage_CLIENT1 = "0-0";
    private static String lastSeenMessage_CLIENT2 = "0-0";
    private static String lastSeenMessage_CLIENT3 = "0-0";
    private static String lastSeenMessage_CLIENT4 = "0-0";
    private static String lastSeenMessage_CLIENT5 = "0-0";

    private static String redis_address = "127.0.0.1";
    private static Integer redis_port = 6374;
    private static String redis_password = "password";
    private static Boolean sslEnabled = false;

    private static Boolean closed = false;

    public static void setRedis_address(String address) {
        redis_address = address;
    }

    public static void setRedis_port(Integer port) {
        redis_port = port;
    }

    public static void setRedis_password(String password) {
        redis_password = password;
    }

    public static void setSslEnabled(Boolean enabled) {
        sslEnabled = enabled;
    }

    public static String getLastSeenMessage_CLIENT1() {
        return lastSeenMessage_CLIENT1;
    }

    public static String getLastSeenMessage_CLIENT2() {
        return lastSeenMessage_CLIENT2;
    }

    public static String getLastSeenMessage_CLIENT3() {
        return lastSeenMessage_CLIENT3;
    }

    public static String getLastSeenMessage_CLIENT4() {
        return lastSeenMessage_CLIENT4;
    }

    public static String getLastSeenMessage_CLIENT5() {
        return lastSeenMessage_CLIENT5;
    }

    public static void setLastSeenMessage_CLIENT1(String data) {
        lastSeenMessage_CLIENT1 = data;
    }

    public static void setLastSeenMessage_CLIENT2(String data) {
        lastSeenMessage_CLIENT2 = data;
    }

    public static void setLastSeenMessage_CLIENT3(String data) {
        lastSeenMessage_CLIENT3 = data;
    }

    public static void setLastSeenMessage_CLIENT4(String data) {
        lastSeenMessage_CLIENT4 = data;
    }

    public static void setLastSeenMessage_CLIENT5(String data) {
        lastSeenMessage_CLIENT5 = data;
    }


    @Deprecated
    public static void RedisConnect() {
        RedisURI redisURI = RedisURI.Builder.redis(redis_address, redis_port).withPassword(redis_password).build();
        client = RedisClient.create(redisURI);
        connection = client.connect();
        commands = connection.sync();
    }

    public static void RedisDisConnect() {
        connection.close();
        closed = true;
    }

    private static StatefulRedisConnection<String, String> getRedisConnection() {
        if (connection == null || !connection.isOpen()) {
            connection = client.connect();
        }
        return connection;
    }

    private static RedisStreamCommands<String, String> getRedisCommands() {
        if(commands == null) {
            commands = getRedisConnection().sync();
        }
        return commands;
    }

    public static void RedisStreamReader() {

        new BukkitRunnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        ReadClient1_Redis();
                        ReadClient2_Redis();
                        ReadClient3_Redis();
                        ReadClient4_Redis();
                        ReadClient5_Redis();
                        Thread.sleep(5);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(closed) {
                        break;
                    }
                }
            }
        }.runTaskAsynchronously(Main.getInstance());


        //String lastSeenMessage = "0-0";

    }

    private static void ReadClient1_Redis() {
        List<StreamMessage<String, String>> messages = getRedisCommands().xread(
                XReadArgs.StreamOffset.from("client1", lastSeenMessage_CLIENT1));

        for (StreamMessage<String, String> message : messages) {
            lastSeenMessage_CLIENT1 = message.getId();
            String compressedhashmap = message.getBody().get("commands");
            ReadCompressedHashMap_READ("client1", compressedhashmap);
        }
    }

    private static void ReadClient2_Redis() {
        List<StreamMessage<String, String>> messages = getRedisCommands().xread(
                XReadArgs.StreamOffset.from("client2", lastSeenMessage_CLIENT2));

        for (StreamMessage<String, String> message : messages) {
            lastSeenMessage_CLIENT2 = message.getId();
            String compressedhashmap = message.getBody().get("commands");
            ReadCompressedHashMap_READ("client2", compressedhashmap);
        }
    }

    private static void ReadClient3_Redis() {
        List<StreamMessage<String, String>> messages = getRedisCommands().xread(
                XReadArgs.StreamOffset.from("client3", lastSeenMessage_CLIENT3));

        for (StreamMessage<String, String> message : messages) {
            lastSeenMessage_CLIENT3 = message.getId();
            String compressedhashmap = message.getBody().get("commands");
            ReadCompressedHashMap_READ("client3", compressedhashmap);
        }
    }

    private static void ReadClient4_Redis() {
        List<StreamMessage<String, String>> messages = getRedisCommands().xread(
                XReadArgs.StreamOffset.from("client4", lastSeenMessage_CLIENT4));

        for (StreamMessage<String, String> message : messages) {
            lastSeenMessage_CLIENT4 = message.getId();
            String compressedhashmap = message.getBody().get("commands");
            ReadCompressedHashMap_READ("client4", compressedhashmap);
        }
    }

    private static void ReadClient5_Redis() {
        List<StreamMessage<String, String>> messages = getRedisCommands().xread(
                XReadArgs.StreamOffset.from("client5", lastSeenMessage_CLIENT5));

        for (StreamMessage<String, String> message : messages) {
            lastSeenMessage_CLIENT5 = message.getId();
            String compressedhashmap = message.getBody().get("commands");
            ReadCompressedHashMap_READ("client5", compressedhashmap);
        }
    }

    @Deprecated
    private static void ReadCompressedHashMap_READ(String clientname, String compressedhashmap) {
        HashMap<String, String> map = JedisUtils.String2HashMap(compressedhashmap);
        if (!map.isEmpty()) {
            for(int c = 1; c <= Integer.valueOf(map.get("MAXAMOUNT")); c++) {
                JedisManager.updatehashmap(map.get(String.valueOf(c)), clientname);
            }
        }
    }

    public static void RedisStreamWriter() {
        new BukkitRunnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String compressedhashmap;
                        synchronized (JedisTempStorage.TempCommandQueue) {
                            compressedhashmap = JedisUtils.HashMap2String(JedisTempStorage.TempCommandQueue);
                            JedisTempStorage.TempCommandQueue.clear();
                        }
                        if(compressedhashmap != null) {
                            Map<String, String> body = Collections.singletonMap("commands", compressedhashmap);
                            getRedisCommands().xadd(Main.ServerName, body);
                        }
                        Thread.sleep(50);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(closed) {
                        break;
                    }
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }
}
