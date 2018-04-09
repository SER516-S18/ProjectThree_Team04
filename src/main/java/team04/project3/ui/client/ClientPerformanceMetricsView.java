package team04.project3.ui.client;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import team04.project3.constants.ColorConstants;
import team04.project3.constants.TextConstants;
import team04.project3.listeners.ClientListener;
import team04.project3.model.Emotion;
import team04.project3.model.ValueTuple;
import team04.project3.model.client.ClientModel;
import team04.project3.util.Log;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ClientPerformanceMetricsView extends JPanel {
    private LinkedList<Color> availableColors = new LinkedList<>();
    private HashMap<Emotion, Color> emotionColors = new HashMap<>();

    private XYSeriesCollection dataset;
    private JFreeChart chart;
    private ChartPanel panelChart;
    private float range = 30;

    public ClientPerformanceMetricsView() {
        availableColors.add(Color.RED);
        availableColors.add(Color.ORANGE);
        availableColors.add(Color.YELLOW);
        availableColors.add(Color.GREEN);
        availableColors.add(Color.CYAN);
        availableColors.add(Color.BLUE);
        availableColors.add(Color.MAGENTA);

        for(Emotion emotion : Emotion.values()) {
            emotionColors.put(emotion, availableColors.removeFirst());
        }

        ClientModel.get().addListener(new ClientListener() {
            @Override
            public void valuesChanged() {
                updateGraphValues();
            }

            @Override
            public void valuesReset() { }

            @Override
            public void valuesAdded() { }

            @Override
            public void started() { }

            @Override
            public void shutdown() { }
        });

        this.setLayout(new BorderLayout());

        // Left - Graph
        JPanel panelGraph = buildInitialGraph();
        this.add(panelGraph, BorderLayout.WEST);

        // Right - Settings
        JPanel panelSettings = new JPanel();
        panelSettings.setLayout(new BorderLayout());
        this.add(panelSettings, BorderLayout.EAST);

        JPanel panelEmotions = new JPanel();
        panelEmotions.setLayout(new BoxLayout(panelEmotions, BoxLayout.Y_AXIS));
        panelSettings.add(panelEmotions, BorderLayout.PAGE_START);
        for(Emotion emotion : Emotion.values()) {
            JPanel panelEmotion = new JPanel();
            panelEmotion.setLayout(new BoxLayout(panelEmotion, BoxLayout.X_AXIS));
            panelEmotions.add(panelEmotion);

            JLabel labelName = new JLabel(emotion.NAME);
            labelName.setFont(TextConstants.LARGE_FONT);
            labelName.setVerticalAlignment(JLabel.CENTER);
            labelName.setHorizontalAlignment(SwingConstants.LEFT);
            panelEmotion.add(labelName);

            panelEmotion.add(Box.createHorizontalStrut(8));

            JButton buttonColor = new JButton(Character.toString((char) 0x2022));
            buttonColor.setFont(TextConstants.LARGE_FONT);
            buttonColor.setOpaque(false);
            buttonColor.setContentAreaFilled(false);
            buttonColor.setBorderPainted(false);
            buttonColor.setFocusPainted(false);
            buttonColor.setForeground(emotionColors.get(emotion));
            buttonColor.addActionListener(e -> {
                availableColors.addLast(emotionColors.get(emotion));
                emotionColors.put(emotion, availableColors.removeFirst());
                buttonColor.setForeground(emotionColors.get(emotion));
                updateGraphColors();
            });
            panelEmotion.add(buttonColor);
        }

        updateGraphColors();

        JPanel panelTime = new JPanel();
        panelTime.setMaximumSize(new Dimension(64, 32));
        panelTime.setLayout(new BoxLayout(panelTime, BoxLayout.X_AXIS));
        panelSettings.add(panelTime, BorderLayout.PAGE_END);

        JLabel labelTime = new JLabel("Display length:");
        labelTime.setFont(TextConstants.LARGE_FONT);
        panelTime.add(labelTime);

        panelTime.add(Box.createHorizontalStrut(8));

        JSpinner spinnerTime = new JSpinner(new SpinnerNumberModel(range, 1, 1440, 1));
        spinnerTime.setFont(TextConstants.LARGE_FONT);
        spinnerTime.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                try {
                    spinnerTime.commitEdit();
                    range = (float) ((double) spinnerTime.getValue());
                } catch(ParseException e) {
                    Log.w("Invalid time entered for graph range (" + e.getMessage() + ")", ClientPerformanceMetricsView.class);
                }
            }
        });
        panelTime.add(spinnerTime);

        panelTime.add(Box.createHorizontalStrut(8));

        JLabel labelTimeSeconds = new JLabel("sec");
        labelTimeSeconds.setFont(TextConstants.LARGE_FONT);
        panelTime.add(labelTimeSeconds);
    }

    /**
     * Creates and adds the graph to the panel
     */
    private JPanel buildInitialGraph() {
        JPanel panelGraph = new JPanel(new BorderLayout());
        panelGraph.setBackground(ColorConstants.BACKGROUND_PINK);
        panelGraph.setBorder(BorderFactory.createLineBorder(Color.black));

        // Create the chart
        dataset = new XYSeriesCollection();
        this.updateSeries();
        chart = ChartFactory.createXYLineChart("", "", "", dataset, PlotOrientation.VERTICAL, false, false, false);
        chart.setBackgroundPaint(Color.WHITE);
        chart.getPlot().setOutlineVisible(false);
        chart.getPlot().setBackgroundAlpha(0);
        for(int i = 0; i < Emotion.values().length; i++) {
            chart.getXYPlot().getRenderer().setSeriesStroke(i,
                    new BasicStroke(4.0f)
            );
        }

        ((XYPlot) chart.getPlot()).getDomainAxis().setVisible(false);
        ((XYPlot) chart.getPlot()).setDomainGridlinesVisible(false);
        ((XYPlot) chart.getPlot()).setDomainMinorGridlinesVisible(false);
        ((XYPlot) chart.getPlot()).getRangeAxis().setVisible(false);
        ((XYPlot) chart.getPlot()).setRangeGridlinesVisible(false);
        ((XYPlot) chart.getPlot()).setRangeMinorGridlinesVisible(false);
        ((XYPlot) chart.getPlot()).getRangeAxis().setRange(0.0d, 1.0d);

        // Put chart in panel
        panelChart = new ChartPanel(chart, false);
        panelChart.setDomainZoomable(false);
        panelChart.setRangeZoomable(false);

        // Put in buffer (for border), then in the ClientView
        panelGraph.add(panelChart, BorderLayout.CENTER);

        return panelGraph;
    }

    /**
     * Called when the number of channels changes, to update the series (graph's lines)
     */
    private void updateSeries() {
        for(Emotion emotion : Emotion.values()) {
            // Each series is a line, displaying a channel
            java.util.List<ValueTuple> packets = ClientModel.get().getEmotionPackets(emotion);
            XYSeries series = new XYSeries(emotion.NAME);
            for (ValueTuple tuple : packets) {
                series.add(tuple.TICK, tuple.VALUE);
            }
            dataset.addSeries(series);
        }
    }

    private void updateGraphColors() {
        for(int i = 0; i < Emotion.values().length; i++) {
            Emotion emotion = Emotion.values()[i];
            chart.getXYPlot().getRenderer().setSeriesPaint(i, emotionColors.get(emotion));
        }
        panelChart.repaint();
    }

    /**
     * Called when new values need to be added to the graph
     */
    public void updateGraphValues() {
        double tick = 0.0d;

        for(int i = 0; i < Emotion.values().length; i++) {
            Emotion emotion = Emotion.values()[i];

            List<ValueTuple> packets = ClientModel.get().getEmotionPackets(emotion);
            if (packets.size() > 0) {
                ValueTuple tuple = packets.get(packets.size() - 1);
                XYSeries series = dataset.getSeries(i);
                if (tuple != null) {
                    series.add(tuple.TICK, tuple.VALUE);
                    if(tuple.TICK > tick)
                        tick = tuple.TICK;
                }
            } else {
                dataset.getSeries(i).clear();
            }
        }

        ((XYPlot) chart.getPlot()).getDomainAxis().setRange(Math.max(0, tick - range), Math.max(tick, range));
    }
}
