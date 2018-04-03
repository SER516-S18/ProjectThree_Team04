package team04.project3.ui;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Creates a menu bar for opening the server, facial expressions and performance metrics menu items
 * @author vkasam@asu.edu
 */

public class ClientMenuView extends JFrame{

    ImageIcon serverIcon = new ImageIcon(getClass().getResource("/team04/project3/images/server.gif"));
    Image serverImage = serverIcon.getImage();
    Image scaledServerImg = serverImage.getScaledInstance(30,30,Image.SCALE_SMOOTH);
    private JMenuItem emotivXavierComposerItem = new JMenuItem("Emotiv Xavier Composer",
            new ImageIcon(scaledServerImg));

    private JMenuItem emotivXavierEmoKeyItem = new JMenuItem("Emotiv Xavier EmoKey");
    private JMenuItem saveCurrentScreenshotItem = new JMenuItem("Save Current Screenshot(Ctrl+S)");


    private JMenuItem connectDriverItem = new JMenuItem("Connect Driver");
    private JMenuItem connectComposerItem = new JMenuItem("Connect Composer");
    private JMenuItem reconnectDriverItem = new JMenuItem("Reconnect Driver");

    private JMenuItem mentalCommandsItem = new JMenuItem("Mental Commands");

    ImageIcon facialExpressionsIcon = new ImageIcon(getClass().getResource("/team04/project3/images/facialexpressions.gif"));
    Image facialExpressionsImage = facialExpressionsIcon.getImage();
    Image scaledFacialExpressionsImg = facialExpressionsImage.getScaledInstance(30,30,Image.SCALE_SMOOTH);
    private JMenuItem facialExpressionsItem = new JMenuItem("Facial Expressions",
            new ImageIcon(scaledFacialExpressionsImg));

    ImageIcon performanceMetricsIcon = new ImageIcon(getClass().getResource("/team04/project3/images/performanceMetrics.gif"));
    Image performanceMetricsImage = performanceMetricsIcon.getImage();
    Image scaledPerformanceMetricsImg = performanceMetricsImage.getScaledInstance(30,30,Image.SCALE_SMOOTH);
    private JMenuItem performanceMetricsItem = new JMenuItem("Performance Metrics",
            new ImageIcon(scaledPerformanceMetricsImg));

    private JMenuItem initialSensorsItem = new JMenuItem("Initial Sensors");

    private JMenuItem emotivOnGithubItem = new JMenuItem("Emotiv on Github");
    private JMenuItem aboutXavierControlPanelItem = new JMenuItem("About Xavier Control Panel");


    ImageIcon setupIcon = new ImageIcon(getClass().getResource("/team04/project3/images/setup.gif"));
    Image setupImage = setupIcon.getImage();
    Image scaledsetupImg = setupImage.getScaledInstance(30,30,Image.SCALE_SMOOTH);
    private JMenuItem setUpItem = new JMenuItem("Setup",new ImageIcon(scaledsetupImg));

    ImageIcon recordingIcon = new ImageIcon(getClass().getResource("/team04/project3/images/recording.gif"));
    Image recordingImage = recordingIcon.getImage();
    Image scaledRecordingImg = recordingImage.getScaledInstance(30,30,Image.SCALE_SMOOTH);
    private JMenuItem recordingItem = new JMenuItem("Recording",new ImageIcon(scaledRecordingImg));


    ImageIcon reportIcon = new ImageIcon(getClass().getResource("/team04/project3/images/report.gif"));
    Image reportImage = reportIcon.getImage();
    Image scaledReportImg = reportImage.getScaledInstance(30,30,Image.SCALE_SMOOTH);
    private JMenuItem reportsItem = new JMenuItem("Reports",new ImageIcon(scaledReportImg));

