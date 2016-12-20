package de.gurkengewuerz.twitchbotr2;

import de.gurkengewuerz.twitchbotr2.gui.DashboardGUI;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by gurkengewuerz.de on 18.12.2016.
 */
public class TwitchBotR2 {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
            // UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            Logger.getLogger(TwitchBotR2.class.getName()).log(Level.SEVERE, null, e);
        }
        new DashboardGUI();
    }

}
