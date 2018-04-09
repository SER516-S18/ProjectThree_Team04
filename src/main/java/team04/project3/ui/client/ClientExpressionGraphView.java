package team04.project3.ui.client;

import team04.project3.model.Emotion;
import team04.project3.model.Expression;

import javax.swing.*;

public class ClientExpressionGraphView extends JPanel {
    private Expression expression;

    public ClientExpressionGraphView(Expression expression) {
        this.expression = expression;
    }
}
