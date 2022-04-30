package com.itndev.FBTM.Database.Redis;

import com.itndev.FBTM.Factions.FactionStorage;
import com.itndev.FBTM.Server;
import com.itndev.FBTM.Transaction.Processor;
import com.itndev.FBTM.Factions.UserInfoStorage;
import com.itndev.FBTM.Utils.Cache.CachedStorage;

public class CmdExecute {

    private static String CMD_SPLITTER = "<CMD>&%CMD_12%<CMD>";
    private static String CMD_Announce = "<&@CMD>";
    private static String ADD_Announce = "<&@ADD>";
    private static String USER_Announce = "<&@USER>";
    private static String CMD_ARGS_SPLITTER = ":=:";

    @Deprecated
    public static void CMD_READ(String CMD) {
        try {
            if(CMD.startsWith(CMD_SPLITTER)) {
                CMD = CMD.replaceFirst(CMD_SPLITTER, "");
                if(CMD.contains(CMD_SPLITTER)) {
                    String[] CMD_TEMP_ARGS = CMD.split(CMD_SPLITTER);
                    String ServerName = CMD_TEMP_ARGS[0];
                    String UUID = null;
                    String Command = null;
                    String Additional = null;
                    if(CMD_TEMP_ARGS[1].startsWith(USER_Announce)) {
                        UUID = CMD_TEMP_ARGS[1].replaceFirst(USER_Announce, "");
                    }
                    if(CMD_TEMP_ARGS[2].startsWith(CMD_Announce)) {
                        Command = CMD_TEMP_ARGS[2].replaceFirst(CMD_Announce, "");
                    }
                    if(CMD_TEMP_ARGS[3].startsWith(ADD_Announce)) {
                        Additional = CMD_TEMP_ARGS[3].replaceFirst(ADD_Announce, "");
                    }
                    if(Command == null) {
                        //System.out.println("[ERROR LOG] []" + CMD_TEMP_ARGS[0] + "  -  " + CMD_TEMP_ARGS[1] + "  -  " + CMD_TEMP_ARGS[2] + "  -  " + CMD_TEMP_ARGS[3]);
                    }
                    if(Command.contains(CMD_ARGS_SPLITTER)) {
                        //System.out.println("[TRANSACTION SUCCESS]");
                        Processor.Processor(UUID, Command.split(CMD_ARGS_SPLITTER), Additional, ServerName);
                    } else {
                        String[] args = new String[1];
                        args[0] = Command;
                        Processor.Processor(UUID, args, Additional, ServerName);
                        //System.out.println("[ERROR LOG] []" + CMD_TEMP_ARGS[0] + "  -  " + CMD_TEMP_ARGS[1] + "  -  " + CMD_TEMP_ARGS[2] + "  -  " + CMD_TEMP_ARGS[3]);
                    }
                }
            } else {
                updatehashmap(CMD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updatehashmap(String k) {
        if(k.equalsIgnoreCase("-buffer-")) {
            return;
        }
        double c = 1000;
        if(c > 600) {
            String[] args = k.split(":=:");
            if (args[0].equalsIgnoreCase("update")) {
                if (args[1].equalsIgnoreCase("FactionToLand")
                        || args[1].equalsIgnoreCase("LandToFaction")
                        || args[1].equalsIgnoreCase("FactionRank")
                        || args[1].equalsIgnoreCase("PlayerFaction")
                        || args[1].equalsIgnoreCase("FactionMember")
                        || args[1].equalsIgnoreCase("FactionNameToFactionName")
                        || args[1].equalsIgnoreCase("FactionNameToFactionUUID")
                        || args[1].equalsIgnoreCase("FactionUUIDToFactionName")
                        || args[1].equalsIgnoreCase("FactionInviteQueue")
                        || args[1].equalsIgnoreCase("FactionDTR")
                        || args[1].equalsIgnoreCase("FactionInfo")
                        || args[1].equalsIgnoreCase("FactionInfoList")
                        || args[1].equalsIgnoreCase("Timeout2")
                        || args[1].equalsIgnoreCase("Timeout2info")
                        || args[1].equalsIgnoreCase("FactionOutPost")
                        || args[1].equalsIgnoreCase("FactionOutPostList")
                        || args[1].equalsIgnoreCase("FactionToOutPost")
                        || args[1].equalsIgnoreCase("OutPostToFaction")
                        || args[1].equalsIgnoreCase("DESTORYED_FactionToLand")
                        || args[1].equalsIgnoreCase("DESTORYED_LandToFaction")) {
                    FactionStorage.FactionStorageUpdateHandler(args, "");
                } else if (args[1].equalsIgnoreCase("namename")
                        || args[1].equalsIgnoreCase("nameuuid")
                        || args[1].equalsIgnoreCase("uuidname")) {
                    UserInfoStorage.UserInfoStorageUpdateHandler(args);
                } else if (args[1].equalsIgnoreCase("cachedDTR")
                        || args[1].equalsIgnoreCase("cachedBank")) {
                    CachedStorage.JedisCacheSync(args);
                }
            } else if(args[0].equalsIgnoreCase("notify")) {

            } else if(args[0].equalsIgnoreCase("eco")) {

            } else if(args[0].equalsIgnoreCase("discord")) {

            } else if(args[0].equalsIgnoreCase("syncandclose")) {
                Server.Close = true;
            } else {
                System.out.println("[WARNING (REDIS)] WRONG COMMAND USAGE FROM REDIS" + " ( '" + k + "' )");
            }


        } else if(c <= 600) {
            String warn = "data update failed... trying to update data that should already been processed or update has been duplicated / processed delayed (" + String.valueOf(c) + ")";
            //utils.broadcastwarn(warn);
        }
        //example command "update:=:hashmap1~4:=:add/remove:=:key:=:add/remove/(앞에 remove일 경우 여기랑 이 뒤는 쓸 필요 없다):=:value"


    }
}
