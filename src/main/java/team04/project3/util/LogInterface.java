package team04.project3.util;

import java.util.ArrayList;

import team04.project3.listeners.ConsoleRecordListener;

interface LogInterface {
	    public enum POLICY{};

	    static POLICY CONSOLE_POLICY = null;
	    static int _longestPolicyNameLength = 0;
	    static ArrayList<ConsoleRecordListener> _recordListeners = new ArrayList<>();

	    /**
	     * To notify any listeners for system log messages
	     * @param formattedMessage, a formatted string with all the info in a log message (class, policy, message)
	     * @param message, the message without policy or class
	     * @param policy, the policy of the log message
	     * @param javaClass, the originating Java class
	     */
	    static void record() {
		}

	    /**
	     * Adds a listener to subscribe and listen to new Log messages
	     * @param listener, the listener to notify
	     */
	    public static void addRecordListener(ConsoleRecordListener listener) {}

	    /**
	     * Private method to notify all listeners of a new log record
	     * @param record, the record added
	     */
	    static void notifyRecordAdded(LogRecord record) {
	    }

	    /**
	     * Log system messages to the terminal and console
	     * @param msg, the message to display
	     * @param javaClass, the originating Java class
	     * @param policy, the log policy of the message
	     */
	    public static void log(String msg, Class javaClass, POLICY policy) {	    }

	    /**
	     * Log an error message
	     * @param msg Message to log
	     * @param javaClass Class log message is from
	     */
	    public static void error(String msg, Class javaClass) {
	    }

	    /**
	     * Log an error message (Shorthand for "error")
	     * @param msg Message to log
	     * @param javaClass Class log message is from
	     */
	    public static void e(String msg, Class javaClass) {
	    }

	    /**
	     * Log an warn message
	     * @param msg Message to log
	     * @param javaClass Class log message is from
	     */
	    public static void warn(String msg, Class javaClass) {
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
	    }

	    /**
	     * Log an info message (Shorthand for "info")
	     * @param msg Message to log
	     * @param javaClass Class log message is from
	     */
	    public static void i(String msg, Class javaClass) {
	    }

	    /**
	     * Log an debug message
	     * @param msg Message to log
	     * @param javaClass Class log message is from
	     */
	    public static void debug(String msg, Class javaClass) {
	    }

	    /**
	     * Log an debug message (Shorthand for "debug")
	     * @param msg Message to log
	     * @param javaClass Class log message is from
	     */
	    public static void d(String msg, Class javaClass) {
	    }

	    /**
	     * Log an verbose message
	     * @param msg Message to log
	     * @param javaClass Class log message is from
	     */
	    public static void verbose(String msg, Class javaClass) {
	    }

	    /**
	     * Log an verbose message (Shorthand for "verbose")
	     * @param msg Message to log
	     * @param javaClass Class log message is from
	     */
	    public static void v(String msg, Class javaClass) {
	    }

	    /**
	     * Set the minimum policy to display in the console and terminal
	     * @param POLICY The minimum policy to display in console
	     */
	    public static void setConsolePolicy(POLICY POLICY) {
	    }

	    /**
	     * Get the minimum policy to display in the console and terminal
	     */
	    public static void getConsolePolicy() {
	    }

	    /**
	     * Specific method to set the policy from the initial program startup arguments
	     * @param arg String argument to parse into policy
	     */
	    public static void setPoliciesFromArg(String arg) {	}

}
