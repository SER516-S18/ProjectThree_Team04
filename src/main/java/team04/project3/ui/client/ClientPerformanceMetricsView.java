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
import team04.project3.model.EmostatePacket;
import team04.project3.model.Emotion;
import team04.project3.model.Expression;
import team04.project3.model.ValueTuple;
import team04.project3.model.client.ClientModel;
import team04.project3.util.Log;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.*;
import java.util.List;

/**
 * UI for showing the emotion performance metrics
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ClientPerformanceMetricsView extends JPanel {
    private HashMap<String, Color> availableColors = new HashMap<>();
    private HashMap<Emotion, Color> emotionColors = new HashMap<>();

    private XYSeriesCollection dataset;
    private JFreeChart chart;
    private ChartPanel panelChart;
    private float range = 30;

    /**
     * Constructor for the ClientPerformanceMetricsView, showing emotion metrics
     */
    public ClientPerformanceMetricsView() {
        availableColors.put("Red", Color.RED);
        availableColors.put("Orange", Color.ORANGE);
        availableColors.put("Yellow", Color.YELLOW);
        availableColors.put("Green", Color.GREEN);
        availableColors.put("Cyan", Color.CYAN);
        availableColors.put("Blue", Color.BLUE);
        availableColors.put("Magenta", Color.MAGENTA);

        Collection colors = availableColors.values();
        Iterator it = colors.iterator();
        for(int i = 0; i < Emotion.values().length; i++) {
            emotionColors.put(Emotion.values()[i], (Color) it.next());
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

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        // Left - Graph
        JPanel panelGraph = buildInitialGraph();
        this.add(panelGraph);

        // Right - Settings
        JPanel panelSettings = new JPanel();
        panelSettings.setLayout(new BorderLayout());
        this.add(panelSettings);

        JLabel labelColorInstructions = new JLabel("<html><center>Click an emotion<br> to change color/visibility</center></html>");
        labelColorInstructions.setFont(TextConstants.DEFAULT_FONT);
        labelColorInstructions.setHorizontalAlignment(SwingConstants.CENTER);
        panelSettings.add(labelColorInstructions, BorderLayout.PAGE_START);

        JPanel panelEmotions = new JPanel();
        panelEmotions.setLayout(new BoxLayout(panelEmotions, BoxLayout.Y_AXIS));
        panelSettings.add(panelEmotions, BorderLayout.CENTER);
        for(Emotion emotion : Emotion.values()) {
            JPanel panelEmotion = new JPanel();
            panelEmotion.setLayout(new BoxLayout(panelEmotion, BoxLayout.X_AXIS));
            panelEmotions.add(panelEmotion);

            JButton buttonColor = new JButton(Character.toString((char) 0x2022));
            buttonColor.setFont(TextConstants.LARGE_FONT);
            buttonColor.setOpaque(false);
            buttonColor.setContentAreaFilled(false);
            buttonColor.setBorderPainted(false);
            buttonColor.setFocusPainted(false);
            buttonColor.setForeground(emotionColors.get(emotion));
            buttonColor.addActionListener(e -> {
                handleChangeColorClick(buttonColor, emotion);
            });

            JButton buttonName = new JButton(emotion.NAME);
            buttonName.setFont(TextConstants.LARGE_FONT);
            buttonName.setOpaque(false);
            buttonName.setContentAreaFilled(false);
            buttonName.setBorderPainted(false);
            buttonName.setFocusPainted(false);
            buttonName.addActionListener( e -> {
                handleChangeColorClick(buttonColor, emotion);
            });
            panelEmotion.add(buttonName);

            panelEmotion.add(Box.createHorizontalStrut(8));

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
     * @return The graph created
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

    /**
     * Updates the graph line colors to correspond with the set emotion color
     */
    private void updateGraphColors() {
        for(int i = 0; i < Emotion.values().length; i++) {
            Emotion emotion = Emotion.values()[i];
            Color color = emotionColors.get(emotion);
            if(color == null)
                chart.getXYPlot().getRenderer().setSeriesVisible(i, false);
            else {
                chart.getXYPlot().getRenderer().setSeriesPaint(i, color);
                chart.getXYPlot().getRenderer().setSeriesVisible(i, true);
            }
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

    /**
     * Handles a click for changing the color
     * @param button JButton to set color
     * @param emotion Emotion to set color of
     */
    private void handleChangeColorClick(JButton button, Emotion emotion) {
        ArrayList<String> colors = new ArrayList<>();
        Set<Map.Entry<String, Color>> set = availableColors.entrySet();

        colors.add("Invisible");
        String current = "Invisible";
        Iterator<Map.Entry<String,Color>> it = set.iterator();
        while (it.hasNext()) {
            Map.Entry<String, Color> entry = it.next();
            if(entry.getValue().equals(emotionColors.get(emotion)))
                current = entry.getKey();

            colors.add(entry.getKey());
        }

        String input = (String) JOptionPane.showInputDialog(
                this,
                "Choose color",
                emotion.NAME + " color", JOptionPane.QUESTION_MESSAGE,
                null,
                colors.toArray(new String[colors.size()]),
                current
        );

        if(input == null || input.length() == 0)
            return;

        if(input.equals("Invisible")) {
            emotionColors.put(emotion, null);
            button.setVisible(false);
        } else {
            emotionColors.put(emotion, availableColors.get(input));
            button.setForeground(emotionColors.get(emotion));
            button.setVisible(true);
        }

        updateGraphColors();
    }
}
