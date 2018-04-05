package team04.project3.ui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeriesCollection;
import team04.project3.constants.ColorConstants;
import team04.project3.listeners.ClientListener;
import team04.project3.model.client.ClientModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * The line graph for an expression in the client view
 * @author  Nagarjuna Kalluri (nkalluri@asu.edu)
 */
public class ClientLineGraphView extends JPanel {
    private XYSeriesCollection dataset;
    private JFreeChart chart;
    private ChartPanel panelChart;
    private JPanel panelBuffer;

    /**
     * The left hand side of the client view, containing the graph
     */
    public ClientLineGraphView(String title) {
        ClientModel.get().addListener(new ClientListener() {
            @Override
            public void valuesChanged() { }

            @Override
            public void valuesReset() { }

            @Override
            public void valuesAdded() { }

            @Override
            public void started() {
                // If we're restarting, clear the previous values stored in the dataset
                for(int i = 0; i < dataset.getSeriesCount(); i++) {
                    dataset.getSeries(i).clear();
                }
            }

            @Override
            public void shutdown() {}
        });

        // Create transparent border around graph
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.setBorder(new EmptyBorder(8, 8, 8, 8));

        initGraph(title);
    }

    /**
     * Creates and adds the graph to the panel
     */
    private void initGraph(String title) {
        panelBuffer = new JPanel(new BorderLayout());
        panelBuffer.setBackground(ColorConstants.BACKGROUND_GRAY);
        panelBuffer.setBorder(BorderFactory.createLineBorder(Color.black));

        // Create the chart
        dataset = new XYSeriesCollection();
        chart = ChartFactory.createXYLineChart(title, "", "", dataset, PlotOrientation.VERTICAL, false, false, false);
        chart.setBackgroundPaint(ColorConstants.BACKGROUND_GRAY);
        chart.getPlot().setOutlineVisible(false);
        chart.getPlot().setBackgroundAlpha(0);
        ((XYPlot) chart.getPlot()).getDomainAxis().setVisible(false);
        ((XYPlot) chart.getPlot()).setDomainGridlinesVisible(false);
        ((XYPlot) chart.getPlot()).setDomainMinorGridlinesVisible(false);
        ((XYPlot) chart.getPlot()).getRangeAxis().setVisible(false);
        ((XYPlot) chart.getPlot()).setRangeGridlinesVisible(false);
        ((XYPlot) chart.getPlot()).setRangeMinorGridlinesVisible(false);

        // Put chart in panel
        panelChart = new ChartPanel(chart, false);
        panelChart.setDomainZoomable(false);
        panelChart.setRangeZoomable(false);

        // Put in buffer (for border), then in the ClientView
        panelBuffer.add(panelChart, BorderLayout.CENTER);
        this.add(panelBuffer, BorderLayout.CENTER);
    }
}
