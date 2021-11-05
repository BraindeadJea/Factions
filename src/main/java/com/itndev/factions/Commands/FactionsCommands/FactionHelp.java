package com.itndev.factions.Commands.FactionsCommands;

import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.entity.Player;

public class FactionHelp {

    @Deprecated
    public static void FactionHelp(Player p) {
        SystemUtils.sendmessage(p, "&r&m&l------------&r&3&l[ &f&o국가 명령어 &3&l]&r&m&l------------\n" +
                "&r/국가 도움말 &8&l-&r &7해당 도움말을 보여줍니다\n" +
                "&r/국가 생성 &7(국가이름) &8&l-&r &7새 국가를 생성합니다\n" +
                "&r/국가 이름 &7(국가이름) &8&l-&r &7국가 이름을 변경합니다\n" +
                "&r/국가 해체 &8&l-&r &7국가 해체를 시작합니다\n" +
                "&r/국가 해체수락 &8&l-&r &7국가를 성공적으로 해체합니다\n" +
                "&r/국가 초대 &7(이름) &8&l-&r &7국가에 해당 유저를 초대합니다\n" +
                "&r/국가 초대취소 &7(이름) &8&l-&r &7해당 유저에게 보낸 초대를 취소합니다\n" +
                "&r/국가 수락 &7(국가이름) &8&l-&r &7해당 국가가 보낸 초대를 수락합니다\n" +
                "&r/국가 진급 &7(부리더/간부) &7(이름) &8&l-&r &7해당 유저를 해당 직급으로 승급시킵니다\n" +
                "&r/국가 강등 &7(부리더/간부) &7(이름) &8&l-&r &7해당 유저를 해당 직급에서 해임시킵니다\n" +
                "&r/국가 정보 &7(국가이름) &8&l-&r &7해당 국가의 정보를 보여줍니다\n" +
                "&r/국가 소속 &7(이름) &8&l-&r &7해당 유저의 국가 소속을 보여줍니다\n" +
                "&r/국가 나가기 &8&l-&r &7해당 국가에서 나갑니다\n" +
                "&r&m&l------------&r&3&l[ &f&o국가 명령어 &3&l]&r&m&l------------\n");
    }
}
