package edu.asu.ser516.projecttwo.team04.listeners;

import edu.asu.ser516.projecttwo.team04.util.Log;

public interface ConsoleRecordListener {
    public void added(Log.LogRecord record);
    public void changed();
}
