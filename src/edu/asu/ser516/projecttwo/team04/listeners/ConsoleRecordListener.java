package edu.asu.ser516.projecttwo.team04.listeners;

import edu.asu.ser516.projecttwo.team04.util.LogRecord;

/**
 * ConsoleRecordListener, a listener to notify new console record lines
 *
 * @author  David Henderson (dchende2@asu.edu)
 * @version 1.0
 * @since   2018-02-15
 */

public interface ConsoleRecordListener {
    /**
     * added - Called when a new log record is added
     * @param record The added record
     */
    public void added(LogRecord record);
}
