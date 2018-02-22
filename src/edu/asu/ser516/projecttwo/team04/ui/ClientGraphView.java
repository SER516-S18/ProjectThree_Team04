package edu.asu.ser516.projecttwo.team04.ui;

import edu.asu.ser516.projecttwo.team04.constants.ColorConstants;
import edu.asu.ser516.projecttwo.team04.listeners.ClientListener;
import edu.asu.ser516.projecttwo.team04.model.client.ClientChannel;
import edu.asu.ser516.projecttwo.team04.model.client.ClientModel;
import edu.asu.ser516.projecttwo.team04.model.client.ClientValueTuple;
import edu.asu.ser516.projecttwo.team04.util.Log;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * ClientGraphView - The left hand JPanel containing the graph in the client view
 */
public class ClientGraphView extends JPanel {
    private XYSeriesCollection dataset;
    private JFreeChart chart;
    private ChartPanel panelChart;
    private JPanel panelBuffer;

    /**
     * ClientGraphView - The left hand side of the client view, containing the graph
     */
    public ClientGraphView() {
        ClientModel.get().addListener(new ClientListener() {
            @Override
            public void changedValues() {
                // When the channel(s) get new values, add them to the graph
                ClientGraphView.this.updateValues();
            }

            @Override
            public void changedChannelCount() {
                // When the number of channels changes, update the series (dataset lines in graph)
                ClientGraphView.this.updateSeries();
            }

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

        initGraph();
    }

    /**
     * initGraph - Creates and adds the graph to the panel
     */
    private void initGraph() {
        panelBuffer = new JPanel(new BorderLayout());
        panelBuffer.setBackground(ColorConstants.BACKGROUND_PINK);
        panelBuffer.setBorder(BorderFactory.createLineBorder(Color.black));

        // Create the chart
        dataset = new XYSeriesCollection();
        updateSeries();
        chart = ChartFactory.createXYLineChart("", "", "", dataset, PlotOrientation.VERTICAL, false, false, false);
        chart.setBackgroundPaint(ColorConstants.BACKGROUND_PINK);
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

    /**
     * updateSeries - Called when the number of channels changes, to update the series (graph's lines)
     */
    private void updateSeries() {
        // Each series is a line, displaying a channel
        java.util.List<ClientChannel> channels = ClientModel.get().getChannels();

        if(channels.size() > dataset.getSeriesCount()) {
            // Was added
            for(int i = dataset.getSeriesCount(); i < channels.size(); i++) {
                ClientChannel channel = channels.get(i);
                XYSeries series = new XYSeries("Channel " + channel.id);
                for(ClientValueTuple tuple : channel.getValues()) {
                    series.add(tuple.tick, tuple.value);
                }
                dataset.addSeries(series);
            }
        } else if(channels.size() < dataset.getSeriesCount()) {
            // Was removed
            for(int i = dataset.getSeriesCount() - 1; i > channels.size() - 1; i--) {
                XYSeries series = dataset.getSeries(i);
                series.clear();
                dataset.removeSeries(i);
            }
        }
    }

    /**
     * updateValues - Called when new values need to be added to the graph
     */
    private void updateValues() {
        List<ClientChannel> channels = ClientModel.get().getChannels();
        for(int i = 0; i < dataset.getSeriesCount(); i++) {
            if(channels.size() == dataset.getSeriesCount()) {
                XYSeries series = dataset.getSeries(i);
                ClientValueTuple tuple = channels.get(i).getLast();
                if(tuple != null)
                    series.add(tuple.tick, tuple.value);
            } else {
                Log.w("Channel and series size differ (" + channels.size() + " : " + dataset.getSeriesCount() + ")", ClientGraphView.class);
            }
        }
    }
}
