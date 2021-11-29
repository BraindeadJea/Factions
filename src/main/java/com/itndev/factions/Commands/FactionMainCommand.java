package com.itndev.factions.Commands;

import com.itndev.factions.Commands.FactionsCommands.*;
import com.itndev.factions.Config.Config;
import com.itndev.factions.Config.Lang;
import com.itndev.factions.Faction;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Main;
import com.itndev.factions.Storage.TempStorage;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.UserInfoUtils;
import com.itndev.factions.Utils.ValidChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Locale;
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
                    if(args.length < 2) {
                        SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 생성 &7(국가이름)");
                        return;
                    }

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
                    if(args.length < 2) {
                        SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 초대 &7(이름)");
                        return;
                    }

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

                    if(args.length < 2) {
                        SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 초대취소 &7(이름)");
                        return;
                    }

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

                    if(args.length < 2) {
                        SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 수락 &7(국가이름)");
                        return;
                    }

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

                    if(args.length < 2) {
                        SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 추방 &7(이름)");
                        return;
                    }

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

                    if(args.length < 2) {
                        SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 설정 &7(설정)\n" +
                                "&a&o&l[ &r&f국가 &a&o&l] &r&f설정으로는 &7등급&8, &7설명&8, &7공지&8, &7스폰&8, &f(&7동맹&8, &7적대&8, &7중립&f) 이 있습니다.");
                        return;
                    }

                    if(FactionUtils.isInFaction(UUID)) {
                        if(args[1].equalsIgnoreCase("등급")) {

                            if(args.length < 4) {
                                SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 설정 등급 &7(등급이름) &7(이름)\n" +
                                        "&a&o&l[ &r&f국가 &a&o&l] &r&등급으로는 &7" + Config.CoLeader_Lang + "&8, &7" + Config.VipMember_Lang + "&8, &7" + Config.Warrior_Lang + "&8, &7" + Config.Member_Lang + " &f이 있습니다.");
                                return;
                            }

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
                                if(TheRank.equalsIgnoreCase(Config.Leader)) {
                                    SystemUtils.sendfactionmessage(sender, "&r&f해당 등급 " + Config.Leader_Lang + "(은)는 다른 사람에게 부여할수 없습니다. /국가 양도 &7(이름) &r&f국가에 대한 소유권을 양도할수 있습니다");
                                    return;
                                }
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

                            //=================설명=================

                            if(!FactionUtils.HigherThenorSameRank(UUID, Config.CoLeader)) {
                                SystemUtils.sendfactionmessage(sender, "&r&f권한이 없습니다. &r&c" + Config.CoLeader_Lang + " &r&f등급 이상부터 사용이 가능합니다");
                                return;
                            }

                            if(args.length < 3) {
                                SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 설정 설명 &7(설명)");
                                return;
                            }

                            String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                            String Message = SystemUtils.Args2String(args, 2);
                            FactionUtils.SetFactionDesc(FactionUUID, Message);

                            SystemUtils.sendfactionmessage(sender, "&r&f국가 설명을 &7( " + Message + " ) &r&f으로 변경했습니다");

                            //=================설명=================

                        } else if(args[1].equalsIgnoreCase("공지")) {

                            //=================공지=================

                            if(!FactionUtils.HigherThenorSameRank(UUID, Config.CoLeader)) {
                                SystemUtils.sendfactionmessage(sender, "&r&f권한이 없습니다. &r&c" + Config.CoLeader_Lang + " &r&f등급 이상부터 사용이 가능합니다");
                                return;
                            }

                            if(args.length < 3) {
                                SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 설정 공지 &7(공지)");
                                return;
                            }

                            String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                            String Message = SystemUtils.Args2String(args, 2);
                            FactionUtils.SetFactionNotice(FactionUUID, Message);

                            SystemUtils.sendfactionmessage(sender, "&r&f국가 공지를 &7( " + Message + " ) &r&f으로 변경했습니다");


                            //=================공지=================

                        } else if(args[1].equalsIgnoreCase("스폰")) {

                            //=================스폰=================

                            FactionSpawn.SetFactionSpawn(sender);

                            //=================스폰=================

                        } else if(args[1].equalsIgnoreCase("동맹") || args[1].equalsIgnoreCase("적대") || args[1].equalsIgnoreCase("중립")) {

                            //=================동맹/적대/중립=================

                            if(args.length < 3) {
                                SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 설정 &7(동맹/적대/중립) &7(국가이름)\n");
                                return;
                            }

                            if(!FactionUtils.HigherThenorSameRank(UUID, Config.CoLeader)) {
                                SystemUtils.sendfactionmessage(sender, "&r&f권한이 없습니다. &r&c" + Config.CoLeader_Lang + " &r&f등급 이상부터 사용이 가능합니다");
                                return;
                            }

                            String OptionText = FactionUtils.FactionStatusConvert(args[1]);

                            //ally , enemy , neutral

                            //=================동맹/적대/중립=================

                        }
                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
                    }

                    //=================설정=================

                } else if(args[0].equalsIgnoreCase("스폰")) {

                    //=================스폰=================

                    if(FactionUtils.isInFaction(UUID)) {
                        String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                        if(FactionUtils.FactionSpawnExists(FactionUUID)) {
                            String[] temp222 = FactionUtils.getFactionSpawn(FactionUUID).split("===");
                            String TargetServerName = temp222[0];
                            Location loc = SystemUtils.string2loc(temp222[1]);

                            if(TargetServerName.equalsIgnoreCase(Main.ServerName)) {

                                //wait and teleport to location
                                SystemUtils.sendfactionmessage(sender, "&r&c5초&r&f후 국가 스폰으로 이동됩니다");
                                Long time = System.currentTimeMillis();
                                TempStorage.TeleportLocation.put(sender.getUniqueId().toString(), time);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if(TempStorage.TeleportLocation.containsKey(UUID)) {
                                            if(TempStorage.TeleportLocation.get(UUID).equals(time)) {
                                                SystemUtils.sendfactionmessage(sender, "&r&f국가 스폰으로 이동합니다");
                                                sender.teleport(loc);
                                                return;
                                            }
                                        }
                                        SystemUtils.sendfactionmessage(sender, "&r&f이동이 취소되었습니다");
                                    }
                                }.runTaskLater(Main.getInstance(), 100L);
                            } else {
                                SystemUtils.sendfactionmessage(sender, "&r&c5초&r&f후 국가 스폰으로 이동됩니다");
                                Long time = System.currentTimeMillis();
                                TempStorage.TeleportLocation.put(sender.getUniqueId().toString(), time);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if(TempStorage.TeleportLocation.containsKey(UUID)) {
                                            if(TempStorage.TeleportLocation.get(UUID).equals(time)) {
                                                SystemUtils.SendtoServer(sender, Main.ServerName);
                                                FactionUtils.WarpLocation(UUID, TargetServerName, temp222[1], false);
                                                new BukkitRunnable() {
                                                    @Override
                                                    public void run() {
                                                        FactionUtils.WarpLocation(UUID, TargetServerName, temp222[1], true);
                                                    }
                                                }.runTaskLaterAsynchronously(Main.getInstance(), 400L);
                                                return;
                                            }
                                        }
                                        SystemUtils.sendfactionmessage(sender, "&r&f이동이 취소되었습니다");
                                    }
                                }.runTaskLater(Main.getInstance(), 100L);

                                //
                                //서버로 이동
                                //그다음에 텔레포트
                            }

                        } else {
                            SystemUtils.sendfactionmessage(sender, "&r&f국가 스폰이 설정되어 있지 않습니다");
                        }
                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
                    }

                    //=================스폰=================

                } else if(args[0].equalsIgnoreCase("영토")) {

                    //=================영토=================

                    if(args.length < 2) {
                        SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 영토 &7(구매/해제)\n" +
                                "&7(영토는 청크단위로 구매하며 청크당 가격이 " + Config.LandClaimPrice + "원 입니다. 또한 한 청크의 경계는 F3 + G 로 확인 가능합니다)");
                        return;
                    }

                    if(FactionUtils.isInFaction(UUID)) {

                        if(!FactionUtils.HigherThenorSameRank(UUID, Config.VipMember)) {
                            SystemUtils.sendfactionmessage(sender, "&r&f권한이 없습니다. &r&c" + Config.VipMember_Lang + " &r&f등급 이상부터 사용이 가능합니다");
                            return;
                        }

                        Boolean Buy = null;
                        if(args[1].equalsIgnoreCase("구매")) {
                            Buy = true;
                        } else if(args[1].equalsIgnoreCase("해제")) {
                            Buy = false;
                        } else {
                            SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 영토 &7(구매/해제)\n" +
                                    "&7(영토는 청크단위로 구매하며 청크당 가격이 " + Config.LandClaimPrice + "원 입니다. 또한 한 청크의 경계는 F3 + G 로 확인 가능합니다)");
                            return;
                        }
                        String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                        Location loc = sender.getLocation();
                        String Chunkkey = FactionUtils.getChunkKey(loc);

                        if(Buy) {
                            if(FactionUtils.isClaimed(loc)) {
                                if(FactionUtils.WhosClaim(loc).equalsIgnoreCase(FactionUUID)) {
                                    SystemUtils.sendfactionmessage(sender, "&r&f해당 영토는 이미 당신의 국가가 점령을 했습니다");
                                } else {
                                    SystemUtils.sendfactionmessage(sender, "&r&f해당 영토는 다른 국가에 의해 점령되있습니다");
                                }
                            } else {
                                CompletableFuture<Double> futurebank = Main.database.AddFactionBank(FactionUUID, Config.LandClaimPrice);
                                double FinalBank = futurebank.get(30, TimeUnit.MILLISECONDS);
                                if(FinalBank >= 0) {
                                    FactionUtils.ClaimLand(FactionUUID, Chunkkey);
                                    SystemUtils.sendfactionmessage(sender, "&r&f성공적으로 해당 영토&r&7(청크)&r&f를 구매했습니다. \n" +
                                            "&r&7(남은금액 : " + FinalBank + "원)");
                                } else {
                                    SystemUtils.sendfactionmessage(sender, "&r&f영토를 구매하려면 적어도 국가 금고에 " + Config.LandClaimPrice + "원 초과가 있어야 합니다");
                                }
                            }
                        } else {
                            if(!FactionUtils.WhosClaim(loc).equalsIgnoreCase(FactionUUID)) {
                                SystemUtils.sendfactionmessage(sender, "&r&f해당 영토는 당신의 국가의 소유가 아닙니다");
                                return;
                            }
                            FactionUtils.UnClaimLand(FactionUUID, Chunkkey);
                            SystemUtils.sendfactionmessage(sender, "&r&f성공적으로 해당 영토&r&7(청크)&r&f에 대한 점령을 해제했습니다. \n");
                        }


                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
                    }

                    //=================영토=================

                } else if(args[0].equalsIgnoreCase("금고")) {

                    //=================금고=================

                    if(FactionUtils.isInFaction(UUID)) {
                        if(!FactionUtils.HigherThenorSameRank(UUID, Config.VipMember)) {
                            SystemUtils.sendfactionmessage(sender, "&r&f권한이 없습니다. &r&c" + Config.VipMember_Lang + " &r&f등급 이상부터 사용이 가능합니다");
                            return;
                        }

                        if(args.length < 3) {
                            SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 금고 &7(입금/출금) &7(금액)\n");
                            return;
                        }

                        Boolean Take = null;

                        if(args[1].equalsIgnoreCase("입금")) {
                            Take = false;
                        } else if(args[1].equalsIgnoreCase("출금")) {
                            Take = true;
                        } else {
                            SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 금고 &7(입금/출금) &7(금액)\n");
                            return;
                        }

                        if(args[2].length() > 20) {
                            SystemUtils.sendfactionmessage(sender, "&r&c오류 ! &7명령어가 너무 깁니다... 확인 불가\n");
                            return;
                        }

                        if(!ValidChecker.instanceofNumber(args[2])) {
                            SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 금고 &7(입금/출금) &7(금액)\n" +
                                    "&7> &f금액에는 숫자를 입력해야 합니다.");
                            return;
                        }

                        double amount = Double.parseDouble(args[2]);

                        if(!Take) {
                            double bal = Main.getEconomy().getBalance(Bukkit.getOfflinePlayer(sender.getUniqueId()));
                            if(bal <= amount) {
                                SystemUtils.sendfactionmessage(sender, "&r&f국가 금고에 해당 금액을 넣기에는 돈이 부족합니다.\n");
                                return;
                            }
                            amount = amount * -1;
                        }

                        String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);

                        CompletableFuture<Double> futurebank = Main.database.AddFactionBank(FactionUUID, amount);

                        OfflinePlayer op = Bukkit.getOfflinePlayer(sender.getUniqueId());

                        double finalbank = futurebank.get();

                        if(finalbank < 0) {
                            SystemUtils.sendfactionmessage(sender, "&r&f국가 금고에서 해당 금액만큼을 출금하기에는 돈이 부족합니다\n");
                            return;
                        }

                        String TakeorGet = null;

                        if(Take) {
                            TakeorGet = "&a출금";
                            Main.econ.depositPlayer(op, amount);
                        } else {
                            TakeorGet = "&a입금";
                            Main.econ.withdrawPlayer(op, amount * -1);
                        }

                        SystemUtils.sendfactionmessage(sender, "&r&f성공적으로 해당 금액만큼을 국가 금고에서 " + TakeorGet + " &r했습니다. \n" +
                                "&r&7(남은금액 : " + finalbank + "원)");

                    } else {
                        SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
                    }

                    //=================금고=================

                } else if(args[0].equalsIgnoreCase("정보")) {

                    //=================정보=================

                    Boolean MyFaction = false;
                    String TargetFactionUUID;
                    if(args.length < 2) {
                        MyFaction = true;
                        if(!FactionUtils.isInFaction(UUID)) {
                            SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 정보 &7(국가이름)\n");
                            return;
                        }
                    }

                    if(MyFaction) {
                        TargetFactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                    } else {
                        if(!FactionUtils.isExistingFaction(args[1])) {
                            SystemUtils.sendfactionmessage(sender,   "&r&f해당 국가 " + args[1] + " &r&f(은)는 존재하지 않는 국가입니다");
                            return;
                        }
                        TargetFactionUUID = FactionUtils.getFactionUUID(args[1]);
                    }
                    Faction faction = new Faction();
                    faction.BuildFactionInfo(TargetFactionUUID);
                    SystemUtils.sendmessage(sender, "&r&f&m-----------------&r&a&o&l[ &r&f국가 &r&a&o&l]&r&f&m-----------------\n" +
                            "&r\n" +
                            "&r&7&l> &r 국가이름 &r&8&l: &f" + faction.getFactionCapName() + "\n" +
                            "&r\n" +
                            "&r&7&l> &r 설명 &r&8&l: &7" + faction.getFactionDesc() + "&r\n" +
                            "&r\n" +
                            "&r&7&l> &r 금고잔액 &r&8&l: &7" + faction.getBank() + "\n" +
                            "&r&7&l> &r 남은파워 &r&8&l: &7" + faction.getDTR() + "&f/&7100.0\n" +
                            "&r&7&l> &r 설립일 &r&8&l: &7" + SystemUtils.FactionUUIDToDate(TargetFactionUUID) + "\n" +
                            "&r&7&l> &r 영토 &r&8&l: &7" + faction.getClaimLand() + " 청크\n" +
                            "&r\n" +
                            "&r&7&l> &r " + Config.Leader_Lang + " &r&8&l: &7" + faction.getFormattedMembers(Config.Leader) + "\n" +
                            "&r&7&l> &r " + Config.CoLeader_Lang + " &r&8&l: &7" + faction.getFormattedMembers(Config.CoLeader) + "\n" +
                            "&r&7&l> &r " + Config.VipMember_Lang + " &r&8&l: &7" + faction.getFormattedMembers(Config.VipMember) + "\n" +
                            "&r&7&l> &r " + Config.Warrior_Lang + " &r&8&l: &7" + faction.getFormattedMembers(Config.Warrior) + "\n" +
                            "&r&7&l> &r " + Config.Member_Lang + " &r&8&l: &7" + faction.getFormattedMembers(Config.Member) + "\n" +
                            "&r\n" +
                            "&r&f&m-----------------&r&a&o&l[ &r&f국가 &r&a&o&l]&r&f&m-----------------\n");

                    //=================정보=================

                } else if(args[0].equalsIgnoreCase("소속")) {

                    //=================소속=================

                    if(args.length < 2) {
                        SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 소속 &7(이름)\n");
                        return;
                    }

                    if(!UserInfoUtils.hasJoined(args[1])) {
                        SystemUtils.sendfactionmessage(sender, "&r&f해당 유저는 서버에 접속한 적이 없습니다\n");
                        return;
                    }

                    String TargetUUID = UserInfoUtils.getPlayerUUID(args[1].toLowerCase(Locale.ROOT));
                    String TargetOriginName = UserInfoUtils.getPlayerOrginName(args[1].toLowerCase(Locale.ROOT));

                    if(!FactionUtils.isInFaction(TargetUUID)) {
                        SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + TargetOriginName + " (은)는 소속된 국가가 없습니다\n");
                        return;
                    }

                    String TargetFactionName = FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUtils.getPlayerFactionUUID(TargetUUID)));

                    SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + TargetOriginName + " 이의 소속국가 &7: &r&f" + TargetFactionName + " \n");
                    //=================소속=================

                } else if(args[0].equalsIgnoreCase("나가기")) {

                    //=================나가기=================

                    if(!FactionUtils.isInFaction(UUID)) {
                        SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
                        return;
                    }

                    if(FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Leader)) {
                        SystemUtils.sendfactionmessage(sender, "&r&f국가의 " + Config.Leader_Lang + " 은 국가를 나갈 수 없습니다. 국가의 소유권을 양도하거나 해체해야 합니다.\n" +
                                "&f(&7/국가 양도 (이름)&8, &7/국가 해체&f)");
                        return;
                    }
                    String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                    FactionUtils.SetFactionMember(UUID, FactionUUID, true);
                    FactionUtils.SetPlayerFaction(UUID, null);
                    FactionUtils.SetPlayerRank(UUID, Config.Nomad);
                    SystemUtils.sendfactionmessage(sender, "&r&f성공적으로 국가 " + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + " 에서 나왔습니다.");
                    JedisTempStorage.AddCommandToQueue("notify:=:" + FactionUtils.getFactionLeader(FactionUUID) + ":=:" + "SIBAL" + ":=:" + "&r&f" + sender.getName() + " 이가 당신의 국가에서 나갔습니다" + ":=:" + "true");

                    //=================나가기=================

                } else if(args[0].equalsIgnoreCase("양도")) {

                    //=================양도=================

                    if(args.length < 2) {
                        SystemUtils.sendfactionmessage(sender, "&r&f명령어 사용법 : &f/국가 소속 &7(이름)\n");
                        return;
                    }
                    String name = args[1];
                    if(!FactionUtils.isInFaction(UUID)) {
                        SystemUtils.sendfactionmessage(sender, "&r&f당신은 소속된 국가가 없습니다");
                        return;
                    }

                    if(!FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Leader)) {
                        SystemUtils.sendfactionmessage(sender, "&r&f국가의 " + Config.Leader_Lang + " 만 이 명령어를 사용할수 있습니다\n");
                        return;
                    }

                    if(!UserInfoUtils.hasJoined(args[1].toLowerCase(Locale.ROOT))) {
                        SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + name + "(은)는 서버에 접속한 적이 없습니다");
                        return;
                    }

                    String TargetUUID = UserInfoUtils.getPlayerUUID(name.toLowerCase(Locale.ROOT));
                    if(!FactionUtils.isSameFaction(UUID, TargetUUID)) {
                        SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(TargetUUID)) + "(은)는 당신의 국가 소속이 아닙니다");
                        return;
                    }

                    FactionUtils.SetPlayerRank(TargetUUID, Config.Leader);
                    FactionUtils.SetPlayerRank(UUID, Config.CoLeader);
                    SystemUtils.sendfactionmessage(sender, "&r&f해당 유저 " + UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(TargetUUID)) + " 에게 국가의 소유권을 양도하였습니다\n" +
                            "변경된 당신의 등급 &7&l: &r&f" + Config.CoLeader_Lang);
                    FactionUtils.SendFactionMessage(TargetUUID, TargetUUID, "single", "&r&f국가의 " + Config.Leader_Lang + "인 " + sender.getName() + " 이가 당신에게 국가의 소유권을 양도하였습니다\n" +
                            "변경된 당신의 등급 &7&l: &r&f" + Config.Leader_Lang);
                    JedisTempStorage.AddCommandToQueue("notify:=:" + UUID + ":=:" + "SIBAL" + ":=:" + "&r&f" + sender.getName() + " 이가 국가의 소유권을 " + UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(TargetUUID)) + " 에게 넘겨주었습니다" + ":=:" + "true");

                    //=================양도=================

                } else if(args[0].equalsIgnoreCase("목록")) {

                    //=================목록=================

                    //d

                    //=================목록=================

                } else if(args[0].equalsIgnoreCase("공지")) {

                    //=================공지=================

                    if(FactionUtils.isInFaction(UUID)) {
                        String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                        if(!FactionUtils.FactionNoticeExists(FactionUUID)) {
                            SystemUtils.sendfactionmessage(sender, "&a&o&l[ &f공지 &a&o&l] &r&f" + Lang.FACTION_DEFAULT_NOTICE);
                            return;
                        }
                        SystemUtils.sendfactionmessage(sender, "&a&o&l[ &r&f공지 &a&o&l] &r&f" + FactionUtils.GetFactionNotice(FactionUUID));
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
