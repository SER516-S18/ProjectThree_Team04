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
import team04.project3.model.Expression;
import team04.project3.model.ValueTuple;
import team04.project3.model.client.ClientModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * UI for displaying a single expression's name, most recent value, and graph
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ClientExpressionGraphView extends JPanel {
    private final Expression expression;
    private JLabel labelValue;

    private XYSeriesCollection dataset;
    private JFreeChart chart;
    private ChartPanel panelChart;

    /**
     * Constructor for the ClientExpressionGraphView, showing the name label, graph, and most recent value for an expression
     * @param expression Expression to display
     */
    public ClientExpressionGraphView(Expression expression) {
        this.expression = expression;

        this.setLayout(new BorderLayout());

        // Labels
        JPanel panelLabels = new JPanel();
        panelLabels.setBorder(BorderFactory.createEmptyBorder(0,8,0,16));
        panelLabels.setLayout(new BorderLayout());
        panelLabels.setOpaque(false);
        panelLabels.setPreferredSize(new Dimension(192, 32));
        this.add(panelLabels, BorderLayout.WEST);

        JLabel labelName = new JLabel(expression.NAME);
        labelName.setFont(TextConstants.DEFAULT_FONT);
        labelName.setVerticalAlignment(JLabel.CENTER);
        labelName.setHorizontalAlignment(SwingConstants.LEFT);
        panelLabels.add(labelName, BorderLayout.WEST);

        labelValue = new JLabel("N/A");
        labelValue.setFont(TextConstants.LARGE_FONT);
        labelValue.setVerticalAlignment(JLabel.CENTER);
        labelValue.setHorizontalAlignment(SwingConstants.RIGHT);
        panelLabels.add(labelValue, BorderLayout.EAST);

        // Graph
        JPanel panelGraph = this.buildInitialGraph();
        this.add(panelGraph, BorderLayout.EAST);
    }

    /**
     * Creates and adds the graph to the panel
     * @return The graph that is created
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
        chart.getXYPlot().getRenderer().setSeriesStroke(0,
                new BasicStroke(4.0f)
        );

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
        // Each series is a line, displaying a channel
        List<ValueTuple> packets = ClientModel.get().getExpressionPackets(expression);
        XYSeries series = new XYSeries(expression.NAME);
        for(ValueTuple tuple : packets) {
            series.add(tuple.TICK, tuple.VALUE);
        }
        dataset.addSeries(series);
    }

    /**
     * Called when new values need to be added to the graph
     */
    public void updateGraphs() {
        List<ValueTuple> packets = ClientModel.get().getExpressionPackets(expression);
        if(packets.size() > 0) {
            ValueTuple tuple = packets.get(packets.size() - 1);
            XYSeries series = dataset.getSeries(0);
            if (tuple != null) {
                // Put a point immediately after the previous one for a vertical line for binary values
                if(expression.isBinary() && packets.size() > 1)
                    series.add(packets.get(packets.size() - 2).TICK + 0.0001, tuple.VALUE);

                series.add(tuple.TICK, tuple.VALUE);
            }
        } else {
            dataset.getSeries(0).clear();
        }
    }

    /**
     * Sets the label's text to the expression's value
     * @param value Value to set
     */
    public void setValue(float value) {
        labelValue.setText(Float.toString(value));
    }

    /**
     * Returns which expression the panel represents
     * @return Expression the panel represents
     */
    public Expression getExpression() {
        return expression;
    }
}
