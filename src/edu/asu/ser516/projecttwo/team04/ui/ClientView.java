package edu.asu.ser516.projecttwo.team04.ui;

import edu.asu.ser516.projecttwo.team04.ClientModel;
import edu.asu.ser516.projecttwo.team04.constants.AppConstants;
import edu.asu.ser516.projecttwo.team04.listeners.ClientListener;
import edu.asu.ser516.projecttwo.team04.util.Log;
import edu.asu.ser516.projecttwo.team04.constants.UIStandards;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Random;

/**
 * ClientView, the main UI for the client application
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ClientView extends JPanel {
    private AppView parent;
    private JTextPane maxValTextPanel;
    private JTextPane minValTextPanel;
    private JTextPane avgValTextPanel;

    public ClientView(AppView appView) {
        parent = appView;

        JPanel clientPanel = new JPanel();
        clientPanel.setBackground(Color.lightGray);
        clientPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        clientPanel.setBounds(5, 50, 780, 400);
        clientPanel.setLayout(null);

        PlotGraph plotGraph = new PlotGraph();
        ChartPanel plottingChart = new ChartPanel(plotGraph.graph);
        plottingChart.setLocation(0, 0);
        plottingChart.setSize(new Dimension(520,400));
        plotGraph.graph.setBackgroundPaint(UIStandards.BACKGROUND_PINK);
        
        
        JPanel graphView = new JPanel();
        graphView.setBackground(UIStandards.BACKGROUND_PINK);
        graphView.setBounds(15, 15, 520, 370);
        graphView.setLayout(null);
        graphView.setBorder(BorderFactory.createLineBorder(Color.black));
        graphView.add(plottingChart);
        clientPanel.add(graphView);

        JLabel maxValLabel = new JLabel(AppConstants.HIGHEST_VALUE_STRING, JLabel.CENTER);
        maxValLabel.setFont(UIStandards.SMALL_FONT);
        maxValLabel.setHorizontalAlignment(JLabel.CENTER);
        maxValLabel.setVerticalAlignment(JLabel.CENTER);

        JPanel maxValLabelPanel = new JPanel();
        maxValLabelPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        maxValLabelPanel.setBounds(555, 16, 86, 59);
        maxValLabelPanel.setBackground(UIStandards.BACKGROUND_BLUE);
        maxValLabelPanel.add(maxValLabel);
        clientPanel.add(maxValLabelPanel);

        maxValTextPanel = new JTextPane();
        maxValTextPanel.setText("-");
        maxValTextPanel.setFont(UIStandards.SMALL_FONT);
        maxValTextPanel.setEditable(false);
        maxValTextPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        maxValTextPanel.setBounds(650, 16, 86, 59);
        maxValTextPanel.setBackground(UIStandards.BACKGROUND_PINK);
        clientPanel.add(maxValTextPanel);

        JLabel minValLabel = new JLabel(AppConstants.LOWEST_VALUE_STRING, JLabel.CENTER);
        minValLabel.setFont(UIStandards.SMALL_FONT);
        minValLabel.setHorizontalAlignment(JLabel.CENTER);
        minValLabel.setVerticalAlignment(JLabel.CENTER);

        JPanel minValLabelPanel = new JPanel();
        minValLabelPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        minValLabelPanel.setBounds(555, 91, 86, 59);
        minValLabelPanel.setBackground(UIStandards.BACKGROUND_PINK);
        minValLabelPanel.add(minValLabel);
        clientPanel.add(minValLabelPanel);

        minValTextPanel = new JTextPane();
        minValTextPanel.setText("-");
        minValTextPanel.setFont(UIStandards.SMALL_FONT);
        minValTextPanel.setEditable(false);
        minValTextPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        minValTextPanel.setBounds(650, 91, 86, 59);
        minValTextPanel.setBackground(UIStandards.BACKGROUND_BLUE);
        clientPanel.add(minValTextPanel);

        JLabel averageLabel = new JLabel(AppConstants.AVERAGE_VALUE_STRING, JLabel.CENTER);
        averageLabel.setFont(UIStandards.SMALL_FONT);
        averageLabel.setHorizontalAlignment(JLabel.CENTER);
        averageLabel.setVerticalAlignment(JLabel.CENTER);

        JPanel avgValLabelPanel = new JPanel();
        avgValLabelPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        avgValLabelPanel.setBounds(555, 166, 88, 59);
        avgValLabelPanel.setBackground(UIStandards.BACKGROUND_BLUE);
        avgValLabelPanel.add(averageLabel);
        clientPanel.add(avgValLabelPanel);

        avgValTextPanel = new JTextPane();
        avgValTextPanel.setText("-");
        avgValTextPanel.setFont(UIStandards.SMALL_FONT);
        avgValTextPanel.setEditable(false);
        avgValTextPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        avgValTextPanel.setBounds(650, 166, 86, 59);
        avgValTextPanel.setBackground(UIStandards.BACKGROUND_PINK);
        clientPanel.add(avgValTextPanel);

        JLabel channelLabel = new JLabel(AppConstants.CHANNELS_VALUE_STRING, JLabel.CENTER);
        channelLabel.setFont(UIStandards.SMALL_FONT);
        channelLabel.setHorizontalAlignment(JLabel.CENTER);
        channelLabel.setVerticalAlignment(JLabel.CENTER);

        JPanel channelLabelPanel = new JPanel();
        channelLabelPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        channelLabelPanel.setBounds(555, 246, 86, 59);
        channelLabelPanel.setBackground(UIStandards.BACKGROUND_PINK);
        channelLabelPanel.add(channelLabel);
        clientPanel.add(channelLabelPanel);

        JSpinner channelValue = new JSpinner( new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1) );
        channelValue.setVisible(true);
        channelValue.setBorder(BorderFactory.createLineBorder(Color.black));
        channelValue.setBounds(650, 246, 86, 59);
        channelValue.getEditor().getComponent(0).setBackground(UIStandards.BACKGROUND_BLUE);
        channelValue.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ClientModel.get().setChannelCount((Integer) channelValue.getValue());
            }
        });
        clientPanel.add(channelValue);

        JLabel frequencyLabel = new JLabel(AppConstants.FREQUENCY_VALUE_STRING, JLabel.CENTER);
        frequencyLabel.setFont(UIStandards.SMALL_FONT);
        frequencyLabel.setHorizontalAlignment(JLabel.CENTER);
        frequencyLabel.setVerticalAlignment(JLabel.CENTER);

        JPanel frequencyLabelPanel = new JPanel();
        frequencyLabelPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        frequencyLabelPanel.setBounds(555, 326, 86, 59);
        frequencyLabelPanel.setBackground(UIStandards.BACKGROUND_BLUE);
        frequencyLabelPanel.add(frequencyLabel);
        clientPanel.add(frequencyLabelPanel);

        JTextPane frequencyTextPanel = new JTextPane();
        frequencyTextPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        frequencyTextPanel.setBounds(650, 326, 86, 59);
        frequencyTextPanel.setBackground(UIStandards.BACKGROUND_PINK);
        frequencyTextPanel.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                try {
                    int freq = Integer.parseInt(frequencyTextPanel.getText());
                    ClientModel.get().setFrequency(freq);
                } catch(NumberFormatException e) {
                    Log.w("Invalid frequency entered, must be an integer (" + e.getMessage() + ")", ClientView.class);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                try {
                    int freq = Integer.parseInt(frequencyTextPanel.getText());
                    ClientModel.get().setFrequency(freq);
                } catch(NumberFormatException e) {
                    Log.w("Invalid frequency entered, must be an integer (" + e.getMessage() + ")", ClientView.class);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) { }
        });
        frequencyTextPanel.setText(Integer.toString(ClientModel.get().getFrequency()));
        clientPanel.add(frequencyTextPanel);

        parent.add(clientPanel);
        clientPanel.repaint();

        ClientModel.get().addListener(new ClientListener() {
            @Override
            public void changedValues() {
                ClientView.this.minValTextPanel.setText( ClientModel.get().getMinimum() == null ? "-" : Integer.toString(ClientModel.get().getMinimum()) );
                ClientView.this.maxValTextPanel.setText( ClientModel.get().getMaximum() == null ? "-" : Integer.toString(ClientModel.get().getMaximum()) );
                ClientView.this.avgValTextPanel.setText( ClientModel.get().getAverage() == null ? "-" : Integer.toString(ClientModel.get().getAverage()) );
            }

            @Override
            public void changedChannelCount(int count) {

            }

            @Override
            public void started() {}

            @Override
            public void shutdown() {}
        });
    }

    /**
     * Class to plot the graph on client server depending on number of channels selected
     * @author  Sai Saran Kandimalla.
     */
    class PlotGraph {
        JFreeChart graph;

        public PlotGraph() {
            this.graph= ChartFactory.createLineChart(
                    "Display",
                    "Number","Value",
                    createDataset(),
                    PlotOrientation.VERTICAL,
                    true,true,false);
        }

        /*
         * Method that creates datasets to plot the graph receiving the random values from server.
         * this method creates number of datasets equal to number of channels
         */
        private DefaultCategoryDataset createDataset( ) {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
            Random randomNumber = new Random();

            for(int i=1;i<=30;i++)
            {
                dataset.addValue((Number)randomNumber.nextInt(1024), "values", i);
            }

            return dataset;
        }
    }
}