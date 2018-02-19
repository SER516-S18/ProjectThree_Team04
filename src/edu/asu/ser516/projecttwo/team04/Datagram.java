package edu.asu.ser516.projecttwo.team04;

import java.io.Serializable;

public class Datagram implements Serializable {
    public enum TYPE {
        PING,
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
