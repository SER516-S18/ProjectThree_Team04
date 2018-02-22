package edu.asu.ser516.projecttwo.team04.util;

import edu.asu.ser516.projecttwo.team04.listeners.ConsoleRecordListener;

import java.util.ArrayList;

/**
 * Log, a class that handles all system logging, to both terminal and console
 * @author  David Henderson (dchende2@asu.edu)
 */
public class Log {
    /**
     * Policy, the level of urgency of a log message
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
         *
         * @param level
         * @param name
         */
        private POLICY(int level, String name) {
            this._level = level;
            this._name = name;
        }

        /**
         * getLevel - Returns the record's urgency level
         * @return Policy's level
         */
        public int getLevel() {
            return this._level;
        }

        /**
         * getName - Returns the policy name
         * @return Policy's name
         */
        public String getName() {
            return this._name;
        }

        /**
         * isEqualOrWorse - Compares two policies to check which is worse
         * @param POLICY Second policy to compare
         * @return Whether this policy is equal or worse than input POLICY
         */
        public boolean isEqualOrWorse(POLICY POLICY) {
            return this._level >= POLICY._level;
        }

        /**
         * isWorse - Compares two policies to check which is worse
         * @param POLICY Second policy to compare
         * @return Whether this policy is worse than input POLICY
         */
        public boolean isWorse(POLICY POLICY) {
            return this._level > POLICY._level;
        }
    }

    private static POLICY CONSOLE_POLICY = POLICY.ERROR;
    private static int _length = -1;
    private static transient ArrayList<ConsoleRecordListener> _recordListeners = new ArrayList<>();

    /**
     * Record, to notify any listeners for system log messages
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
     * addRecordListener, adds a listener to subscribe and listen to new Log messages
     * @param listener, the listener to notify
     */
    public static void addRecordListener(ConsoleRecordListener listener) {
        _recordListeners.add(listener);
    }

    /**
     * notifyRecordAdded, private method to notify all listeners of a new log record
     * @param record, the record added
     */
    private static void notifyRecordAdded(LogRecord record) {
        for(ConsoleRecordListener listener : _recordListeners) {
            listener.added(record);
        }
    }

    /**
     * log, to log system messages to the terminal and console
     * @param msg, the message to display
     * @param javaClass, the originating Java class
     * @param policy, the log policy of the message
     */
    public static void log(String msg, Class javaClass, POLICY policy) {
        // Formatting
        if(_length < 0) {
            for(POLICY POLICY : POLICY.values()) {
                if(POLICY.getName().length() > _length)
                    _length = POLICY.getName().length();
            }
            _length += 3;
        }

        String message = null;
        if(policy.isEqualOrWorse(CONSOLE_POLICY)) {
            message = String.format("%" + _length + "s", "[" + policy.getName() + "] ") + javaClass.getSimpleName() + ": " + msg;

            // Print to terminal
            System.out.println( message );

            // Print to console
            Log.record(message, msg, policy, javaClass);
        }
    }

    /**
     * error - Log an error message
     * @param msg Message to log
     * @param javaClass Class log message is from
     */
    public static void error(String msg, Class javaClass) { Log.e(msg, javaClass); }

    /**
     * e - Log an error message
     * @param msg Message to log
     * @param javaClass Class log message is from
     */
    public static void e(String msg, Class javaClass) {
        Log.log(msg, javaClass, POLICY.ERROR);
    }

    /**
     * warn - Log an warn message
     * @param msg Message to log
     * @param javaClass Class log message is from
     */
    public static void warn(String msg, Class javaClass) { Log.w(msg, javaClass); }

    /**
     * w - Log an warn message
     * @param msg Message to log
     * @param javaClass Class log message is from
     */
    public static void w(String msg, Class javaClass) {
        Log.log(msg, javaClass, POLICY.WARNING);
    }

    /**
     * info - Log an info message
     * @param msg Message to log
     * @param javaClass Class log message is from
     */
    public static void info(String msg, Class javaClass) { Log.i(msg, javaClass); }

    /**
     * i - Log an info message
     * @param msg Message to log
     * @param javaClass Class log message is from
     */
    public static void i(String msg, Class javaClass) {
        Log.log(msg, javaClass, POLICY.INFO);
    }

    /**
     * debug - Log an debug message
     * @param msg Message to log
     * @param javaClass Class log message is from
     */
    public static void debug(String msg, Class javaClass) { Log.d(msg, javaClass); }

    /**
     * d - Log an debug message
     * @param msg Message to log
     * @param javaClass Class log message is from
     */
    public static void d(String msg, Class javaClass) {
        Log.log(msg, javaClass, POLICY.DEBUG);
    }

    /**
     * verbose - Log an verbose message
     * @param msg Message to log
     * @param javaClass Class log message is from
     */
    public static void verbose(String msg, Class javaClass) { Log.v(msg, javaClass); }

    /**
     * v - Log an verbose message
     * @param msg Message to log
     * @param javaClass Class log message is from
     */
    public static void v(String msg, Class javaClass) {
        Log.log(msg, javaClass, POLICY.VERBOSE);
    }

    /**
     * Set the minimum policy to display in the console and terminal
     * @param POLICY
     */
    public static void setConsolePolicy(POLICY POLICY) {
        CONSOLE_POLICY = POLICY;
    }

    /**
     * Get the minimum policy to display in the console and terminal
     * @return
     */
    public static POLICY getConsolePolicy() {
        return CONSOLE_POLICY;
    }

    /**
     * setPolicies - Set the console's minimum policy
     * @param policy Policy to set
     */
    public static void setPolicies(POLICY policy) {
        CONSOLE_POLICY = policy;
    }

    /**
     * Specific method to set the policy from the initial program startup arguments
     * @param arg
     */
    public static void setPoliciesFromArg(String arg) {
        if(Util.isInteger(arg)) {
            int argLevel = Integer.parseInt(arg);
            for(Log.POLICY POLICY : Log.POLICY.values()) {
                if(POLICY.getLevel() == argLevel) {
                    Log.setPolicies(POLICY);
                    break;
                }
            }
        } else if(!arg.startsWith("-")) {
            boolean match = false;

            for(Log.POLICY POLICY : Log.POLICY.values()) {
                if(POLICY.getName().equalsIgnoreCase(arg)) {
                    Log.setPolicies(POLICY);
                    match = true;
                    break;
                }
            }

            if(!match) {
                Log.setPolicies(Log.POLICY.VERBOSE);
                Log.d("Unable to match log level \"" + arg + "\", defaulting to VERBOSE", Log.class);
            }
        } else {
            Log.setPolicies(Log.POLICY.VERBOSE);
        }
    }
}