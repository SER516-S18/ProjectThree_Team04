package edu.asu.ser516.projecttwo.team04.ui;

import edu.asu.ser516.projecttwo.team04.ClientModel;
import edu.asu.ser516.projecttwo.team04.constants.ColorConstants;
import edu.asu.ser516.projecttwo.team04.constants.StringConstants;
import edu.asu.ser516.projecttwo.team04.listeners.ClientListener;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.List;

/**
 * ClientView, the main UI for the client application
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ClientView extends JPanel {
    private ClientGraphView graphView;
    private ClientSettingsView settingsView;

    public ClientView() {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel panelBuffer = new JPanel(new GridLayout(1, 2, 8, 8));
        panelBuffer.setBackground(ColorConstants.BACKGROUND_GRAY);
        panelBuffer.setBorder(BorderFactory.createLineBorder(Color.black));

        graphView = new ClientGraphView();
        panelBuffer.add(graphView, BorderLayout.LINE_START);

        settingsView = new ClientSettingsView();
        panelBuffer.add(settingsView, BorderLayout.LINE_END);

        this.add(panelBuffer, BorderLayout.CENTER);
    }

    private class ClientGraphView extends JPanel {
        private XYSeriesCollection dataset;
        private JFreeChart chart;
        private ChartPanel panelChart;
        private JPanel panelBuffer;

        private ClientGraphView() {
            ClientModel.get().addListener(new ClientListener() {
                @Override
                public void changedValues() {
                    ClientGraphView.this.updateValues();
                }

                @Override
                public void changedChannelCount(int count) {
                    ClientGraphView.this.updateSeries();
                }

                @Override
                public void started() {}

                @Override
                public void shutdown() {}
            });

            this.setLayout(new BorderLayout());
            this.setOpaque(false);
            this.setBorder(new EmptyBorder(8, 8, 8, 8));

            initGraph();
        }

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

        private void updateSeries() {
            // Each series is a line, displaying a channel
            List<ClientModel.ClientChannel> channels = ClientModel.get().getChannels();

            if(channels.size() > dataset.getSeriesCount()) {
                // Was added
                for(int i = dataset.getSeriesCount(); i < channels.size(); i++) {
                    ClientModel.ClientChannel channel = channels.get(i);
                    XYSeries series = new XYSeries("Channel " + channel.id);
                    for(ClientModel.ClientValueTuple tuple : channel.getValues()) {
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

        private void updateValues() {
            List<ClientModel.ClientChannel> channels = ClientModel.get().getChannels();
            for(int i = 0; i < dataset.getSeriesCount(); i++) {
                if(channels.size() == dataset.getSeriesCount()) {
                    XYSeries series = dataset.getSeries(i);
                    ClientModel.ClientValueTuple tuple = channels.get(i).getLast();
                    if(tuple != null)
                        series.add(tuple.tick, tuple.value);
                } else {
                    Log.w("Channel and series size differ (" + channels.size() + " : " + dataset.getSeriesCount() + ")", ClientView.class);
                }
            }
        }
    }

    private class ClientSettingsView extends JPanel {
        private JLabel labelValueMaximum;
        private JLabel labelValueMinimum;
        private JLabel labelValueAverage;
        private JSpinner spinnerInputChannels;
        private JSpinner spinnerInputFrequency;

        private ClientSettingsView() {
            this.setLayout(new GridLayout(5, 2, 8, 8));
            this.setBorder(new EmptyBorder(8, 8, 8, 8));
            this.setOpaque(false);

            // Maximum - Prompt
            JLabel labelPromptMaximum = new JLabel(StringConstants.HIGHEST_VALUE_STRING);
            labelPromptMaximum.setFont(ColorConstants.DEFAULT_FONT);
            labelPromptMaximum.setHorizontalAlignment(JLabel.CENTER);
            labelPromptMaximum.setVerticalAlignment(JLabel.CENTER);

            JPanel panelPromptMaximum = new JPanel();
            panelPromptMaximum.setBorder(BorderFactory.createLineBorder(Color.black));
            panelPromptMaximum.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
            panelPromptMaximum.add(labelPromptMaximum);
            this.add(panelPromptMaximum);

            // Maximum - Value
            labelValueMaximum = new JLabel(String.valueOf(ClientModel.get().getMaximum() == null ? "" : ClientModel.get().getMinimum()));
            labelValueMaximum.setFont(ColorConstants.LARGE_FONT);

            JPanel panelValueMaximum = new JPanel();
            panelValueMaximum.setBorder(BorderFactory.createLineBorder(Color.black));
            panelValueMaximum.setBackground(ColorConstants.BACKGROUND_PINK);
            panelValueMaximum.add(labelValueMaximum);
            this.add(panelValueMaximum);

            // Minimum - Prompt
            JLabel labelPromptMinimum = new JLabel(StringConstants.LOWEST_VALUE_STRING);
            labelPromptMinimum.setFont(ColorConstants.DEFAULT_FONT);
            labelPromptMinimum.setHorizontalAlignment(JLabel.CENTER);
            labelPromptMinimum.setVerticalAlignment(JLabel.CENTER);

            JPanel panelPromptMinimum = new JPanel();
            panelPromptMinimum.setBorder(BorderFactory.createLineBorder(Color.black));
            panelPromptMinimum.setBackground(ColorConstants.BACKGROUND_PINK);
            panelPromptMinimum.add(labelPromptMinimum);
            this.add(panelPromptMinimum);

            // Minimum - Value
            labelValueMinimum = new JLabel(String.valueOf(ClientModel.get().getMinimum() == null ? "" : ClientModel.get().getMinimum()));
            labelValueMinimum.setFont(ColorConstants.LARGE_FONT);

            JPanel panelValueMinimum = new JPanel();
            panelValueMinimum.setBorder(BorderFactory.createLineBorder(Color.black));
            panelValueMinimum.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
            panelValueMinimum.add(labelValueMinimum);
            this.add(panelValueMinimum);

            // Average - Prompt
            JLabel labelPromptAverage = new JLabel(StringConstants.AVERAGE_VALUE_STRING);
            labelPromptAverage.setFont(ColorConstants.DEFAULT_FONT);
            labelPromptAverage.setHorizontalAlignment(JLabel.CENTER);
            labelPromptAverage.setVerticalAlignment(JLabel.CENTER);

            JPanel panelPromptAverage = new JPanel();
            panelPromptAverage.setBorder(BorderFactory.createLineBorder(Color.black));
            panelPromptAverage.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
            panelPromptAverage.add(labelPromptAverage);
            this.add(panelPromptAverage);

            // Average - Value
            labelValueAverage = new JLabel(String.valueOf(ClientModel.get().getAverage() == null ? "" : ClientModel.get().getAverage()));
            labelValueAverage.setFont(ColorConstants.LARGE_FONT);

            JPanel panelValueAverage = new JPanel();
            panelValueAverage.setBorder(BorderFactory.createLineBorder(Color.black));
            panelValueAverage.setBackground(ColorConstants.BACKGROUND_PINK);
            panelValueAverage.add(labelValueAverage);
            this.add(panelValueAverage);

            // Channels - Prompt
            JLabel labelPromptChannels = new JLabel(StringConstants.CHANNELS_VALUE_STRING);
            labelPromptChannels.setFont(ColorConstants.DEFAULT_FONT);
            labelPromptChannels.setHorizontalAlignment(JLabel.CENTER);
            labelPromptChannels.setVerticalAlignment(JLabel.CENTER);

            JPanel panelPromptChannels = new JPanel();
            panelPromptChannels.setBorder(BorderFactory.createLineBorder(Color.black));
            panelPromptChannels.setBackground(ColorConstants.BACKGROUND_PINK);
            panelPromptChannels.add(labelPromptChannels);
            this.add(panelPromptChannels);

            // Channels - Input
            spinnerInputChannels = new JSpinner( new SpinnerNumberModel(ClientModel.get().getChannelCount(), 1, 65536, 1) );
            spinnerInputChannels.setVisible(true);
            spinnerInputChannels.setBorder(null);
            spinnerInputChannels.getEditor().getComponent(0).setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
            spinnerInputChannels.setFont(ColorConstants.LARGE_FONT);
            spinnerInputChannels.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    ClientModel.get().setChannelCount((Integer) spinnerInputChannels.getValue());
                }
            });

            JPanel panelInputChannels = new JPanel();
            panelInputChannels.setBorder(BorderFactory.createLineBorder(Color.black));
            panelInputChannels.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
            panelInputChannels.add(spinnerInputChannels);
            this.add(panelInputChannels);

            // Frequency - Prompt
            JLabel labelPromptFrequency = new JLabel(StringConstants.FREQUENCY_VALUE_STRING);
            labelPromptFrequency.setFont(ColorConstants.DEFAULT_FONT);
            labelPromptFrequency.setHorizontalAlignment(JLabel.CENTER);
            labelPromptFrequency.setVerticalAlignment(JLabel.CENTER);

            JPanel panelPromptFrequency = new JPanel();
            panelPromptFrequency.setBorder(BorderFactory.createLineBorder(Color.black));
            panelPromptFrequency.setBackground(ColorConstants.BACKGROUND_BLUEGRAY);
            panelPromptFrequency.add(labelPromptFrequency);
            this.add(panelPromptFrequency);

            // Frequency - Input
            spinnerInputFrequency = new JSpinner( new SpinnerNumberModel(ClientModel.get().getFrequency(), 1, 65536, 1) );
            spinnerInputFrequency.setVisible(true);
            spinnerInputFrequency.setBorder(null);
            spinnerInputFrequency.getEditor().getComponent(0).setBackground(ColorConstants.BACKGROUND_PINK);
            spinnerInputFrequency.setFont(ColorConstants.LARGE_FONT);
            spinnerInputFrequency.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    ClientModel.get().setFrequency((Integer) spinnerInputFrequency.getValue());
                }
            });

            JPanel panelInputFrequency = new JPanel();
            panelInputFrequency.setBorder(BorderFactory.createLineBorder(Color.black));
            panelInputFrequency.setBackground(ColorConstants.BACKGROUND_PINK);
            panelInputFrequency.add(spinnerInputFrequency);
            this.add(panelInputFrequency);

            ClientModel.get().addListener(new ClientListener() {
                @Override
                public void changedValues() {
                    ClientSettingsView.this.labelValueMaximum.setText( ClientModel.get().getMaximum() == null ? "" : Integer.toString(ClientModel.get().getMaximum()) );
                    ClientSettingsView.this.labelValueMinimum.setText( ClientModel.get().getMinimum() == null ? "" : Integer.toString(ClientModel.get().getMinimum()) );
                    ClientSettingsView.this.labelValueAverage.setText( ClientModel.get().getAverage() == null ? "" : Integer.toString(ClientModel.get().getAverage()) );
                }

                @Override
                public void changedChannelCount(int count) {}

                @Override
                public void started() {}

                @Override
                public void shutdown() {}
            });
        }
    }
}