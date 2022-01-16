package com.itndev.factions.FactionCommands;

import com.itndev.factions.FactionCommands.FactionsCommands.*;
import com.itndev.factions.Config.Config;
import com.itndev.factions.Config.Lang;
import com.itndev.factions.Faction.Faction;
import com.itndev.factions.Jedis.JedisTempStorage;
import com.itndev.factions.Utils.FactionList.FactionList;
import com.itndev.factions.Utils.FactionUtils;
import com.itndev.factions.Utils.SystemUtils;
import com.itndev.factions.Utils.UserInfoUtils;
import com.itndev.factions.Utils.ValidChecker;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class FactionMainCommand implements CommandExecutor {

    //public MySQLManager sqlmanager = Main.getInstance().sqlmanager;

    //public MySQLUtils sqlUtils = Main.getInstance().sqlutils;
    private HashMap<String, Long> commandcooldown = new HashMap<>();

    private static DecimalFormat df = new DecimalFormat("0.00");

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

                    CreateFaction.CreateFaction(sender, UUID, args);

                    //=================생성=================

                } else if(args[0].equalsIgnoreCase("해체")) {

                    //=================해체=================

                    DeleteFaction.DeleteFactionQueue(sender, UUID, args);

                    //=================해체=================

                } else if(args[0].equalsIgnoreCase("해체수락")) {

                    //=================해체수락=================

                    DeleteFaction.DeleteFaction(sender, UUID, args);

                    //=================해체수락=================

                } else if(args[0].equalsIgnoreCase("초대")) {

                    //=================초대=================

                    FactionInvite.FactionInvite(sender, UUID, args);

                    //=================초대=================

                } else if(args[0].equalsIgnoreCase("초대취소")) {

                    //=================초대취소=================

                    FactionInvite.FactionInviteCancel(sender, UUID, args);

                    //=================초대취소=================

                } else if(args[0].equalsIgnoreCase("수락")) {

                    //=================수락=================

                    FactionInvite.FactionInviteAccept(sender, UUID, args);

                    //=================수락=================

                } else if(args[0].equalsIgnoreCase("추방")) {

                    //=================추방=================

                    FactionKick.FactionKick(sender, UUID, args);

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
                            String Message = SystemUtils.Args2String(args, 2).replace("&", "&.");
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
                            FactionUtils.SetFactionNotice(FactionUUID, Message.replace("&", "&."));

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

                    FactionSpawn.FactionSpawn(sender, UUID, args);

                    //=================스폰=================

                } else if(args[0].equalsIgnoreCase("영토")) {

                    //=================영토=================

                    ClaimLand.ClaimLand(sender, UUID, args);

                    //=================영토=================

                } else if(args[0].equalsIgnoreCase("금고")) {

                    //=================금고=================

                    FactionBank.FactionBank(sender, UUID, args);

                    //=================금고=================

                } else if(args[0].equalsIgnoreCase("정보")) {

                    //=================정보=================

                    FactionInfo.FactionInfo(sender, UUID, args);

                    //=================정보=================

                } else if(args[0].equalsIgnoreCase("소속")) {

                    //=================소속=================

                    PlayerFaction.PlayerFaction(sender, UUID, args);

                    //=================소속=================

                } else if(args[0].equalsIgnoreCase("나가기")) {

                    //=================나가기=================

                    FactionLeave.FactionLeave(sender, UUID, args);

                    //=================나가기=================

                } else if(args[0].equalsIgnoreCase("양도")) {

                    //=================양도=================

                    FactionLeaderPromote.FactionLeaderPromote(sender, UUID, args);

                    //=================양도=================

                } else if(args[0].equalsIgnoreCase("목록")) {

                    //=================목록=================

                    com.itndev.factions.FactionCommands.FactionsCommands.FactionList.FactionList(sender, UUID, args);

                    //=================목록=================

                } else if(args[0].equalsIgnoreCase("공지")) {

                    //=================공지=================

                    FactionAnnounce.FactionAnnounce(sender, UUID, args);

                    //=================공지=================

                } else if(args[0].equalsIgnoreCase("전초기지점령")) {

                    //=================전초기지점령=================

                    FactionOutPostClaim.FactionOutPostClaimCommand(sender, UUID, args);

                    //=================전초기지점령=================

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
