package team04.project3.util;

import team04.project3.listeners.ConsoleRecordListener;

import java.util.ArrayList;

/**
 * Handles all system logging, to both terminal and console
 * @author  David Henderson (dchende2@asu.edu)
 */
public class Log {
    /**
     * Level of urgency of a log message
     */
    public enum POLICY {
        VERBOSE(0, "VERBOSE"),
        DEBUG(1, "DEBUG"),
        INFO(2, "INFO"),
        WARNING(3, "WARNING"),
        ERROR(4, "ERROR");

        private final int _level;
        private final String _name;

        /**
         * The enumeration with a level of severity and the name of it
         * @param level Severity level of the policy
         * @param name The name of the policy
         */
        private POLICY(int level, String name) {
            this._level = level;
            this._name = name;
        }

        /**
         * gReturns the record's urgency level
         * @return Policy's level
         */
        public int getLevel() {
            return this._level;
        }

        /**
         * Returns the policy name
         * @return Policy's name
         */
        public String getName() {
            return this._name;
        }

        /**
         * Compares two policies to check which is worse
         * @param POLICY Second policy to compare
         * @return Whether this policy is equal or worse than input POLICY
         */
        public boolean isEqualOrWorse(POLICY POLICY) {
            return this._level >= POLICY._level;
        }
    }

    private static POLICY CONSOLE_POLICY = POLICY.ERROR;
    private static int _longestPolicyNameLength = -1;
    private static transient ArrayList<ConsoleRecordListener> _recordListeners = new ArrayList<>();

    /**
     * To notify any listeners for system log messages
     * @param formattedMessage, a formatted string with all the info in a log message (class, policy, message)
     * @param message, the message without policy or class
     * @param policy, the policy of the log message
     * @param javaClass, the originating Java class
     */
    private static void record(String formattedMessage, String message, POLICY policy, Class javaClass) {
        LogRecord record = new LogRecord(formattedMessage, message, policy, javaClass);
        notifyRecordAdded(record);
    }

    /**
     * Adds a listener to subscribe and listen to new Log messages
     * @param listener, the listener to notify
     */
    public static void addRecordListener(ConsoleRecordListener listener) {
        _recordListeners.add(listener);
    }

    /**
     * Private method to notify all listeners of a new log record
     * @param record, the record added
     */
    private static void notifyRecordAdded(LogRecord record) {
        for(ConsoleRecordListener listener : _recordListeners) {
            listener.added(record);
        }
    }

    /**
     * Log system messages to the terminal and console
     * @param msg, the message to display
     * @param javaClass, the originating Java class
     * @param policy, the log policy of the message
     */
    public static void log(String msg, Class javaClass, POLICY policy) {
        // Formatting
        if(_longestPolicyNameLength < 0) {
            for(POLICY POLICY : POLICY.values()) {
                if(POLICY.getName().length() > _longestPolicyNameLength)
                    _longestPolicyNameLength = POLICY.getName().length();
            }
            _longestPolicyNameLength += 3;
        }

        String message = String.format(
                "%" + _longestPolicyNameLength + "s",
                "[" + policy.getName() + "] ")
                + javaClass.getSimpleName() + ": "
                + msg;

        // Print to terminal
        System.out.println( message );

        if(policy.isEqualOrWorse(Log.getConsolePolicy())) {
            // Print to console
            Log.record(message, msg, policy, javaClass);
        }
    }

    /**
     * Log an error message
     * @param msg Message to log
     * @param javaClass Class log message is from
     */
    public static void error(String msg, Class javaClass) {
        Log.log(msg, javaClass, POLICY.ERROR);
    }

    /**
     * Log an error message (Shorthand for "error")
     * @param msg Message to log
     * @param javaClass Class log message is from
     */
    public static void e(String msg, Class javaClass) {
        Log.error(msg, javaClass);
    }

    /**
     * Log an warn message
     * @param msg Message to log
     * @param javaClass Class log message is from
     */
    public static void warn(String msg, Class javaClass) {
        Log.log(msg, javaClass, POLICY.WARNING);
    }

    /**
     * Log an warn message (Shorthand for "warn")
     * @param msg Message to log
     * @param javaClass Class log message is from
     */
    public static void w(String msg, Class javaClass) {
        Log.warn(msg, javaClass);
    }

    /**
     * Log an info message
     * @param msg Message to log
     * @param javaClass Class log message is from
     */
    public static void info(String msg, Class javaClass) {
        Log.log(msg, javaClass, POLICY.INFO);
    }

    /**
     * Log an info message (Shorthand for "info")
     * @param msg Message to log
     * @param javaClass Class log message is from
     */
    public static void i(String msg, Class javaClass) {
        Log.info(msg, javaClass);
    }

    /**
     * Log an debug message
     * @param msg Message to log
     * @param javaClass Class log message is from
     */
    public static void debug(String msg, Class javaClass) {
        Log.log(msg, javaClass, POLICY.DEBUG);
    }

    /**
     * Log an debug message (Shorthand for "debug")
     * @param msg Message to log
     * @param javaClass Class log message is from
     */
    public static void d(String msg, Class javaClass) {
        Log.debug(msg, javaClass);
    }

    /**
     * Log an verbose message
     * @param msg Message to log
     * @param javaClass Class log message is from
     */
    public static void verbose(String msg, Class javaClass) {
        Log.log(msg, javaClass, POLICY.VERBOSE);
    }

    /**
     * Log an verbose message (Shorthand for "verbose")
     * @param msg Message to log
     * @param javaClass Class log message is from
     */
    public static void v(String msg, Class javaClass) {
        Log.verbose(msg, javaClass);
    }

    /**
     * Set the minimum policy to display in the console and terminal
     * @param POLICY The minimum policy to display in console
     */
    public static void setConsolePolicy(POLICY POLICY) {
        CONSOLE_POLICY = POLICY;
    }

    /**
     * Get the minimum policy to display in the console and terminal
     * @return The minimum policy to display in console
     */
    public static POLICY getConsolePolicy() {
        return CONSOLE_POLICY;
    }

    /**
     * Specific method to set the policy from the initial program startup arguments
     * @param arg String argument to parse into policy
     */
    public static void setPoliciesFromArg(String arg) {
        if(Util.isInteger(arg)) {
            int argLevel = Integer.parseInt(arg);
            for(Log.POLICY POLICY : Log.POLICY.values()) {
                if(POLICY.getLevel() == argLevel) {
                    Log.setConsolePolicy(POLICY);
                    break;
                }
            }
        } else if(!arg.startsWith("-")) {
            boolean match = false;

            for(Log.POLICY POLICY : Log.POLICY.values()) {
                if(POLICY.getName().equalsIgnoreCase(arg)) {
                    Log.setConsolePolicy(POLICY);
                    match = true;
                    break;
                }
            }

            if(!match) {
                Log.setConsolePolicy(Log.POLICY.VERBOSE);
                Log.d("Unable to match log level \"" + arg + "\", defaulting to VERBOSE", Log.class);
            }
        } else {
            Log.setConsolePolicy(Log.POLICY.VERBOSE);
        }
    }
}