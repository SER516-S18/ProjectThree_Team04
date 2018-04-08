package team04.project3.ui.server;

import team04.project3.constants.ColorConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * The main UI for the server application
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ServerView extends JFrame {

    /**
     * The container for the left (indicator) and right (input) views
     */
    public ServerView() {
        this.setLayout(new BorderLayout());
        this.setTitle("Emotiv Composer");
    }
}
