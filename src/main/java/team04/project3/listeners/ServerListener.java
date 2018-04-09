package team04.project3.listeners;

/**
 * A listener to notify changes in the model
 *
 * @author  David Henderson (dchende2@asu.edu)
 * @version 1.0
 * @since   2018-02-18
 */

public interface ServerListener {
    /**
     * Called when the server is started (and open for connections)
     */
    public void started();

    /**
     * Called when the server is stopped (and connections are closed)
     */
    public void shutdown();

    public void clientConnected();

    public void clientDisconnected();

    /**
     * Called when a packet it sent
     */
    public void packetSent();

    /**
     * Called when the model is set to connect or stop repeatedly send packets
     */
    public void packetRepeatingToggled();

    public void packetRepeatingModeChanged();
}
