package com.itndev.factions.MySQL;

import com.itndev.factions.Main;
import com.itndev.factions.Storage.FactionStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class wtfDatabase {

    public void AddNewFactionName(String FactionNameCapped, String FactionUUID) {
        new Thread(() -> {
            String FactionName = FactionNameCapped.toLowerCase(Locale.ROOT);
            if(!Main.getInstance().sqlmanager.FactionNameExists(FactionName)) {
                try {
                    PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("INSERT IGNORE INTO FactionName"
                            + " (FactionName,FactionUUID) VALUES (?,?)");
                    ps.setString(1, FactionName);
                    ps.setString(2, FactionUUID);
                    ps.executeUpdate();
                    PreparedStatement ps2 = Main.hikariCP.getHikariConnection().prepareStatement("UPDATE FactionName" +
                            " SET FactionNameCap=? WHERE FactionUUID=?");
                    ps2.setString(1, FactionNameCapped);
                    ps2.setString(2, FactionUUID);
                    ps2.executeQuery();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void DeleteFactionName(String FactionUUID) {
        new Thread(() -> {
            try {
                PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("DELETE FROM FactionName WHERE FactionUUID=?");
                ps.setString(1, FactionUUID);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void CreateNewDTR(String FactionUUID, String FactionName) {
        new Thread(() -> {
            try {
                PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("INSERT IGNORE INTO FactionDTR" +
                         " (FactionUUID,FactionName) VALUES (?,?)");
                ps.setString(1, FactionUUID);
                ps.setString(2, FactionName);
                ps.executeUpdate();
                PreparedStatement ps2 = Main.hikariCP.getHikariConnection().prepareStatement("UPDATE FactionDTR" +
                        " SET FactionDTR=? WHERE FactionUUID=?");
                ps2.setString(1, "100");
                ps2.setString(2, FactionUUID);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void CreateNewBank(String FactionUUID, String FactionName) {
        new Thread(() -> {
            try {
                PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("INSERT IGNORE INTO FactionBank" +
                        " (FactionUUID,FactionName) VALUES (?,?)");
                ps.setString(1, FactionUUID);
                ps.setString(2, FactionName);
                ps.executeUpdate();
                PreparedStatement ps2 = Main.hikariCP.getHikariConnection().prepareStatement("UPDATE FactionBank" +
                        " SET FactionDTR=? WHERE FactionUUID=?");
                ps2.setString(1, "0");
                ps2.setString(2, FactionUUID);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public CompletableFuture<Boolean> isExistingName(String FactionName) {
        CompletableFuture<Boolean> FutureBoolean = new CompletableFuture<>();
        new Thread(() -> {
            if(FactionStorage.FactionNameToFactionUUID.containsKey(FactionName.toLowerCase(Locale.ROOT))) {
                FutureBoolean.complete(true);
            } else {
                try {
                    PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("SELECT * FROM FactionName WHERE FactionName=?");
                    ps.setString(1, FactionName.toLowerCase(Locale.ROOT));
                    ResultSet rs = ps.executeQuery();
                    if(rs.next()) {
                        FutureBoolean.complete(true);
                    } else {
                        FutureBoolean.complete(false);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        return FutureBoolean;
    }

    public void DeleteFactionDTR(String FactionUUID) {
        new Thread(() -> {
            try {
                PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("DELETE FROM FactionDTR WHERE FactionUUID=?");
                ps.setString(1, FactionUUID);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void DeleteFactionBank(String FactionUUID) {
        new Thread(() -> {
            try {
                PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("DELETE FROM FactionBank WHERE FactionUUID=?");
                ps.setString(1, FactionUUID);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public CompletableFuture<Double> GetFactionDTR(String FactionUUID) {
        CompletableFuture<Double> FutureDTR = new CompletableFuture();
        new Thread( () -> {
            try {
                PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("SELECT " +
                        "FactionDTR FROM FactionDTR WHERE FactionUUID='" + FactionUUID + "'");
                ResultSet rs = ps.executeQuery();
                double DTR = 0;
                if(rs.next()) {
                    DTR = Double.parseDouble(rs.getString("FactionDTR"));
                    FutureDTR.complete(DTR);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
        return FutureDTR;
    }

    public CompletableFuture<Double> GetFactionBank(String FactionUUID) {
        CompletableFuture<Double> FutureBank = new CompletableFuture();
        new Thread( () -> {
            try {
                PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("SELECT " +
                        "FactionBank FROM FactionBank WHERE FactionUUID='" + FactionUUID + "'");
                ResultSet rs = ps.executeQuery();
                double Bank = 0;
                if(rs.next()) {
                    Bank = Double.parseDouble(rs.getString("FactionBank"));
                    FutureBank.complete(Bank);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
        return FutureBank;
    }

    public CompletableFuture<Double> AddFactionDTR(String FactionUUID, double DTR) {
        CompletableFuture<Double> futureDTR = new CompletableFuture<>();
        new Thread( () ->{
            try {

                PreparedStatement DTRUPDATE = Main.hikariCP.getHikariConnection().prepareStatement("SELECT @ORIGINNAME := (SELECT FactionDTR FROM FactionDTR WHERE FactionUUID='"+ FactionUUID +"');" +
                        "UPDATE FactionDTR SET FactionDTR=CONVERT(CONVERT(@ORIGINNAME, DOUBLE) + " + String.valueOf(DTR) + ", CHAR) WHERE FactionUUID='"+ FactionUUID +"';" +
                        "SELECT FactionDTR FROM FactionDTR WHERE FactionUUID='"+ FactionUUID +"';");
                ResultSet rs = DTRUPDATE.executeQuery();
                if(rs.next()) {
                    futureDTR.complete(rs.getDouble("FactionDTR"));
                }
                /*PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("SELECT " +
                        "FactionDTR FROM FactionDTR WHERE FactionUUID=?");
                ps.setString(1, FactionUUID);
                ResultSet rs = ps.executeQuery();
                double originDTR = Double.parseDouble(rs.getString("FactionDTR"));

                PreparedStatement ps2 = Main.hikariCP.getHikariConnection().prepareStatement("UPDATE FactionDTR" +
                        " SET FactionDTR=? WHERE FactionUUID=?");
                ps2.setString(1, String.valueOf(originDTR + DTR));
                ps2.setString(2, FactionUUID);
                ps2.executeUpdate();
                PreparedStatement unlock = Main.hikariCP.getHikariConnection().prepareStatement("UNLOCK " +
                        "TABLE FactionDTR");
                unlock.executeUpdate();*/
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
        return futureDTR;
    }

    public CompletableFuture<Double> AddFactionBank(String FactionUUID, double DTR) {
        CompletableFuture<Double> futureBank = new CompletableFuture<>();
        new Thread( () ->{
            try {

                PreparedStatement DTRUPDATE = Main.hikariCP.getHikariConnection().prepareStatement("SELECT @ORIGINNAME := (SELECT FactionBank FROM FactionBank WHERE FactionUUID='"+ FactionUUID +"');" +
                        "UPDATE FactionBank SET FactionBank=CONVERT(CONVERT(@ORIGINNAME, DOUBLE) + " + String.valueOf(DTR) + ", CHAR) WHERE FactionUUID='"+ FactionUUID +"';" +
                        "SELECT FactionBank FROM FactionBank WHERE FactionUUID='"+ FactionUUID +"';");
                ResultSet rs = DTRUPDATE.executeQuery();
                if(rs.next()) {
                    futureBank.complete(rs.getDouble("FactionBank"));
                } else {
                    futureBank.complete(null);
                }
                /*PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("SELECT " +
                        "FactionDTR FROM FactionDTR WHERE FactionUUID=?");
                ps.setString(1, FactionUUID);
                ResultSet rs = ps.executeQuery();
                double originDTR = Double.parseDouble(rs.getString("FactionDTR"));

                PreparedStatement ps2 = Main.hikariCP.getHikariConnection().prepareStatement("UPDATE FactionDTR" +
                        " SET FactionDTR=? WHERE FactionUUID=?");
                ps2.setString(1, String.valueOf(originDTR + DTR));
                ps2.setString(2, FactionUUID);
                ps2.executeUpdate();
                PreparedStatement unlock = Main.hikariCP.getHikariConnection().prepareStatement("UNLOCK " +
                        "TABLE FactionDTR");
                unlock.executeUpdate();*/
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
        return futureBank;
    }

}