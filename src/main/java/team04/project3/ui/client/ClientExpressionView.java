package team04.project3.ui.client;

import javax.swing.*;
import java.awt.*;

public class ClientExpressionView extends JPanel {
    public ClientExpressionView() {
        this.setLayout(new BorderLayout());

        ClientGraphView graphView;
        ClientGraphsView clientGraphsview;
        // Add left (graphical) view
        graphView = new ClientGraphView();
        this.add(graphView, BorderLayout.LINE_START);


        // Add right (input/output) view
        clientGraphsview = new ClientGraphsView();
        this.add(clientGraphsview, BorderLayout.LINE_END);
    }
}
