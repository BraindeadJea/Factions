package com.itndev.factions.Utils;

import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Storage.UserInfoStorage;

import java.util.Locale;

public class UserInfoUtils {

    public static void setPlayerName(String UUID, String newName) {
        JedisTempStorage.AddCommandToQueue("update:=:uuidname:=:add:=:" + UUID + ":=:add:=:" + newName.toLowerCase(Locale.ROOT));
    }
    public static void setPlayerUUID(String newName, String UUID) {

        String oldname = UserInfoStorage.uuidname.get(UUID);
        if(!newName.equalsIgnoreCase(oldname)) {
            JedisTempStorage.AddCommandToQueue("update:=:nameuuid:=:add:=:" + newName.toLowerCase(Locale.ROOT) + ":=:add:=:" + UUID);
        }
    }
    public static void setPlayerOrginName(String newName, String UUID) {
        String oldname = UserInfoStorage.uuidname.get(UUID);
        if(!newName.equalsIgnoreCase(oldname)) {
            JedisTempStorage.AddCommandToQueue("update:=:namename:=:remove:=:" + oldname.toLowerCase(Locale.ROOT) + ":=:add:=:" + newName);
            JedisTempStorage.AddCommandToQueue("update:=:namename:=:add:=:" + newName.toLowerCase(Locale.ROOT) + ":=:add:=:" + newName);
        } else {
            JedisTempStorage.AddCommandToQueue("update:=:namename:=:add:=:" + newName.toLowerCase(Locale.ROOT) + ":=:add:=:" + newName);
        }
    }

    public static void getPlayerUUID() {

    }

    public static Boolean hasJoined(String name) {
        return (UserInfoStorage.nameuuid.containsKey(name.toLowerCase(Locale.ROOT)));
    }

    public static Boolean hasJoinedUUID(String UUID) {
        return (UserInfoStorage.uuidname.containsKey(UUID));
    }

    public static String getPlayerName(String UUID) {
        String finalname = null;
        if(UserInfoStorage.uuidname.containsKey(UUID)) {
            finalname = UserInfoStorage.uuidname.get(UUID);
        }
        return finalname;
    }
    public static String getPlayerUUID(String name) {
        String finalUUID = null;
        if(UserInfoStorage.nameuuid.containsKey(name)) {
            finalUUID = UserInfoStorage.nameuuid.get(name);
        }
        return finalUUID;
    }
    public static String getPlayerOrginName(String name) {
        String finalname = null;
        if(UserInfoStorage.namename.containsKey(name)) {
            finalname = UserInfoStorage.namename.get(name);
        }
        return finalname;
    }






}
