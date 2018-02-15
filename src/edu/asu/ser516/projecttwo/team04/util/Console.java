package edu.asu.ser516.projecttwo.team04.util;

import edu.asu.ser516.projecttwo.team04.ui.App;

import java.util.Scanner;

public class Console {
    private static Console _instance;

    public static Console get() {
        if(_instance == null)
            _instance = new Console();

        return _instance;
    }

    private Scanner scan;
    private boolean run;

    private Console() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> _instance.run = false));
        run = true;
    }

    public void start() {
        new Thread(new ConsoleRunnable()).start();
    }

    private class ConsoleRunnable implements Runnable {
        @Override
        public void run() {
            scan = new Scanner(System.in);
            while (run) {
                Log.d("Console started, type \"help\" for available commands", Console.class);
                scan = new Scanner(System.in);
                while (run) {
                    if(scan.hasNext()) {
                        Console.handle(scan.nextLine());
                    }

                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        Log.w("Interrupted thread in Console", Console.class);
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void handle(String line) {
        if(line.equalsIgnoreCase("help")) {
            Log.d("Valid commands include:" +
                    "\n\t- Uh, we don't have much yet." +
                    "\n\t- \"help\" to view this help list.", Console.class);
        } else if (line.equalsIgnoreCase("server")) {
            App.getInstance().setType(App.TYPE_SERVER);
        } else if (line.equalsIgnoreCase("client")) {
            App.getInstance().setType(App.TYPE_CLIENT);
        } else {
            Log.d("Invalid command, type \"help\" for list of all commands", Console.class);
        }
    }
}
