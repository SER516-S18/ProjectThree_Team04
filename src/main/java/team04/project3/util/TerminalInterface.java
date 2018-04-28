package team04.project3.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

import team04.project3.model.client.ClientModel;
import team04.project3.model.server.ServerModel;

public interface TerminalInterface {

	static Terminal _instance = null;

    /**
     * Singleton pattern getter
     */
    public static void get() {}

    Scanner scan = null;
    boolean run = false;
    

    /**
     * Creates the terminal, and allows shutting down when triggered through a disconnect hook
     * @return 
     */
    void Terminal();

    /**
     * Starts the Terminal thread to accept input
     */
    public void start();

    /**
     * Large conditional chain to handle input from a console, accepts the user input line to handle
     * @param line Line of text to handle (input)
     */
    public static void handle(String line) {}

}
