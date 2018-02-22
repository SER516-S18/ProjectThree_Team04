package edu.asu.ser516.projecttwo.team04.model.client;

/**
 * ClientValueTuple - Pairs a value with a tick (time)
 */
public class ClientValueTuple {
    public final Integer value;
    public final Integer tick;

    /**
     * ClientValueTuple, pairs a value with a tick (time)
     * @param value Value from input
     * @param tick Tick that the value occurred from
     */
    public ClientValueTuple(Integer value, Integer tick) {
        this.value = value;
        this.tick = tick;
    }
}
