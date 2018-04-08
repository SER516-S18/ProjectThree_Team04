package team04.project3.ui.server;

import team04.project3.constants.TextConstants;
import team04.project3.listeners.ServerListener;
import team04.project3.model.server.ServerModel;
import team04.project3.util.Log;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ServerValuesView extends JPanel {

    private JTextField textfieldTime;
    private Float textfieldSelected = null;

    public ServerValuesView() {
        ServerModel.get().addListener(new ServerListener() {
            @Override
            public void started() { }

            @Override
            public void shutdown() { }

            @Override
            public void clientConnected() { }

            @Override
            public void clientDisconnected() { }

            @Override
            public void packetSent() {
                if(textfieldSelected == null)
                    textfieldTime.setText(Float.toString(ServerModel.get().getTick()));
            }

            @Override
            public void packetRepeatingToggled(boolean repeating) { }
        });
        this.init();
    }

    private void init() {
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel panelTop = new JPanel();
        panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.X_AXIS));
        this.add(panelTop, BorderLayout.PAGE_START);

        JLabel labelTime = new JLabel("Time:");
        labelTime.setFont(TextConstants.LARGE_FONT);
        panelTop.add(labelTime);

        panelTop.add(Box.createHorizontalStrut(8));

        textfieldTime = new JTextField(Float.toString(ServerModel.get().getTick()));
        textfieldTime.setFont(TextConstants.LARGE_FONT);
        textfieldTime.setMaximumSize(new Dimension(128, 128));
        textfieldTime.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent event) {
                textfieldSelected = ServerModel.get().getTick();
            }

            @Override
            public void focusLost(FocusEvent event) {
                try {
                    Float tick = Float.parseFloat(textfieldTime.getText());
                    if(!textfieldSelected.equals(tick))
                        ServerModel.get().setTick(tick);
                    else
                        textfieldTime.setText(Float.toString(ServerModel.get().getTick()));
                } catch(NumberFormatException e) {
                    Log.w("Failed to parse time (" + e.getMessage() + ")", ServerValuesView.class);
                    textfieldTime.setText(Float.toString(ServerModel.get().getTick()));
                }

                textfieldSelected = null;
            }
        });
        panelTop.add(textfieldTime);

        panelTop.add(Box.createHorizontalStrut(8));

        JLabel labelTimeSeconds = new JLabel("sec");
        labelTimeSeconds.setFont(TextConstants.LARGE_FONT);
        panelTop.add(labelTimeSeconds);
    }
}
