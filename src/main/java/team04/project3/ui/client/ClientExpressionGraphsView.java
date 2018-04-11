package team04.project3.ui.client;

import team04.project3.constants.ColorConstants;
import team04.project3.constants.TextConstants;
import team04.project3.listeners.ClientListener;
import team04.project3.model.EmostatePacket;
import team04.project3.model.Expression;
import team04.project3.model.client.ClientModel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * UI for showing all the expression graphs in the ClientView
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ClientExpressionGraphsView extends JPanel {
    private HashMap<Expression, JLabel> labels;
    private HashMap<Expression, ClientExpressionGraphView> graphs;

    /**
     * Constructor for the ClientExpressionGraphsView, showing all the graphs for each expression
     */
    public ClientExpressionGraphsView() {
        ClientModel.get().addListener(new ClientListener() {
            @Override
            public void valuesChanged() {
                updateGraphs();
            }

            @Override
            public void valuesReset() {
                updateValueLabels();
            }

            @Override
            public void valuesAdded() {
                updateValueLabels();
            }

            @Override
            public void started() { }

            @Override
            public void shutdown() { }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        this.graphs = new HashMap<>();
        this.labels = new HashMap<>();

        // Add graphs
        for(int i = 0; i < Expression.values().length; i++) {
            Expression expression = Expression.values()[i];

            JLabel labelName = new JLabel(expression.NAME);
            labelName.setFont(TextConstants.DEFAULT_FONT);
            labelName.setVerticalAlignment(JLabel.CENTER);
            labelName.setHorizontalAlignment(SwingConstants.LEFT);
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.1; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
            this.add(labelName, gbc);

            JLabel labelValue = new JLabel("N/A");
            labelValue.setFont(TextConstants.LARGE_FONT);
            labelValue.setVerticalAlignment(JLabel.CENTER);
            labelValue.setHorizontalAlignment(SwingConstants.RIGHT);
            gbc.gridx = 1; gbc.gridy = i; gbc.weightx = 0.0; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
            this.add(labelValue, gbc);
            this.labels.put(expression, labelValue);

            ClientExpressionGraphView view = new ClientExpressionGraphView(expression);
            gbc.gridx = 2; gbc.gridy = i; gbc.weightx = 1.0; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
            this.add(view, gbc);
            this.graphs.put(expression, view);
        }

        // Populate with values
        updateValueLabels();
    }

    /**
     * Updates the expression's label value to the latest packet
     */
    private void updateValueLabels() {
        EmostatePacket packet = ClientModel.get().getNewestPacket();
        for(Expression expression : Expression.values()) {
            labels.get(expression).setText( String.format("%.2f", packet == null ? 0.0f : packet.getExpression(expression)) );
        }
    }

    /**
     * Updates the graphs for each expression
     */
    private void updateGraphs() {
        for(Expression expression : Expression.values()) {
            graphs.get(expression).updateGraphs();
        }
    }
}
