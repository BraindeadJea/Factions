package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Config.Config;
import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.entity.Player;

public class FactionHelp {

    @Deprecated
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
