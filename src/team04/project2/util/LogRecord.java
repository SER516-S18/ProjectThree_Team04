package team04.project2.util;

/**
 * LogRecord, a databag with the message and its other attributes
 */
public class LogRecord {
    private String _formattedMessage;
    private String _message;
    private Log.POLICY _policy;
    private Class _class;
    private long _timestamp;

    /**
     * LogRecord - Creates a record of a log message
     * @param formattedMessage The full formatted message
     * @param message The log message
     * @param policy The policy of the log message
     * @param javaClass The class the log message is from
     */
    public LogRecord(String formattedMessage, String message, Log.POLICY policy, Class javaClass) {
        _formattedMessage = formattedMessage;
        _message = message;
        _policy = policy;
        _class = javaClass;
        _timestamp = System.currentTimeMillis();
    }

    /**
     * getFormattedMessage - Gets the formatted message
     * @return The formatted message
     */
    public String getFormattedMessage() {
        return this._formattedMessage;
    }

    /**
     * getMessage - Returns the original log message
     * @return The original log message
     */
    public String getMessage() {
        return this._message;
    }

    /**
     * getOriginatingPolicy - Get the policy the log record is
     * @return The log record's policy
     */
    public Log.POLICY getOriginatingPolicy() {
        return this._policy;
    }

    /**
     * getOriginatingClass - Gets the originating class
     * @return The class the record is from
     */
    public Class getOriginatingClass() {
        return this._class;
    }

    /**
     * getTimestamp - Gets the timestamp of the record
     * @return The timestamp the record was created on
     */
    public long getTimestamp() {
        return this._timestamp;
    }
}