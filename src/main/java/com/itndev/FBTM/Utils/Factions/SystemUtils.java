package com.itndev.FBTM.Utils.Factions;

import com.itndev.FBTM.Database.Redis.Obj.Storage;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemUtils {

    public static String getPrefix() {
        return "&a&o&l[ &r&f국가 &a&o&l] &r&f";
    }

    private static DecimalFormat df = new DecimalFormat("0.00");

    public static void SendMoney(String UUID, Double Amount) {
        Storage.AddCommandToQueue("eco:=:give:=:" + UUID + ":=:" + df.format(Amount));
    }

    public static SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    public static UUID Convert2UUID(String UUID2) {
        return UUID.fromString(UUID2);
    }

    public static String FactionUUIDToDate(String FactionUUID) {
        Long time = Long.valueOf(FactionUUID.split("=")[0]);
        return timeformat.format(new Date(time));
    }

    public static String getDate(Long timedata) {
        return timeformat.format(new Date(timedata));
    }

    public static ArrayList string2list(String k) {
        if(k.contains("<%&LISTSPLITTER&%>")) {
            String[] parts = k.split("<%&LISTSPLITTER&%>");
            ArrayList<String> memlist = new ArrayList<>();
            for(String d : parts) {
                memlist.add(d);
            }
            return memlist;
        } else {
            ArrayList<String> memlist = new ArrayList<>();
            memlist.add(k);
            return memlist;
        }
    }
    public static String list2string(ArrayList<String> list) {
        String k = "";
        int i = 0;
        for(String c : list) {
            i = i + 1;
            if(list.size() >= i) {
                k = k + c;
            } else {
                k = k + c + "<%&LISTSPLITTER&%>";
            }


        }
        return k;
    }

    public static String Args2String(String[] args, int Start) {
        String FinalString = "";
        for(int k = Start; k < args.length; k++) {
            if(args[k] == null) {
                break;
            }
            if(k == args.length - 1) {
                FinalString = FinalString + args[k];
            } else {
                FinalString = FinalString + args[k] + " ";
            }
        }
        return FinalString;
    }

    public static void UUID_BASED_MSG_SENDER(String UUID, String Message) {
        Storage.AddCommandToQueue("notify:=:" + UUID + ":=:" + UUID + ":=:" + Message + ":=:" + "false");
    }
}
