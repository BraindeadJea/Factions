package com.itndev.factions.MySQL;

import com.itndev.factions.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateProcedures {
    public static void setupProcedures() {
        new Thread( () -> {
            /*try {
                //createbank
                PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("DELIMITER $$ " +
                        " CREATE PROCEDURE CREATEBANK(" +
                        "    VALUE_FactionUUID CHAR(100)," +
                        "    VALUE_FactionName CHAR(100)," +
                        "    VALUE_DEFAULTBANK CHAR(100)" +
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
                        "    VALUE_FactionUUID CHAR(100)," +
                        "    VALUE_FactionName CHAR(100)," +
                        "    VALUE_DEFAULTDTR CHAR(100)" +
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
            } */
            try {
                //updatename
                PreparedStatement ps = Main.hikariCP.getHikariConnection().prepareStatement("DELIMITER $$\n " +
                        "CREATE PROCEDURE CHECKNAME(\n" +
                        "\tVALUE_FactionName CHAR(100),\n" +
                        "    VALUE_FactionUUID CHAR(100),\n" +
                        "    VALUE_FactionCapName CHAR(100),\n" +
                        "    VALUE_Boolean DOUBLE\n" +
                        ")\n" +
                        "BEGIN\n" +
                        "\tIF EXISTS (SELECT * FROM FactionName WHERE FactionName=VALUE_FactionName) THEN\n" +
                        "\t\tSET VALUE_Boolean = 1;\n" +
                        "        SELECT VALUE_Boolean;\n" +
                        "    ELSE\n" +
                        "\t\tSET VALUE_Boolean = 0;\n" +
                        "        SELECT VALUE_Boolean;\n" +
                        "\tEND IF;\n" +
                        "END$$\n" +
                        "DELIMITER ;");
                ps.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
