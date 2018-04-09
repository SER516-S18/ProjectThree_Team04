package team04.project3.util;

/**
 * A databag with the message and its other attributes
 * @author  David Henderson (dchende2@asu.edu)
 */
public class LogRecord {
    private final String _formattedMessage;
    private final String _message;
    private final Log.POLICY _policy;
    private final Class _class;
    private final long _timestamp;

    /**
     * Creates a record of a log message
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
     * Gets the formatted message
     * @return The formatted message
     */
    public String getFormattedMessage() {
        return this._formattedMessage;
    }

    /**
     * Returns the original log message
     * @return The original log message
     */
    public String getMessage() {
        return this._message;
    }

    /**
     * Get the policy the log record is
     * @return The log record's policy
     */
    public Log.POLICY getOriginatingPolicy() {
        return this._policy;
    }

    /**
     * Gets the originating class
     * @return The class the record is from
     */
    public Class getOriginatingClass() {
        return this._class;
    }

    /**
     * Gets the timestamp of the record
     * @return The timestamp the record was created on
     */
    public long getTimestamp() {
        return this._timestamp;
    }
}