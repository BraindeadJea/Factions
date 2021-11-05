package com.itndev.factions.Commands;

import com.itndev.factions.Commands.FactionsCommands.CreateFaction;
import com.itndev.factions.Commands.FactionsCommands.FactionHelp;
import com.itndev.factions.Config.Config;
import com.itndev.factions.Main;
import com.itndev.factions.MySQL.MySQLManager;
import com.itndev.factions.MySQL.MySQLUtils;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.ValidChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FactionMainCommand implements CommandExecutor {

    //public MySQLManager sqlmanager = Main.getInstance().sqlmanager;

    //public MySQLUtils sqlUtils = Main.getInstance().sqlutils;
    private HashMap<String, Long> commandcooldown = new HashMap<>();

    private static HashMap<UUID, Long> FactionCommandCoolDown = new HashMap<>();

    @Deprecated
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(label.equalsIgnoreCase("국가")) {
                if (commandcooldown.containsKey(sender.getName())) {
                    int cooldownTime = 2;
                    long secondsLeft = ((Long) commandcooldown.get(sender.getName())).longValue() / 1000L + cooldownTime - System.currentTimeMillis() / 1000L;
                    if (secondsLeft > 0L) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&o&l[ &r&f국가 &a&o&l] &r&f해당 명령어는 &c" + secondsLeft + "&r&f초 후에 다시 사용 가능합니다"));
                        return true;
                    }
                }
                commandcooldown.put(sender.getName(), Long.valueOf(System.currentTimeMillis()));
                try {
                    factioncommand(p, args);
                } catch (ExecutionException | InterruptedException e) {
                    SystemUtils.warning(   "&r&f" + p.getName() + "&r&7이가 국가 명령어를 실행하던 중 에러가 발생했습니다.");
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @Deprecated
    private void factioncommand(Player sender, String[] args) throws ExecutionException, InterruptedException {

        if(args.length < 1) {
            FactionHelp.FactionHelp(sender);
            return;


        } else {
            String UUID = sender.getUniqueId().toString();
            switch (args[0].toLowerCase(Locale.ROOT)) {
                case "도움말":
                    FactionHelp.FactionHelp(sender);
                case "생성":
                    CompletableFuture<Boolean> FutureValidCheck = ValidChecker.ValidCheck(args[1]);

                    if(FactionUtils.getPlayerRank(sender.getUniqueId().toString()).equalsIgnoreCase("nomad")) {
                        if (FutureValidCheck.get()) {
                            OfflinePlayer op = Bukkit.getOfflinePlayer(sender.getUniqueId());
                            double bal = Main.econ.getBalance(op);
                            if(bal > Config.FactionCreateBalance) {
                                if(!Main.getInstance().sqlmanager.FactionNameExists(args[1])) {
                                    Main.econ.withdrawPlayer(op, Config.FactionCreateBalance);
                                    CreateFaction.CreateFaction(sender, args[1]);


                                } else {
                                    SystemUtils.sendfactionmessage(sender, "&r&f해당 국가가 이미 존재합니다");
                                }
                            } else {
                                SystemUtils.sendfactionmessage(sender, "&r&f국가를 생성하기 위한 비용이 부족합니다 &7( " + String.valueOf(Config.FactionCreateBalance - bal) + "원 )");
                            }

                        } else {
                            SystemUtils.sendfactionmessage(sender, "&r&f해당 문자/단어는 국가 이름에 들어갈수 없습니다");
                        }
                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f당신은 이미 다른 국가에 소속되어 있습니다. 기존 국가를 나간후에 다시 시도해 주십시오");
                    }
                    return;

                case "해제":

                    if(FactionUtils.getPlayerRank(UUID).equalsIgnoreCase("leader")) {

                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f당신은 국가에 소속되어 있지 않거나 국가의 왕이 아닙니다");
                    }


                case "해제수락":



                case "초대":

                case "초대취소":

                case "수락":

                case "진급":

                case "강등":

                case "정보":

                case "소속":

                case "temp":

                default:
                    FactionHelp.FactionHelp(sender);
            }
        }

    }
}
