package com.itndev.factions.FactionCommands.FactionsCommands;

import com.itndev.factions.Utils.SystemUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class FactionChatToggle {
    public static HashMap<Player, Boolean> FactionChatToggled = new HashMap<>();

    public static void FactionChatToggle(Player p) {
        if(FactionChatToggled.containsKey(p)) {
            if(FactionChatToggled.get(p)) {
                FactionChatToggled.put(p, false);
                FactionChatToggleMsg(p, false);
            } else {
                FactionChatToggled.put(p, true);
                FactionChatToggleMsg(p, true);
            }
        } else {
            FactionChatToggled.put(p, true);
            FactionChatToggleMsg(p, true);
        }
    }

    private static void FactionChatToggleMsg(Player p, Boolean option) {
        String booleanval;
        if(option) {
            booleanval = "&a&l켰습니다";
        } else {
            booleanval = "&c&l껐습니다";
        }
        SystemUtils.sendmessage(p, "&a&o&l[ &r&f국가 &a&o&l] &r&f국가 채팅을 " + booleanval);
    }

}
