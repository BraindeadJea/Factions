package com.itndev.factions.AdminCommands;

import com.itndev.factions.Utils.FactionList.FactionList;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdminMainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] strings) {
        if(!sender.hasPermission("itndevfactions.admin")) {
            sender.sendMessage(SystemUtils.colorize("&c&lERROR &r&7권한이 없습니다"));
            return false;
        }
        if(label.equalsIgnoreCase("factionadmin")) {
            RunAdminCommand((Player) sender, strings);
        }
        return false;
    }

    public void RunAdminCommand(Player p, String[] args) {
        if(args.length < 1) {
            SystemUtils.sendmessage(p, SystemUtils.colorize("&c&lERROR &r&f존재하지 않는 명령어"));
            return;
        }
        if(args[0].equalsIgnoreCase("reloadfactionbaltop")) {
            FactionList.FactionTopExecute();
            SystemUtils.sendmessage(p, "&c&lSUCESS &r&7국가 금고 정보 로컬 데이터 리프레시 중...");
        } else if(args[0].equalsIgnoreCase("buildfactionbaltop")) {
            FactionList.BuildFactionTop();
            SystemUtils.sendmessage(p, "&c&lSUCESS &r&7국가 목록 빌드 중...");
        }
    }

}
