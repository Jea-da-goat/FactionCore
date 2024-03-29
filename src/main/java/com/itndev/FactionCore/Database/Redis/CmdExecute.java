package com.itndev.FactionCore.Database.Redis;

import com.itndev.FactionCore.Dump.RedisDump;
import com.itndev.FactionCore.Factions.Storage.FactionStorage;
import com.itndev.FactionCore.Server;
import com.itndev.FactionCore.SocketConnection.PastCachedStream.StorageSyncTask;
import com.itndev.FactionCore.Transaction.Processor;
import com.itndev.FactionCore.Factions.UserInfoStorage;
import com.itndev.FactionCore.Utils.Cache.CachedStorage;
import com.itndev.FactionCore.Utils.CommonUtils;
import com.itndev.FactionCore.Utils.Database.Redis.StaticVal;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;

public class CmdExecute {

    public static void CMD_READ(String CMD, String ServerName2) {
        try {
            String CMD_SPLITTER = " ";
            if(CMD.startsWith(StaticVal.getCmdAnnouncer())) {
                String CMD_Announce = "<&@CMD>";
                String ADD_Announce = "<&@ADD>";
                String USER_Announce = "<&@USER>";
                String CMD_ARGS_SPLITTER = ":=:";
                CMD = CMD.replaceFirst(StaticVal.getCmdAnnouncer() + " ", "");
                if(CMD.contains(CMD_SPLITTER)) {
                    String[] CMD_TEMP_ARGS = CMD.split(CMD_SPLITTER);
                    String ServerName = CMD_TEMP_ARGS[0];
                    String UUID = null;
                    String Command = null;
                    String Additional = null;
                    if(CMD_TEMP_ARGS[1].startsWith(USER_Announce)) {
                        UUID = CommonUtils.Byte2String(CMD_TEMP_ARGS[1].replaceFirst(USER_Announce, ""));
                    }
                    if(CMD_TEMP_ARGS[2].startsWith(CMD_Announce)) {
                        Command = CommonUtils.Byte2String(CMD_TEMP_ARGS[2].replaceFirst(CMD_Announce, ""));
                    }
                    if(CMD_TEMP_ARGS[3].startsWith(ADD_Announce)) {
                        Additional = CommonUtils.Byte2String(CMD_TEMP_ARGS[3].replaceFirst(ADD_Announce, ""));
                    }
                    if(Command != null && Command.equalsIgnoreCase("KEEPALIVECHECK_REDISCLEANUP")) {
                        RedisTRIM.KeepAliveResponce(ServerName, UUID);
                        return;
                    }
                    assert Command != null;
                    if(Command.contains(CMD_ARGS_SPLITTER)) {
                        String[] tempargs = Command.split(CMD_ARGS_SPLITTER);
                        String[] args = new String[tempargs.length];
                        int c = 0;
                        for(String k : tempargs) {
                            args[c] = CommonUtils.Byte2String(k);
                            c++;
                        }
                        //System.out.println("[TRANSACTION SUCCESS]");
                        Processor.Processor(UUID, args, Additional, ServerName);
                    } else {
                        String[] args = new String[1];
                        args[0] = CommonUtils.Byte2String(Command);
                        Processor.Processor(UUID, args, Additional, ServerName);
                        args = null;
                        //System.out.println("[ERROR LOG] []" + CMD_TEMP_ARGS[0] + "  -  " + CMD_TEMP_ARGS[1] + "  -  " + CMD_TEMP_ARGS[2] + "  -  " + CMD_TEMP_ARGS[3]);
                    }
                    CMD_TEMP_ARGS = null;
                    ServerName = null;
                    UUID = null;
                    Command = null;
                    Additional = null;
                }
            } else {
                updatehashmap(CMD, ServerName2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static StorageSyncTask task;

    public static void updatehashmap(String k, String ServerName) {
        if(k.equalsIgnoreCase("-buffer-")) {
            return;
        }
        String[] args = k.split(":=:");
        /*switch (args[0].toLowerCase(Locale.ROOT)) {
            case "update":
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
                        || args[1].equalsIgnoreCase("DESTORYED_LandToFaction")
                        || args[1].equalsIgnoreCase("DESTROYED_FactionUUIDToFactionName")) {
                    FactionStorage.FactionStorageUpdateHandler(args, "");
                } else if (args[1].equalsIgnoreCase("namename")
                        || args[1].equalsIgnoreCase("nameuuid")
                        || args[1].equalsIgnoreCase("uuidname")) {
                    UserInfoStorage.UserInfoStorageUpdateHandler(args);
                } else if (args[1].equalsIgnoreCase("cachedDTR")
                        || args[1].equalsIgnoreCase("cachedBank")) {
                    CachedStorage.JedisCacheSync(args);
                }
                break;
            case "reloadstorage":
                if(args.length == 2) {
                    RedisDump.ReloadStorageFromRemoteServer(args[1]);
                    SystemUtils.logger("Reloaded Storage From Redis With The Key \"" + args[1] + "\"");
                } else if(args.length == 1) {
                    RedisDump.ReloadStorageFromRemoteServer(null);
                    SystemUtils.logger("Reloaded Storage From Redis Without A Key");
                }
                break;
            case "uploadstorage":
                if(args.length == 2) {
                    RedisDump.UploadStorageToRedis(args[1]);
                    SystemUtils.logger("Uploaded Storage To Redis With The Key \"" + args[1] + "\"");
                } else if(args.length == 1) {
                    RedisDump.UploadStorageToRedis(null);
                    SystemUtils.logger("Uploaded Storage To Redis Without A Key");
                }
                break;
            case "syncandclose":
                Server.Close = true;
                break;
            case "syncstorage":
                task = new StorageSyncTask(ServerName);
                task.run();
                //ServerName;
            case "synccomplete":
                task.finish();
            case "notify":
            case "eco":
            case "discord":
                break;
        }*/
        if (args[0].equalsIgnoreCase("update")) {
            if(args.length >= 6) {
                args[3] = CommonUtils.Byte2String(args[3]);
                args[5] = CommonUtils.Byte2String(args[5]);
                update(args);
            } else if(args.length >= 4) {
                args[3] = CommonUtils.Byte2String(args[3]);
                update(args);
            } else  {
                update(args);
            }
        } else if(args[0].equalsIgnoreCase("notify")) {
        } else if(args[0].equalsIgnoreCase("eco")) {
        } else if(args[0].equalsIgnoreCase("discord")) {
        } else if(args[0].equalsIgnoreCase("syncstorage")) {
            task = new StorageSyncTask(ServerName);
            task.run();
        } else if(args[0].equalsIgnoreCase("synccomplete")) {
            task.finish();
        } else if(args[0].equalsIgnoreCase("reloadstorage")) {
            if(args.length == 2) {
                RedisDump.ReloadStorageFromRemoteServer(args[1]);
                SystemUtils.logger("Reloaded Storage From Redis With The Key \"" + args[1] + "\"");
            } else if(args.length == 1) {
                RedisDump.ReloadStorageFromRemoteServer(null);
                SystemUtils.logger("Reloaded Storage From Redis Without A Key");
            }
        } else if(args[0].equalsIgnoreCase("uploadstorage")) {
            if(args.length == 2) {
                RedisDump.UploadStorageToRedis(args[1]);
                SystemUtils.logger("Uploaded Storage To Redis With The Key \"" + args[1] + "\"");
            } else if(args.length == 1) {
                RedisDump.UploadStorageToRedis(null);
                SystemUtils.logger("Uploaded Storage To Redis Without A Key");
            }
        } else if(args[0].equalsIgnoreCase("syncandclose")) {
            Server.Close = true;
        }
        args = null;


        //example command "update:=:hashmap1~4:=:add/remove:=:key:=:add/remove/(앞에 remove일 경우 여기랑 이 뒤는 쓸 필요 없다):=:value"


    }

    private static void update(String[] args) {
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
                || args[1].equalsIgnoreCase("DESTORYED_LandToFaction")
                || args[1].equalsIgnoreCase("DESTROYED_FactionUUIDToFactionName")) {
            FactionStorage.FactionStorageUpdateHandler(args, "");
        } else if (args[1].equalsIgnoreCase("namename")
                || args[1].equalsIgnoreCase("nameuuid")
                || args[1].equalsIgnoreCase("uuidname")) {
            UserInfoStorage.UserInfoStorageUpdateHandler(args);
        } else if (args[1].equalsIgnoreCase("cachedDTR")
                || args[1].equalsIgnoreCase("cachedBank")) {
            CachedStorage.JedisCacheSync(args);
        }
    }
}
