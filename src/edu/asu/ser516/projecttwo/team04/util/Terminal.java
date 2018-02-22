package edu.asu.ser516.projecttwo.team04.util;

import edu.asu.ser516.projecttwo.team04.ClientModel;
import edu.asu.ser516.projecttwo.team04.ServerModel;
import edu.asu.ser516.projecttwo.team04.ui.AppView;

import java.util.Scanner;

/**
 * Terminal, a non-UI singleton for terminal user input
 *
 * @author  David Henderson (dchende2@asu.edu)
 * @version 1.0
 * @since   2018-02-15
 */
public class Terminal {
    private static Terminal _instance;

    /**
     * Singleton pattern getter
     * @return The only terminal instance
     */
    public static Terminal get() {
        if(_instance == null)
            _instance = new Terminal();

        return _instance;
    }

    private Scanner scan;
    private boolean run;

    /**
     * Creates the terminal, and allows shutting down when triggered through a shutdown hook
     */
    private Terminal() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> _instance.run = false));
        run = true;
    }

    /**
     * Starts the Terminal thread to accept input
     */
    public void start() {
        new Thread(() -> {
            scan = new Scanner(System.in);
            while (run) {
                Log.d("Terminal started, type \"help\" for available commands", Terminal.class);
                scan = new Scanner(System.in);
                while (run) {
                    if(scan.hasNext()) {
                        Terminal.handle(scan.nextLine());
                    }

                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        Log.w("Interrupted thread in Terminal", Terminal.class);
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * Large conditional chain to handle input from a console, accepts the user input line to handle
     * @param line
     */
    public static void handle(String line) {
        String[] words = line.split(" ");
        if(line.equalsIgnoreCase("help")) {
            Log.d("Valid commands include:" +
                    "\n\t- \"init (client | server)\" to initialize the app as a client or server" +
                    "\n\t- \"port (#)\" to set the communication port number" +
                    "\n\t- \"channels (#)\" to set the number of channels in a client" +
                    "\n\t- \"help\" to view this help list.", Terminal.class);
        } else if (line.toLowerCase().startsWith("init") && words.length == 2) {
            if(words[1].equalsIgnoreCase("server"))
                AppView.get().setType(AppView.TYPE_SERVER);
            else if(words[1].equalsIgnoreCase("client"))
                AppView.get().setType(AppView.TYPE_CLIENT);
        } else if (line.toLowerCase().startsWith("port") && words.length == 2 && Util.isInteger(words[1])) {
            int port = Integer.parseInt(words[1]);
            ClientModel.get().setPort(port);
            ServerModel.get().setPort(port);
            Log.d("Port set to " + port, Terminal.class);
        } else if(line.toLowerCase().startsWith("channels") && words.length == 2 && Util.isInteger(words[1])) {
            if(AppView.get().isClient()) {
                ClientModel.get().setChannelCount(Integer.parseInt(words[1]));
                Log.d("Channel count set to " + words[1], Terminal.class);
            } else {
                Log.d("Failed to set channel count (Must be a client app to set channels)", Terminal.class);
            }
        } else {
            Log.d("Invalid command, type \"help\" for list of all commands", Terminal.class);
        }
    }
}
