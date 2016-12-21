package de.gurkengewuerz.twitchbotr2.database;

import org.apache.commons.lang3.Validate;

import java.io.File;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by gurkengewuerz.de on 20.12.2016.
 */
public class SQLite {
    private File sqliteFile;
    private Connection con;
    private final Logger log = Logger.getLogger(SQLite.class.getName());

    public SQLite(File sqliteFile) {
        this.sqliteFile = sqliteFile;
        Validate.notNull(sqliteFile);
        try {
            con = DriverManager.getConnection("jdbc:sqlite:" + sqliteFile.getPath());
        } catch (SQLException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public boolean hasConnection() {
        try {
            return con != null && con.isValid(1);
        } catch (SQLException e) {
            return false;
        }
    }

    public void queryUpdate(String query, Object... args) {
        Connection connLoc = con;
        PreparedStatement st = null;
        try {
            st = connLoc.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            for (Object o : args) {
                st.setObject(i, o);
                i++;
            }
            st.executeUpdate();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Failed to send update: {0} - {1}", new Object[]{query, e.getLocalizedMessage()});
        }
        closeRessources(st);
    }

    public ResultSet querySelect(String query, Object... args) {
        try {
            PreparedStatement st = con.prepareStatement(query);
            int i = 1;
            for (Object o : args) {
                st.setObject(i, o);
                i++;
            }
            return querySelect(st);
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "Error trying to build Prepared Statement", ex);
        }
        return null;
    }

    private ResultSet querySelect(PreparedStatement st) {
        ResultSet rs;
        try {
            rs = st.executeQuery();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Failed to send SELECT query: " + st.toString(), e);
            return null;
        }
        return rs;
    }

    private void closeRessources(PreparedStatement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException ignored) {
            }
        }
    }
}
