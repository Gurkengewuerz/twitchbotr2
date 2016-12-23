package de.gurkengewuerz.twitchbotr2.object;

import de.gurkengewuerz.twitchbotr2.database.DB;
import de.lukweb.twitchchat.twitch.TurboUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by gurkengewuerz.de on 18.12.2016.
 */
public class Viewer extends TurboUser {

    private int coin = 0;
    private int watch_time = 0;
    private int rank = 0;
    private long firstVisit = -1;
    private long lastVisit = -1;

    public Viewer(String name) {
        super(name);
        DB.get("main").queryUpdate(
                "INSERT INTO coin(username, coin) \n" +
                        "SELECT '" + name + "', '" + 0 + "' " +
                        "WHERE NOT EXISTS(SELECT 1 FROM coin WHERE username = '" + name + "');"
        );
        DB.get("main").queryUpdate(
                "INSERT INTO user(username, rank, watch_time, first_visit, last_visit) \n" +
                        "SELECT '" + name + "', '" + 0 + "', '" + 0 + "', '" + System.currentTimeMillis() + "', '" + System.currentTimeMillis() + "' " +
                        "WHERE NOT EXISTS(SELECT 1 FROM user WHERE username = '" + name + "');"
        );
        getfirstVisit();
    }

    public long getfirstVisit() {
        if (firstVisit < 0) {
            ResultSet rs = DB.get("main").querySelect("SELECT * from user WHERE username = '" + getName() + "'");
            try {
                while (rs.next()) {
                    firstVisit = rs.getInt("first_visit");
                    break;
                }
            } catch (SQLException e) {
                Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return firstVisit;
    }

    public long getLastVisit() {
        if (lastVisit < 0) {
            ResultSet rs = DB.get("main").querySelect("SELECT * from user WHERE username = '" + getName() + "'");
            try {
                while (rs.next()) {
                    lastVisit = rs.getInt("last_visit");
                    break;
                }
            } catch (SQLException e) {
                Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return lastVisit;
    }

    public void updateLastVisit() {
        DB.get("main").queryUpdate("UPDATE user SET last_visit = '" + System.currentTimeMillis() + "' WHERE username = '" + getName() + "'");
    }

    public void setRank(int rank) {
        DB.get("main").queryUpdate("UPDATE user SET rank = '" + rank + "' WHERE username = '" + getName() + "'");
    }

    public int getRank() {
        ResultSet rs = DB.get("main").querySelect("SELECT * from user WHERE username = '" + getName() + "'");
        try {
            while (rs.next()) {
                rank = rs.getInt("rank");
                break;
            }
        } catch (SQLException e) {
            Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, e);
        }
        return coin;
    }

    public void addCoins(int amount) {
        DB.get("main").queryUpdate("UPDATE coin SET coin = coin + '" + amount + "' WHERE username = '" + getName() + "'");
    }

    public int getCoins(int amount) {
        ResultSet rs = DB.get("main").querySelect("SELECT * from coin WHERE username = '" + getName() + "'");
        try {
            while (rs.next()) {
                coin = rs.getInt("coin");
                break;
            }
        } catch (SQLException e) {
            Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, e);
        }
        return coin;
    }

    public void addWatchTime(int amount) {
        DB.get("main").queryUpdate("UPDATE user SET watch_time = watch_time + '" + amount + "' WHERE username = '" + getName() + "'");
    }

    public int getWatchTime(int amount) {
        ResultSet rs = DB.get("main").querySelect("SELECT * from user WHERE username = '" + getName() + "'");
        try {
            while (rs.next()) {
                watch_time = rs.getInt("watch_time");
                break;
            }
        } catch (SQLException e) {
            Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, e);
        }
        return watch_time;
    }
}
