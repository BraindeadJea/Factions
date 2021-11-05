package com.itndev.factions.Utils;

import com.itndev.factions.Commands.FactionMainCommand;
import com.itndev.factions.Listener.PlayerListener;
import com.itndev.factions.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class RegisterStuff {

    public static void onEnablemethod() {

    }


    public static void RegisterFactionCommands() {

        ((PluginCommand) Objects.<PluginCommand>requireNonNull(Main.getInstance().getCommand("국가"))).setExecutor(new FactionMainCommand());


    }
    public static void RegisterListener() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), (Plugin)Main.getInstance());
    }

}
