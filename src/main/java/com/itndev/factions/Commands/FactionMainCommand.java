package com.itndev.factions.Commands;

import com.itndev.factions.Commands.FactionsCommands.*;
import com.itndev.factions.Config.Config;
import com.itndev.factions.Main;
import com.itndev.factions.MySQL.MySQLManager;
import com.itndev.factions.MySQL.MySQLUtils;
import com.itndev.factions.Storage.FactionStorage;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.UserInfoUtils;
import com.itndev.factions.Utils.ValidChecker;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FactionMainCommand implements CommandExecutor {

    //public MySQLManager sqlmanager = Main.getInstance().sqlmanager;

    //public MySQLUtils sqlUtils = Main.getInstance().sqlutils;
    private HashMap<String, Long> commandcooldown = new HashMap<>();

    //private static HashMap<UUID, Long> FactionCommandCoolDown = new HashMap<>();

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
                factioncommand(p, args);
            }
        }
        return false;
    }

    @Deprecated
    private void factioncommand(Player sender, String[] args) {
        if(args.length < 1) {
            FactionHelp.FactionHelp(sender);
            return;
        } else {
            try {
                String UUID = sender.getUniqueId().toString();
                if(args[0].equalsIgnoreCase("도움말")) {
                    FactionHelp.FactionHelp(sender);
                } else if(args[0].equalsIgnoreCase("생성")) {

                    //=================생성=================

                    CompletableFuture<Boolean> FutureValidCheck = ValidChecker.ValidCheck(args[1]);

                    if (FactionUtils.getPlayerRank(sender.getUniqueId().toString()).equalsIgnoreCase("nomad")) {
                        if (FutureValidCheck.get(40, TimeUnit.MILLISECONDS)) {
                            OfflinePlayer op = Bukkit.getOfflinePlayer(sender.getUniqueId());
                            double bal = Main.econ.getBalance(op);
                            if (bal > Config.FactionCreateBalance) {
                                if (args[1].length() <= 12) {
                                    String newFactionUUID = FactionUtils.newFactionUUID();
                                    if (Main.database.TryClaimName(args[1], newFactionUUID).get()) {//try creating a faction name) {
                                        Main.econ.withdrawPlayer(op, Config.FactionCreateBalance);
                                        CreateFaction.CreateFaction(sender, newFactionUUID, args[1]);
                                    } else {
                                        SystemUtils.sendfactionmessage(sender, "&r&f해당 국가가 이미 존재합니다");
                                    }
                                } else {
                                    SystemUtils.sendfactionmessage(sender, "&r&f국가 이름의 길이는 12자를 초과할수 없습니다");
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

                    //=================생성=================

                } else if(args[0].equalsIgnoreCase("해체")) {

                    //=================해체=================

                    if (FactionUtils.getPlayerRank(UUID).equalsIgnoreCase("leader")) {

                        String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                        FactionTimeOut.DeleteFactionTEMP(FactionUUID ,sender);
                        SystemUtils.sendfactionmessage(sender, "&r&7/국가 해체수락 &r&f으로 국가 해체를 수락합니다\n" +
                                "해당 명령어는 &r&c20초&r&f후 자동 만료됩니다.");
                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f당신은 국가에 소속되어 있지 않거나 국가의 왕이 아닙니다");
                    }

                    //=================해체=================

                } else if(args[0].equalsIgnoreCase("해체수락")) {

                    //=================해체수락=================

                    if (FactionUtils.getPlayerRank(UUID).equalsIgnoreCase("leader")) {
                        String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                        if(FactionTimeOut.Timeout1info.containsKey(FactionUUID)) {
                            String temp394328UUID = FactionTimeOut.Timeout1info.get(FactionUUID);
                            FactionTimeOut.Timeout1.remove(FactionUUID + ":=:" + temp394328UUID);
                            DeleteFaction.DeteleFaction(sender, FactionUUID);
                        } else {
                            SystemUtils.sendfactionmessage(sender, "&r&f국가를 해체하시려면 먼저 &r&7/국가 해체 &r&f를 먼저 해주세요");
                        }
                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f당신은 국가에 소속되어 있지 않거나 국가의 왕이 아닙니다");
                    }

                    //=================해체수락=================

                } else if(args[0].equalsIgnoreCase("초대")) {

                    //=================초대=================

                    if (FactionUtils.isInFaction(UUID)) {
                        if (FactionUtils.HigherThenorSameRank(UUID, Config.VipMember)) {
                            if(UserInfoUtils.hasJoined(args[1])) {
                                String InviteUUID = UserInfoUtils.getPlayerUUID(args[1].toLowerCase(Locale.ROOT));
                                String CasedName = UserInfoUtils.getPlayerOrginName(InviteUUID);
                                if(!FactionUtils.isInFaction(InviteUUID)) {
                                    FactionTimeOut.InvitePlayer(sender, FactionUtils.getPlayerFactionUUID(UUID), InviteUUID);
                                } else {
                                    SystemUtils.sendfactionmessage(sender, "&r&c" + CasedName + "&r&f(은)는 이미 다른 국가에 소속되어 있습니다");
                                }
                            } else {
                                SystemUtils.sendfactionmessage(sender,   "&r&c" + args[1] + "&r&f(은)는 존재하지 않는 유저입니다");
                            }
                        } else {
                            SystemUtils.sendfactionmessage(sender, "&r&f권한이 없습니다. &c" + Config.VipMember_Lang + " &r&f랭크 이상부터 사용이 가능합니다");
                        }
                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
                    }

                    //=================초대=================

                } else if(args[0].equalsIgnoreCase("초대취소")) {

                    //=================초대취소=================

                    if (FactionUtils.isInFaction(UUID)) {
                        if (FactionUtils.HigherThenorSameRank(UUID, Config.VipMember)) {
                            if (UserInfoUtils.hasJoined(args[1])) {
                                String InviteUUID = UserInfoUtils.getPlayerUUID(args[1].toLowerCase(Locale.ROOT));
                                String CasedName = UserInfoUtils.getPlayerOrginName(InviteUUID);
                                FactionTimeOut.cancelInvite(sender, FactionUtils.getPlayerFactionUUID(UUID), InviteUUID);
                            } else {
                                SystemUtils.sendfactionmessage(sender, "&r&c" + args[1] + "&r&f(은)는 존재하지 않는 유저입니다");
                            }
                        } else {
                            SystemUtils.sendfactionmessage(sender, "&r&f권한이 없습니다. &c" + Config.VipMember_Lang + " &r&f랭크 이상부터 사용이 가능합니다");
                        }
                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
                    }

                    //=================초대취소=================

                } else if(args[0].equalsIgnoreCase("수락")) {

                    //=================수락=================

                    if(!FactionUtils.isInFaction(UUID)) {
                        if(FactionUtils.isExistingFaction(args[1])) {
                            String FactionUUID = FactionUtils.getFactionUUID(args[1].toLowerCase(Locale.ROOT));
                            FactionTimeOut.AcceptInvite(sender, UUID, FactionUUID);
                        } else {
                            SystemUtils.sendfactionmessage(sender, "&r&f해당 국가 " + args[1] + " 은 존재하지 않습니다");
                        }
                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f당신은 이미 다른 국가에 소속되어 있습니다");
                    }

                    //=================수락=================

                } else if(args[0].equalsIgnoreCase("추방")) {

                    //=================추방=================

                    if (FactionUtils.isInFaction(UUID)) {
                        if (FactionUtils.HigherThenorSameRank(UUID, Config.VipMember)) {
                            String name = args[1];
                            if(UserInfoUtils.hasJoined(args[1].toLowerCase(Locale.ROOT))) {
                                String KICKUUID = UserInfoUtils.getPlayerUUID(args[1].toLowerCase(Locale.ROOT));
                                String OriginName = UserInfoUtils.getPlayerOrginName(args[1].toLowerCase(Locale.ROOT));
                                if(FactionUtils.isInFaction(KICKUUID) && FactionUtils.isSameFaction(UUID, KICKUUID)) {
                                    if(FactionUtils.HigherThenRank(UUID, FactionUtils.getPlayerRank(KICKUUID))) {
                                        String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                                        SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + OriginName + " 이를 국가에서 추방했습니다.");
                                        FactionUtils.SetPlayerFaction(KICKUUID, null);
                                        FactionUtils.SetFactionMember(KICKUUID, FactionUUID, true);
                                        FactionUtils.SetPlayerRank(KICKUUID, Config.Nomad);
                                        FactionUtils.SendFactionMessage(KICKUUID, KICKUUID, "single", "&r&f당신은 " + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + " 에서 추방했습니다.");
                                        FactionUtils.SendFactionMessage(UUID, UUID, UUID, "&r&f" + sender.getName() + " 이가 " + OriginName + " 이를 국가에서 추방했습니다.");
                                    } else {
                                        SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + OriginName + "(은)는 당신보다 등급이 높은 &r&c" + FactionUtils.getPlayerRank(KICKUUID) + " &r&f입니다. 자신보다 높은 등급인 멤버를 추방할수 없습니다");
                                    }
                                } else {
                                    SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + OriginName + "(은)는 당신의 팀이 아닙니다");
                                }
                            } else {
                                SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + name + "(은)는 서버에 접속한 적이 없습니다");
                            }
                        } else {
                            SystemUtils.sendfactionmessage(sender, "&r&f권한이 없습니다. &r&c" + Config.VipMember_Lang + " &r&f랭크 이상부터 사용이 가능합니다");
                        }
                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
                    }

                    //=================추방=================

                } else if(args[0].equalsIgnoreCase("채팅")) {

                    //=================채팅=================

                    if(FactionUtils.isInFaction(UUID)) {
                        FactionChatToggle.FactionChatToggle(sender);
                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
                    }

                    //=================채팅=================

                } else if(args[0].equalsIgnoreCase("설정")) {

                    //=================설정=================

                    if(FactionUtils.isInFaction(UUID)) {
                        if(args[1].equalsIgnoreCase("등급")) {

                            String name = args[3];
                            Boolean RealRank = FactionUtils.isAExistingLangRank(args[2]);
                            Boolean RealPlayer = UserInfoUtils.hasJoined(args[3].toLowerCase(Locale.ROOT));

                            //=================등급=================
                            if(FactionUtils.HigherThenorSameRank(UUID, Config.VipMember)) {
                                if(!RealRank) {
                                    SystemUtils.sendfactionmessage(sender, "&r&f해당 등급 " + args[2] + "(은)는 존재하지 않습니다");
                                    return;
                                }
                                String TheRank = FactionUtils.RankConvert(args[2]);
                                if(!RealPlayer) {
                                    SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + name + "(은)는 서버에 접속한 적이 없습니다");
                                    return;
                                }
                                String TargetUUID = UserInfoUtils.getPlayerUUID(name.toLowerCase(Locale.ROOT));
                                String CasedTargetName = UserInfoUtils.getPlayerOrginName(name.toLowerCase(Locale.ROOT));
                                if(TargetUUID.equals(UUID)) {
                                    SystemUtils.sendfactionmessage(sender, "&r&f자신의 등급를 변경할 수 없습니다");
                                    return;
                                }
                                if(!FactionUtils.HigherThenRank(UUID, FactionUtils.getPlayerRank(TargetUUID))) {
                                    SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + CasedTargetName + "(은)는 당신보다 등급이 높은 &r&c" + FactionUtils.getPlayerRank(TargetUUID) + " &r&f입니다. 자신보다 높은 등급인 멤버의 등급를 바꿀 수 없습니다");
                                    return;
                                }
                                if(!FactionUtils.HigherThenRank(UUID, TheRank)) {
                                    SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + CasedTargetName + " 에게 지급할 등급은 자신의 등급보다 높거나 같아서는 안됩니다");
                                    return;
                                }
                                //성공
                                FactionUtils.SetPlayerRank(TargetUUID, TheRank);
                                FactionUtils.SendFactionMessage(UUID, UUID, "single", "&r&f" + CasedTargetName + " 이의 등급을 " + FactionUtils.LangRankConvert(TheRank) + " 으로 변경하였습니다");
                                FactionUtils.SendFactionMessage(TargetUUID, TargetUUID, "single", "&r&f당신의 등급이 " + FactionUtils.LangRankConvert(TheRank) + " 으로 변경되었습니다");
                            } else {
                                SystemUtils.sendfactionmessage(sender, "&r&f권한이 없습니다. &r&c" + Config.VipMember_Lang + " &r&f등급 이상부터 사용이 가능합니다");
                            }

                            //=================등급=================

                        } else if(args[1].equalsIgnoreCase("설명")) {

                        } else if(args[1].equalsIgnoreCase("공지")) {

                        } else if(args[1].equalsIgnoreCase("스폰")) {

                            //=================스폰=================
                            String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                            CompletableFuture<Double> bal = Main.database.GetFactionBank(FactionUUID);
                            if(FactionUtils.HigherThenorSameRank(UUID, Config.CoLeader)) {

                                if(bal.get(40, TimeUnit.MILLISECONDS) > 500) {
                                    CompletableFuture<Double> finalbank = Main.database.AddFactionBank(FactionUUID, 500);
                                    double finalbankget = finalbank.get(20, TimeUnit.MILLISECONDS);
                                    if(finalbankget > -1) {
                                        Location loc = sender.getLocation();
                                        String convertedloc = SystemUtils.loc2string(loc);

                                        FactionUtils.SetFactionSpawn(FactionUUID, convertedloc);
                                        SystemUtils.sendfactionmessage(sender, "&r&c성공! &r&f해당 위치로 국가 스폰을 설정했습니다.");

                                    } else {
                                        SystemUtils.sendfactionmessage(sender, "&r&f스폰을 재설정하려면 적어도 국가 금고에 500원 초과가 있어야 합니다");
                                    }
                                } else {
                                    SystemUtils.sendfactionmessage(sender, "&r&f스폰을 재설정하려면 적어도 국가 금고에 500원 초과가 있어야 합니다");
                                }
                            } else {
                                SystemUtils.sendfactionmessage(sender, "&r&f권한이 없습니다. &r&c" + Config.CoLeader_Lang + " &r&f등급 이상부터 사용이 가능합니다");
                            }

                            //=================스폰=================

                        } else if(args[1].equalsIgnoreCase("동맹")) {

                        } else if(args[1].equalsIgnoreCase("적대")) {

                        } else if(args[1].equalsIgnoreCase("중립")) {

                        }
                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
                    }

                    //=================설정=================

                } else if(args[0].equalsIgnoreCase("스폰")) {

                    //=================스폰=================

                    if(FactionUtils.isInFaction(UUID)) {
                        String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                        Boolean FactionSpawnExists = FactionUtils.FactionSpawnExists(FactionUUID);
                        if(FactionSpawnExists) {
                            String[] temp222 = FactionUtils.getFactionSpawn(FactionUUID).split("=");
                            String TargetServerName = temp222[0];
                            Location loc = SystemUtils.string2loc(temp222[1]);


                        }
                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
                    }

                    //=================스폰=================

                } else if(args[0].equalsIgnoreCase("영토")) {

                    //=================영토=================

                    if(FactionUtils.isInFaction(UUID)) {
                        if(args[1].equalsIgnoreCase("구매")) {

                        } else if(args[1].equalsIgnoreCase("해제")) {

                        }
                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
                    }

                    //=================영토=================

                } else if(args[0].equalsIgnoreCase("정보")) {

                } else if(args[0].equalsIgnoreCase("소속")) {

                } else if(args[0].equalsIgnoreCase("목록")) {

                } else if(args[0].equalsIgnoreCase("공지")) {

                    //=================공지=================

                    if(FactionUtils.isInFaction(UUID)) {

                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
                    }

                    //=================공지=================

                }






                /*
                switch (args[0].toLowerCase(Locale.ROOT)) {
                    case "도움말":
                        FactionHelp.FactionHelp(sender);
                    case "생성":
                        CompletableFuture<Boolean> FutureValidCheck = ValidChecker.ValidCheck(args[1]);


                        if (FactionUtils.getPlayerRank(sender.getUniqueId().toString()).equalsIgnoreCase("nomad")) {
                            if (FutureValidCheck.get(40, TimeUnit.MILLISECONDS)) {
                                OfflinePlayer op = Bukkit.getOfflinePlayer(sender.getUniqueId());
                                double bal = Main.econ.getBalance(op);
                                if (bal > Config.FactionCreateBalance) {
                                    if (args[1].length() <= 12) {
                                        String newFactionUUID = FactionUtils.newFactionUUID();
                                        if (Main.database.TryClaimName(args[1], newFactionUUID).get(30, TimeUnit.MILLISECONDS)) {//try creating a faction name) {
                                            Main.econ.withdrawPlayer(op, Config.FactionCreateBalance);
                                            CreateFaction.CreateFaction(sender, newFactionUUID, args[1]);
                                        } else {
                                            SystemUtils.sendfactionmessage(sender, "&r&f해당 국가가 이미 존재합니다");
                                        }
                                    } else {
                                        SystemUtils.sendfactionmessage(sender, "&r&f국가 이름의 길이는 12자를 초과할수 없습니다");
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
                        if (FactionUtils.getPlayerRank(UUID).equalsIgnoreCase("leader")) {

                            String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                            FactionTimeOut.DeleteFactionTEMP(FactionUUID ,sender);
                            SystemUtils.sendfactionmessage(sender, "&r&7/국가 해체수락 &r&f으로 국가 해체를 수락합니다\n" +
                                    "해당 명령어는 &r&c20초&r&f후 자동 만료됩니다.");
                        } else {
                            SystemUtils.sendfactionmessage(sender, "&r&f당신은 국가에 소속되어 있지 않거나 국가의 왕이 아닙니다");
                        }
                    case "해제수락":
                        if (FactionUtils.getPlayerRank(UUID).equalsIgnoreCase("leader")) {
                            String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                            if(FactionTimeOut.Timeout1info.containsKey(FactionUUID)) {
                                String temp394328UUID = FactionTimeOut.Timeout1info.get(FactionUUID);
                                FactionTimeOut.Timeout1.remove(FactionUUID + ":=:" + temp394328UUID);
                                DeleteFaction.DeteleFaction(sender, FactionUUID);
                            } else {
                                SystemUtils.sendfactionmessage(sender, "&r&f국가를 해체하시려면 먼저 &r&7/국가 해체 &r&f를 먼저 해주세요");
                            }
                        } else {
                            SystemUtils.sendfactionmessage(sender, "&r&f당신은 국가에 소속되어 있지 않거나 국가의 왕이 아닙니다");
                        }
                    case "초대":
                        if (FactionUtils.isInFaction(UUID)) {
                            if (FactionUtils.HigherThenRank(UUID, Config.VipMember)) {
                                if(UserInfoUtils.hasJoined(args[1])) {
                                    String InviteUUID = UserInfoUtils.getPlayerUUID(args[1].toLowerCase(Locale.ROOT));
                                    String CasedName = UserInfoUtils.getPlayerOrginName(InviteUUID);
                                    if(!FactionUtils.isInFaction(InviteUUID)) {
                                        FactionTimeOut.InvitePlayer(sender, FactionUtils.getPlayerFactionUUID(UUID), InviteUUID);
                                    } else {
                                        SystemUtils.sendfactionmessage(sender, "&r&c" + CasedName + "&r&f(은)는 이미 다른 국가에 소속되어 있습니다");
                                    }
                                } else {
                                    SystemUtils.sendfactionmessage(sender,   "&r&c" + args[1] + "&r&f(은)는 존재하지 않는 유저입니다");
                                }
                            } else {
                                SystemUtils.sendfactionmessage(sender, "&r&f권한이 없습니다. &c" + Config.VipMember_Lang + " &r&f랭크 이상부터 사용이 가능합니다");
                            }
                        } else {
                            SystemUtils.sendfactionmessage(sender, "&r&f");
                        }
                    case "초대취소":
                        if (FactionUtils.HigherThenRank(FactionUtils.getPlayerRank(UUID), Config.VipMember)) {
                            if(UserInfoUtils.hasJoined(args[1])) {
                                String InviteUUID = UserInfoUtils.getPlayerUUID(args[1].toLowerCase(Locale.ROOT));
                                String CasedName = UserInfoUtils.getPlayerOrginName(InviteUUID);
                                FactionTimeOut.cancelInvite(sender, FactionUtils.getPlayerFactionUUID(UUID), InviteUUID);
                            } else {
                                SystemUtils.sendfactionmessage(sender,   "&r&c" + args[1] + "&r&f(은)는 존재하지 않는 유저입니다");
                            }
                        } else {
                            SystemUtils.sendfactionmessage(sender, "&r&f권한이 없습니다. &c" + Config.VipMember_Lang + " &r&f랭크 이상부터 사용이 가능합니다");
                        }
                    case "수락":
                        if(!FactionUtils.isInFaction(UUID)) {
                            if(FactionUtils.isExistingFaction(args[1])) {
                                String FactionUUID = FactionUtils.getFactionUUID(args[1].toLowerCase(Locale.ROOT));
                                FactionTimeOut.AcceptInvite(sender, UUID, FactionUUID);
                            } else {
                                SystemUtils.sendfactionmessage(sender, "&r&f해당 국가 " + args[1] + " 은 존재하지 않습니다");
                            }
                        } else {
                            SystemUtils.sendfactionmessage(sender, "&r&f당신은 이미 다른 국가에 소속되어 있습니다");
                        }
                    case "추방":

                    case "설정":
                        switch (args[1].toLowerCase(Locale.ROOT)) {
                            case "계급":

                            case "설명":

                            case "공지":

                            case "스폰":

                            case "동맹":

                            case "적대":

                            case "중립":

                            default:
                                //send help message

                        }
                    case "스폰":

                    case "영토":
                        //if is in faction & has permission to claim land
                        if(FactionUtils.isInFaction(UUID)) {
                            if(FactionUtils.HigherThenRank(UUID, Config.CoLeader)) {
                                switch (args[1].toLowerCase(Locale.ROOT)) {
                                    case "구매":


                                    case "해제":

                                        return;
                                }
                            } else {

                            }
                        } else {

                        }


                    case "정보":

                    case "소속":

                    case "목록":

                    case "공지":

                    case "temp":

                    default:
                        FactionHelp.FactionHelp(sender);
                }*/
            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
