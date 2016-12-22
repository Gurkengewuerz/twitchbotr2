package de.gurkengewuerz.twitchbotr2;

import de.gurkengewuerz.twitchbotr2.api.callback.TwitchBotCallback;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by gurkengewuerz.de on 21.12.2016.
 */
public class Error {
    private String message;
    private TwitchBotCallback callback;

    public Error(String message) {
        this.message = message;
    }

    public Error(String message, TwitchBotCallback callback) {
        this.message = message;
        this.callback = callback;
    }

    public Error(Throwable t) {
        this.message = getStackTrace(t);
    }

    public Error(Throwable t, TwitchBotCallback callback) {
        this.message = getStackTrace(t);
        this.callback = callback;
    }

    public void show() {
        EventQueue.invokeLater(() -> {
            int returnInt = JOptionPane.showConfirmDialog(null, message, "An Error occurred", JOptionPane.YES_NO_OPTION);
            if (callback != null && returnInt == 0) callback.callback(true);
        });
    }

    private String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
