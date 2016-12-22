package de.gurkengewuerz.twitchbotr2.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.mb3364.http.RequestParams;
import com.mb3364.twitch.api.handlers.ChannelResponseHandler;
import com.mb3364.twitch.api.handlers.CommercialResponseHandler;
import com.mb3364.twitch.api.handlers.GamesResponseHandler;
import com.mb3364.twitch.api.models.Channel;
import com.mb3364.twitch.api.models.Game;
import de.gurkengewuerz.twitchbotr2.Config;
import de.gurkengewuerz.twitchbotr2.Error;
import de.gurkengewuerz.twitchbotr2.TwitchBotR2;
import de.gurkengewuerz.twitchbotr2.object.Viewer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by gurkengewuerz.de on 18.12.2016.
 */
public class DashboardGUI {
    private JPanel mainPane;
    private JTabbedPane paneSites;
    private JPanel configPane;
    private JPanel consolePane;
    private JPanel userDatabasePane;
    private JPanel commandsPane;
    private JPanel musicPane;
    private JPanel giveawayPane;
    private JButton DEBUGButton;
    private JList viewerList;
    private JComboBox sendUser;
    private JTextField sendMessage;
    private JButton sendButton;
    private JList chatList;
    private JScrollPane dashboardPane;
    private JButton button1;
    private JTextField channelStatus;
    private JComboBox gameList;
    private JButton updateButton;
    private JButton a30SecButton;
    private JButton a60SecButton;
    private JButton a90SecButton;
    private JButton a120SecButton;
    private JButton a150SecButton;
    private JButton a180SecButton;
    private JPanel creditsPane;
    private JTextPane credits;
    private JButton button2;
    private JList list1;
    private JButton button3;
    private JTextField textField2;
    private JButton addChannelButton;
    private JButton startAutomatedHostingButton;
    private JButton botLoginButton;
    private JButton streamerLoginButton;
    private JScrollPane filterPane;
    private JTextPane botLoginCurrent;
    private JTextPane streamerLoginCurrent;
    private JPanel commercialPane;
    private long lastKeyPressed;

