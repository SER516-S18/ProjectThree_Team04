package edu.asu.ser516.projecttwo.team04.util;

/**
 * LogRecord, a databag with the message and its other attributes
 */
public class LogRecord {
    private String _formattedMessage;
    private String _message;
    private Log.POLICY _policy;
    private Class _class;
    private long _timestamp;

    public LogRecord(String formattedMessage, String message, Log.POLICY policy, Class javaClass) {
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

    public Log.POLICY getOriginatingPolicy() {
        return this._policy;
    }

    public Class getOriginatingClass() {
        return this._class;
    }

    public long getTimestamp() {
        return this._timestamp;
    }
}