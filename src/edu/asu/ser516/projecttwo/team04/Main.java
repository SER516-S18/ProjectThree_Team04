package edu.asu.ser516.projecttwo.team04;

import edu.asu.ser516.projecttwo.team04.ui.AppView;
import edu.asu.ser516.projecttwo.team04.util.Log;
import edu.asu.ser516.projecttwo.team04.util.Terminal;

/**
 * Main, contains Java main, arg processing, console initialization, and UI initialization
 * Pass "-init server" or "-init client" to start the program as a server or client
 *
 * @author  David Henderson (dchende2@asu.edu)
 * @version 1.0
 * @since   2018-02-15
 */

public class Main {
    // Private constructor, container for Java main
    private Main() {}

    /**
     * Java main for both the client and server
     * @param args String arguments
     */
    public static void main(final String[] args) {
        Log.setPolicies(Log.POLICY.VERBOSE);

        // Argument processing (to console)
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];

                // Args - Log mode
                if (arg.equalsIgnoreCase("-d") || arg.equalsIgnoreCase("--dev")) {
                    Log.setPoliciesFromArg(args[i + 1]);
                }

                // Args - Pass others to console
                else if(arg.startsWith("-") && i < args.length - 1) {
                    String line = "";
                    for (int j = i + 1; j < args.length; j++) {
                        String argj = args[j];
                        if(!argj.startsWith("-"))
                            line += " " + argj;

                        if ((argj.startsWith("-") || j == args.length - 1) && line.length() > 0) {
                            String temp = arg.substring(1) + line;
                            Log.i("Passing program argument \"" + temp + "\" to console", Main.class);
                            Terminal.handle(temp);
                            break;
                        }
                    }
                }
            }
        }

        // Start console
        Terminal.get().start();

        // Start UI
        AppView.get().init();
    }
}
