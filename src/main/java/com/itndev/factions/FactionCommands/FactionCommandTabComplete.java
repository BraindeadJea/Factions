package com.itndev.factions.FactionCommands;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FactionCommandTabComplete implements TabCompleter {

    private static List<String> firstarg = new ArrayList<>();
    private static List<String> ranklist = new ArrayList<>();
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> tabcomplete = new ArrayList<>();
        if(firstarg.isEmpty() || ranklist.isEmpty()) {
            BuildFirstArgs();
        }
        if(args.length == 1) {
            return firstarg;
        } else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("생성") || args[0].equalsIgnoreCase("이름") || args[0].equalsIgnoreCase("수락") || args[0].equalsIgnoreCase("정보")) {
                tabcomplete.add("<국가이름>");
            } else if(args[0].equalsIgnoreCase("설정")) {
                tabcomplete.add("등급");
                tabcomplete.add("설명");
                tabcomplete.add("공지");
                tabcomplete.add("스폰");
            } else if(args[0].equalsIgnoreCase("금고")) {
                tabcomplete.add("입금");
                tabcomplete.add("출금");
            } else if(args[0].equalsIgnoreCase("영토")) {
                tabcomplete.add("구매");
                tabcomplete.add("해제");
            } else if(args[0].equalsIgnoreCase("전초기지")) {
                tabcomplete.add("점령");
                tabcomplete.add("목록");
                tabcomplete.add("워프");
            }
        } else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("설정") && args[1].equalsIgnoreCase("등급")) {
                return ranklist;
            }
        }
        if(tabcomplete.isEmpty()) {
            Bukkit.getOnlinePlayers().forEach(Player -> tabcomplete.add(Player.getName()));
        }
        return tabcomplete;
    }
    public static void BuildFirstArgs() {
        firstarg.add("도움말");
        firstarg.add("생성");
        firstarg.add("이름");
        firstarg.add("해체");
        firstarg.add("초대");
        firstarg.add("초대취소");
        firstarg.add("수락");
        firstarg.add("설정");
        firstarg.add("금고");
        firstarg.add("영토");
        firstarg.add("전초기지");
        firstarg.add("스폰");
        firstarg.add("공지");
        firstarg.add("정보");
        firstarg.add("소속");
        firstarg.add("나가기");
        ranklist.add(Config.Member_Lang);
        ranklist.add(Config.Warrior_Lang);
        ranklist.add(Config.VipMember_Lang);
        ranklist.add(Config.CoLeader_Lang);
    }
    public static void FactionHelp(Player p) {
        SystemUtils.sendmessage(p, "&r&m&l------------&r&3&l[ &f&o국가 명령어 &3&l]&r&m&l------------\n" +
                "&r/국가 도움말 &8&l-&r &7해당 도움말을 보여줍니다\n" +
                "&r/국가 생성 &7(국가이름) &8&l-&r &7새 국가를 생성합니다. 가격 : " + Config.FactionCreateBalance + "\n" +
                "&r/국가 이름 &7(국가이름) &8&l-&r &7국가 이름을 변경합니다 (현재 사용불가)\n" +
                "&r/국가 해체 &8&l-&r &7국가를 해체합니다\n" +
                "&r/국가 초대 &7(이름) &8&l-&r &7국가에 해당 유저를 초대합니다 (20초후 만료)\n" +
                "&r/국가 초대취소 &7(이름) &8&l-&r &7해당 유저에게 보낸 초대를 취소합니다\n" +
                "&r/국가 수락 &7(국가이름) &8&l-&r &7해당 국가가 보낸 초대를 수락합니다\n" +
                "&r/국가 설정 &7(&7등급&8/&7설명&8/&7공지&8/&7스폰&8/&f(&7동맹&8/&7적대&8/&7중립&f)&7) &8&l-&r &7해당 설정을 만집니다\n" +
                "&r/국가 금고 &7(입금/출금) &7(금액) &8&l-&r &7해당 금액을 국가 금고에서 입금/출금 합니다\n" +
                "&r/국가 영토 &7(구매/해제) &8&l-&r &7해당 영토(청크)를 구매/구매해제합니다. 가격 : " + Config.LandClaimPrice + "\n" +
                "&r/국가 전초기지 &7(점령&8/&7목록&8/&7워프) &8&l-&r &7국가 전초기지 관련 명령어\n" +
                "&r/국가 스폰 &8&l-&r &7국가 스폰으로 이동합니다\n" +
                "&r/국가 공지 &8&l-&r &7국가 공지를 확인합니다\n" +
                "&r/국가 정보 &7(국가이름) &8&l-&r &7해당 국가의 정보를 보여줍니다\n" +
                "&r/국가 소속 &7(이름) &8&l-&r &7해당 유저의 국가 소속을 보여줍니다\n" +
                "&r/국가 나가기 &8&l-&r &7해당 국가에서 나갑니다\n" +
                "&r&m&l------------&r&3&l[ &f&o국가 명령어 &3&l]&r&m&l------------\n");
    }
}
