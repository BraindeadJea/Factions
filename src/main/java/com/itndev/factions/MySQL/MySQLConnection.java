package com.itndev.factions.MySQL;

import com.itndev.factions.Utils.SystemUtils;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {


    /*

    private String host = "db.itndev.com";
    private String port = "5567";
    private String database = "factioninfo";
    private String username = "Skadi";
    private String password = "l80oKGTFA#@fCRH75v5w6fefw";
    private Boolean useSSL = true;

    private HikariDataSource dataSource;

    public void SetupMySQL() {
        host = "db.itndev.com";
        port = "5567";
        database = "FactionInfo";
        username = "Skadi";
        password = "l80oKGTFA#@fCRH75v5w6few";
        useSSL = true;
    }

    public void connecthikari() {
        dataSource = new HikariDataSource();
        dataSource.setMaximumPoolSize(10);
        dataSource.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");

    }

    private Connection connection;

    public Boolean isConnected() {
        return (connection != null);
    }

    @Deprecated
    public void connect() throws ClassNotFoundException, SQLException {

        if(!isConnected()) {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=" + useSSL.toString(), username, password);
        }
        if(connection == null) {
            SystemUtils.warning("&7NOT CONNECTED TO DATABASE");
        }
    }

    public void disconnect() {
        if(isConnected()) {
            try {
                connection.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
    */
}
