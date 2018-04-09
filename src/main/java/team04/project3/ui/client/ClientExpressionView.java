package team04.project3.ui.client;

import javax.swing.*;
import java.awt.*;

public class ClientExpressionView extends JPanel {
    public ClientExpressionView() {
        this.setLayout(new BorderLayout());

        ClientExpressionFaceView faceView = new ClientExpressionFaceView();
        this.add(faceView, BorderLayout.EAST);

        ClientExpressionGraphsView graphsView = new ClientExpressionGraphsView();
        this.add(graphsView, BorderLayout.EAST);
    }
}
