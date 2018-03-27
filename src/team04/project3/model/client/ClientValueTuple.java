package team04.project3.model.client;

/**
 * Pairs a value with a tick (time)
 */
public class ClientValueTuple {
    public final Integer value;
    public final Integer tick;

    /**
     * Pairs a value with a tick (time)
     * @param value Value from input
     * @param tick Tick that the value occurred from
     */
    public ClientValueTuple(Integer value, Integer tick) {
        this.value = value;
        this.tick = tick;
    }
}
