package com.itndev.FactionCore.Transaction.TransactionUtils;

import com.itndev.FactionCore.Factions.FactionTimeOut;
import com.itndev.FactionCore.Factions.Storage.FactionSync;
import com.itndev.FactionCore.Lock.Lock;
import com.itndev.FactionCore.Transaction.TransactionUtils.FactionCD.DeleteFactionUtils;
import com.itndev.FactionCore.Utils.Factions.FactionUtils;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DeleteFaction {
    public static void DeleteFactionQueue(String UUID, String[] args) {
        if (FactionUtils.getPlayerRank(UUID).equalsIgnoreCase("leader")) {
            if(FactionUtils.isInWar(FactionUtils.getPlayerFactionUUID(UUID))) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f전쟁 도중에는 국가를 해체할수 없습니다");
                return;
            }
            String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
            FactionTimeOut.DeleteFactionTEMP(FactionUUID ,UUID);
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&7/국가 해체수락 &r&f으로 국가 해체를 수락합니다\n" +
                    "해당 명령어는 &r&c20초&r&f후 자동 만료됩니다.");
            FactionUUID = null;
        } else {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f당신은 국가에 소속되어 있지 않거나 국가의 왕이 아닙니다");
        }
    }


    public static void DeleteFaction(String UUID, String[] args) {
        try {
            synchronized (Lock.tryOptainLock(UUID).get(Lock.Timeout, TimeUnit.MILLISECONDS).getLock()) {
                String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
                synchronized (Lock.tryOptainLock(FactionUUID).get(Lock.Timeout, TimeUnit.MILLISECONDS).getLock()) {
                    run(UUID);
                }
            }
        } catch (TimeoutException | ExecutionException | InterruptedException e) {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&c&lERROR &7오류 발생 : 오류코드 DB-D02 (시스템시간:" + SystemUtils.getDate(System.currentTimeMillis()) + ")");
            e.printStackTrace();
        }
        /*if(Lock.CachedhasLock(UUID)) {
            synchronized (Lock.getLock(UUID).getLock()) {
                DeleteFaction_lock_2(UUID);
            }
        } else {
            if (Lock.hasLock(UUID)) {
                synchronized (Lock.getLock(UUID).getLock()) {
                    DeleteFaction_lock_2(UUID);
                    Lock.AckLock(UUID);
                }
            } else {
                synchronized (Lock.getPublicLock()) {
                    DeleteFaction_lock_2(UUID);
                }
            }
        }*/
    }

    /*private static void DeleteFaction_lock_2(String UUID) {

        if(Lock.CachedhasLock(FactionUUID)) {
            synchronized (Lock.getLock(FactionUUID).getLock()) {
                run(UUID);
            }
        } else {
            if (Lock.hasLock(FactionUUID)) {
                synchronized (Lock.getLock(FactionUUID).getLock()) {
                    run(UUID);
                    Lock.AckLock(FactionUUID);
                }
            } else {
                synchronized (Lock.getPublicLock()) {
                    run(UUID);
                }
            }
        }
    }*/

    private static void run(String UUID) {
        if (FactionUtils.getPlayerRank(UUID).equalsIgnoreCase("leader")) {
            if (FactionUtils.isInWar(FactionUtils.getPlayerFactionUUID(UUID))) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f전쟁 도중에는 국가를 해체할수 없습니다");
                return;
            }
            String FactionUUID = FactionUtils.getPlayerFactionUUID(UUID);
            if (FactionTimeOut.Timeout1info.containsKey(FactionUUID)) {
                FactionTimeOut.Timeout1.remove(FactionUUID + "%" + FactionTimeOut.Timeout1info.get(FactionUUID));
                DeleteFactionUtils.DeteleFaction(UUID, FactionUUID);
            } else {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f국가를 해체하시려면 먼저 &r&7/국가 해체 &r&f를 먼저 해주세요");
            }
        } else {
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f당신은 국가에 소속되어 있지 않거나 국가의 왕이 아닙니다");
        }
    }
}