    // Constructor for the view containing the menu bar
    public ClientMenuView(){
        JFrame menuFrame = new JFrame();

        JMenuBar clientMenuBar = new JMenuBar();

        clientMenuBar.setSize(80,80);
        JMenu clientMenu = new JMenu("|||");

        JMenu applicationMenu = new JMenu("Application");//,new ImageIcon(scaledApplicationImg));
        JMenu connectMenu = new JMenu("Connect");
        JMenu detectionMenu = new JMenu("Detection");
        JMenu helpMenu = new JMenu("Help");

        // adding the items to the application sub menu
        applicationMenu.add(emotivXavierComposerItem);
        applicationMenu.add(new JSeparator());
        applicationMenu.add(emotivXavierEmoKeyItem);
        applicationMenu.add(new JSeparator());
        applicationMenu.add(saveCurrentScreenshotItem);

        //adding the items to the connect sub menu
        connectMenu.add(connectComposerItem);
        connectMenu.add(new JSeparator());
        connectMenu.add(connectDriverItem);
        connectMenu.add(new JSeparator());
        connectMenu.add(reconnectDriverItem);

        //adding the items to the detection sub menu
        detectionMenu.add(mentalCommandsItem);
        detectionMenu.add(new JSeparator());
        detectionMenu.add(facialExpressionsItem);
        detectionMenu.add(new JSeparator());
        detectionMenu.add(performanceMetricsItem);
        detectionMenu.add(new JSeparator());
        detectionMenu.add(initialSensorsItem);

        //adding the items to the help sub menu
        helpMenu.add(emotivOnGithubItem);
        helpMenu.add(new JSeparator());
        helpMenu.add(aboutXavierControlPanelItem);

        //adding the items to the clientMenu
        clientMenu.add(applicationMenu);
        clientMenu.add(new JSeparator());
        clientMenu.add(connectMenu);
        clientMenu.add(new JSeparator());
        clientMenu.add(setUpItem);
        clientMenu.add(new JSeparator());
        clientMenu.add(detectionMenu);
        clientMenu.add(new JSeparator());
        clientMenu.add(recordingItem);
        clientMenu.add(new JSeparator());
        clientMenu.add(reportsItem);
        clientMenu.add(new JSeparator());
        clientMenu.add(helpMenu);

        // adding the clientMenu to the MenuBar
        clientMenuBar.add(clientMenu);

        // adding the Home page link for the client
        JLabel emotivClient =  new JLabel("  EMOTIV Xavier Control Panel  ");
        clientMenuBar.add(emotivClient);

        // adding a clickable EPOC JLabel
        JLabel epoc = new JLabel("  EPOC  ");
        epoc.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                // open the respective panel or pop up window

            }
        });
        clientMenuBar.add(epoc);

        // adding a clickable Insight JLabel
        JLabel inSight = new JLabel("  Insight  ");
        inSight.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                // open the respective panel or pop up window

            }
        });
        clientMenuBar.add(inSight);

        // adding a clickable Add User JLabel
        JLabel addUser = new JLabel("  Add User  ");
        addUser.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                // open the respective panel or pop up window

            }
        });
        clientMenuBar.add(addUser);

        // adding a clickable Training Profile JLabel
        JLabel trainingProfile = new JLabel("  Training Profile  ");
        trainingProfile.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                // open the respective panel or pop up window

            }
        });
        clientMenuBar.add(trainingProfile);

        // Adding the clock icon to the menu bar
        // ImageIcon clockIcon = new ImageIcon(getClass().getResource("/team04/project3/images/clock.gif"));
        // Image clockImage = clockIcon.getImage();
        // Image scaledClockImg = clockImage.getScaledInstance(30,30,Image.SCALE_SMOOTH);
        // clientMenuBar.add(new ImageIcon(scaledClockImage));

        /** setting the action listener for the facial expressions and
         * opening the server from the client option
         */
        //facialExpressionsItem.addActionListener(this);
        //emotivXavierComposerItem.addActionListener(this);
        //performanceMetricsItem.addActionListener(this);

        menuFrame.setJMenuBar(clientMenuBar);
        menuFrame.setSize(500,500);
        menuFrame.setVisible(true);

    }

    public void actionPerformed(ActionEvent E){
        if(E.getSource().equals(emotivXavierComposerItem)){
            // opens the server
        }
        if(E.getSource().equals(facialExpressionsItem)){
            //opens the facial expression panel
        }
        if(E.getSource().equals(performanceMetricsItem)){
            //opens the performance metrics panel
        }
    }

    // used for testing
    public static void main(String args[]) {
        new ClientMenuView();
    }

}
