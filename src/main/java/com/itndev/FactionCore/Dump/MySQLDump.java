package com.itndev.FactionCore.Dump;

import com.itndev.FactionCore.Discord.DiscordUtils;
import com.itndev.FaxLib.Utils.Data.MapFixer;
import com.itndev.FactionCore.Database.MySQL.SQL;
import com.itndev.FactionCore.Discord.AuthStorage;
import com.itndev.FactionCore.Factions.Storage.FactionStorage;
import com.itndev.FactionCore.Factions.UserInfoStorage;
import com.itndev.FactionCore.Utils.Factions.SystemUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MySQLDump {

    private static void TruncateBackup() {
        try {
            SQL.getConnection().getHikariConnection().prepareStatement("TRUNCATE FactionBackup").executeUpdate();
        } catch (SQLException throwables) {
            SystemUtils.logger(throwables.getMessage());
            DiscordUtils.info_logger(throwables.getMessage());
        }
    }

    public static void LoadFromMySQL() throws SQLException {
        try {
            FactionStorage.FactionInfo = MapFixer.get().Fixer_1_2(LoadHASHMAP("FactionInfo")); //(ConcurrentHashMap<String, String>) Connect.getSetcommands().hmget(key + "-" + "FactionInfo");
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            DiscordUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionMember = MapFixer.get().Fixer_3_1(LoadHASHMAP("FactionMember"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            DiscordUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionNameToFactionName = MapFixer.get().Fixer_1_2(LoadHASHMAP("FactionNameToFactionName"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            DiscordUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionInfoList = MapFixer.get().Fixer_3_1(LoadHASHMAP("FactionInfoList"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            DiscordUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionNameToFactionUUID = MapFixer.get().Fixer_1_2(LoadHASHMAP("FactionNameToFactionUUID"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            DiscordUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionOutPost = MapFixer.get().Fixer_1_2(LoadHASHMAP("FactionOutPost"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            DiscordUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionOutPostList = MapFixer.get().Fixer_3_1(LoadHASHMAP("FactionOutPostList"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            DiscordUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionRank = MapFixer.get().Fixer_1_2(LoadHASHMAP("FactionRank"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            DiscordUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionToLand = MapFixer.get().Fixer_2_1(LoadHASHMAP("FactionToLand"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            DiscordUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionToOutPost = MapFixer.get().Fixer_2_1(LoadHASHMAP("FactionToLand"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            DiscordUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionUUIDToFactionName = MapFixer.get().Fixer_1_2(LoadHASHMAP("FactionUUIDToFactionName"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            DiscordUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.FactionWarpLocations = MapFixer.get().Fixer_1_2(LoadHASHMAP("FactionWarpLocations"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            DiscordUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.OutPostToFaction = LoadHASHMAP("OutPostToFaction");
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            DiscordUtils.error_logger(e.getMessage());
        }

        try {
            FactionStorage.PlayerFaction = MapFixer.get().Fixer_1_2(LoadHASHMAP("PlayerFaction"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            DiscordUtils.error_logger(e.getMessage());
        }

        try {
            UserInfoStorage.namename = MapFixer.get().Fixer_1_2(LoadHASHMAP("namename"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            DiscordUtils.error_logger(e.getMessage());
        }

        try {
            UserInfoStorage.uuidname = MapFixer.get().Fixer_1_2(LoadHASHMAP("uuidname"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            DiscordUtils.error_logger(e.getMessage());
        }

        try {
            UserInfoStorage.nameuuid = MapFixer.get().Fixer_1_2(LoadHASHMAP("nameuuid"));
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            DiscordUtils.error_logger(e.getMessage());
        }

        try {
            AuthStorage.DISCORDID_TO_UUID = LoadHASHMAP("DISCORDID_TO_UUID");
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            DiscordUtils.error_logger(e.getMessage());
        }

        try {
            AuthStorage.UUID_TO_DISCORDID = LoadHASHMAP("UUID_TO_DISCORDID");
        } catch (Exception e) {
            SystemUtils.error_logger(e.getMessage());
            DiscordUtils.error_logger(e.getMessage());
        }
    }

    public static void DumpToMySQL() throws SQLException {
        TruncateBackup();

        if (!FactionStorage.FactionInfo.isEmpty()) {
            DumpHASHMAP("FactionInfo", MapFixer.get().Fixer_1_1(FactionStorage.FactionInfo));
        }

        if(!FactionStorage.FactionMember.isEmpty()) {
            DumpHASHMAP("FactionMember", MapFixer.get().Fixer_3_2(FactionStorage.FactionMember));
        }

        if(!FactionStorage.FactionNameToFactionName.isEmpty()) {
            DumpHASHMAP("FactionNameToFactionName", MapFixer.get().Fixer_1_1(FactionStorage.FactionNameToFactionName));
        }

        if(!FactionStorage.FactionInfoList.isEmpty()) {
            DumpHASHMAP("FactionInfoList", MapFixer.get().Fixer_3_2(FactionStorage.FactionInfoList));
        }

        //------>
        if(!FactionStorage.FactionNameToFactionUUID.isEmpty()) {
            DumpHASHMAP("FactionNameToFactionUUID", MapFixer.get().Fixer_1_1(FactionStorage.FactionNameToFactionUUID));

        }

        if(!FactionStorage.FactionOutPost.isEmpty()) {
            DumpHASHMAP("FactionOutPost", MapFixer.get().Fixer_1_1(FactionStorage.FactionOutPost));
        }

        if(!FactionStorage.FactionOutPostList.isEmpty()) {
            DumpHASHMAP("FactionOutPostList", MapFixer.get().Fixer_3_2(FactionStorage.FactionOutPostList));
        }

        if(!FactionStorage.FactionRank.isEmpty()) {
            DumpHASHMAP("FactionRank", MapFixer.get().Fixer_1_1(FactionStorage.FactionRank));
        }

        if(!FactionStorage.FactionToLand.isEmpty()) {
            DumpHASHMAP("FactionToLand", MapFixer.get().Fixer_2_2(FactionStorage.FactionToLand));
        }

        if(!FactionStorage.FactionToOutPost.isEmpty()) {
            DumpHASHMAP("FactionToOutPost", MapFixer.get().Fixer_2_2(FactionStorage.FactionToOutPost));
        }

        if(!FactionStorage.FactionUUIDToFactionName.isEmpty()) {
            DumpHASHMAP("FactionUUIDToFactionName", MapFixer.get().Fixer_1_1(FactionStorage.FactionUUIDToFactionName));
        }

        if(!FactionStorage.FactionWarpLocations.isEmpty()) {
            DumpHASHMAP("FactionWarpLocations", MapFixer.get().Fixer_1_1(FactionStorage.FactionWarpLocations));
        }

        if(!FactionStorage.OutPostToFaction.isEmpty()) {
            DumpHASHMAP("OutPostToFaction", FactionStorage.OutPostToFaction);
        }

        if(!FactionStorage.PlayerFaction.isEmpty()) {
            DumpHASHMAP("PlayerFaction", MapFixer.get().Fixer_1_1(FactionStorage.PlayerFaction));
        }

        if(!UserInfoStorage.namename.isEmpty()) {
            DumpHASHMAP("namename", MapFixer.get().Fixer_1_1(UserInfoStorage.namename));
        }

        if(!UserInfoStorage.nameuuid.isEmpty()) {
            DumpHASHMAP("nameuuid", MapFixer.get().Fixer_1_1(UserInfoStorage.nameuuid));
        }

        if(!UserInfoStorage.uuidname.isEmpty()) {
            DumpHASHMAP("uuidname", MapFixer.get().Fixer_1_1(UserInfoStorage.uuidname));
        }

        if(!AuthStorage.DISCORDID_TO_UUID.isEmpty()) {
            DumpHASHMAP("DISCORDID_TO_UUID", AuthStorage.DISCORDID_TO_UUID);
        }

        if(!AuthStorage.UUID_TO_DISCORDID.isEmpty()) {
            DumpHASHMAP("UUID_TO_DISCORDID", AuthStorage.UUID_TO_DISCORDID);
        }
    }

    private static void DumpHASHMAP(String MapName, HashMap<String, String> map) throws SQLException {
        for(Map.Entry<String, String> entry : map.entrySet()) {
            //Connection connection = SQL.getConnection().getHikariConnection();
            PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement("INSERT INTO FactionBackup (MAP_NAME, MAP_KEY, MAP_VALUE) VALUES (?, ?, ?)");
            ps.setString(1, MapName);
            ps.setString(2, entry.getKey());
            ps.setString(3, entry.getValue());
            ps.executeUpdate();
            SQL.closeConnections(null, ps, null);
            entry = null;
        }
    }

    private static HashMap<String, String> LoadHASHMAP(String MapName) throws SQLException {
        HashMap<String, String> finalmap = new HashMap<>();
        //Connection connection = SQL.getConnection().getHikariConnection();
        PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement("SELECT * FROM FactionBackup WHERE MAP_NAME=?");
        ps.setString(1, MapName);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            finalmap.put(
                    rs.getString("MAP_KEY"),
                    rs.getString("MAP_VALUE")
            );
        }
        SQL.closeConnections(null, ps, rs);
        return finalmap;
    }
}
