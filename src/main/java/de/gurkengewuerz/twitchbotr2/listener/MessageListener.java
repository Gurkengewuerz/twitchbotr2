package de.gurkengewuerz.twitchbotr2.listener;

import de.lukweb.twitchchat.events.Handler;
import de.lukweb.twitchchat.events.Listener;
import de.lukweb.twitchchat.events.irc.IrcReceiveMessageEvent;
import de.lukweb.twitchchat.events.user.UserSendMessageEvent;
import de.lukweb.twitchchat.events.user.UserSendWhisperEvent;

/**
 * Created by gurkengewuerz.de on 21.12.2016.
 */
public class MessageListener extends Listener {

    @Handler
    public void onMessage(UserSendMessageEvent e) {

    }

    @Handler
    public void onWhisper(UserSendWhisperEvent e) {

    }

    @Handler
    public void onIRCMessage(IrcReceiveMessageEvent e) {
        // TwitchBotR2.getDashboardInstance().addChatMessage(""); // TODO: Replace at API Update with Message Object
        System.out.println(e.getMessage());
    }
}
