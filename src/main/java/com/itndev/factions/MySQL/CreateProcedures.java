package com.itndev.factions.MySQL;

import com.itndev.factions.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateProcedures {
    public void setupProcedures() {
        new Thread( () -> {
            try {
                //createbank
                PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("DELIMITER $$ " +
                        " CREATE PROCEDURE CREATEBANK(" +
                        "    VALUE_FactionUUID CHAR," +
                        "    VALUE_FactionName CHAR," +
                        "    VALUE_DEFAULTBANK CHAR" +
                        " )" +
                        " BEGIN " +
                        "    INSERT IGNORE INTO FactionBank (FactionUUID,FactionName) VALUES (VALUE_FactionUUID, VALUE_FactionName);" +
                        "    UPDATE FactionBank SET FactionBank=VALUE_DEFAULTBANK WHERE FactionUUID=VALUE_FactionUUID;" +
                        " END$$ " +
                        " DELIMITER ;");
                ps.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                //updatebank (reverse the num to add)
                PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("DELIMITER $$" +
                        " CREATE PROCEDURE UPDATEBANK(" +
                        "        ORIGINALBANK DOUBLE," +
                        "        TAKEFROMBANK DOUBLE," +
                        "        FINALBANK DOUBLE" +
                        "        VALUE_FactionUUID CHAR(100)" +
                        "    )" +
                        " BEGIN" +
                        "    SET ORIGINALBANK = CONVERT((SELECT FactionBank FROM FactionBank WHERE FactionUUID=VALUE_FactionUUID), DOUBLE);" +
                        "    IF ORIGINALBANK > TAKEFROMBANK THEN" +
                        "        SET FINALBANK = ORIGINALBANK - TAKEFROMBANK;" +
                        "        UPDATE FactionBank SET FactionBank=CONVERT(FINALBANK, CHAR) WHERE FactionUUID=VALUE_FactionUUID;" +
                        "        SELECT FINALBANK;" +
                        "    ELSE" +
                        "        SET FINALBANK = -1;" +
                        "        SELECT FINALBANK;" +
                        "    END IF;" +
                        " END$$" +
                        " DELIMITER ;");
                ps.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                //createdtr
                PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("DELIMITER $$" +
                        " CREATE PROCEDURE CREATEDTR(" +
                        "    VALUE_FactionUUID CHAR," +
                        "    VALUE_FactionName CHAR," +
                        "    VALUE_DEFAULTDTR CHAR" +
                        " )" +
                        " BEGIN" +
                        "    INSERT IGNORE INTO FactionDTR (FactionUUID,FactionName) VALUES (VALUE_FactionUUID, VALUE_FactionName);" +
                        "    UPDATE FactionDTR SET FactionDTR=VALUE_DEFAULTDTR WHERE FactionUUID=VALUE_FactionUUID;" +
                        " END$$" +
                        " DELIMITER ;");
                ps.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                //updatedtr
                PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("DELIMITER $$" +
                        " CREATE PROCEDURE UPDATEDTR(" +
                        "        ORIGINALDTR DOUBLE," +
                        "        TAKEFROMDTR DOUBLE," +
                        "        FINALDTR DOUBLE," +
                        "        VALUE_FactionUUID CHAR(100)" +
                        "    )" +
                        " BEGIN" +
                        "    SET ORIGINALDTR = CONVERT((SELECT FactionDTR FROM FactionDTR WHERE FactionUUID=VALUE_FactionUUID), DOUBLE);" +
                        "    IF ORIGINALDTR > TAKEFROMDTR THEN" +
                        "        SET FINALDTR = ORIGINALDTR - TAKEFROMDTR;" +
                        "        UPDATE FactionDTR SET FactionDTR=CONVERT(FINALDTR, CHAR) WHERE FactionUUID=VALUE_FactionUUID;" +
                        "        SELECT FINALDTR;" +
                        "    ELSE" +
                        "        SET FINALDTR = -404;" +
                        "        SELECT FINALDTR;" +
                        "    END IF;" +
                        " END$$" +
                        " DELIMITER ;");
                ps.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                //createname
                PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("DELIMITER $$" +
                    " CREATE PROCEDURE CREATENAME(" +
                    "    VALUE_FactionName CHAR(100)," +
                    "    VALUE_FactionUUID CHAR(100)," +
                    "    VALUE_FactionNameCap CHAR(100)" +
                    " )" +
                    " BEGIN" +
                    "    INSERT IGNORE INTO FactionName (FactionName,FactionUUID) VALUES (VALUE_FactionName,VALUE_FactionUUID);" +
                    "    UPDATE FactionName SET FactionNameCap=VALUE_FactionNameCap WHERE FactionUUID=VALUE_FactionUUID;" +
                    " END$$" +
                    " DELIMITER ;");
                ps.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                //updatename
                PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement(" DELIMITER $$" +
                        " CREATE PROCEDURE UPDATENAME(" +
                        "     VALUE_FactionName CHAR(100)," +
                        "     VALUE_FactionUUID CHAR(100)," +
                        "     VALUE_FactionNameCap CHAR(100)" +
                        " )" +
                        " BEGIN" +
                        "     UPDATE FactionName SET FactionName=VALUE_FactionName WHERE FactionUUID=VALUE_FactionUUID;" +
                        "     UPDATE FactionName SET FactionNameCap=VALUE_FactionNameCap WHERE FactionUUID=VALUE_FactionUUID;" +
                        " END$$" +
                        " DELIMITER ;");
                ps.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
