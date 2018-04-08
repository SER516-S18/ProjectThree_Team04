package team04.project3.ui.server;

import team04.project3.model.server.ServerModel;

import javax.swing.*;
import java.awt.*;

/**
 * The main UI for the server application
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ServerView extends JFrame {
    private static ServerView instance;

    public static synchronized ServerView getInstance() {
        if(instance == null)
            instance = new ServerView();

        return instance;
    }

    private ServerToolbarView panelToolbar;
    private ServerTransmitView panelTransmit;
    private ServerValuesView panelValues;

    private ServerView() {
        // Start the server model if it isn't running
        if(!ServerModel.get().isRunning())
            ServerModel.get().start();

        this.init();
    }

    public void init() {
        this.setTitle("Emotiv Composer");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setMinimumSize(new Dimension(600, 800));

        this.setLayout(new BorderLayout());

        panelToolbar = new ServerToolbarView();
        this.add(panelToolbar, BorderLayout.PAGE_START);

        panelTransmit = new ServerTransmitView();
        this.add(panelTransmit, BorderLayout.CENTER);

        panelValues = new ServerValuesView();
        this.add(panelValues, BorderLayout.PAGE_END);

        this.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }
}
