package team04.project2.model.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ClientChannel - Represents the data values in a channel
 */
public class ClientChannel {
    private final ArrayList<ClientValueTuple> values;
    public final int id;

    /**
     * ClientChannel - A wrapper for a list of values, with an ID for the channel
     * @param id The channel identifier
     */
    public ClientChannel(int id) {
        this.id = id;
        values = new ArrayList<>();
    }

    /**
     * add - Add a value input to the channel
     * @param value The input value
     * @param tick The tick (time) it happened
     */
    public void add(Integer value, Integer tick) {
        values.add(new ClientValueTuple(value, tick));
    }

    /**
     * clear - Remove all values from the channel
     */
    public void clear() {
        values.clear();
    }

    /**
     * getLast - Returns the last value in the list
     * @return Last value (or null if empty) in the channel
     */
    public ClientValueTuple getLast() {
        if(values.size() > 0)
            return values.get(values.size() - 1);
        else
            return null;
    }

    /**
     * getValues - Returns all values in the channel
     * @return Unmodifiable list of values (which can be null)
     */
    public List<ClientValueTuple> getValues() {
        return Collections.unmodifiableList(values);
    }
}