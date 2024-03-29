package com.itndev.FactionCore.Factions;

import com.itndev.FactionCore.Database.Redis.Obj.Storage;
import com.itndev.FactionCore.Factions.Storage.FactionSync;
import com.itndev.FactionCore.Lock.Lock;
import com.itndev.FactionCore.Server;
import com.itndev.FactionCore.Utils.CommonUtils;
import com.itndev.FactionCore.Utils.Factions.FactionUtils;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;
import com.itndev.FactionCore.Utils.Factions.UserInfoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FactionTimeOut {


    public static HashMap<String, String> Timeout1info = new HashMap<>();
    public static HashMap<String, Integer> Timeout1 = new HashMap<>();

    public static HashMap<String, ArrayList<String>> Timeout2info = new HashMap<>();
    public static HashMap<String, Integer> Timeout2 = new HashMap<>();

    public static HashMap<String, ArrayList<String>> Timeout3info = new HashMap<>();
    public static HashMap<String, Integer> Timeout3 = new HashMap<>();
    private static final Object TimeOutSync3 = new Object();


    public static void TimeoutManager() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(Server.Streamable) {
                    break;
                }
                new Thread(() -> {
                    for(String k : Timeout2.keySet()) {
                        Integer temp = Timeout2.get(k) - 1;
                        Timeout2.put(k, temp);
                        if(temp <= 0) {
                            Timeout2.remove(k);

                            String parts[] = k.split("%");

                            String PlayerUUID = parts[0];
                            String FactionUUID = parts[1];

                            ArrayList<String> templist = Timeout2info.get(PlayerUUID);
                            if(!templist.isEmpty()) {
                                if(templist.contains(FactionUUID)) {
                                    templist.remove(FactionUUID);
                                    //FactionUtils.SendFactionMessage(PlayerUUID, PlayerUUID, "single", "&r&c" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + "&r&f 에서 보낸 초대가 만료되었습니다");
                                    SystemUtils.UUID_BASED_MSG_SENDER(PlayerUUID, "&r&c" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + "&r&f 에서 보낸 초대가 만료되었습니다");
                                    if(templist.isEmpty()) {
                                        Timeout2info.remove(PlayerUUID);
                                    } else {
                                        Timeout2info.put(PlayerUUID, templist);
                                    }
                                }

                            }
                            parts = null;
                            PlayerUUID = null;
                            FactionUUID = null;
                            templist = null;
                        }
                        k = null;
                        temp = null;

                    }
                }).start();
                new Thread(() -> {
                    for(String k : Timeout3.keySet()) {
                        Integer temp = Timeout3.get(k) - 1;
                        Timeout3.put(k, temp);
                        if(temp <= 0) {
                            Timeout3.remove(k);

                            String parts[] = k.split("%");

                            String RECIEVE_FactionUUID = parts[0];
                            String FactionUUID = parts[1];

                            ArrayList<String> templist = Timeout3info.get(RECIEVE_FactionUUID);
                            if(!templist.isEmpty()) {
                                if(templist.contains(FactionUUID)) {
                                    templist.remove(FactionUUID);
                                    //FactionUtils.SendFactionMessage(PlayerUUID, PlayerUUID, "single", "&r&c" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + "&r&f 에서 보낸 초대가 만료되었습니다");
                                    SystemUtils.UUID_BASED_MSG_SENDER(FactionUtils.getFactionLeader(RECIEVE_FactionUUID.replace("=WAR", "")), "&r&c" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + "&r&f 에서 보낸 &c&l전쟁 &r신청이 만료되었습니다");
                                    if(templist.isEmpty()) {
                                        Timeout3info.remove(RECIEVE_FactionUUID);
                                    } else {
                                        Timeout3info.put(RECIEVE_FactionUUID, templist);
                                    }
                                }

                            }
                            parts = null;
                            RECIEVE_FactionUUID = null;
                            FactionUUID = null;
                            templist = null;
                        }
                        k = null;
                        temp = null;
                    }
                }).start();
                new Thread(() -> {
                    for(String k : Timeout1.keySet()) {
                        Integer temp = Timeout1.get(k) - 1;
                        Timeout1.put(k, temp);
                        if(temp <= 0) {
                            Timeout1.remove(k);

                            String[] parts = k.split("%");

                            String FactionUUID = parts[0];

                            Timeout1info.remove(FactionUUID);

                            String playeruuid = parts[1];

                            FactionUtils.SendFactionMessage(playeruuid, playeruuid, "single", "&r&c" + FactionUtils.getCapFactionNameFromUUID(FactionUUID) + "&r&f 에 대한 해체수락이 만료되었습니다");
                        }
                    }
                }).start();
            }
        }).start();
    }

    public static void FactionWarRequest(String LeaderUUID, String FactionUUID, String RECIEVE_FactionUUID) {
        synchronized (TimeOutSync3) {
            if (Timeout3info.containsKey(FactionUUID + "=WAR")) {
                ArrayList<String> temp = Timeout3info.get(FactionUUID + "=WAR");
                if (temp.contains(RECIEVE_FactionUUID)) {
                    SystemUtils.UUID_BASED_MSG_SENDER(LeaderUUID, "&r&f이미 해당국가 &c" + FactionUtils.getCapFactionNameFromUUID(RECIEVE_FactionUUID) + " &r에게 전쟁을 신청하였습니다");
                } else {
                    temp.add(RECIEVE_FactionUUID);
                    Timeout3info.put(FactionUUID + "=WAR", temp);
                    Timeout3.put(FactionUUID + "=WAR%" + RECIEVE_FactionUUID, 60);
                    SystemUtils.UUID_BASED_MSG_SENDER(LeaderUUID, "&r&f해당국가 &c" + FactionUtils.getCapFactionNameFromUUID(RECIEVE_FactionUUID) + " &r에게 성공적으로 전쟁을 신청하였습니다");
                    SystemUtils.UUID_BASED_MSG_SENDER(FactionUtils.getFactionLeader(RECIEVE_FactionUUID), "&r&f해당국가 &c" + FactionUtils.getCapFactionNameFromUUID(FactionUUID) + " &r에서 전쟁을 신청하였습니다.\n&r수락하려면 &7(/국가 전쟁 수락 " + FactionUtils.getCapFactionNameFromUUID(FactionUUID) + ")");
                }
            } else {
                ArrayList<String> temp = new ArrayList<>();
                temp.add(RECIEVE_FactionUUID);
                Timeout3info.put(FactionUUID + "=WAR", temp);
                Timeout3.put(FactionUUID + "=WAR%" + RECIEVE_FactionUUID, 60);
                SystemUtils.UUID_BASED_MSG_SENDER(LeaderUUID, "&r&f해당국가 &c" + FactionUtils.getCapFactionNameFromUUID(RECIEVE_FactionUUID) + " &r에게 성공적으로 전쟁을 신청하였습니다");
                SystemUtils.UUID_BASED_MSG_SENDER(FactionUtils.getFactionLeader(RECIEVE_FactionUUID), "&r&f해당국가 &c" + FactionUtils.getCapFactionNameFromUUID(FactionUUID) + " &r에서 전쟁을 신청하였습니다.\n&r수락하려면 &7(/국가 전쟁 수락 " + FactionUtils.getCapFactionNameFromUUID(FactionUUID) + ")");
            }
        }
    }

    public static void FactionWarAccept(String LeaderUUID, String FactionUUID, String REQUEST_FactionUUID) {
        synchronized (TimeOutSync3) {
            if (Timeout3info.containsKey(REQUEST_FactionUUID + "=WAR") && Timeout3info.get(REQUEST_FactionUUID + "=WAR").contains(FactionUUID)) {
                Timeout3info.remove(REQUEST_FactionUUID + "=WAR");
                Timeout3.remove(REQUEST_FactionUUID + "=WAR%" + FactionUUID);
                FactionUtils.setOPPWar(REQUEST_FactionUUID, FactionUUID);
                String FactionName = FactionUtils.getCapFactionNameFromUUID(FactionUUID);
                String REQUEST_FactionName = FactionUtils.getCapFactionNameFromUUID(REQUEST_FactionUUID);
                //ANNOUNCE WAR
                SystemUtils.UUID_BASED_MSG_SENDER(LeaderUUID, "&r&f해당 국가 " + REQUEST_FactionName + "&r(이)의 전쟁 신청을 수락했습니다");
                Storage.AddCommandToQueue("notify:=:" + LeaderUUID + ":=:" + "all" + ":=:" + "&r&f" + "&c" + REQUEST_FactionName + " &r(이)와 &c" + FactionName + " &r이가 전쟁을 시작합니다:=:" + "true");
                FactionName = null;
                REQUEST_FactionName = null;
            }
        }
    }

    public static void DeleteFactionTEMP(String FactionUUID, String UUID) {
        Timeout1.put(FactionUUID + "%" + UUID, 20);
        Timeout1info.put(FactionUUID, UUID);
    }

    public static void InvitePlayer(String FactionUUID, String UUID) {
        try {
            synchronized (Lock.tryOptainLock(UUID).get(Lock.Timeout, TimeUnit.MILLISECONDS).getLock()) {
                synchronized (Lock.tryOptainLock(FactionUUID).get(Lock.Timeout, TimeUnit.MILLISECONDS).getLock()) {
                    InvitePlayer_run(FactionUUID, UUID);
                }
            }
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&c&lERROR &7오류 발생 : 오류코드 TIMEOUT_LOCK_002 (시스템시간:" + SystemUtils.getDate(System.currentTimeMillis()) + ")");
            e.printStackTrace();
        }
        /*if(Lock.CachedhasLock(UUID)) {
            synchronized (Lock.getLock(UUID).getLock()) {
                InvitePlayer_lock(UUID, FactionUUID);
            }
        } else {
            if (Lock.hasLock(UUID)) {
                synchronized (Lock.getLock(UUID).getLock()) {
                    InvitePlayer_lock(UUID, FactionUUID);
                    Lock.AckLock(UUID);
                }
            } else {
                synchronized (Lock.getPublicLock()) {
                    InvitePlayer_lock(UUID, FactionUUID);
                }
            }
        }*/

    }

    /*private static void InvitePlayer_lock(String FactionUUID, String UUID) {
        if(Lock.CachedhasLock(UUID)) {
            synchronized (Lock.getLock(UUID).getLock()) {
                InvitePlayer_run(UUID, FactionUUID);
            }
        } else {
            if (Lock.hasLock(UUID)) {
                synchronized (Lock.getLock(UUID).getLock()) {
                    InvitePlayer_run(UUID, FactionUUID);
                    Lock.AckLock(UUID);
                }
            } else {
                synchronized (Lock.getPublicLock()) {
                    InvitePlayer_run(UUID, FactionUUID);
                }
            }
        }
    }*/

    private static void InvitePlayer_run(String FactionUUID, String UUID) {
        if(FactionUtils.isUsedFactionUUID(FactionUUID)) {
            Storage.AddCommandToQueue("update:=:Timeout2:=:add:=:" + CommonUtils.String2Byte(UUID + "%" + FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte(String.valueOf(30)));
            Storage.AddCommandToQueue("update:=:Timeout2info:=:add:=:" + CommonUtils.String2Byte(UUID) + ":=:add:=:" + CommonUtils.String2Byte(FactionUUID));
            String FactionName = FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID));
            FactionUtils.SendFactionMessage(UUID, UUID, "single", "&r&f" + FactionName + " 에서 당신을 초대했습니다.\n" +
                    "&7(/국가 수락 " + FactionName + ")");
        }
    }


    /*public static void AcceptInvite(String UUID, String FactionUUID) {
        if(Lock.CachedhasLock(UUID)) {
            synchronized (Lock.getLock(UUID).getLock()) {
                AcceptInvite_lock(UUID, FactionUUID);
            }
        } else {
            if (Lock.hasLock(UUID)) {
                synchronized (Lock.getLock(UUID).getLock()) {
                    AcceptInvite_lock(UUID, FactionUUID);
                    Lock.AckLock(UUID);
                }
            } else {
                synchronized (Lock.getPublicLock()) {
                    AcceptInvite_lock(UUID, FactionUUID);
                }
            }
        }
    }

    private static void AcceptInvite_lock(String UUID, String FactionUUID) {
        if(Lock.CachedhasLock(FactionUUID)) {
            synchronized (Lock.getLock(FactionUUID).getLock()) {
                AcceptInvite_run(UUID, FactionUUID);
            }
        } else {
            if (Lock.hasLock(FactionUUID)) {
                synchronized (Lock.getLock(FactionUUID).getLock()) {
                    AcceptInvite_run(UUID, FactionUUID);
                    Lock.AckLock(FactionUUID);
                }
            } else {
                synchronized (Lock.getPublicLock()) {
                    AcceptInvite_run(UUID, FactionUUID);
                }
            }
        }
    }*/

    public static void AcceptInvite(String UUID, String FactionUUID) {
        if (Timeout2.containsKey(UUID + "%" + FactionUUID) && Timeout2info.get(UUID).contains(FactionUUID)) {
            Storage.AddCommandToQueue("update:=:Timeout2:=:remove:=:" + CommonUtils.String2Byte(UUID + "%" + FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte(String.valueOf(30)));
            Storage.AddCommandToQueue("update:=:Timeout2info:=:remove:=:" + CommonUtils.String2Byte(UUID) + ":=:add:=:" + CommonUtils.String2Byte(String.valueOf(30)));
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f국가 " + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + " 에 성공적으로 가입했습니다");
            //Storage.AddCommandToQueue("notify:=:" + FactionUtils.getFactionLeader(FactionUUID) + ":=:" + "SIBAL" + ":=:" + "&r&f" + UserInfoUtils.getPlayerUUIDOriginName(UUID) + " 이가 당신의 국가에 가입했습니다" + ":=:" + "true");
            //FactionUtils.FactionUUIDNotify();
            FactionUtils.SendFactionMessage(FactionUtils.getFactionLeader(FactionUUID), "SIBAL", "SIBAL", "&r&f" + UserInfoUtils.getPlayerUUIDOriginName(UUID) + " 이가 당신의 국가에 가입했습니다");
            FactionUtils.SetPlayerFaction(UUID, FactionUUID);
            FactionUtils.SetFactionMember(UUID, FactionUUID, false);
            FactionUtils.SetPlayerRank(UUID, Config.Member);
        } else {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f해당 국가"
                    + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID))
                    + " 에서 보낸 초대장이 만료되었거나 존재하지 않습니다");
        }
    }

    public static void cancelInvite(String InviteUUID, String FactionUUID, String UUID) {
        if (Timeout2info.containsKey(UUID) && Timeout2info.get(UUID).contains(FactionUUID)) {
            SystemUtils.UUID_BASED_MSG_SENDER(InviteUUID, "&r&f해당 유저 " + UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(UUID)) + " 에게 보낸 초대장을 취소하였습니다");
            FactionUtils.SendFactionMessage(UUID, UUID, "single", "&r&f국가" + FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUUID)) + " 에서 당신에게 보낸 초대장을 취소하였습니다");
            Storage.AddCommandToQueue("update:=:Timeout2:=:remove:=:" + CommonUtils.String2Byte(UUID + "%" + FactionUUID) + ":=:add:=:" + CommonUtils.String2Byte(String.valueOf(30)));
            Storage.AddCommandToQueue("update:=:Timeout2info:=:remove:=:" + CommonUtils.String2Byte(UUID) + ":=:add:=:" + CommonUtils.String2Byte(FactionUUID));
        } else {
            SystemUtils.UUID_BASED_MSG_SENDER(InviteUUID, "&r&f해당 유저 " + UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(UUID)) + " 에게 보낸 초대장이 만료되었거나 존재하지 않습니다");
        }
    }

}
