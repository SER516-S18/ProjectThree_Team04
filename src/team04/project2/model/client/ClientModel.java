package team04.project2.model.client;

import team04.project2.listeners.ClientListener;
import team04.project2.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ClientModel, the main model for client
 * @author  David Henderson (dchende2@asu.edu)
 */

public class ClientModel {
    private static InetAddress LOCALHOST;
    private static ClientModel _instance;

    static {
        // Helper function to save the localhost as a default
        try {
            LOCALHOST = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            LOCALHOST = null;
            Log.e("Failed to get the default localhost (" + e.getMessage() + ")", ClientModel.class);
        }
    }

    /**
     * Singleton ClientModel getter
     * @return ClientModel instance
     */
    public static ClientModel get() {
        if(_instance == null)
            _instance = new ClientModel();

        return _instance;
    }

    private int PORT;
    private InetAddress HOST;
    private int FREQUENCY;
    private int CHANNEL_COUNT;

    private final ArrayList<ClientListener> listeners = new ArrayList<>();
    private boolean run = false;
    private ClientWorker worker;

    private ArrayList<ClientChannel> channels = new ArrayList<>();
    private ArrayList<Integer> valueList = new ArrayList<>();
    private Integer valueMin = null;
    private Integer valueMax = null;
    private Integer valueAvg = null;

    /**
     * ClientModel - Default constructor, defaulting to port 1516 and LOCALHOST
     */
    private ClientModel() {
        this(1516, LOCALHOST);
    }

