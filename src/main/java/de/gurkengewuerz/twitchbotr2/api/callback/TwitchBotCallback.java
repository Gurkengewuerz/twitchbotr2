package de.gurkengewuerz.twitchbotr2.api.callback;

/**
 * Created by gurkengewuerz.de on 21.12.2016.
 */
public interface TwitchBotCallback<Type> {
    public abstract void callback(Type cb);
}