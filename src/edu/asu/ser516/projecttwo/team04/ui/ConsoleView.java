package edu.asu.ser516.projecttwo.team04.ui;

import edu.asu.ser516.projecttwo.team04.util.Log;
import edu.asu.ser516.projecttwo.team04.util.Terminal;
import edu.asu.ser516.projecttwo.team04.util.UIStandards;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        this.init();
    }

    public void init() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.black));

        labelHeader = new JLabel("Console:");
        labelHeader.setFont(UIStandards.DEFAULT_FONT);
        this.add(labelHeader, BorderLayout.PAGE_START);

        textareaLog = new JTextArea();
        textareaLog.setEditable(false);
        textareaLog.setRows(5);
        textareaLog.setOpaque(false);
        textareaLog.setFont(UIStandards.SMALL_FONT);
        textareaLog.setLineWrap(true);
        textareaLog.setWrapStyleWord(true);
        this.add(textareaLog, BorderLayout.CENTER);


        textfieldInput = new JTextField();
        textfieldInput.setOpaque(false);
        textfieldInput.setBorder(null);
        textfieldInput.setFont(UIStandards.DEFAULT_FONT);
        textfieldInput.addActionListener(e -> {
            textareaLog.append("> " + textfieldInput.getText() + "\n");
            Terminal.handle(textfieldInput.getText());
            textareaLog.setCaretPosition(textareaLog.getText().length());
            textfieldInput.setText("");
        });
        this.add(textfieldInput, BorderLayout.PAGE_END);

        // Subscribe and listen to new records
        Log.addRecordListener(record -> {
            textareaLog.append(record.getFormattedMessage() + "\n");
            textareaLog.setCaretPosition(textareaLog.getText().length());
        });
    }
}
