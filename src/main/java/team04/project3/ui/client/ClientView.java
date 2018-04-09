package team04.project3.ui.client;

import team04.project3.model.client.ClientModel;
import team04.project3.model.server.ServerModel;
import team04.project3.ui.server.ServerToolbarView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Main UI for the client application
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ClientView extends JFrame {
    private static ClientView instance;

    public static ClientView getInstance() {
        ClientView result = instance;
        if(result == null){
            synchronized (ClientView.class) {
                result = instance;
                if (result == null)
                    instance = result = new ClientView();
            }
        }
        return result;
    }

    private ClientToolbarView panelToolbar;

    /**
     * Representing the UI for the client application
     */
    private ClientView() {
        // Start the client model if it isn't running
        if(!ClientModel.get().isConnected())
            ClientModel.get().connect();
    }

    public void init() {
        this.setTitle("Emotiv Control Panel");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setMinimumSize(new Dimension(600, 800));

        this.setLayout(new BorderLayout());

        panelToolbar = new ClientToolbarView();
        this.add(panelToolbar, BorderLayout.PAGE_START);

        // Create a transparent border around this view
        ClientMenuView clientMenuView = new ClientMenuView();
        this.add(clientMenuView, BorderLayout.NORTH);

        // Show frame
        this.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }

    private void getFaceExpressionView() {

        ClientGraphView graphView;
        ClientGraphsView clientGraphsview;
        // Add left (graphical) view
        graphView = new ClientGraphView();
        this.add(graphView, BorderLayout.LINE_START);


        // Add right (input/output) view
        clientGraphsview = new ClientGraphsView();
        this.add(clientGraphsview, BorderLayout.LINE_END);
    }

    private void getPerformanceMetricView() {

        PerformanceMetricGraphView graphView;
        PerformanceMetricEmotionalStatesView emotionalStateView;

        graphView = new PerformanceMetricGraphView ();
        this.add(graphView, BorderLayout.LINE_START);

        emotionalStateView = new PerformanceMetricEmotionalStatesView();
        this.add(emotionalStateView, BorderLayout.LINE_END);
    }
}