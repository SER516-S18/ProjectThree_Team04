package edu.asu.ser516.projecttwo.team04.util;

import edu.asu.ser516.projecttwo.team04.listeners.ConsoleRecordListener;

import java.util.ArrayList;

public class Log {
    public enum POLICY {
        VERBOSE(0, "VERBOSE"),
        DEBUG(1, "DEBUG"),
        INFO(2, "INFO"),
        WARNING(3, "WARNING"),
        ERROR(4, "ERROR");

        private final int _level;
        private final String _name;

        private POLICY(int level, String name) {
            this._level = level;
            this._name = name;
        }

        public int getLevel() {
            return this._level;
        }

        public String getName() {
            return this._name;
        }

        public boolean isEqualOrWorse(POLICY POLICY) {
            return this._level >= POLICY._level;
        }

        public boolean isWorse(POLICY POLICY) {
            return this._level > POLICY._level;
        }
    }

    private static POLICY CONSOLE_POLICY = POLICY.ERROR;
    private static int _length = -1;
    private static transient ArrayList<ConsoleRecordListener> _recordListeners = new ArrayList<>();

    // Log saving
    private static void record(String formattedMessage, String message, POLICY policy, Class javaClass) {
        LogRecord record = new LogRecord(formattedMessage, message, policy, javaClass);
        notifyRecordAdded(record);
    }

    public static void addRecordListener(ConsoleRecordListener listener) {
        _recordListeners.add(listener);
    }

    private static void notifyRecordAdded(LogRecord record) {
        for(ConsoleRecordListener listener : _recordListeners) {
            listener.added(record);
        }
    }

    public static class LogRecord {
        private String _formattedMessage;
        private String _message;
        private POLICY _policy;
        private Class _class;
        private long _timestamp;

        public LogRecord(String formattedMessage, String message, POLICY policy, Class javaClass) {
            _formattedMessage = formattedMessage;
            _message = message;
            _policy = policy;
            _class = javaClass;
            _timestamp = System.currentTimeMillis();
        }

        public String getFormattedMessage() {
            return this._formattedMessage;
        }

        public String getMessage() {
            return this._message;
        }

        public POLICY getOriginatingPolicy() {
            return this._policy;
        }

        public Class getOriginatingClass() {
            return this._class;
        }

        public long getTimestamp() {
            return this._timestamp;
        }
    }

    // Console Logging
    @SuppressWarnings("rawtypes")
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

    public static void error(String msg, Class javaClass) { Log.e(msg, javaClass); }
    public static void e(String msg, Class javaClass) {
        Log.log(msg, javaClass, POLICY.ERROR);
    }

    public static void warn(String msg, Class javaClass) { Log.w(msg, javaClass); }
    public static void w(String msg, Class javaClass) {
        Log.log(msg, javaClass, POLICY.WARNING);
    }

    public static void info(String msg, Class javaClass) { Log.i(msg, javaClass); }
    public static void i(String msg, Class javaClass) {
        Log.log(msg, javaClass, POLICY.INFO);
    }

    public static void debug(String msg, Class javaClass) { Log.d(msg, javaClass); }
    public static void d(String msg, Class javaClass) {
        Log.log(msg, javaClass, POLICY.DEBUG);
    }

    public static void verbose(String msg, Class javaClass) { Log.v(msg, javaClass); }
    public static void v(String msg, Class javaClass) {
        Log.log(msg, javaClass, POLICY.VERBOSE);
    }

    public static void setConsolePolicy(POLICY POLICY) {
        CONSOLE_POLICY = POLICY;
    }

    public static POLICY getConsolePolicy() {
        return CONSOLE_POLICY;
    }

    public static void setPolicies(POLICY policy) {
        CONSOLE_POLICY = policy;
    }

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