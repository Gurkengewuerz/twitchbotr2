package de.gurkengewuerz.twitchbotr2.object;

import de.gurkengewuerz.twitchbotr2.Error;
import de.gurkengewuerz.twitchbotr2.database.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by gurkengewuerz.de on 20.12.2016.
 */
public class ConfigEntry {
    private Object config;
    private String name;
    private Object defaults = "";
    private String description;

    public ConfigEntry(String name, Object defaults, String description) {
        this.name = name;
        this.defaults = defaults;
        this.description = description;
        DB.get("main").queryUpdate(
                "INSERT INTO config(configname, value, description) \n" +
                        "SELECT '" + name + "', '" + defaults + "', '" + description + "' " +
                        "WHERE NOT EXISTS(SELECT 1 FROM config WHERE configname = '" + name + "');"
        );

        get();
    }

    public Object set(Object value) {
        config = value;
        DB.get("main").queryUpdate(
                "UPDATE config SET value = '" + value + "' WHERE configname = '" + name + "';"
        );
        return config;
    }

    public Object get() {
        ResultSet rs = DB.get("main").querySelect("SELECT * FROM config WHERE configname = '" + name + "';");
        int rowCount = 0;
        try {
            while (rs.next()) {
                rowCount++;
                config = rs.getObject("value");
            }
        } catch (SQLException e) {
            Logger.getLogger(ConfigEntry.class.getName()).log(Level.SEVERE, null, e);
        }

        if (rowCount != 1) {
            new Error("Not enough Rows for Config Entry " + name).show();
        }
        return config;
    }

    public Object update() {
        return get();
    }

    public Integer toInt() {
        return (int) config;
    }

    public String toStr() {
        return (String) config;
    }

    public Boolean toBool() {
        return (Boolean) config;
    }

    public Object setDefault() {
        return set(defaults);
    }
}
