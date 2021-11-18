package com.itndev.factions.MySQL;

import com.itndev.factions.Main;
import com.itndev.factions.Storage.FactionStorage;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.UserInfoUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.*;

public class MySQLManager {



    /*

    public void FactionDataInfoTableUpdate() {
        CreateFactionNameTable();
        CreateFactionDTRTable();
        CreateFactionBankTable();
    }

    public void CreateFactionNameTable() {
        try {
            PreparedStatement ps = Main.getInstance().sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS FactionName "
                    + "(FactionName VARCHAR(100),"
                    + "FactionUUID VARCHAR(100),"
                    + "FactionNameCap VARCHAR(100),"
                    + "PRIMARY KEY (FactionName))");
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void CreateFactionDTRTable() {
        try {
            PreparedStatement ps = Main.getInstance().sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS FactionDTR "
                    + "(FactionUUID VARCHAR(100),"
                    + "FactionName VARCHAR(100),"
                    + "FactionDTR VARCHAR(100),"
                    + "PRIMARY KEY (FactionName))");
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void CreateFactionBankTable() {
        try {
            PreparedStatement ps = Main.getInstance().sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS FactionBank "
                    + "(FactionUUID VARCHAR(100),"
                    + "FactionName VARCHAR(100),"
                    + "FactionBank VARCHAR(100),"
                    + "PRIMARY KEY (FactionName))");
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void AddDTR(String FactionUUID, double DTR, Boolean isMinus, String causeUUID) {


        String name = UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(causeUUID));
        FactionUtils.SendFactionMessage(causeUUID, causeUUID, "TeamChat", "&c" + name + "&r&f이가 죽어 &7DTR&r&f이 &c" + DTR + "&r&f만큼 감소하였습니다");


    }
    @Deprecated
    public void DTRUpdate(String FactionUUID, double DTR, Boolean isMinus, String causeUUID) {

        new BukkitRunnable() {
            @Override
            public void run() {
                if(FactionDTRExists(FactionUUID)) {
                    if(isMinus) {
                        if(causeUUID != null) {
                            if(causeUUID.equalsIgnoreCase("PLAYERLEAVE")) {

                            } else {

                            }
                        } else {
                            SystemUtils.warning("&7해당 국가 [ UUID : " + FactionUUID
                                    + " / NAME : " + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + " ] 이의 DTR 업데이트 와중에 &ccauseUUID&7가 &6Null &7임으로 실패 하였습니다");
                        }
                    } else {
                        //DTR 추가
                    }
                } else {
                    SystemUtils.warning("&7해당 국가 [ UUID : " + FactionUUID
                            + " / NAME : " + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + " ] 이의 DTR이 존재하지 않습니다");
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public Boolean FactionNameExists(String FactionName) {
        if(FactionStorage.FactionNameToFactionUUID.containsKey(FactionName)) {
            return true;
        } else {
            try {
                PreparedStatement ps = Main.getInstance().sql.getConnection().prepareStatement("SELECT * FROM FactionName WHERE FactionName=?");
                ps.setString(1, FactionName);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }



    public Boolean FactionDTRExists(String FactionUUID) {
        try {
            PreparedStatement ps = Main.getInstance().sql.getConnection().prepareStatement("SELECT * FROM FactionDTR WHERE FactionUUID=?");
            ps.setString(1, FactionUUID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public CompletableFuture<Double> getAsyncFactionDTR(String FactionUUID) {
        CompletableFuture<Double> FutureDTR = new CompletableFuture<>();

        new Thread( () -> {
            try {
                PreparedStatement ps = Main.getInstance().sql.getConnection().prepareStatement("SELECT " +
                        "FactionDTR FROM FactionDTR WHERE FactionUUID=?");
                ps.setString(1, FactionUUID);
                ResultSet rs = ps.executeQuery();
                double DTR = 0;
                if(rs.next()) {
                    DTR = rs.getDouble("FactionDTR");
                    FutureDTR.complete(DTR);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();

        return FutureDTR;
    }


    public Boolean AsyncFactionDTRExists(String FactionUUID) {
        new Thread() {

        };
        try {
            PreparedStatement ps = Main.getInstance().sql.getConnection().prepareStatement("SELECT * FROM FactionDTR WHERE FactionUUID=?");
            ps.setString(1, FactionUUID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return false;
    }
    public Boolean FactionBankExists(String FactionUUID) {
        try {
            PreparedStatement ps = Main.getInstance().sql.getConnection().prepareStatement("SELECT * FROM FactionBank WHERE FactionUUID=?");
            ps.setString(1, FactionUUID);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return false;
    }
    */
}
