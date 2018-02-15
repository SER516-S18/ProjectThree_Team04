package edu.asu.ser516.projecttwo.team04;

import edu.asu.ser516.projecttwo.team04.ui.App;
import edu.asu.ser516.projecttwo.team04.util.Console;
import edu.asu.ser516.projecttwo.team04.util.Log;

public class Main {
    private Main() {}

    public static void main(final String[] args) {
        Log.setPolicies(Log.POLICY.VERBOSE);

        // Args
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];

                // Args - Pass others to console
                if(arg.startsWith("-") && i < args.length - 1) {
                    String line = "";
                    for (int j = i + 1; j < args.length; j++) {
                        String argj = args[j];
                        if(!argj.startsWith("-"))
                            line += " " + argj;

                        if ((argj.startsWith("-") || j == args.length - 1) && line.length() > 0) {
                            String temp = arg.substring(1) + line;
                            Log.i("Passing program argument \"" + temp + "\" to console", Main.class);
                            Console.handle(temp);
                            break;
                        }
                    }
                }
            }
        }

        // Start console
        Console.get().start();

        // Start UI
        App.getInstance().init();
    }
}
