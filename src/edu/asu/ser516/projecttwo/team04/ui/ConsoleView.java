package edu.asu.ser516.projecttwo.team04.ui;

import edu.asu.ser516.projecttwo.team04.constants.ColorConstants;
import edu.asu.ser516.projecttwo.team04.util.Log;
import edu.asu.ser516.projecttwo.team04.util.Terminal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * ConsoleView, UI element to display system messages through a console
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ConsoleView extends JPanel {
    private JLabel labelHeader;
    private JScrollPane scrollpaneLog;
    private JTextArea textareaLog;
    private JTextField textfieldInput;

    public ConsoleView() {
        // It is customary to have an initialize method for Swing UIs to allow reinitialization
        this.init();
    }

    public void init() {
        // Create transparent border around class
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.setBorder(new EmptyBorder(8, 8, 8, 8));

        // Actual content pane (which the transparent border encompasses)
        JPanel panelBuffer = new JPanel(new BorderLayout());
        panelBuffer.setBackground(ColorConstants.BACKGROUND_GRAY);
        panelBuffer.setBorder(BorderFactory.createLineBorder(Color.black));

        labelHeader = new JLabel("Console:");
        labelHeader.setFont(ColorConstants.DEFAULT_FONT);
        panelBuffer.add(labelHeader, BorderLayout.PAGE_START);

        textareaLog = new JTextArea();
        textareaLog.setEditable(false);
        textareaLog.setRows(6);
        textareaLog.setOpaque(false);
        textareaLog.setFont(ColorConstants.SMALL_FONT);
        textareaLog.setLineWrap(true);
        textareaLog.setWrapStyleWord(true);
        panelBuffer.add(textareaLog, BorderLayout.CENTER);

        textfieldInput = new JTextField();
        textfieldInput.setOpaque(false);
        textfieldInput.setBorder(null);
        textfieldInput.setFont(ColorConstants.DEFAULT_FONT);
        textfieldInput.addActionListener(e -> {
            // Upon taking input, append the input to the console and send it to the terminal to handle
            textareaLog.append("> " + textfieldInput.getText() + "\n");
            Terminal.handle(textfieldInput.getText());
            textareaLog.setCaretPosition(textareaLog.getText().length());
            textfieldInput.setText("");
        });
        panelBuffer.add(textfieldInput, BorderLayout.PAGE_END);

        scrollpaneLog = new JScrollPane(textareaLog);
        scrollpaneLog.setOpaque(false);
        scrollpaneLog.getViewport().setOpaque(false);
        scrollpaneLog.setBorder(null);
        panelBuffer.add(scrollpaneLog, BorderLayout.CENTER);

        this.add(panelBuffer, BorderLayout.CENTER);

        // Subscribe and listen to new records
        Log.addRecordListener(record -> {
            textareaLog.append(record.getFormattedMessage() + "\n");
            textareaLog.setCaretPosition(textareaLog.getText().length());
        });
    }
}
