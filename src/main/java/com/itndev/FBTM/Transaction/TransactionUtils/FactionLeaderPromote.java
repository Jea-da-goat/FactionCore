package com.itndev.FBTM.Transaction.TransactionUtils;

import com.itndev.FBTM.Database.Redis.Obj.Storage;
import com.itndev.FBTM.Factions.Config;
import com.itndev.FBTM.Utils.Factions.FactionUtils;
import com.itndev.FBTM.Utils.Factions.SystemUtils;
import com.itndev.FBTM.Utils.Factions.UserInfoUtils;

import java.util.Locale;

public class FactionLeaderPromote {

    public static void FactionLeaderPromote(String UUID, String[] args) {
        new Thread( () -> {
            if(args.length < 2) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f명령어 사용법 : &f/국가 소속 &7(이름)\n");
                return;
            }
            String name = args[1];
            if(!FactionUtils.isInFaction(UUID)) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f당신은 소속된 국가가 없습니다");
                return;
            }


            if(!FactionUtils.getPlayerRank(UUID).equalsIgnoreCase(Config.Leader)) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f국가의 " + Config.Leader_Lang + " 만 이 명령어를 사용할수 있습니다\n");
                return;
            }

            if(!UserInfoUtils.hasJoined(args[1].toLowerCase(Locale.ROOT))) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f해당 유저 " + name + "(은)는 서버에 접속한 적이 없습니다");
                return;
            }

            String TargetUUID = UserInfoUtils.getPlayerUUID(name.toLowerCase(Locale.ROOT));
            String givername = UserInfoUtils.getPlayerUUIDOriginName(UUID);
            String originname = UserInfoUtils.getPlayerOrginName(TargetUUID);
            if(!FactionUtils.isSameFaction(UUID, TargetUUID)) {
                SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f해당 유저 " + originname + "(은)는 당신의 국가 소속이 아닙니다");
                return;
            }

            FactionUtils.SetPlayerRank(TargetUUID, Config.Leader);
            FactionUtils.SetPlayerRank(UUID, Config.CoLeader);
            SystemUtils.UUID_BASED_MSG_SENDER(UUID, "&r&f해당 유저 " + originname + " 에게 국가의 소유권을 양도하였습니다\n" +
                    "변경된 당신의 등급 &7&l: &r&f" + Config.CoLeader_Lang);
            FactionUtils.SendFactionMessage(TargetUUID, TargetUUID, "single", "&r&f국가의 " + Config.Leader_Lang + "인 " + givername + " 이가 당신에게 국가의 소유권을 양도하였습니다\n" +
                    "변경된 당신의 등급 &7&l: &r&f" + Config.Leader_Lang);
            Storage.AddCommandToQueue("notify:=:" + UUID + ":=:" + "SIBAL" + ":=:" + "&r&f" + givername + " 이가 국가의 소유권을 " + UserInfoUtils.getPlayerOrginName(UserInfoUtils.getPlayerName(TargetUUID)) + " 에게 넘겨주었습니다" + ":=:" + "true");
        }).start();
    }

}