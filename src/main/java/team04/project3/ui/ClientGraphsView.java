package team04.project3.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeriesCollection;

import team04.project3.constants.ColorConstants;
import team04.project3.listeners.ClientListener;
import team04.project3.model.client.ClientModel;

import java.awt.*;

/**
 * The JFrame containing panels for each graph in the client view
 * @author  Nagarjuna Kalluri (nkalluri@asu.edu)
 */

public class ClientGraphsView extends JPanel{
    private ClientLineGraphView blinkGraph;
    private ClientLineGraphView rightWinkGraph;
    private ClientLineGraphView leftWinkGraph;
    private ClientLineGraphView LookRightLeftGraph;
    private ClientLineGraphView raiseBrowGraph;
    private ClientLineGraphView furrowBrowGraph;
    private ClientLineGraphView smileGraph;
    private ClientLineGraphView clenchGraph;
    private ClientLineGraphView rightSmirkGraph;
    private ClientLineGraphView leftSmirkGraph;
    private ClientLineGraphView laughGraph;

    /**
     * The container for the left (indicator) and right (input) views
     */
    public ClientGraphsView() {
        // Create transparent border around view
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.setBorder(new EmptyBorder(8, 8, 8, 8));

        // Buffer for an opaque border surrounding ServerView, the buffer is the actual visible panel
        JPanel panelBuffer = new JPanel(new GridLayout(11, 1, 0, 0));
        panelBuffer.setBackground(ColorConstants.BACKGROUND_GRAY);
        panelBuffer.setBorder(BorderFactory.createLineBorder(Color.black));

        // Add the graph for blink
        blinkGraph = new ClientLineGraphView("Blink");
        panelBuffer.add(blinkGraph, BorderLayout.PAGE_START);

        // Add the graph for right wink
        rightWinkGraph = new ClientLineGraphView("Right Wink");
        panelBuffer.add(rightWinkGraph, BorderLayout.CENTER);

        // Add the graph for Left wink
        leftWinkGraph = new ClientLineGraphView("Left Wink");
        panelBuffer.add(leftWinkGraph, BorderLayout.PAGE_START);

        // Add the graph for Look Right/Left
        LookRightLeftGraph = new ClientLineGraphView("Look Right/Left");
        panelBuffer.add(LookRightLeftGraph, BorderLayout.CENTER);
 
        // Add the graph for Raise brow
        raiseBrowGraph = new ClientLineGraphView("Raise Brow");
        panelBuffer.add(raiseBrowGraph, BorderLayout.PAGE_START);

        // Add the graph for Furrow brow
        furrowBrowGraph = new ClientLineGraphView("Furrow Brow");
        panelBuffer.add(furrowBrowGraph, BorderLayout.CENTER);
 
        // Add the graph for Smile
        smileGraph = new ClientLineGraphView("Smile");
        panelBuffer.add(smileGraph, BorderLayout.PAGE_START);

        // Add the graph for Clench
        clenchGraph = new ClientLineGraphView("Clench");
        panelBuffer.add(clenchGraph, BorderLayout.CENTER);
        
        // Add the graph for Right Smirk
        rightSmirkGraph = new ClientLineGraphView("Right Smirk");
        panelBuffer.add(rightSmirkGraph, BorderLayout.PAGE_START);

        // Add the graph for Left Smirk
        leftSmirkGraph = new ClientLineGraphView("Left Smirk");
        panelBuffer.add(leftSmirkGraph, BorderLayout.CENTER);

        // Add the graph for Laugh
        laughGraph = new ClientLineGraphView("Laugh");
        panelBuffer.add(laughGraph, BorderLayout.CENTER);

        this.add(panelBuffer, BorderLayout.CENTER);
    }
    

}
