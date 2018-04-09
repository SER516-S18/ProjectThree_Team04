package team04.project3.model;

/**
 * A tuple associating a value with a tick
 * @author  David Henderson (dchende2@asu.edu)
 */
public class ValueTuple {
    public final float VALUE;
    public final float TICK;

    /**
     * Tuple associating a value with a tick
     * @param value Value to associate
     * @param tick Tick to associate to
     */
    public ValueTuple(float value, float tick) {
        this.VALUE = value;
        this.TICK = tick;
    }
}
