package team04.project3.ui.server;

import team04.project3.constants.ColorConstants;
import team04.project3.constants.TextConstants;
import team04.project3.util.Log;
import team04.project3.util.LogRecord;
import team04.project3.util.Terminal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * UI element to display system messages through a console
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ServerConsoleView extends JPanel {
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private JLabel labelHeader;
    private JScrollPane scrollpaneLog;
    private JTextArea textareaLog;
    private JButton buttonClear;
    private JTextField textfieldInput;
    public boolean timestamp = false;

    /**
     * Constructor for the view containing the console
     */
    public ServerConsoleView() {
        // Create transparent border around class
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.setBorder(new EmptyBorder(8, 8, 8, 8));

        // Actual content pane (which the transparent border encompasses)
        JPanel panelBuffer = new JPanel(new BorderLayout());
        panelBuffer.setBackground(ColorConstants.BACKGROUND_GRAY);
        panelBuffer.setBorder(BorderFactory.createLineBorder(Color.black));

        labelHeader = new JLabel("Console:");
        labelHeader.setFont(TextConstants.SMALL_FONT);
        panelBuffer.add(labelHeader, BorderLayout.PAGE_START);

        textareaLog = new JTextArea();
        textareaLog.setEditable(false);
        textareaLog.setRows(6);
        textareaLog.setOpaque(false);
        textareaLog.setFont(TextConstants.SMALL_FONT);
        textareaLog.setLineWrap(true);
        textareaLog.setWrapStyleWord(true);
        panelBuffer.add(textareaLog, BorderLayout.CENTER);

        JPanel panelConsoleInput = new JPanel();
        panelConsoleInput.setLayout(new BoxLayout(panelConsoleInput, BoxLayout.X_AXIS));
        panelConsoleInput.setOpaque(false);
        panelBuffer.add(panelConsoleInput, BorderLayout.PAGE_END);

        buttonClear = new JButton("Clear");
        buttonClear.setMinimumSize(new Dimension(16, 32));
        buttonClear.setPreferredSize(new Dimension(64, 32));
        buttonClear.setBorder(null);
        buttonClear.setBackground(ColorConstants.BACKGROUND_BLUE);
        buttonClear.setFocusPainted(false);
        buttonClear.addActionListener(e -> {
            textareaLog.setText("");
            textfieldInput.setText("");
        });
        panelConsoleInput.add(buttonClear);

        panelConsoleInput.add(Box.createHorizontalStrut(8));

        textfieldInput = new JTextField();
        textfieldInput.setOpaque(false);
        textfieldInput.setBorder(null);
        textfieldInput.setFont(TextConstants.DEFAULT_FONT);
        textfieldInput.addActionListener(e -> {
            // Upon taking input, append the input to the console and send it to the terminal to handle
            if(timestamp) {
                // Show a timestamp
                textareaLog.append(timestampFormat.format(new Date()) + " > " + textfieldInput.getText() + "\n");
            } else {
                // Don't show a timestamp
                textareaLog.append("> " + textfieldInput.getText() + "\n");
            }

            Terminal.handle(textfieldInput.getText());
            textareaLog.setCaretPosition(textareaLog.getText().length());
            textfieldInput.setText("");
        });
        panelConsoleInput.add(textfieldInput);

        scrollpaneLog = new JScrollPane(textareaLog);
        scrollpaneLog.setOpaque(false);
        scrollpaneLog.getViewport().setOpaque(false);
        scrollpaneLog.setBorder(null);
        panelBuffer.add(scrollpaneLog, BorderLayout.CENTER);

        this.add(panelBuffer, BorderLayout.CENTER);

        // Subscribe and listen to new records
        Log.addRecordListener(record -> {
            ServerConsoleView.this.handleLogRecord(record);
        });
    }

    /**
     * Adds a new log record to the textarea
     * @param record New record to add
     */
    private void handleLogRecord(LogRecord record) {
        String msg;

        if(record.getOriginatingClass() == Terminal.class && !record.getOriginatingPolicy().isEqualOrWorse(Log.POLICY.WARNING)) {
            // If the record is from the terminal and not a warning or error (from handling user input)
            msg = "- " + record.getMessage();
        } else {
            // Otherwise display message
            msg = record.getFormattedMessage();
        }

        // Add timestamps if necessary
        if(timestamp)
            msg = timestampFormat.format(new Date(record.getTimestamp())) + " " + msg;

        textareaLog.append(msg + "\n");
        textareaLog.setCaretPosition(textareaLog.getText().length());
    }
}