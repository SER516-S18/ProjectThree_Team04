import edu.asu.ser516.projecttwo.team04.ui.AppView;
import edu.asu.ser516.projecttwo.team04.util.Log;
import edu.asu.ser516.projecttwo.team04.util.Terminal;

/**
 * ServerApp, contains Java main, arg processing, console initialization, and UI initialization
 *
 * @author  David Henderson (dchende2@asu.edu)
 * @version 1.0
 * @since   2018-02-22
 */
public class ServerApp {
    // Private constructor, container for Java main
    private ServerApp() {}

    /**
     * Java main for both the Server
     * @param args String arguments
     */
    public static void main(final String[] args) {
        // Show verbose messages in the log
        Log.setPolicies(Log.POLICY.VERBOSE);

        // Set the application type to Server
        AppView.get().setType(AppView.TYPE_SERVER);

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
                            Log.i("Passing program argument \"" + temp + "\" to console", ServerApp.class);
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
