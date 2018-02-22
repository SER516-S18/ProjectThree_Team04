package edu.asu.ser516.projecttwo.team04;

import java.io.Serializable;

/**
 * Datagram, a listener to notify changes in the model
 *
 * @author  David Henderson (dchende2@asu.edu)
 * @version 1.0
 * @since   2018-02-18
 */

public class Datagram implements Serializable {
    /**
     * Types of datagrams
     * - Setting (For sending settings such as channel)
     * - Shutdown (For notifying the other side of the connection itself is shutting down)
     * - Payload (For the actual payload data)
     */
    public enum TYPE {
        SETTING,
        SHUTDOWN,
        PAYLOAD
    }

    public final TYPE type;
    public final Object data;

    /**
     * Datagram - A payload tuple with the type of payload and data
     * @param type The type of payload
     * @param data The data of payload
     */
    public Datagram(TYPE type, Object data) {
        this.type = type;
        this.data = data;
    }
}
