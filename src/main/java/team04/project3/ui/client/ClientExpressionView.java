package team04.project3.ui.client;

import javax.swing.*;
import java.awt.*;

/**
 * UI for the client face view and client graphs view
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ClientExpressionView extends JPanel {
    /**
     * Shows the client face view and the client graphs view, side-by-side
     */
    public ClientExpressionView() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        ClientExpressionFaceView faceView = new ClientExpressionFaceView();
        this.add(faceView);

        ClientExpressionGraphsView graphsView = new ClientExpressionGraphsView();
        this.add(graphsView);
    }
}