    /**
     * ClientModel - Full constructor
     * @param port Port to connect to
     * @param host Host to connect to
     */
    private ClientModel(int port, InetAddress host) {
        this.setHost(host);
        this.setPort(port);
        this.setFrequency(5);
        this.setChannelCount(1);

        // Shutdown client on program shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(ClientModel.this.isRunning())
                ClientModel.this.shutdown();
        }));
    }

    /**
     * start - Called to start the client model and connect
     */
    public void start() {
        if(run)
            throw new IllegalArgumentException("Server is already running");

        run = true;
        try {
            // Start worker to gather and handle input from server
            worker = new ClientWorker( new Socket(HOST, PORT) );

            // Notify the client is now running
            this.notifyClientStarted();
        } catch (IOException e) {
            Log.e("Failed to connect to server at " + HOST.getCanonicalHostName() + " port " + PORT + " (" + e.getMessage() + ")", ClientModel.class);
            run = false;
        }
    }

    /**
     * shutdown - Called to disconnect the client from the server
     */
    public void shutdown() {
        if(!run)
            throw new IllegalArgumentException("Server is already stopped");

        run = false;

        // Wait for the worker to shut down gracefully
        while(worker != null && worker.isRunning()) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                Log.w("Failed to wait for worker while shutting down", ClientModel.class);
            }
        }

        // Clear past values
        worker = null;
        valueList.clear();
        valueMin = null;
        valueMax = null;
        valueAvg = null;
        for(ClientChannel channel : channels)
            channel.clear();

        this.notifyClientShutdown();
    }

    /**
     * handleNewInputValues - Called to add new input values from the Server into the Client
     * @param values List of the values for the respective channels
     * @param tick The tick (time) the values were retrieved on
     */
    public void handleNewInputValues(ArrayList<Integer> values, int tick) {
        // For each value, add to the correct channel (and calculate min/max/avg)
        for(int i = 0; i < values.size(); i++) {
            Integer value = values.get(i);
            if(value == null)
                continue;

            // Add value to the channel
            channels.get(i).add(value, tick);

            // Now to calculate the min/max/avg across all channels
            // First value edge case
            if (valueList.size() == 0) {
                valueMin = value;
                valueMax = value;
                valueAvg = value;
            }

            // Average
            valueList.add(value);
            valueAvg = 0;
            for (int val : valueList)
                valueAvg += val;
            valueAvg /= valueList.size();

            // Minimum
            if (value < ClientModel.this.valueMin)
                valueMin = value;

            // Maximum
            if (value > ClientModel.this.valueMax)
                valueMax = value;

            // Notify listeners of the new value
            this.notifyValuesChanged();
        }
    }

    /**
     * getChannels - The list of channels
     * @return List of all channels
     */
    public List<ClientChannel> getChannels() {
        return Collections.unmodifiableList(channels);
    }

    /**
     * getMaximum - Gets the maximum, if any
     * @return Maximum or null, if there are no values
     */
    public Integer getMaximum() {
        return valueMax;
    }

    /**
     * getMinimum - Gets the minimum, if any
     * @return Minimum or null, if there are no values
     */
    public Integer getMinimum() {
        return valueMin;
    }

    /**
     * getAverage - Gets the average, if any
     * @return Average or null, if there are no values
     */
    public Integer getAverage() {
        return valueAvg;
    }

    /**
     * addListener - Add ClientListeners to ClientModel
     * @param listener ClientListener to inform
     */
    public void addListener(ClientListener listener) {
        if(listener == null)
            throw new IllegalArgumentException("Listener must not be null");
        else
            listeners.add(listener);
    }

    /**
     * notifyClientShutdown - Method called when ClientModel is shut down
     */
    private void notifyClientShutdown() {
        Log.i("Client shutdown successfully", ClientModel.class);
        for(ClientListener listener : listeners) {
            listener.shutdown();
        }
    }

    /**
     * notifyClientShutdown - Method called when ClientModel is started up
     */
    private void notifyClientStarted() {
        Log.i("Client started and connected to server at " + HOST.getCanonicalHostName() + " port " + PORT, ClientModel.class);
        for(ClientListener listener : listeners) {
            listener.started();
        }
    }

    /**
     * notifyClientShutdown - Method called when the input values from servers change
     */
    private void notifyValuesChanged() {
        for(ClientListener listener : listeners) {
            listener.changedValues();
        }
    }

    /**
     * notifyClientShutdown - Method called when the number of channels changes (added/removed)
     */
    private void notifyChannelCountChanged() {
        for(ClientListener listener : listeners) {
            listener.changedChannelCount();
        }
    }

    /**
     * isRunning - If the client is running
     * @return boolean if client is running
     */
    public boolean isRunning() {
        return run;
    }

    /**
     * Setter for server's port
     * @param port Server's port
     */
    public void setPort(int port) {
        if(port < 0)
            throw new IllegalArgumentException("Port must be greater than zero");
        else if(this.getPort() != port)
            PORT = port;
    }

    /**
     * Getter for server's port
     * @return port Server's port
     */
    public int getPort() {
        return PORT;
    }

    /**
     * setHost - Changes the host (WILL shutdown if Client is running)
     * @param host The host to connect to
     */
    public void setHost(InetAddress host) {
        if(host == null)
            throw new IllegalArgumentException("Host must be non-null");
        else {
            // Shutdown if running
            if(this.isRunning())
                this.shutdown();

            HOST = host;
        }
    }

    /**
     * Sets the host to localhost
     */
    public void setHostToLocalhost() {
        this.setHost(LOCALHOST);
    }

    /**
     * Setter for client's frequency
     * @param freq Client's frequency, must be greater than 0
     */
    public void setFrequency(int freq) {
        if(freq < 1)
            throw new IllegalArgumentException("Frequency must be greater than zero");
        else
            FREQUENCY = freq;
    }

    /**
     * Getter for client's frequency
     * @return frequency
     */
    public int getFrequency() {
        return FREQUENCY;
    }

    /**
     * Setter for channel count
     * @param count Number of channels, must be greater than 0
     */
    public void setChannelCount(int count) {
        if(count < 1)
            throw new IllegalArgumentException("Channel count must be greater than zero");
        else if(count != CHANNEL_COUNT) {
            // Create new channels on clientside
            if(count > CHANNEL_COUNT) {
                // Adding channels
                for(int i = CHANNEL_COUNT; i < count; i++) {
                    channels.add(new ClientChannel(i));
                }
            } else if(count < CHANNEL_COUNT) {
                // Removing channels
                for(int i = CHANNEL_COUNT - 1; i > count - 1; i--) {
                    channels.remove(i);
                }
            }

            CHANNEL_COUNT = count;
            this.notifyChannelCountChanged();
        }
    }

    /**
     * Getter for channel count
     * @return Number of channels
     */
    public int getChannelCount() {
        return CHANNEL_COUNT;
    }
}
