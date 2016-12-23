package de.gurkengewuerz.twitchbotr2.api.timer;

import de.gurkengewuerz.twitchbotr2.Config;
import de.gurkengewuerz.twitchbotr2.TwitchBotR2;
import de.gurkengewuerz.twitchbotr2.api.Utils;
import de.gurkengewuerz.twitchbotr2.object.Viewer;
import de.gurkengewuerz.twitchbotr2.object.ViewerList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by gurkengewuerz.de on 23.12.2016.
 */
public class ViewerRefreshTask implements Runnable {
    @Override
    public void run() {
        if (Config.TWITCH_CHANNEL.getEntry().toStr() == null) return;
        if (Config.TWITCH_CHANNEL.getEntry().toStr() == "") return;
        JSONObject obj = null;
        try {
            obj = Utils.readJsonFromUrl("https://tmi.twitch.tv/group/user/" + Config.TWITCH_CHANNEL.getEntry().toStr().toLowerCase() + "/chatters");
        } catch (IOException e) {
            Logger.getLogger(ViewerRefreshTask.class.getName()).log(Level.SEVERE, null, e);
        }
        if (obj == null) return;
        JSONArray array = Utils.concatArray(
                obj.getJSONObject("chatters").getJSONArray("viewers"),
                obj.getJSONObject("chatters").getJSONArray("global_mods"),
                obj.getJSONObject("chatters").getJSONArray("staff"),
                obj.getJSONObject("chatters").getJSONArray("admins"),
                obj.getJSONObject("chatters").getJSONArray("moderators")
        );

        ViewerList list = new ViewerList();
        for(int i = 0 ; i < array.length() ; i++){
            String name = array.getString(i);
            Viewer v = new Viewer(name);
            v.addWatchTime(1);
            list.add(v);
        }
        TwitchBotR2.setViewerList(list);

    }
}
