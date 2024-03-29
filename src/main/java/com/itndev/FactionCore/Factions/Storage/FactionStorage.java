package com.itndev.FactionCore.Factions.Storage;

import com.itndev.FactionCore.Factions.FactionTimeOut;
import com.itndev.FactionCore.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class FactionStorage {

    public static HashMap<String, ArrayList<String>> FactionToLand = new HashMap<>();

    public static HashMap<String, String> LandToFaction = new HashMap<>();

    public static HashMap<String, ArrayList<String>> DESTORYED_FactionToLand = new HashMap<>();

    public static HashMap<String, String> DESTORYED_LandToFaction = new HashMap<>();

    public static HashMap<String, String> DESTROYED_FactionUUIDToFactionName = new HashMap<>();

    public static ConcurrentHashMap<String, String> AsyncLandToFaction = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, String> PlayerFaction = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, String> FactionUUIDToFactionName = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, String> FactionNameToFactionUUID = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, String> FactionNameToFactionName = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, String> FactionRank = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, ArrayList<String>> FactionMember = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, String> FactionInfo = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, ArrayList<String>> FactionInfoList = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, ArrayList<String>> FactionInviteQueue = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, String> FactionOutPost = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, String> FactionWarpLocations = new ConcurrentHashMap<>();

    public static HashMap<String, String> OutPostToFaction = new HashMap<>();

    public static ConcurrentHashMap<String, String> AsyncOutPostToFaction = new ConcurrentHashMap<>();

    public static HashMap<String, ArrayList<String>> FactionToOutPost = new HashMap<>();

    public static ConcurrentHashMap<String, ArrayList<String>> FactionOutPostList = new ConcurrentHashMap<>();


    public static void FactionStorageUpdateHandler(String[] args, String ServerName) {
        if(args[1].equalsIgnoreCase("FactionToLand")) {
            if(args.length > 6 && args[6].equalsIgnoreCase(Server.getServerName())) {
                return;
            }
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3];
                String value = args[5];
                if(args[4].equalsIgnoreCase("add")) {
                    if (!FactionStorage.FactionToLand.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionToLand.containsKey(key)) {

                            //해당 키가 있으면
                            ArrayList<String> updatelist = FactionStorage.FactionToLand.get(key);
                            if(!updatelist.contains(value)) {
                                updatelist.add(value);
                            }
                            FactionStorage.FactionToLand.put(key, updatelist);
                            updatelist = null;
                        } else {

                            //해당 키가 없으면
                            ArrayList<String> updatelist2 = new ArrayList<>();
                            updatelist2.add(value);
                            FactionStorage.FactionToLand.put(key, updatelist2);
                            updatelist2 = null;
                        }
                    } else { //만약 비어있으면
                        ArrayList<String> updatelist3 = new ArrayList<>();
                        updatelist3.add(value);
                        FactionStorage.FactionToLand.put(key, updatelist3);
                        updatelist3 = null;
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {
                    if (!FactionStorage.FactionToLand.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionToLand.containsKey(key)) {

                            //해당 키가 있으면
                            ArrayList<String> updatelist = FactionStorage.FactionToLand.get(key);
                            updatelist.remove(value);
                            if(updatelist.isEmpty()) {
                                FactionStorage.FactionToLand.remove(key);
                            } else {
                                FactionStorage.FactionToLand.put(key, updatelist);
                            }
                        } else {

                            //해당 키가 없으면
                            //없으니까 하지 말자
                        }
                    } else { //만약 비어있으면
                        //시발 존재하지도 않는데 어떻게 없애냐 뭔시발 병신이냐
                    }
                }
                key = null;
                value = null;
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.FactionToLand.remove(key);
                key = null;
            }

        } else if(args[1].equalsIgnoreCase("LandToFaction")) {
            if(args.length > 6 && args[6].equalsIgnoreCase(Server.getServerName())) {
                return;
            }
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값

                if(args[4].equalsIgnoreCase("add")) {

                    if(FactionStorage.LandToFaction.containsKey(key)) {
                        FactionStorage.LandToFaction.remove(key);
                        FactionStorage.LandToFaction.put(key, value);
                    } else {
                        FactionStorage.LandToFaction.put(key, value);
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {

                    if(FactionStorage.LandToFaction.containsKey(key)) {

                        FactionStorage.LandToFaction.remove(key);

                        //할거 없다
                    } else {
                        //할거 없다
                    }
                }
                key = null;
                value = null;

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.LandToFaction.remove(key);
                key = null;
            }

        } else if(args[1].equalsIgnoreCase("FactionRank")) {
            if(args[2].equalsIgnoreCase("add")) {

                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값
                if(args[4].equalsIgnoreCase("add")) {

                    if(FactionStorage.FactionRank.containsKey(key)) {
                        FactionStorage.FactionRank.remove(key);
                        FactionStorage.FactionRank.put(key, value);
                    } else {
                        FactionStorage.FactionRank.put(key, value);
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {

                    if(FactionStorage.FactionRank.containsKey(key)) {

                        FactionStorage.FactionRank.remove(key);

                        //할거 없다
                    } else {
                        //할거 없다
                    }
                }
                key = null;
                value = null;

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                if(FactionStorage.FactionRank.containsKey(key)) {
                    FactionStorage.FactionRank.remove(key);
                }
                key = null;
            }

        } else if(args[1].equalsIgnoreCase("PlayerFaction")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키

                String value = args[5]; //추가하고 싶은 값

                if(args[4].equalsIgnoreCase("add")) {

                    if(FactionStorage.PlayerFaction.containsKey(key)) {
                        FactionStorage.PlayerFaction.remove(key);
                        FactionStorage.PlayerFaction.put(key, value);
                    } else {
                        FactionStorage.PlayerFaction.put(key, value);
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {

                    if(FactionStorage.PlayerFaction.containsKey(key)) {

                        FactionStorage.PlayerFaction.remove(key);

                        //할거 없다
                    } else {
                        //할거 없다
                    }
                }
                key = null;
                value = null;

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                if(FactionStorage.PlayerFaction.containsKey(key)) {
                    FactionStorage.PlayerFaction.remove(key);
                }
                key = null;
            }

        } else if(args[1].equalsIgnoreCase("FactionMember")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3];
                String value = args[5];
                if(args[4].equalsIgnoreCase("add")) {
                    if (!FactionStorage.FactionMember.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionMember.containsKey(key)) {

                            //해당 키가 있으면
                            ArrayList<String> updatelist = FactionStorage.FactionMember.get(key);
                            if(!updatelist.contains(value)) {
                                updatelist.add(value);
                            }
                            FactionStorage.FactionMember.put(key, updatelist);
                            updatelist = null;
                        } else {

                            //해당 키가 없으면
                            ArrayList<String> updatelist2 = new ArrayList<>();
                            updatelist2.add(value);
                            FactionStorage.FactionMember.put(key, updatelist2);
                            updatelist2 = null;
                        }
                    } else { //만약 비어있으면
                        ArrayList<String> updatelist3 = new ArrayList<>();
                        updatelist3.add(value);
                        FactionStorage.FactionMember.put(key, updatelist3);;
                        updatelist3 = null;
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {
                    if (!FactionStorage.FactionMember.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionMember.containsKey(key)) {

                            //해당 키가 있으면
                            ArrayList<String> updatelist = FactionStorage.FactionMember.get(key);
                            if(updatelist.contains(value)) {
                                updatelist.remove(value);
                            }
                            FactionStorage.FactionMember.put(key, updatelist);
                            updatelist = null;
                        } else {

                            //해당 키가 없으면
                            //없으니까 하지 말자
                        }
                    } else { //만약 비어있으면
                        //시발 존재하지도 않는데 어떻게 없애냐 뭔시발 병신이냐
                    }
                }
                key = null;
                value = null;
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                if(FactionStorage.FactionMember.containsKey(key)) {
                    FactionStorage.FactionMember.remove(key);
                }
                key = null;
            }

        } else if(args[1].equalsIgnoreCase("FactionNameToFactionName")) {

            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값

                if(args[4].equalsIgnoreCase("add")) {

                    if(FactionStorage.FactionNameToFactionName.containsKey(key)) {
                        FactionStorage.FactionNameToFactionName.remove(key);
                        FactionStorage.FactionNameToFactionName.put(key, value);
                    } else {
                        FactionStorage.FactionNameToFactionName.put(key, value);
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {

                    if(FactionStorage.FactionNameToFactionName.containsKey(key)) {

                        FactionStorage.FactionNameToFactionName.remove(key);

                        //할거 없다
                    } else {
                        //할거 없다
                    }
                }
                key = null;
                value = null;

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                if(FactionStorage.FactionNameToFactionName.containsKey(key)) {
                    FactionStorage.FactionNameToFactionName.remove(key);
                }
                key = null;
            }

        } else if(args[1].equalsIgnoreCase("FactionNameToFactionUUID")) {

            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값

                if(args[4].equalsIgnoreCase("add")) {

                    if(FactionStorage.FactionNameToFactionUUID.containsKey(key)) {
                        FactionStorage.FactionNameToFactionUUID.remove(key);
                        FactionStorage.FactionNameToFactionUUID.put(key, value);
                    } else {
                        FactionStorage.FactionNameToFactionUUID.put(key, value);
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {

                    if(FactionStorage.FactionNameToFactionUUID.containsKey(key)) {

                        FactionStorage.FactionNameToFactionUUID.remove(key);

                        //할거 없다
                    } else {
                        //할거 없다
                    }
                }
                key = null;
                value = null;

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                if(FactionStorage.FactionNameToFactionUUID.containsKey(key)) {
                    FactionStorage.FactionNameToFactionUUID.remove(key);
                }
                key = null;
            }

        } else if(args[1].equalsIgnoreCase("FactionUUIDToFactionName")) {

            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값

                if(args[4].equalsIgnoreCase("add")) {

                    if(FactionStorage.FactionUUIDToFactionName.containsKey(key)) {
                        FactionStorage.FactionUUIDToFactionName.remove(key);
                        FactionStorage.FactionUUIDToFactionName.put(key, value);
                    } else {
                        FactionStorage.FactionUUIDToFactionName.put(key, value);
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {

                    if(FactionStorage.FactionUUIDToFactionName.containsKey(key)) {

                        FactionStorage.FactionUUIDToFactionName.remove(key);

                        //할거 없다
                    } else {
                        //할거 없다
                    }
                }
                key = null;
                value = null;

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                if(FactionStorage.FactionUUIDToFactionName.containsKey(key)) {
                    FactionStorage.FactionUUIDToFactionName.remove(key);
                }
                key = null;
            }

        } else if(args[1].equalsIgnoreCase("FactionInfo")) { //no longer used

            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값

                if(args[4].equalsIgnoreCase("add")) {

                    if(FactionStorage.FactionInfo.containsKey(key)) {
                        FactionStorage.FactionInfo.remove(key);
                        FactionStorage.FactionInfo.put(key, value);
                    } else {
                        FactionStorage.FactionInfo.put(key, value);
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {

                    if(FactionStorage.FactionInfo.containsKey(key)) {

                        FactionStorage.FactionInfo.remove(key);

                        //할거 없다
                    } else {
                        //할거 없다
                    }
                }
                key = null;
                value = null;

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                if(FactionStorage.FactionInfo.containsKey(key)) {
                    FactionStorage.FactionInfo.remove(key);
                }
                key = null;
            }
        } else if(args[1].equalsIgnoreCase("FactionInfoList")) {

            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3];
                String value = args[5];
                if(args[4].equalsIgnoreCase("add")) {
                    if (!FactionStorage.FactionInfoList.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionInfoList.containsKey(key)) {

                            //해당 키가 있으면
                            ArrayList<String> updatelist = FactionStorage.FactionInfoList.get(key);
                            if(!updatelist.contains(value)) {
                                updatelist.add(value);
                            }
                            FactionStorage.FactionInfoList.put(key, updatelist);
                            updatelist = null;
                        } else {

                            //해당 키가 없으면
                            ArrayList<String> updatelist2 = new ArrayList<>();
                            updatelist2.add(value);
                            FactionStorage.FactionInfoList.put(key, updatelist2);
                            updatelist2 = null;
                        }
                    } else { //만약 비어있으면
                        ArrayList<String> updatelist3 = new ArrayList<>();
                        updatelist3.add(value);
                        FactionStorage.FactionInfoList.put(key, updatelist3);;
                        updatelist3 = null;
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {
                    if (!FactionStorage.FactionInfoList.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionInfoList.containsKey(key)) {

                            //해당 키가 있으면
                            ArrayList<String> updatelist = FactionStorage.FactionInfoList.get(key);
                            updatelist.remove(value);
                            FactionStorage.FactionInfoList.put(key, updatelist);
                            updatelist = null;
                        } else {

                            //해당 키가 없으면
                            //없으니까 하지 말자
                        }
                    } else { //만약 비어있으면
                        //시발 존재하지도 않는데 어떻게 없애냐 뭔시발 병신이냐
                    }
                }
                key = null;
                value = null;
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.FactionInfoList.remove(key);
                key = null;
            }

        } else if(args[1].equalsIgnoreCase("FactionInviteQueue")) {

            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3];
                String value = args[5];
                if(args[4].equalsIgnoreCase("add")) {
                    if (!FactionStorage.FactionInviteQueue.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionInviteQueue.containsKey(key)) {

                            //해당 키가 있으면
                            ArrayList<String> updatelist = FactionStorage.FactionInviteQueue.get(key);
                            if(!updatelist.contains(value)) {
                                updatelist.add(value);
                            }
                            FactionStorage.FactionInviteQueue.put(key, updatelist);
                            updatelist = null;
                        } else {

                            //해당 키가 없으면
                            ArrayList<String> updatelist2 = new ArrayList<>();
                            if(!updatelist2.contains(value)) {
                                updatelist2.add(value);
                            }
                            FactionStorage.FactionInviteQueue.put(key, updatelist2);
                            updatelist2 = null;
                        }
                    } else { //만약 비어있으면
                        ArrayList<String> updatelist3 = new ArrayList<>();
                        updatelist3.add(value);
                        FactionStorage.FactionInviteQueue.put(key, updatelist3);;
                        updatelist3 = null;
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {
                    if (!FactionStorage.FactionInviteQueue.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionInviteQueue.containsKey(key)) {

                            //해당 키가 있으면
                            ArrayList<String> updatelist = FactionStorage.FactionInviteQueue.get(key);
                            if(updatelist.contains(value)) {
                                updatelist.remove(value);
                            }
                            FactionStorage.FactionInviteQueue.put(key, updatelist);
                            updatelist = null;
                        } else {

                            //해당 키가 없으면
                            //없으니까 하지 말자
                        }
                    } else { //만약 비어있으면
                        //시발 존재하지도 않는데 어떻게 없애냐 뭔시발 병신이냐
                    }
                }
                key = null;
                value = null;
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                if(FactionStorage.FactionInviteQueue.containsKey(key)) {
                    FactionStorage.FactionInviteQueue.remove(key);
                }
                key = null;
            }

        } else if(args[1].equalsIgnoreCase("Timeout2info")) {

            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3];
                String value = args[5];
                if(args[4].equalsIgnoreCase("add")) {
                    if (!FactionTimeOut.Timeout2info.isEmpty()) { //비어있지 않으면
                        if (FactionTimeOut.Timeout2info.containsKey(key)) {

                            //해당 키가 있으면
                            ArrayList<String> updatelist = FactionTimeOut.Timeout2info.get(key);
                            if(!updatelist.contains(value)) {
                                updatelist.add(value);
                            }
                            FactionTimeOut.Timeout2info.put(key, updatelist);
                            updatelist = null;
                        } else {

                            //해당 키가 없으면
                            ArrayList<String> updatelist2 = new ArrayList<>();
                            updatelist2.add(value);
                            FactionTimeOut.Timeout2info.put(key, updatelist2);
                            updatelist2 = null;
                        }
                    } else { //만약 비어있으면
                        ArrayList<String> updatelist3 = new ArrayList<>();
                        updatelist3.add(value);
                        FactionTimeOut.Timeout2info.put(key, updatelist3);;
                        updatelist3 = null;
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {
                    if (!FactionTimeOut.Timeout2info.isEmpty()) { //비어있지 않으면
                        if (FactionTimeOut.Timeout2info.containsKey(key)) {

                            //해당 키가 있으면
                            ArrayList<String> updatelist = FactionTimeOut.Timeout2info.get(key);
                            if(updatelist.contains(value)) {
                                updatelist.remove(value);
                            }
                            FactionTimeOut.Timeout2info.put(key, updatelist);
                            updatelist = null;
                        } else {

                            //해당 키가 없으면
                            //없으니까 하지 말자
                        }
                    } else { //만약 비어있으면
                        //시발 존재하지도 않는데 어떻게 없애냐 뭔시발 병신이냐
                    }
                }
                key = null;
                value = null;
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                if(FactionTimeOut.Timeout2info.containsKey(key)) {
                    FactionTimeOut.Timeout2info.remove(key);
                }
                key = null;
            }

        } else if(args[1].equalsIgnoreCase("Timeout2")) {

            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값
                Integer intvalue = Integer.parseInt(value);


                if(args[4].equalsIgnoreCase("add")) {

                    if(FactionTimeOut.Timeout2.containsKey(key)) {
                        FactionTimeOut.Timeout2.remove(key);
                        FactionTimeOut.Timeout2.put(key, intvalue);
                    } else {
                        FactionTimeOut.Timeout2.put(key, intvalue);
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {

                    if(FactionTimeOut.Timeout2.containsKey(key)) {

                        FactionTimeOut.Timeout2.remove(key);

                        //할거 없다
                    } else {
                        //할거 없다
                    }
                }
                key = null;
                value = null;
                intvalue = null;

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                if(FactionTimeOut.Timeout2.containsKey(key)) {
                    FactionTimeOut.Timeout2.remove(key);
                }
                key = null;
            }

        } else if(args[1].equalsIgnoreCase("OutPostToFaction")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값

                if(args[4].equalsIgnoreCase("add")) {

                    if(FactionStorage.OutPostToFaction.containsKey(key)) {
                        FactionStorage.OutPostToFaction.remove(key);
                        FactionStorage.OutPostToFaction.put(key, value);
                    } else {
                        FactionStorage.OutPostToFaction.put(key, value);
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {

                    if(FactionStorage.OutPostToFaction.containsKey(key)) {

                        FactionStorage.OutPostToFaction.remove(key);

                        //할거 없다
                    } else {
                        //할거 없다
                    }
                }
                key = null;
                value = null;

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                if(FactionStorage.OutPostToFaction.containsKey(key)) {
                    FactionStorage.OutPostToFaction.remove(key);
                }
                key = null;
            }
        } else if(args[1].equalsIgnoreCase("FactionToOutPost")) {
            if(args.length > 6 && args[6].equalsIgnoreCase(Server.getServerName())) {
                return;
            }
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3];
                String value = args[5];
                if(args[4].equalsIgnoreCase("add")) {
                    if (!FactionStorage.FactionToOutPost.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionToOutPost.containsKey(key)) {

                            //해당 키가 있으면
                            ArrayList<String> updatelist = FactionStorage.FactionToOutPost.get(key);
                            if(!updatelist.contains(value)) {
                                updatelist.add(value);
                            }
                            FactionStorage.FactionToOutPost.put(key, updatelist);
                            updatelist = null;
                        } else {

                            //해당 키가 없으면
                            ArrayList<String> updatelist2 = new ArrayList<>();
                            updatelist2.add(value);
                            FactionStorage.FactionToOutPost.put(key, updatelist2);
                            updatelist2 = null;
                        }
                    } else { //만약 비어있으면
                        ArrayList<String> updatelist3 = new ArrayList<>();
                        updatelist3.add(value);
                        FactionStorage.FactionToOutPost.put(key, updatelist3);
                        updatelist3 = null;
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {
                    if (!FactionStorage.FactionToOutPost.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionToOutPost.containsKey(key)) {

                            //해당 키가 있으면
                            ArrayList<String> updatelist = FactionStorage.FactionToOutPost.get(key);
                            updatelist.remove(value);
                            if(updatelist.isEmpty()) {
                                FactionStorage.FactionToOutPost.remove(key);
                            } else {
                                FactionStorage.FactionToOutPost.put(key, updatelist);
                            }
                            updatelist = null;
                        } else {

                            //해당 키가 없으면
                            //없으니까 하지 말자
                        }
                    } else { //만약 비어있으면
                        //시발 존재하지도 않는데 어떻게 없애냐 뭔시발 병신이냐
                    }
                }
                key = null;
                value = null;
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.FactionToOutPost.remove(key);
                key = null;
            }

        } else if(args[1].equalsIgnoreCase("FactionOutPostList")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3];
                String value = args[5];
                if(args[4].equalsIgnoreCase("add")) {
                    if (!FactionStorage.FactionOutPostList.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionOutPostList.containsKey(key)) {

                            //해당 키가 있으면
                            ArrayList<String> updatelist = FactionStorage.FactionOutPostList.get(key);
                            if(!updatelist.contains(value)) {
                                updatelist.add(value);
                            }
                            FactionStorage.FactionOutPostList.put(key, updatelist);
                            updatelist = null;
                        } else {

                            //해당 키가 없으면
                            ArrayList<String> updatelist2 = new ArrayList<>();
                            updatelist2.add(value);
                            FactionStorage.FactionOutPostList.put(key, updatelist2);
                            updatelist2 = null;
                        }
                    } else { //만약 비어있으면
                        ArrayList<String> updatelist3 = new ArrayList<>();
                        updatelist3.add(value);
                        FactionStorage.FactionOutPostList.put(key, updatelist3);
                        updatelist3 = null;
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {
                    if (!FactionStorage.FactionOutPostList.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.FactionOutPostList.containsKey(key)) {

                            //해당 키가 있으면
                            ArrayList<String> updatelist = FactionStorage.FactionOutPostList.get(key);
                            updatelist.remove(value);
                            if(updatelist.isEmpty()) {
                                FactionStorage.FactionOutPostList.remove(key);
                            } else {
                                FactionStorage.FactionOutPostList.put(key, updatelist);
                            }
                            updatelist = null;
                        } else {

                            //해당 키가 없으면
                            //없으니까 하지 말자
                        }
                    } else { //만약 비어있으면
                        //시발 존재하지도 않는데 어떻게 없애냐 뭔시발 병신이냐
                    }
                }
                key = null;
                value = null;
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.FactionOutPostList.remove(key);
                key = null;
            }

        } else if(args[1].equalsIgnoreCase("FactionOutPost")) { //no longer used
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값

                if(args[4].equalsIgnoreCase("add")) {

                    if(FactionStorage.FactionOutPost.containsKey(key)) {
                        FactionStorage.FactionOutPost.remove(key);
                        FactionStorage.FactionOutPost.put(key, value);
                    } else {
                        FactionStorage.FactionOutPost.put(key, value);
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {

                    if(FactionStorage.FactionOutPost.containsKey(key)) {

                        FactionStorage.FactionOutPost.remove(key);

                        //할거 없다
                    } else {
                        //할거 없다
                    }
                }
                key = null;
                value = null;

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                if(FactionStorage.FactionOutPost.containsKey(key)) {
                    FactionStorage.FactionOutPost.remove(key);
                }
                key = null;
            }
        }else if(args[1].equalsIgnoreCase("DESTORYED_FactionToLand")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3];
                String value = args[5];
                if(args[4].equalsIgnoreCase("add")) {
                    if (!FactionStorage.DESTORYED_FactionToLand.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.DESTORYED_FactionToLand.containsKey(key)) {

                            //해당 키가 있으면
                            ArrayList<String> updatelist = FactionStorage.DESTORYED_FactionToLand.get(key);
                            if(!updatelist.contains(value)) {
                                updatelist.add(value);
                            }
                            FactionStorage.DESTORYED_FactionToLand.put(key, updatelist);
                            updatelist = null;
                        } else {

                            //해당 키가 없으면
                            ArrayList<String> updatelist2 = new ArrayList<>();
                            updatelist2.add(value);
                            FactionStorage.DESTORYED_FactionToLand.put(key, updatelist2);
                            updatelist2 = null;
                        }
                    } else { //만약 비어있으면
                        ArrayList<String> updatelist3 = new ArrayList<>();
                        updatelist3.add(value);
                        FactionStorage.DESTORYED_FactionToLand.put(key, updatelist3);;
                        updatelist3 = null;
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {
                    if (!FactionStorage.DESTORYED_FactionToLand.isEmpty()) { //비어있지 않으면
                        if (FactionStorage.DESTORYED_FactionToLand.containsKey(key)) {

                            //해당 키가 있으면
                            ArrayList<String> updatelist = FactionStorage.DESTORYED_FactionToLand.get(key);
                            updatelist.remove(value);
                            if(updatelist.isEmpty()) {
                                FactionStorage.DESTORYED_FactionToLand.remove(key);
                            } else {
                                FactionStorage.DESTORYED_FactionToLand.put(key, updatelist);
                            }
                            updatelist = null;
                        } else {

                            //해당 키가 없으면
                            //없으니까 하지 말자
                        }
                    } else { //만약 비어있으면
                        //시발 존재하지도 않는데 어떻게 없애냐 뭔시발 병신이냐
                    }
                }
                key = null;
                value = null;
            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.DESTORYED_FactionToLand.remove(key);
                key = null;
            }

        } else if(args[1].equalsIgnoreCase("DESTORYED_LandToFaction")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값

                if(args[4].equalsIgnoreCase("add")) {

                    if(FactionStorage.DESTORYED_LandToFaction.containsKey(key)) {
                        FactionStorage.DESTORYED_LandToFaction.remove(key);
                        FactionStorage.DESTORYED_LandToFaction.put(key, value);
                    } else {
                        FactionStorage.DESTORYED_LandToFaction.put(key, value);
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {

                    if(FactionStorage.DESTORYED_LandToFaction.containsKey(key)) {

                        FactionStorage.DESTORYED_LandToFaction.remove(key);

                        //할거 없다
                    } else {
                        //할거 없다
                    }
                }
                key = null;
                value = null;

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.DESTORYED_LandToFaction.remove(key);
                key = null;
            }

        } else if(args[1].equalsIgnoreCase("DESTROYED_FactionUUIDToFactionName")) {
            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값

                if(args[4].equalsIgnoreCase("add")) {

                    if(FactionStorage.DESTROYED_FactionUUIDToFactionName.containsKey(key)) {
                        FactionStorage.DESTROYED_FactionUUIDToFactionName.remove(key);
                        FactionStorage.DESTROYED_FactionUUIDToFactionName.put(key, value);
                    } else {
                        FactionStorage.DESTROYED_FactionUUIDToFactionName.put(key, value);
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {

                    if(FactionStorage.DESTROYED_FactionUUIDToFactionName.containsKey(key)) {

                        FactionStorage.DESTROYED_FactionUUIDToFactionName.remove(key);

                        //할거 없다
                    } else {
                        //할거 없다

                    }
                }
                key = null;
                value = null;

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                FactionStorage.DESTROYED_FactionUUIDToFactionName.remove(key);
                key = null;
            }


        }
        return;
        /* else if(args[1].equalsIgnoreCase("LandToFaction")) { //====================== Land To Faction

                } else if(args[1].equalsIgnoreCase("FactionRank")) { //====================== FactionRank

                } else if(args[1].equalsIgnoreCase("PlayerFaction")) { //====================== PlayerFaction

                } else if(args[1].equalsIgnoreCase("FactionMember")) { //====================== FactionMember

                } else if(args[1].equalsIgnoreCase("FactionNameToFactionName")) { //====================== FactionName To FactionName

                } else if(args[1].equalsIgnoreCase("FactionNameToFactionUUID")) { //====================== FactionName To FactionUUID

                } else if(args[1].equalsIgnoreCase("FactionUUIDToFactionName")) { //====================== FactionUUID To FactionName

                } else if(args[1].equalsIgnoreCase("FactionInviteQueue")) { //====================== Faction Invite Queue

                }*/
    }
}
