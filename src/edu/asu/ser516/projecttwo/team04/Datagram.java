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
    public enum TYPE {
        SETTING,
        SHUTDOWN,
        PAYLOAD
    }

    public final TYPE type;
    public final Object data;

    public Datagram(TYPE type, Object data) {
        this.type = type;
        this.data = data;
    }
}
