package edu.asu.ser516.projecttwo.team04.listeners;

/**
 * ClientListener, a listener to notify changes in the model
 *
 * @author  David Henderson (dchende2@asu.edu)
 * @version 1.0
 * @since   2018-02-18
 */

public interface ClientListener {
    public void changedValues();
    public void changedChannelCount();
    public void started();
    public void shutdown();
}
