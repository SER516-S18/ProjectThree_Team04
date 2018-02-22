package edu.asu.ser516.projecttwo.team04.listeners;

/**
 * ClientListener, a listener to notify changes in the model
 *
 * @author  David Henderson (dchende2@asu.edu)
 * @version 1.0
 * @since   2018-02-18
 */

public interface ClientListener {
    /**
     * changedValues - Called when values have been added or updated
     */
    public void changedValues();

    /**
     * changedChannelCount - Called when the Client's number of channels changes (added/removed)
     */
    public void changedChannelCount();

    /**
     * started - Called when the Client is started (and connecting)
     */
    public void started();

    /**
     * shutdown - Called when the Client it shut down (and disconnected)
     */
    public void shutdown();
}
