package edu.asu.ser516.projecttwo.team04.ui;

import edu.asu.ser516.projecttwo.team04.util.Log;
import edu.asu.ser516.projecttwo.team04.util.UIStandards;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * ConsoleView, UI element to display system messages through a console
 */
public class ConsoleView extends JPanel {
    private JLabel labelHeader;
    private JScrollPane scrollpaneLog;
    private JTextArea textareaLog;

    public ConsoleView() {
        this.init();
    }

    public void init() {
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(8, 8, 8, 8));

        labelHeader = new JLabel("Console:");
        labelHeader.setFont(UIStandards.DEFAULT_FONT);
        this.add(labelHeader, BorderLayout.PAGE_START);

        textareaLog = new JTextArea();
        textareaLog.setEditable(false);
        textareaLog.setRows(8);
        textareaLog.setOpaque(false);
        textareaLog.setFont(UIStandards.DEFAULT_FONT);

        scrollpaneLog = new JScrollPane(textareaLog);
        scrollpaneLog.setOpaque(false);
        scrollpaneLog.setBorder(null);
        this.add(scrollpaneLog, BorderLayout.CENTER);

        // Subscribe and listen to new records
        Log.addRecordListener(record -> {
            textareaLog.append(record.getFormattedMessage() + "\n");
            textareaLog.setCaretPosition(textareaLog.getText().length());
        });
    }
}
