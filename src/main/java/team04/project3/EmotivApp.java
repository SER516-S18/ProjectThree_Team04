package team04.project3;

import team04.project3.ui.client.ClientView;
import team04.project3.util.Log;
import team04.project3.util.Terminal;

/**
 * Contains Java main, arg processing, console initialization, and UI initialization
 *
 * @author  David Henderson (dchende2@asu.edu)
 * @version 1.0
 * @since   2018-02-22
 */
public class EmotivApp {
    /**
     * Private constructor as the Class is mainly a wrapper for the Java main
     */
    private EmotivApp() {}

    /**
     * Java main for the Client
     * @param args String arguments
     */
    public static void main(final String[] args) {
        // Show verbose messages in the log
        Log.setConsolePolicy(Log.POLICY.DEBUG);

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
                            Log.i("Passing program argument \"" + temp + "\" to console", EmotivApp.class);
                            Terminal.handle(temp);
                            break;
                        }
                    }
                }
            }
        }

        ClientView.getInstance().init();
    }
}
