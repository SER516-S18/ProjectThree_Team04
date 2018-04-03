package team04.project3.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import java.awt.Font;

/**
 * Creates the UI for all the sliders present in the Server
 * @author  Drishty Kapoor (dkapoor3@asu.edu)
 */
public class ServerSlider extends JFrame {
    
    private JPanel contentPane;
    
    /**
     * Creates the frame.
     */
    public ServerSlider() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 750);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(112,112,112));
        setContentPane(contentPane);
        
        JSlider blinkSlider = new JSlider(0, 100, 50);
        blinkSlider.setBounds(220, 6, 334, 43);
        blinkSlider.setMajorTickSpacing(25);
        blinkSlider.setPaintTicks(true);
        
        java.util.Hashtable<Integer,JLabel> labelTable = new java.util.Hashtable<Integer,JLabel>();
        labelTable.put(new Integer(100), new JLabel("1.0"));
        labelTable.put(new Integer(75), new JLabel("0.75"));
        labelTable.put(new Integer(50), new JLabel("0.50"));
        labelTable.put(new Integer(25), new JLabel("0.25"));
        labelTable.put(new Integer(0), new JLabel("0.0"));
        
        blinkSlider.setLabelTable( labelTable );
        blinkSlider.setPaintLabels(true);
        contentPane.setLayout(null);
        
        JLabel blink = new JLabel("Blink");
        blink.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        blink.setBounds(104, 15, 61, 16);
        blink.setForeground(Color.WHITE);
        contentPane.add(blink);
        contentPane.add(blinkSlider);
        
        JLabel labelRightWink = new JLabel("Right Wink");
        labelRightWink.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        labelRightWink.setBounds(104, 70, 82, 16);
        labelRightWink.setForeground(Color.WHITE);
        contentPane.add(labelRightWink);
        
        JSlider rightWinkSlider = new JSlider(0, 100, 50);
        rightWinkSlider.setBounds(220, 61, 334, 43);
        rightWinkSlider.setPaintTicks(true);
        rightWinkSlider.setPaintLabels(true);
        rightWinkSlider.setMajorTickSpacing(25);
        contentPane.add(rightWinkSlider);
        rightWinkSlider.setLabelTable( labelTable );
        
        JLabel labelLeftWink = new JLabel("Left Wink");
        labelLeftWink.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        labelLeftWink.setBounds(104, 125, 70, 16);
        labelLeftWink.setForeground(Color.WHITE);
        contentPane.add(labelLeftWink);
        
        JSlider leftWinkSlider = new JSlider(0, 100, 50);
        leftWinkSlider.setBounds(220, 171, 334, 43);
        leftWinkSlider.setPaintTicks(true);
        leftWinkSlider.setPaintLabels(true);
        leftWinkSlider.setMajorTickSpacing(25);
        contentPane.add(leftWinkSlider);
        leftWinkSlider.setLabelTable( labelTable );
        
        JLabel labelLookRl = new JLabel("Look R/L");
        labelLookRl.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        labelLookRl.setBounds(104, 180, 61, 16);
        labelLookRl.setForeground(Color.WHITE);
        contentPane.add(labelLookRl);
        
        JSlider lookRlSlider = new JSlider(0, 100, 50);
        lookRlSlider.setBounds(220, 116, 334, 43);
        lookRlSlider.setPaintTicks(true);
        lookRlSlider.setPaintLabels(true);
        lookRlSlider.setMajorTickSpacing(25);
        contentPane.add(lookRlSlider);
        lookRlSlider.setLabelTable( labelTable );
        
        JLabel labelFurrowBrow = new JLabel("Furrow Brow");
        labelFurrowBrow.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        labelFurrowBrow.setBounds(104, 266, 93, 16);
        labelFurrowBrow.setForeground(Color.WHITE);
        contentPane.add(labelFurrowBrow);
        
        JSlider furrowBrowSlider = new JSlider(0, 100, 50);
        furrowBrowSlider.setBounds(220, 256, 334, 43);
        furrowBrowSlider.setPaintTicks(true);
        furrowBrowSlider.setPaintLabels(true);
        furrowBrowSlider.setMajorTickSpacing(25);
        contentPane.add(furrowBrowSlider);
        furrowBrowSlider.setLabelTable( labelTable );
        
        JLabel labelRaiseBrow = new JLabel("Raise Brow");
        labelRaiseBrow.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        labelRaiseBrow.setBounds(104, 324, 82, 16);
        labelRaiseBrow.setForeground(Color.WHITE);
        contentPane.add(labelRaiseBrow);
        
        JSlider raiseBrowSlider = new JSlider(0, 100, 50);
        raiseBrowSlider.setBounds(220, 311, 334, 43);
        raiseBrowSlider.setPaintTicks(true);
        raiseBrowSlider.setPaintLabels(true);
        raiseBrowSlider.setMajorTickSpacing(25);
        contentPane.add(raiseBrowSlider);
        raiseBrowSlider.setLabelTable( labelTable );
        
        JLabel labelSmile = new JLabel("Smile");
        labelSmile.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        labelSmile.setBounds(104, 403, 61, 16);
        labelSmile.setForeground(Color.WHITE);
        contentPane.add(labelSmile);
        
        JSlider smileSlider = new JSlider(0, 100, 50);
        smileSlider.setBounds(220, 393, 334, 43);
        smileSlider.setPaintTicks(true);
        smileSlider.setPaintLabels(true);
        smileSlider.setMajorTickSpacing(25);
        contentPane.add(smileSlider);
        smileSlider.setLabelTable( labelTable );
        
        JLabel labelClench = new JLabel("Clench");
        labelClench.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        labelClench.setBounds(104, 461, 61, 16);
        labelClench.setForeground(Color.WHITE);
        contentPane.add(labelClench);
        
        JSlider clenchSlider = new JSlider(0, 100, 50);
        clenchSlider.setBounds(220, 448, 334, 43);
        clenchSlider.setPaintTicks(true);
        clenchSlider.setPaintLabels(true);
        clenchSlider.setMajorTickSpacing(25);
        contentPane.add(clenchSlider);
        clenchSlider.setLabelTable( labelTable );
        
        JLabel labelLeftSmirk = new JLabel("Left Smirk");
        labelLeftSmirk.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        labelLeftSmirk.setBounds(104, 515, 82, 16);
        labelLeftSmirk.setForeground(Color.WHITE);
        contentPane.add(labelLeftSmirk);
        
        JSlider leftSmirkSlider = new JSlider(0, 100, 50);
        leftSmirkSlider.setBounds(220, 505, 334, 43);
        leftSmirkSlider.setPaintTicks(true);
        leftSmirkSlider.setPaintLabels(true);
        leftSmirkSlider.setMajorTickSpacing(25);
        contentPane.add(leftSmirkSlider);
        leftSmirkSlider.setLabelTable( labelTable );
        
        JLabel labelRightSmirk = new JLabel("Right Smirk");
        labelRightSmirk.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        labelRightSmirk.setBounds(104, 573, 82, 16);
        labelRightSmirk.setForeground(Color.WHITE);
        contentPane.add(labelRightSmirk);
        
        JSlider rightSmirkSlider = new JSlider(0, 100, 50);
        rightSmirkSlider.setBounds(220, 560, 334, 43);
        rightSmirkSlider.setPaintTicks(true);
        rightSmirkSlider.setPaintLabels(true);
        rightSmirkSlider.setMajorTickSpacing(25);
        contentPane.add(rightSmirkSlider);
        rightSmirkSlider.setLabelTable( labelTable );
        
        JLabel labelLaugh = new JLabel("Laugh");
        labelLaugh.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        labelLaugh.setBounds(104, 648, 61, 16);
        labelLaugh.setForeground(Color.WHITE);
        contentPane.add(labelLaugh);
        
        JSlider laughSlider = new JSlider(0, 100, 50);
        laughSlider.setBounds(220, 639, 334, 43);
        laughSlider.setPaintTicks(true);
        laughSlider.setPaintLabels(true);
        laughSlider.setMajorTickSpacing(25);
        contentPane.add(laughSlider);
        laughSlider.setLabelTable( labelTable );
    }
}
