package com.itndev.factions;

import com.itndev.factions.Jedis.JedisManager;
import com.itndev.factions.MySQL.*;
import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.RegisterStuff;

import com.itndev.factions.Utils.ValidChecker;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class Main extends JavaPlugin {


    public static String ServerName = "dddd";

    public static Main instance;

    public static Economy econ = null;

    public static HikariCP hikariCP = new HikariCP();

    public static wtfDatabase database = new wtfDatabase();

    public MySQLConnection sql = new MySQLConnection();

    public MySQLManager sqlmanager = new MySQLManager();

    public MySQLUtils sqlutils = new MySQLUtils();

    public static Main getInstance() {
        return instance;
    }

    @Deprecated
    @Override
    public void onEnable() {
        instance = this;
        //sql.SetupMySQL();
        try {
            sql.connect();

            hikariCP.setupHikariInfo();
            hikariCP.ConnectHikari();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        sqlmanager.FactionDataInfoTableUpdate();

        JedisManager.JedisFactory123();

        RegisterStuff.RegisterFactionCommands();
        RegisterStuff.RegisterListener();
        setupEconomy();

        ValidChecker.setvalid();

    }

    @Override
    public void onDisable() {
        sql.disconnect();
        instance = null;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    public static Economy getEconomy() {
        return econ;
    }
}
