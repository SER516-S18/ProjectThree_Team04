package team04.project3.ui;

import team04.project3.constants.ColorConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Main UI for the client application
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ClientView extends JPanel {

    private ClientSettingsView settingsView;

    JPanel panelBuffer;

    /**
     * Representing the UI for the client application
     */
    public ClientView(int type) {
        // Create a transparent border around this view
        ClientMenuView clientMenuView = new ClientMenuView();

        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.setBorder(new EmptyBorder(0, 8, 8, 8));
        this.add(clientMenuView, BorderLayout.NORTH);


        // Content goes in here (inside the invisible border)
        panelBuffer = new JPanel(new GridLayout(1, 2, 8, 8));
        panelBuffer.setBackground(ColorConstants.BACKGROUND_GRAY);
        panelBuffer.setBorder(BorderFactory.createLineBorder(Color.black));

        if (type == AppView.TYPE_CLIENT_FACE_EXPRESSION) {
            getFaceExpressionView();
        } else {
            getPerformanceMetricView();
        }

        this.add(panelBuffer, BorderLayout.CENTER);
    }

    private void getFaceExpressionView() {

        ClientGraphView graphView;
        ClientGraphsView clientGraphsview;
        // Add left (graphical) view
        graphView = new ClientGraphView();
        panelBuffer.add(graphView, BorderLayout.LINE_START);


        // Add right (input/output) view
        clientGraphsview = new ClientGraphsView();
        panelBuffer.add(clientGraphsview, BorderLayout.LINE_END);
    }

    private void getPerformanceMetricView() {

        PerformanceMetricGraphView graphView;
        PerformanceMetricEmotionalStatesView emotionalStateView;

        graphView = new PerformanceMetricGraphView ();
        panelBuffer.add(graphView, BorderLayout.LINE_START);

        emotionalStateView = new PerformanceMetricEmotionalStatesView();
        panelBuffer.add(emotionalStateView, BorderLayout.LINE_END);
    }
}