    public DashboardGUI() {
        JFrame frame = new JFrame("DashboardGUI");
        frame.setContentPane(mainPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        setPanelEnabled(commercialPane, false);
        updateLogin();
        updateDashboard();

        DEBUGButton.addActionListener(e -> {
            TwitchBotR2.startChat();
        });
        botLoginButton.addActionListener(e -> new LoginGUI(cb -> {
            List<String> callback = (List<String>) cb;
            Config.TWITCH_BOT_NAME.getEntry().set(callback.get(0));
            Config.TWITCH_BOT_OAUTH.getEntry().set(callback.get(1));
            updateLogin();
        }));

        streamerLoginButton.addActionListener(e -> new LoginGUI(cb -> {
            List<String> callback = (List<String>) cb;
            Config.TWITCH_STREAMER_NAME.getEntry().set(callback.get(0));
            Config.TWITCH_STREAMER_OAUTH.getEntry().set(callback.get(1));
            updateLogin();
        }));

        // TODO: Message History for chatList

        //        http://stackoverflow.com/a/12479235/5605489
        viewerList.setListData(new Vector<>(TwitchBotR2.getViewerList().toList()));
        viewerList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof Viewer) {
                    ((JLabel) renderer).setText(((Viewer) value).getName());
                }
                return renderer;
            }
        });

        sendButton.addActionListener(e -> {
            String msg = sendMessage.getText();
            System.out.println(sendUser.getSelectedIndex());
            switch (sendUser.getSelectedIndex()) {
                case 0:
                    if (TwitchBotR2.getTwitchBotChat() == null) {
                        new Error("Bot not Logged in!\r\nClick OK to reconnect.", cb -> TwitchBotR2.startChat()).show();
                        return;
                    }
                    TwitchBotR2.getTwitchBotChannel().sendMessage(msg);
                    sendMessage.setText("");
                    break;

                case 1:
                    if (TwitchBotR2.getTwitchStreamerChat() == null) {
                        new Error("Streamer not Logged in!\r\nClick OK to reconnect.", cb -> TwitchBotR2.startChat()).show();
                        return;
                    }
                    TwitchBotR2.getTwitchStreamerChannel().sendMessage(msg);
                    sendMessage.setText("");
                    break;
            }
        });
        paneSites.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                updateLogin();
            }
        });

        updateButton.addActionListener(e -> {
            if (TwitchBotR2.getTwitchAPI() == null) return;
            if (Config.TWITCH_CHANNEL.getEntry().toStr() == null && Config.TWITCH_STREAMER_NAME.getEntry().toStr() == null)
                return;
            if (Config.TWITCH_STREAMER_NAME.getEntry().toStr() != Config.TWITCH_STREAMER_NAME.getEntry().toStr())
                return;
            RequestParams params = new RequestParams();
            params.put("status", channelStatus.getText());
            params.put("game", (String) gameList.getEditor().getItem());
            TwitchBotR2.getTwitchAPI().channels().put(Config.TWITCH_STREAMER_NAME.getEntry().toStr(), params, new ChannelResponseHandler() {
                @Override
                public void onSuccess(Channel channel) {
                    // Success - Channel Object updated
                }

                @Override
                public void onFailure(int i, String statusMessage, String errorMessage) {
                    new Error("Twitch denied Request (" + i + "):\r\n" + statusMessage + "\r\n" + errorMessage).show();
                }

                @Override
                public void onFailure(Throwable throwable) {
                    new Error(throwable).show();
                }
            });
        });

        button1.addActionListener(e -> updateDashboard());
        a30SecButton.addActionListener(e -> sendCommercal(30));
        a60SecButton.addActionListener(e -> sendCommercal(30));
        a90SecButton.addActionListener(e -> sendCommercal(30));
        a120SecButton.addActionListener(e -> sendCommercal(30));
        a150SecButton.addActionListener(e -> sendCommercal(30));
        a180SecButton.addActionListener(e -> sendCommercal(30));

        Thread numThread = new Thread(() -> {
            while (true) {
                if ((System.currentTimeMillis() / 1000) - lastKeyPressed >= 2) {
                    TwitchBotR2.getTwitchAPI().search().games((String) gameList.getEditor().getItem(), new GamesResponseHandler() {
                        @Override
                        public void onSuccess(int i, List<Game> list) {
                            for (Game game : list) {
                                gameList.addItem(game.getName());
                            }
                        }

                        @Override
                        public void onFailure(int i, String statusMessage, String errorMessage) {
                            new Error("Twitch denied Request (" + i + "):\r\n" + statusMessage + "\r\n" + errorMessage).show();
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            new Error(throwable).show();
                        }
                    });
                    break;
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Logger.getLogger(DashboardGUI.class.getName()).log(Level.SEVERE, null, e);
                    new Error(e).show();
                }
            }
        });
        gameList.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                lastKeyPressed = System.currentTimeMillis() / 1000;
                if (!numThread.isAlive()) {
                    numThread.start();
                }
            }
        });
    }

    public void updateDashboard() {
        if (TwitchBotR2.getTwitchAPI() == null) return;
        if (Config.TWITCH_CHANNEL.getEntry().toStr() == null && Config.TWITCH_STREAMER_NAME.getEntry().toStr() == null)
            return;
        TwitchBotR2.getTwitchAPI().channels().get(Config.TWITCH_STREAMER_NAME.getEntry().toStr(), new ChannelResponseHandler() {
            @Override
            public void onSuccess(Channel channel) {
                if (channel.isMature() && (Config.TWITCH_STREAMER_NAME.getEntry().toStr() == Config.TWITCH_STREAMER_NAME.getEntry().toStr()))
                    setPanelEnabled(commercialPane, true);
                gameList.removeAllItems();
                channelStatus.setText(channel.getStatus());
                int index = -1;
                for (int i = 0; i < gameList.getItemCount(); i++) {
                    String item = (String) gameList.getItemAt(i);
                    if (channel.getGame().equals(item)) {
                        index = i;
                        break;
                    }
                }

                if (index == -1) {
                    gameList.addItem(channel.getGame());
                    index = gameList.getItemCount() - 1;
                }

                gameList.setSelectedIndex(index);
            }

            @Override
            public void onFailure(int i, String statusMessage, String errorMessage) {
                new Error("Twitch denied Request (" + i + "):\r\n" + statusMessage + "\r\n" + errorMessage).show();
            }

            @Override
            public void onFailure(Throwable throwable) {
                new Error(throwable).show();
            }
        });
    }

    public void sendCommercal(int length) {
        if (TwitchBotR2.getTwitchAPI() == null) return;
        if (Config.TWITCH_CHANNEL.getEntry().toStr() == null && Config.TWITCH_STREAMER_NAME.getEntry().toStr() == null)
            return;
        if (Config.TWITCH_STREAMER_NAME.getEntry().toStr() != Config.TWITCH_STREAMER_NAME.getEntry().toStr())
            return;
        TwitchBotR2.getTwitchAPI().channels().startCommercial(Config.TWITCH_STREAMER_NAME.getEntry().toStr(), length, new CommercialResponseHandler() {
            @Override
            public void onSuccess() {
                // Success
            }

            @Override
            public void onFailure(int i, String statusMessage, String errorMessage) {
                new Error("Twitch denied Request (" + i + "):\r\n" + statusMessage + "\r\n" + errorMessage).show();
            }

            @Override
            public void onFailure(Throwable throwable) {
                new Error(throwable).show();
            }
        });
    }

    public void updateLogin() {
        botLoginCurrent.setText(
                "Current Bot Data:\r\n" +
                        "  Username: " + Config.TWITCH_BOT_NAME.getEntry().toStr() + "\r\n" +
                        "  OAUTH-Key: " + Config.TWITCH_BOT_OAUTH.getEntry().toStr() + "\r\n" +
                        "  Channel: #" + Config.TWITCH_CHANNEL.getEntry().toStr() + "\r\n"
        );

        streamerLoginCurrent.setText(
                "Current Streamer Data:\r\n" +
                        "  Username: " + Config.TWITCH_STREAMER_NAME.getEntry().toStr() + "\r\n" +
                        "  OAUTH-Key: " + Config.TWITCH_STREAMER_OAUTH.getEntry().toStr() + "\r\n" +
                        "  Channel: #" + Config.TWITCH_CHANNEL.getEntry().toStr() + "\r\n"
        );
    }

    private void setPanelEnabled(JPanel panel, Boolean isEnabled) {
        panel.setEnabled(isEnabled);

        Component[] components = panel.getComponents();

        for (Component component : components) {
            if (component.getClass().getName() == "javax.swing.JPanel") {
                setPanelEnabled((JPanel) component, isEnabled);
            }

            component.setEnabled(isEnabled);
        }
    }

    public void addChatMessage(String msg) {

    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPane = new JPanel();
        mainPane.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        paneSites = new JTabbedPane();
        mainPane.add(paneSites, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(875, 415), null, null, 0, false));
        configPane = new JPanel();
        configPane.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        paneSites.addTab("Config", configPane);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        configPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), "Login Credentials", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(panel1.getFont().getName(), panel1.getFont().getStyle(), 14)));
        streamerLoginButton = new JButton();
        streamerLoginButton.setIcon(new ImageIcon(getClass().getResource("/lock_16x16.png")));
        streamerLoginButton.setText("Streamer Login");
        panel1.add(streamerLoginButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        botLoginButton = new JButton();
        botLoginButton.setIcon(new ImageIcon(getClass().getResource("/lock_16x16.png")));
        botLoginButton.setText("Bot Login");
        panel1.add(botLoginButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Bot:");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Streamer:");
        panel1.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        botLoginCurrent = new JTextPane();
        panel1.add(botLoginCurrent, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 100), new Dimension(150, 100), null, 0, false));
        streamerLoginCurrent = new JTextPane();
        panel1.add(streamerLoginCurrent, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 100), new Dimension(150, 100), null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        configPane.add(spacer2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        DEBUGButton = new JButton();
        DEBUGButton.setText("DEBUG");
        DEBUGButton.setToolTipText("Debug Tools");
        configPane.add(DEBUGButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        consolePane = new JPanel();
        consolePane.setLayout(new GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        paneSites.addTab("Console", consolePane);
        viewerList = new JList();
        consolePane.add(viewerList, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final Spacer spacer3 = new Spacer();
        consolePane.add(spacer3, new GridConstraints(1, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        sendUser = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Bot");
        defaultComboBoxModel1.addElement("Streamer");
        sendUser.setModel(defaultComboBoxModel1);
        consolePane.add(sendUser, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sendMessage = new JTextField();
        consolePane.add(sendMessage, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        sendButton = new JButton();
        sendButton.setIcon(new ImageIcon(getClass().getResource("/send_16x16.png")));
        sendButton.setText("Send");
        consolePane.add(sendButton, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chatList = new JList();
        chatList.setLayoutOrientation(2);
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        defaultListModel1.addElement("please Login");
        chatList.setModel(defaultListModel1);
        chatList.setSelectionMode(0);
        consolePane.add(chatList, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        dashboardPane = new JScrollPane();
        dashboardPane.setVerticalScrollBarPolicy(20);
        paneSites.addTab("Dashboard", dashboardPane);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        dashboardPane.setViewportView(panel2);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), "Title & Game", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(panel3.getFont().getName(), panel3.getFont().getStyle(), 14)));
        button1 = new JButton();
        button1.setIcon(new ImageIcon(getClass().getResource("/refresh_16x16.png")));
        button1.setText("");
        panel3.add(button1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Title:");
        panel3.add(label3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        channelStatus = new JTextField();
        panel3.add(channelStatus, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Game:");
        panel3.add(label4, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gameList = new JComboBox();
        gameList.setEditable(true);
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("Creative");
        defaultComboBoxModel2.addElement("Gaming Talkshow");
        defaultComboBoxModel2.addElement("Minecraft");
        gameList.setModel(defaultComboBoxModel2);
        panel3.add(gameList, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        updateButton = new JButton();
        updateButton.setIcon(new ImageIcon(getClass().getResource("/send_16x16.png")));
        updateButton.setText("Update");
        panel3.add(updateButton, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        commercialPane = new JPanel();
        commercialPane.setLayout(new GridLayoutManager(1, 7, new Insets(0, 0, 0, 0), -1, -1));
        commercialPane.setEnabled(true);
        panel2.add(commercialPane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        commercialPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), "Commercial", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(commercialPane.getFont().getName(), commercialPane.getFont().getStyle(), 14)));
        a30SecButton = new JButton();
        a30SecButton.setText("30 Sec.");
        commercialPane.add(a30SecButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        commercialPane.add(spacer4, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        a60SecButton = new JButton();
        a60SecButton.setText("60 Sec.");
        commercialPane.add(a60SecButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        a90SecButton = new JButton();
        a90SecButton.setText("90 Sec.");
        commercialPane.add(a90SecButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        a120SecButton = new JButton();
        a120SecButton.setText("120 Sec.");
        commercialPane.add(a120SecButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        a150SecButton = new JButton();
        a150SecButton.setText("150 Sec.");
        commercialPane.add(a150SecButton, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        a180SecButton = new JButton();
        a180SecButton.setText("180 Sec.");
        commercialPane.add(a180SecButton, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(3, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), "Auto Hosting", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(panel4.getFont().getName(), panel4.getFont().getStyle(), 14)));
        button2 = new JButton();
        button2.setIcon(new ImageIcon(getClass().getResource("/arrowUp_16x16.png")));
        button2.setText("");
        panel4.add(button2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel4.add(spacer5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        list1 = new JList();
        panel4.add(list1, new GridConstraints(0, 1, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        button3 = new JButton();
        button3.setIcon(new ImageIcon(getClass().getResource("/arrowUp_16x16.png")));
        button3.setText("");
        panel4.add(button3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Channel:");
        panel4.add(label5, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textField2 = new JTextField();
        panel4.add(textField2, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel4.add(spacer6, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        addChannelButton = new JButton();
        addChannelButton.setIcon(new ImageIcon(getClass().getResource("/add_16x16.png")));
        addChannelButton.setText("Add Channel");
        panel4.add(addChannelButton, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        startAutomatedHostingButton = new JButton();
        startAutomatedHostingButton.setText("Start Automated Hosting");
        panel4.add(startAutomatedHostingButton, new GridConstraints(2, 2, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel2.add(spacer7, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        userDatabasePane = new JPanel();
        userDatabasePane.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        paneSites.addTab("User Database", userDatabasePane);
        commandsPane = new JPanel();
        commandsPane.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        paneSites.addTab("Commands", commandsPane);
        musicPane = new JPanel();
        musicPane.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        paneSites.addTab("Music", musicPane);
        giveawayPane = new JPanel();
        giveawayPane.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        paneSites.addTab("Giveaway", giveawayPane);
        filterPane = new JScrollPane();
        paneSites.addTab("Message Filter", filterPane);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        filterPane.setViewportView(panel5);
        creditsPane = new JPanel();
        creditsPane.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        paneSites.addTab("Credits", creditsPane);
        credits = new JTextPane();
        credits.setContentType("text/html");
        credits.setEditable(false);
        credits.setText("<html>\n  <head>\n    \n  </head>\n  <body>\n    <b>About:</b> Creator: Gurkengeuwerz | Twitter: twitter.com/Gurkengeuwerz \n    | Website: gurkengewuerz.de <b>Credits:</b>\n\n    <div>\n      Icons made by <a href=\"http://www.flaticon.com/authors/pixel-buddha\" title=\"Pixel Buddha\">Pixel \n      Buddha</a> from <a href=\"http://www.flaticon.com\" title=\"Flaticon\">www.flaticon.com</a>\n    </div>\n    <div>\n      Icons made by <a href=\"http://www.flaticon.com/authors/google\" title=\"Google\">Google</a> \n      from <a href=\"http://www.flaticon.com\" title=\"Flaticon\">www.flaticon.com</a>\n    </div>\n    <div>\n      Icons made by <a href=\"http://www.flaticon.com/authors/dave-gandy\" title=\"Dave Gandy\">Dave \n      Gandy</a> from <a href=\"http://www.flaticon.com\" title=\"Flaticon\">www.flaticon.com</a>\n    </div>\n    <a href=\"http://www.flaticon.com\" title=\"Flaticon\">www.flaticon.com</a> is \n    licensed by <a href=\"http://creativecommons.org/licenses/by/3.0/\" title=\"Creative Commons BY 3.0\" target=\"_blank\">CC \n    3.0 BY</a>\n  </body>\n</html>\n");
        creditsPane.add(credits, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPane;
    }
}
