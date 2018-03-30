package team04.project3.listeners;

import team04.project3.util.LogRecord;

/**
 * A listener to notify new console record lines
 *
 * @author  David Henderson (dchende2@asu.edu)
 * @version 1.0
 * @since   2018-02-15
 */

public interface ConsoleRecordListener {
    /**
     * Called when a new log record is added
     * @param record The added record
     */
    public void added(LogRecord record);
}
