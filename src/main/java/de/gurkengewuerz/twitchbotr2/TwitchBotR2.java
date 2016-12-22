package de.gurkengewuerz.twitchbotr2;

import com.mb3364.twitch.api.Twitch;
import de.gurkengewuerz.twitchbotr2.database.DB;
import de.gurkengewuerz.twitchbotr2.gui.DashboardGUI;
import de.gurkengewuerz.twitchbotr2.listener.MessageListener;
import de.gurkengewuerz.twitchbotr2.object.ViewerList;
import de.lukweb.twitchchat.TwitchChannel;
import de.lukweb.twitchchat.TwitchChat;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by gurkengewuerz.de on 18.12.2016.
 */
public class TwitchBotR2 {

    private static TwitchChat twitchStreamerChat;
    private static TwitchChat twitchBotChat;
    private static Twitch twitchAPI;
    private static DashboardGUI dashboardInstance;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private static ViewerList viewerList = new ViewerList();

    public static void main(String[] args) {
        startRun();
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
            // UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            Logger.getLogger(TwitchBotR2.class.getName()).log(Level.SEVERE, null, e);
        }
        dashboardInstance = new DashboardGUI();
    }

    private static void startRun() {
        DB.get("main").queryUpdate(
                "CREATE TABLE IF NOT EXISTS `user` (" +
                        " `username` TEXT NOT NULL, " +
                        " `rank` INTEGER, " +
                        " `first_visit` INTEGER, " +
                        " `last_visit` INTEGER, " +
                        " PRIMARY KEY(username)" +
                        ")"
        );

        DB.get("main").queryUpdate(
                "CREATE TABLE IF NOT EXISTS `config` (" +
                        " `configname` TEXT NOT NULL," +
                        " `value` TEXT," +
                        " `description` TEXT, " +
                        " PRIMARY KEY(configname)" +
                        ");"
        );

        DB.get("main").queryUpdate(
                "CREATE TABLE IF NOT EXISTS `log` (" +
                        " `log_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        " `timestamp` INTEGER NOT NULL, " +
                        " `text` TEXT NOT NULL, " +
                        " `executer` TEXT NOT NULL" +
                        ");"
        );

        DB.get("main").queryUpdate(
                "CREATE TABLE IF NOT EXISTS `coin` (" +
                        " `useranme`\tTEXT NOT NULL," +
                        " `coin`\tINTEGER," +
                        " PRIMARY KEY(useranme)" +
                        ");"
        );

        DB.get("main").queryUpdate(
                "CREATE TABLE IF NOT EXISTS `command` (" +
                        " `command_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        " `command` TEXT NOT NULL, " +
                        " `activated` INTEGER NOT NULL DEFAULT 0, " +
                        " `response` TEXT NOT NULL" +
                        ");"
        );

        DB.get("main").queryUpdate(
                "CREATE TABLE IF NOT EXISTS `timer` (" +
                        " `timer_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                        " `name` TEXT NOT NULL, " +
                        " `response` TEXT NOT NULL, " +
                        " `last_time` INTEGER, " +
                        " `interval` INTEGER, " +
                        " `activated` INTEGER NOT NULL DEFAULT 0" +
                        ");"
        );

        startChat();
    }

    public static DashboardGUI getDashboardInstance() {
        return dashboardInstance;
    }

    public static void startChat() {
        if (Config.TWITCH_BOT_NAME.getEntry().toStr() != null && !Config.TWITCH_BOT_NAME.getEntry().toStr().isEmpty()) {
            if (Config.TWITCH_BOT_OAUTH.getEntry().toStr() != null && !Config.TWITCH_BOT_OAUTH.getEntry().toStr().isEmpty()) {
                twitchBotChat = TwitchChat.build(Config.TWITCH_BOT_NAME.getEntry().toStr(), Config.TWITCH_BOT_OAUTH.getEntry().toStr());
            }
        }

        if (Config.TWITCH_STREAMER_NAME.getEntry().toStr() != null && !Config.TWITCH_STREAMER_NAME.getEntry().toStr().isEmpty()) {
            if (Config.TWITCH_STREAMER_OAUTH.getEntry().toStr() != null && !Config.TWITCH_STREAMER_OAUTH.getEntry().toStr().isEmpty()) {
                twitchStreamerChat = TwitchChat.build(Config.TWITCH_STREAMER_NAME.getEntry().toStr(), Config.TWITCH_STREAMER_OAUTH.getEntry().toStr());
                twitchAPI = new Twitch();
                twitchAPI.setClientId("kplkvvjb4po9vff54xz6bvhc29dipbo");
                twitchAPI.auth().setAccessToken(Config.TWITCH_STREAMER_OAUTH.getEntry().toStr().replace("oauth:", ""));
            }
        }

        if(twitchStreamerChat != null){
            twitchStreamerChat.getEventManager().register(new MessageListener());
        } else if (twitchBotChat != null){
            twitchBotChat.getEventManager().register(new MessageListener());
        }
    }

    public static Twitch getTwitchAPI(){
        return twitchAPI;
    }

    public static TwitchChat getTwitchStreamerChat() {
        return twitchStreamerChat;
    }

    public static TwitchChannel getTwitchStreamerChannel() {
        return twitchStreamerChat.getChannel(Config.TWITCH_CHANNEL.getEntry().toStr());
    }

    public static TwitchChat getTwitchBotChat() {
        return twitchBotChat;
    }

    public static TwitchChannel getTwitchBotChannel() {
        return twitchBotChat.getChannel(Config.TWITCH_CHANNEL.getEntry().toStr());
    }

    public static ViewerList getViewerList() {
        return viewerList;
    }

    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }
}
