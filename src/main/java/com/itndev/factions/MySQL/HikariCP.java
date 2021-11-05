package com.itndev.factions.MySQL;

import com.itndev.factions.Main;
import com.zaxxer.hikari.HikariDataSource;

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

    public void ConnectHikari() {
        dataSource = new HikariDataSource();
        dataSource.setMaximumPoolSize(10);
        dataSource.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        dataSource.addDataSourceProperty("serverName", host);
        dataSource.addDataSourceProperty("port", port);
        dataSource.addDataSourceProperty("databaseName", database);
        dataSource.addDataSourceProperty("user", username);
        dataSource.addDataSourceProperty("password", password);

    }

    public Connection getHikariConnection() {
        if(connection != null) {
            return connection;
        } else {
            try {
                connection = dataSource.getConnection();
                return connection;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void createHikairTable() {
        CreateFactionNameTable();
        CreateFactionDTRTable();
        CreateFactionBankTable();
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
}
