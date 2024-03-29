package com.itndev.FactionCore.Database.MySQL;

import com.itndev.FactionCore.Factions.Storage.FactionStorage;
import com.itndev.FactionCore.Utils.Factions.CacheUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class wtfDatabase {

    public void AddNewFactionName(String FactionNameCapped, String FactionUUID) {
        String FactionName = FactionNameCapped.toLowerCase(Locale.ROOT);
        CompletableFuture<Boolean> isExistingNamefuture = SQL.getDatabase().isExistingName(FactionName);
        new Thread(() -> {
            try {
                Boolean isExistingNamed = isExistingNamefuture.get(40, TimeUnit.MILLISECONDS);
                if(!isExistingNamed) {
                    try {
                        //Connection connection = SQL.getConnection().dataSource.getConnection();
                        PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement("Call CREATENAME('" +
                                FactionName + "','" + FactionUUID + "','" + FactionNameCapped + "')");
                        ps.executeQuery();
                        SQL.closeConnections(null, ps, null);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                isExistingNamed = null;
            } catch (InterruptedException | TimeoutException | ExecutionException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void CacheFactionDTR(String FactionUUID) {
        new Thread( () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                //Connection connection = SQL.getConnection().dataSource.getConnection();
                PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement("SELECT " +
                        "FactionDTR FROM FactionDTR WHERE FactionUUID='" + FactionUUID + "'");
                ResultSet rs = ps.executeQuery();
                Double DTR = 0D;
                if(rs.next()) {
                    DTR = Double.parseDouble(rs.getString("FactionDTR"));
                    CacheUtils.UpdateCachedDTR(FactionUUID, DTR);
                }
                DTR = null;
                SQL.closeConnections(null, ps, rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void CacheFactionBank(String FactionUUID) {
        new Thread( () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                //Connection connection = SQL.getConnection().dataSource.getConnection();
                PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement("SELECT " +
                        "FactionBank FROM FactionBank WHERE FactionUUID='" + FactionUUID + "'");
                ResultSet rs = ps.executeQuery();
                Double Bank = 0D;
                if(rs.next()) {
                    Bank = Double.parseDouble(rs.getString("FactionBank"));
                    CacheUtils.UpdateCachedBank(FactionUUID, Bank);
                }
                Bank = null;
                SQL.closeConnections(null, ps, rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public CompletableFuture<Boolean> TryClaimName(String FactionNameCapped, String FactionUUID) {
        CompletableFuture<Boolean> hasSucceed = new CompletableFuture<>();
        new Thread(() -> {
            try {
                //Connection connection = SQL.getConnection().dataSource.getConnection();
                PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement("Call TRYCLAIMNAME('" + FactionNameCapped.toLowerCase(Locale.ROOT) + "','" + FactionUUID + "','" + FactionNameCapped + "',0);");
                //ps.setString(1, FactionUUID);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    Double temp = rs.getDouble("VALUE_Boolean");
                    if(temp == 0) {
                        hasSucceed.complete(true);
                    } else if(temp == 1) {
                        hasSucceed.complete(false);
                    }
                    temp = null;
                }
                SQL.closeConnections(null, ps, rs);
            } catch (SQLException e) {
                e.printStackTrace();
                hasSucceed.complete(false);
            }
        }).start();
        return hasSucceed;
    }


    public void DeleteFactionName(String FactionUUID) {
        new Thread(() -> {
            try {
                //Connection connection = SQL.getConnection().dataSource.getConnection();
                PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement("DELETE FROM FactionName WHERE FactionUUID=?");
                ps.setString(1, FactionUUID);
                ps.executeUpdate();
                SQL.closeConnections(null, ps, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void CreateNewDTR(String FactionUUID, String FactionName) {
        new Thread(() -> {
            try {
                /*PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("INSERT IGNORE INTO FactionDTR" +
                         " (FactionUUID,FactionName) VALUES (?,?)");
                ps.setString(1, FactionUUID);
                ps.setString(2, FactionName);
                ps.executeUpdate();
                PreparedStatement ps2 = Main.hikariCP.getHikariConnection().prepareStatement("UPDATE FactionDTR" +
                        " SET FactionDTR=? WHERE FactionUUID=?");
                ps2.setString(1, "100");
                ps2.setString(2, FactionUUID);*/
                //Connection connection = SQL.getConnection().dataSource.getConnection();
                PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement("Call CREATEDTR('" +
                        FactionUUID + "','" + FactionName + "','100')");
                ps.executeQuery();
                CacheUtils.UpdateCachedDTR(FactionUUID, 100.0);
                SQL.closeConnections(null, ps, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void CreateNewBank(String FactionUUID, String FactionName) {
        new Thread(() -> {
            try {
                //Connection connection = SQL.getConnection().dataSource.getConnection();
                PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement("Call CREATEBANK('" +
                        FactionUUID + "','" + FactionName + "','0')");
                ps.executeQuery();
                CacheUtils.UpdateCachedBank(FactionUUID, 0.0);
                SQL.closeConnections(null, ps, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public CompletableFuture<Boolean> isExistingName(String FactionName) {
        CompletableFuture<Boolean> FutureBoolean = new CompletableFuture<>();
        new Thread(() -> {
            if(FactionStorage.FactionNameToFactionUUID.containsKey(FactionName.toLowerCase(Locale.ROOT))) {
                FutureBoolean.complete(true);
            } else {
                try {
                    //Connection connection = SQL.getConnection().dataSource.getConnection();
                    PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement("SELECT * FROM FactionName WHERE FactionName=?");
                    ps.setString(1, FactionName.toLowerCase(Locale.ROOT));
                    ResultSet rs = ps.executeQuery();
                    if(rs.next()) {
                        FutureBoolean.complete(true);
                    } else {
                        FutureBoolean.complete(false);
                    }

                    SQL.closeConnections(null, ps, rs);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        return FutureBoolean;
    }

    public void DeleteFactionDTR(String FactionUUID) {
        new Thread(() -> {
            try {
                //Connection connection = SQL.getConnection().dataSource.getConnection();
                PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement("DELETE FROM FactionDTR WHERE FactionUUID=?");
                ps.setString(1, FactionUUID);
                ps.executeUpdate();
                CacheUtils.removeCachedDTR(FactionUUID);
                SQL.closeConnections(null, ps, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void DeleteFactionBank(String FactionUUID) {
        new Thread(() -> {
            try {
                //Connection connection = SQL.getConnection().dataSource.getConnection();
                PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement("DELETE FROM FactionBank WHERE FactionUUID=?");
                ps.setString(1, FactionUUID);
                ps.executeUpdate();
                CacheUtils.removeCachedBank(FactionUUID);
                SQL.closeConnections(null, ps, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public CompletableFuture<Double> GetFactionDTR(String FactionUUID) {
        CompletableFuture<Double> FutureDTR = new CompletableFuture();
        new Thread( () -> {
            try {
                //Connection connection = SQL.getConnection().dataSource.getConnection();
                PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement("SELECT " +
                        "FactionDTR FROM FactionDTR WHERE FactionUUID='" + FactionUUID + "'");
                ResultSet rs = ps.executeQuery();
                Double DTR = 0D;
                if(rs.next()) {
                    DTR = Double.parseDouble(rs.getString("FactionDTR"));
                    FutureDTR.complete(DTR);
                    CacheUtils.UpdateCachedDTR(FactionUUID, DTR);
                }
                SQL.closeConnections(null, ps, rs);
                DTR = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
        return FutureDTR;
    }

    public CompletableFuture<Double> GetFactionBank(String FactionUUID) {
        CompletableFuture<Double> FutureBank = new CompletableFuture();
        new Thread( () -> {
            try {
                //Connection connection = SQL.getConnection().dataSource.getConnection();
                PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement("SELECT " +
                        "FactionBank FROM FactionBank WHERE FactionUUID='" + FactionUUID + "'");
                ResultSet rs = ps.executeQuery();
                Double Bank = 0D;
                if(rs.next()) {
                    Bank = Double.parseDouble(rs.getString("FactionBank"));
                    FutureBank.complete(Bank);
                    CacheUtils.UpdateCachedBank(FactionUUID, Bank);
                }
                SQL.closeConnections(null, ps, null);
                Bank = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
        return FutureBank;
    }

    public CompletableFuture<Double> AddFactionDTR(String FactionUUID, double DTR) {
        CompletableFuture<Double> futureDTR = new CompletableFuture<>();
        new Thread( () ->{
            try {

                /*PreparedStatement DTRUPDATE = Main.hikariCP.getHikariConnection().prepareStatement("SELECT @ORIGINNAME := (SELECT FactionDTR FROM FactionDTR WHERE FactionUUID='"+ FactionUUID +"');" +
                        "UPDATE FactionDTR SET FactionDTR=CONVERT(CONVERT(@ORIGINNAME, DOUBLE) + " + String.valueOf(DTR) + ", CHAR) WHERE FactionUUID='"+ FactionUUID +"';" +
                        "SELECT FactionDTR FROM FactionDTR WHERE FactionUUID='"+ FactionUUID +"';");
                ResultSet rs = DTRUPDATE.executeQuery();
                if(rs.next()) {
                    futureDTR.complete(rs.getDouble("FactionDTR"));
                }*/
                //Connection connection = SQL.getConnection().dataSource.getConnection();
                PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement("Call " +
                        "UPDATEDTR(0," + String.valueOf(DTR) + ",0,'" + FactionUUID + "')");
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    Double FinalDTR = rs.getDouble("FINALDTR");
                    futureDTR.complete(FinalDTR);
                    CacheUtils.UpdateCachedDTR(FactionUUID, FinalDTR);
                    FinalDTR = null;
                } else {
                    futureDTR.complete(-420D);
                }
                CacheFactionDTR(FactionUUID);
                SQL.closeConnections(null, ps, rs);
                /*PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("SELECT " +
                        "FactionDTR FROM FactionDTR WHERE FactionUUID=?");
                ps.setString(1, FactionUUID);
                ResultSet rs = ps.executeQuery();
                double originDTR = Double.parseDouble(rs.getString("FactionDTR"));

                PreparedStatement ps2 = Main.hikariCP.getHikariConnection().prepareStatement("UPDATE FactionDTR" +
                        " SET FactionDTR=? WHERE FactionUUID=?");
                ps2.setString(1, String.valueOf(originDTR + DTR));
                ps2.setString(2, FactionUUID);
                ps2.executeUpdate();
                PreparedStatement unlock = Main.hikariCP.getHikariConnection().prepareStatement("UNLOCK " +
                        "TABLE FactionDTR");
                unlock.executeUpdate();*/
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
        return futureDTR;
    }

    public CompletableFuture<Double> AddFactionBank(String FactionUUID, double Bank) {
        CompletableFuture<Double> futureBank = new CompletableFuture<>();
        new Thread( () ->{
            try {
                //Connection connection = SQL.getConnection().dataSource.getConnection();
                String cmd = "Call UPDATEBANK(0," + String.valueOf(Bank) + ",0,'" + FactionUUID + "')";
                PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement(cmd);
                ResultSet rs = ps.executeQuery();
                if(rs.next()) {
                    double Bankf = rs.getDouble("FINALBANK");
                    CacheUtils.UpdateCachedBank(FactionUUID, Bankf);
                    futureBank.complete(Bankf);
                } else {
                    futureBank.complete(-420D);
                }
                CacheFactionBank(FactionUUID);
                cmd = null;
                SQL.closeConnections(null, ps, rs);
                /*PreparedStatement DTRUPDATE = Main.hikariCP.getHikariConnection().prepareStatement("SELECT @ORIGINNAME := (SELECT FactionBank FROM FactionBank WHERE FactionUUID='"+ FactionUUID +"');" +
                        "UPDATE FactionBank SET FactionBank=CONVERT(CONVERT(@ORIGINNAME, DOUBLE) + " + String.valueOf(DTR) + ", CHAR) WHERE FactionUUID='"+ FactionUUID +"';" +
                        "SELECT FactionBank FROM FactionBank WHERE FactionUUID='"+ FactionUUID +"';");
                ResultSet rs = DTRUPDATE.executeQuery();
                if(rs.next()) {
                    futureBank.complete(rs.getDouble("FactionBank"));
                } else {
                    futureBank.complete(null);
                }*/
                /*PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("SELECT " +
                        "FactionDTR FROM FactionDTR WHERE FactionUUID=?");
                ps.setString(1, FactionUUID);
                ResultSet rs = ps.executeQuery();
                double originDTR = Double.parseDouble(rs.getString("FactionDTR"));

                PreparedStatement ps2 = Main.hikariCP.getHikariConnection().prepareStatement("UPDATE FactionDTR" +
                        " SET FactionDTR=? WHERE FactionUUID=?");
                ps2.setString(1, String.valueOf(originDTR + DTR));
                ps2.setString(2, FactionUUID);
                ps2.executeUpdate();
                PreparedStatement unlock = Main.hikariCP.getHikariConnection().prepareStatement("UNLOCK " +
                        "TABLE FactionDTR");
                unlock.executeUpdate();*/
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
        return futureBank;
    }

    public void ChangeFactionDatabaseName(String FactionUUID, String FactionNameCap) {
        new Thread( () ->{
            try {
                //Connection connection = SQL.getConnection().dataSource.getConnection();
                String cmd = "Call UPDATENAME('" + FactionNameCap.toLowerCase(Locale.ROOT) + "','" + FactionUUID + "','" + FactionNameCap + "')";
                PreparedStatement ps = SQL.getConnection().getHikariConnection().prepareStatement(cmd);
                ps.executeQuery();
                cmd = null;
                SQL.closeConnections(null, ps, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }
}