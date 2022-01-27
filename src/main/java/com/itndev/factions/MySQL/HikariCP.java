package com.itndev.factions.MySQL;

import com.itndev.factions.Main;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HikariCP {

    private HikariDataSource dataSource;

    private Connection connection;

    private String host, port, database, username, password;

    private Boolean useSSL;

    public void setupHikariInfo() {
        host = "db.itndev.com";
        port = "5567";
        database = "factioninfo";
        username = "Skadi";
        password = "l80oKGTFA#@fCRH75v5w6fefw";
        useSSL = true;
    }
    //com.mysql.jdbc.jdbc2.optional.MysqlDataSource

    public void ConnectHikari() {
        dataSource = new HikariDataSource();
        dataSource.setMaximumPoolSize(10);
        dataSource.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
        dataSource.addDataSourceProperty("serverName", host);
        dataSource.addDataSourceProperty("port", port);
        dataSource.addDataSourceProperty("databaseName", database);
        dataSource.addDataSourceProperty("user", username);
        dataSource.addDataSourceProperty("password", password);
        PingDatabase();
    }

    public void PingDatabase() {
        try {
            connection = dataSource.getConnection();
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        getHikariConnection().prepareStatement("SELECT FactionName FROM FactionName WHERE='TEST'");
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }.runTaskTimer(Main.getInstance(), 10L, 12000L);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Connection getHikariConnection() {
        if(connection != null) {
            return connection;
        } else {
            try {
                ConnectHikari();
                connection = dataSource.getConnection();
                return connection;
            } catch (SQLException e) {
                e.printStackTrace();
                return connection;
            }
        }
    }

    public void createHikariTable() {
        CreateFactionNameTable();
        CreateFactionDTRTable();
        CreateFactionBankTable();
        CreateFactionELOTable();
    }

    public void CreateFactionNameTable() {
        try {
            PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("CREATE TABLE IF NOT EXISTS FactionName "
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
            PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("CREATE TABLE IF NOT EXISTS FactionDTR "
                    + "(FactionUUID VARCHAR(100),"
                    + "FactionName VARCHAR(100),"
                    + "FactionDTR VARCHAR(100),"
                    + "PRIMARY KEY (FactionUUID))");
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void CreateFactionBankTable() {
        try {
            PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("CREATE TABLE IF NOT EXISTS FactionBank "
                    + "(FactionUUID VARCHAR(100),"
                    + "FactionName VARCHAR(100),"
                    + "FactionBank VARCHAR(100),"
                    + "PRIMARY KEY (FactionUUID))");
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void CreateFactionELOTable() {
        try {
            PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("CREATE TABLE IF NOT EXISTS FactionELO "
                    + "(FactionUUID VARCHAR(100),"
                    + "FactionName VARCHAR(100),"
                    + "FactionELO VARCHAR(100),"
                    + "PRIMARY KEY (FactionUUID))"); //%^G$%G$%D&*3#d^ %^U5jui34jhhy5i4$y7G54U^5ty
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
