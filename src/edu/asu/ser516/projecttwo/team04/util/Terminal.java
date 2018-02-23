package edu.asu.ser516.projecttwo.team04.util;

import edu.asu.ser516.projecttwo.team04.model.client.ClientModel;
import edu.asu.ser516.projecttwo.team04.model.server.ServerModel;
import edu.asu.ser516.projecttwo.team04.ui.AppView;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
                    "\n\t- \"init ('client' | 'server')\" to initialize the app as a client or server" +
                    "\n\t- \"port <#>\" to set the communication port number" +
                    "\n\t- \"host ('localhost' | <hostname or ip>)\" to set host to connect to as a client" +
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
        } else if(line.toLowerCase().startsWith("host") && words.length == 2) {
            if(AppView.get().isClient()) {
                if (words[1].equalsIgnoreCase("localhost")) {
                    ClientModel.get().setHostToLocalhost();
                    Log.d("Host set to localhost", Terminal.class);
                } else {
                    try {
                        ClientModel.get().setHost(InetAddress.getByName(words[1]));
                        Log.d("Host set to " + words[1], Terminal.class);
                    } catch (UnknownHostException e) {
                        Log.d("Failed set host (Host is invalid or unavailable)", Terminal.class);
                    }
                }
            } else {
                Log.d("Failed to set host (Application must be set as client)", Terminal.class);
            }
        } else {
            Log.d("Invalid command, type \"help\" for list of all commands", Terminal.class);
        }
    }
}
