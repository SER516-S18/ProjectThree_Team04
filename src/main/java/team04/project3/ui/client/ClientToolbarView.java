package team04.project3.ui.client;

import team04.project3.constants.ColorConstants;
import team04.project3.constants.TextConstants;
import team04.project3.listeners.ClientListener;
import team04.project3.model.client.ClientModel;
import team04.project3.model.server.ServerModel;
import team04.project3.ui.server.ServerView;
import team04.project3.util.Log;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * UI toolbar for the ClientView
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ClientToolbarView extends JMenuBar {
    private JMenu menu;
    private JMenuItem menuItemStateChange;
    private JButton buttonStatus;
    private JLabel textTime;

    /**
     * Constructor for ClientToolbarView, the toolbar at the top of the ClientView
     */
    public ClientToolbarView() {
        ClientModel.get().addListener(new ClientListener() {
            @Override
            public void valuesChanged() {
                textTime.setText(" " + (ClientModel.get().getNewestPacket() == null ? "0.0" : Float.toString(ClientModel.get().getNewestPacket().getTick())));
            }

            @Override
            public void valuesReset() { }

            @Override
            public void valuesAdded() { }

            @Override
            public void started() {
                menuItemStateChange.setText("Disconnect from server");
            }

            @Override
            public void shutdown() {
                menuItemStateChange.setText("Connect to server");
            }
        });

        JMenuItem menuItem;

        this.add(Box.createHorizontalStrut(8));

        menu = new JMenu(Character.toString((char) 0x2630));
        menu.setFont(TextConstants.LARGE_FONT);
        this.add(menu);

        menuItemStateChange = new JMenuItem(ClientModel.get().isConnected() ? "Disconnect from server" : "Connect to server");
        menuItemStateChange.addActionListener(e -> handleConnectToServer());
        menu.add(menuItemStateChange);

        menuItem = new JMenuItem("Change server host/port");
        menuItem.addActionListener(e -> displayChangeHostDialog());
        menu.add(menuItem);

        menu.addSeparator();

        menuItem = new JMenuItem("Open composer (server)");
        menuItem.addActionListener(e -> {
            openServerPanel();
        });
        menu.add(menuItem);

        this.add(Box.createHorizontalStrut(32));

        JLabel promptStatus = new JLabel("Status:");
        promptStatus.setFont(TextConstants.DEFAULT_FONT);
        this.add(promptStatus);

        buttonStatus = new JButton(Character.toString((char) 0x2022));
        buttonStatus.setFont(new Font("Monospaced", Font.PLAIN, TextConstants.DEFAULT_FONT.getSize() * 2));
        buttonStatus.setOpaque(false);
        buttonStatus.setContentAreaFilled(false);
        buttonStatus.setBorderPainted(false);
        buttonStatus.setFocusPainted(false);
        buttonStatus.setForeground(ColorConstants.INDICATOR_OFF);
        buttonStatus.addActionListener(e -> handleConnectToServer());
        this.add(buttonStatus);

        // Create a timer to blink if on or off
        Timer timer = new Timer(1000, e2 -> {
            if(ClientModel.get().isConnected()) {
                if(buttonStatus.getForeground() == ColorConstants.INDICATOR_ON_DIM) {
                    buttonStatus.setForeground(ColorConstants.INDICATOR_ON_BRIGHT);
                } else {
                    buttonStatus.setForeground(ColorConstants.INDICATOR_ON_DIM);
                }
            } else {
                buttonStatus.setForeground(ColorConstants.INDICATOR_OFF);
            }
        });
        timer.start();

        this.add(Box.createHorizontalStrut(32));

        textTime = new JLabel(" " + (ClientModel.get().getNewestPacket() == null ? "0.0" : Float.toString(ClientModel.get().getNewestPacket().getTick())));
        textTime.setIcon(new ImageIcon("src/main/resources/team04/project3/images/clock.jpeg"));
        textTime.setForeground(Color.BLACK);
        textTime.setFont(TextConstants.LARGE_FONT);
        this.add(textTime);
    }

    /**
     * Prompts the user to input a new host/port input
     */
    private void displayChangeHostDialog() {
        JTextField textfieldHost = new JTextField();
        JTextField textfieldPort = new JTextField();
        Object[] message = {
                "Host: ", textfieldHost,
                "Port: ", textfieldPort
        };
        int option = JOptionPane.showConfirmDialog(this, message, "Change host", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String host = textfieldHost.getText();
            int port = Integer.parseInt(textfieldPort.getText());

            if (host.equalsIgnoreCase("localhost")) {
                ClientModel.get().setHostToLocalhost();
                ClientModel.get().setPort(port);
            } else {
                ClientModel.get().setHost(InetAddress.getByName(host));
                ClientModel.get().setPort(port);
            }
        } catch(NumberFormatException e) {
            Log.e("Invalid port specified (Must be numeric)", ClientToolbarView.class);
        } catch(UnknownHostException e) {
            Log.e("Invalid host specified (" + e.getMessage() + ")", ClientToolbarView.class);
        } catch(IllegalArgumentException e) {
            Log.e("Invalid port specified (" + e.getMessage() + ")", ClientToolbarView.class);
        }
    }

    /**
     * Connects to the server or disconnects if connected
     */
    private void handleConnectToServer() {
        if(ClientModel.get().isRunning()) {
            ClientModel.get().disconnect();
            try {
                long timeout = System.currentTimeMillis() + 1000L;
                while (ClientModel.get().isRunning() && System.currentTimeMillis() < timeout) {
                    Thread.sleep(100L);
                }
            } catch(InterruptedException e) {
                Log.w("Failed to sleep while disconnecting (" + e.getMessage() + ")", ClientToolbarView.class);
            }
        } else {
            ClientModel.get().start();
            try {
                long timeout = System.currentTimeMillis() + 1000L;
                while (!ClientModel.get().isRunning() && System.currentTimeMillis() < timeout) {
                    Thread.sleep(100L);
                }
            } catch(InterruptedException e) {
                Log.w("Failed to sleep while connecting (" + e.getMessage() + ")", ClientToolbarView.class);
            }
        }
    }

    /**
     * Opens the server composer UI, if not open, otherwise refocuses it. Attempts connection if starting up server.
     */
    public void openServerPanel() {
        if(ServerView.getInstance().isDisplayable()) {
            ServerView.getInstance().toFront();
            ServerView.getInstance().repaint();
        } else {
            ServerView.getInstance().init();
            int x = ClientView.getInstance().getX() - (ClientView.getInstance().getWidth() / 2) - (ServerView.getInstance().getWidth() / 2);
            if (x < ServerView.getInstance().getWidth() / 2) {
                x = ClientView.getInstance().getX() + (ClientView.getInstance().getWidth() / 2) + (ServerView.getInstance().getWidth() / 2);
            }
            if(x > Toolkit.getDefaultToolkit().getScreenSize().width - ServerView.getInstance().getWidth() / 2) {
                x = ClientView.getInstance().getX();
            }

            ServerView.getInstance().setLocation(
                    x, ClientView.getInstance().getLocation().getLocation().y
            );
        }

        // Try to connect the client in a moment
        new Thread(() -> {
            try {
                long timeout = System.currentTimeMillis() + 2000L;
                while(System.currentTimeMillis() < timeout && !ServerModel.get().isRunning()) {
                    Thread.sleep(100L);
                }
            } catch(InterruptedException e) {
                Log.w("Failed to wait while server starts up (" + e.getMessage() + ")", ClientToolbarView.class);
            }

            if(!ClientModel.get().isRunning())
                ClientModel.get().start();
        }).start();
    }
}
