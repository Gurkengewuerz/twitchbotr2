package de.gurkengewuerz.twitchbotr2;

import de.gurkengewuerz.twitchbotr2.object.ConfigEntry;

/**
 * Created by gurkengewuerz.de on 20.12.2016.
 */
public enum  Config {
    TWITCH_STREAMER_NAME(new ConfigEntry("TWITCH_STREAMER_NAME", "", "String: Streamer Twitch Name")),
    TWITCH_STREAMER_OAUTH(new ConfigEntry("TWITCH_STREAMER_OAUTH", "", "String: Streamer OAuth Key")),
    TWITCH_BOT_NAME(new ConfigEntry("TWITCH_BOT_NAME", "", "String: Bot Twitch Name")),
    TWITCH_BOT_OAUTH(new ConfigEntry("TWITCH_BOT_OAUTH", "", "String: Bot OAuth Key")),
    TWITCH_CHANNEL(new ConfigEntry("TWITCH_CHANNEL", "gurkengewuerz", "String: Channel where to Join (withour #)")),
    GUI_THEME(new ConfigEntry("THEME", "dark", "String: dark/ light Theme")),

    ;

    private ConfigEntry entry;
    Config(ConfigEntry entry){
        this.entry = entry;
    }

    public ConfigEntry getEntry() {
        return entry;
    }
}