package com.itndev.factions.MySQL;

import com.itndev.factions.Main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;

public class MySQLUtils {



    public void CreateFactionNameAccount(String FactionUUID, String FactionOriginName) {
        String FactionName = FactionOriginName.toLowerCase(Locale.ROOT);

    }

    public void CreateFactionDTRAccount() {

    }

    public void CreateFactionBankAccount() {

    }

    public void AddNewFactionName(String FactionNameCapped, String FactionUUID) {
        String FactionName = FactionNameCapped.toLowerCase(Locale.ROOT);
        if(!Main.getInstance().sqlmanager.FactionNameExists(FactionName)) {
            try {
                PreparedStatement ps = Main.getInstance().sql.getConnection().prepareStatement("INSERT IGNORE INTO FactionName"
                        + " (FactionName,FactionUUID) VALUES (?,?)");
                ps.setString(1, FactionName);
                ps.setString(2, FactionUUID);
                ps.executeUpdate();
                PreparedStatement ps2 = Main.getInstance().sql.getConnection().prepareStatement("UPDATE FactionName" +
                        " SET FactionNameCap=? WHERE FactionUUID=?");
                ps2.setString(1, FactionNameCapped);
                ps2.setString(2, FactionUUID);
                ps2.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void DeleteFactionName(String FactionUUID) {
        try {
            PreparedStatement ps = Main.getInstance().sql.getConnection().prepareStatement("DELETE FROM FactionName WHERE FactionUUID=?");
            ps.setString(1, FactionUUID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
