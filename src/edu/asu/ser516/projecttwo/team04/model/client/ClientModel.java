package edu.asu.ser516.projecttwo.team04.model.client;

import edu.asu.ser516.projecttwo.team04.Datagram;
import edu.asu.ser516.projecttwo.team04.listeners.ClientListener;
import edu.asu.ser516.projecttwo.team04.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private final ExecutorService executors = Executors.newCachedThreadPool();
    private final ArrayList<ClientListener> listeners = new ArrayList<>();
    private boolean run = false;
    private ClientWorker worker;

    private ArrayList<ClientChannel> channels = new ArrayList<>();
    private ArrayList<Integer> valueList = new ArrayList<>();
    private Integer valueMin = null;
    private Integer valueMax = null;
    private Integer valueAvg = null;

    private ClientModel() {
        // Simple default constructor, port 1516 and LOCALHOST
        this(1516, LOCALHOST);
    }

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
        else
            PORT = port;
    }

    /**
     * Getter for server's port
     * @return port Server's port
     */
    public int getPort() {
        return PORT;
    }

    public void setHost(InetAddress host) {
        if(host == null)
            throw new IllegalArgumentException("Host must be non-null");
        else
            HOST = host;
    }

    /**
     * Sets the host to localhost
     */
    public void setHostToLocalhost() {
        this.setHost(LOCALHOST);
    }

    /**
     * Setter for client's frequency
     * @param freq Client's frequency, must be > 0
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
     * @param count Number of channels, must be > 0
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

    /**
     * ClientWorker - Class that contains the input listener and handler, and the output handler
     */
    private class ClientWorker {
        private ObjectInputStream streamIn;
        private ObjectOutputStream streamOut;

        private ClientOutputHandler outputHandler;
        private ClientInputListener inputListener;
        private ClientInputHandler inputHandler;

        private ClientWorker(Socket socket) {
            try {
                // Open streams, start handlers and listeners
                streamOut = new ObjectOutputStream(socket.getOutputStream());
                streamIn = new ObjectInputStream(socket.getInputStream());

                // Create output handler (output to server)
                outputHandler = new ClientOutputHandler(this);
                executors.submit(outputHandler);

                // Create input listener (input from server, at server's frequency)
                inputListener = new ClientInputListener(this);
                executors.submit(inputListener);

                // Create input handler (handles input from listener, at client's frequency)
                inputHandler = new ClientInputHandler(this);
                executors.submit(inputHandler);
            } catch (IOException e) {
                Log.w("Client failed to read input stream (" + e.getMessage() + ")", ClientModel.class);
            }
        }

        /**
         * isRunning - Considered running if all three listeners/handlers are running
         * @return If ClientWorker is running
         */
        private boolean isRunning() {
            return (outputHandler != null && outputHandler.running &&
                    inputListener != null && inputListener.running &&
                    inputHandler != null && inputHandler.running);
        }
    }

    /**
     * ClientInputListener - Necessary because server may send values at a different frequency
     */
    private class ClientInputListener implements Runnable {
        private boolean running;
        private ClientWorker worker;
        private ArrayList<Integer> value;

        /**
         * ClientInputListener - Accepts values from server at server's frequency
         * @param worker Parent worker (to link between input listener/input handler/output handler)
         */
        private ClientInputListener(ClientWorker worker) {
            this.worker = worker;
            this.running = false;
        }

        /**
         * getValue - Gets the most recently received value
         * @param clear - Whether to clear the value to null after getting
         * @return value - Most recent value (or or null if not available)
         */
        public ArrayList<Integer> getValue(boolean clear) {
            if(clear) {
                ArrayList<Integer> temp = value;
                value = null;
                return temp;
            } else {
                return value;
            }
        }

        @Override
        public void run() {
            running = true;

            while (ClientModel.this.run) {
                Datagram data;
                try {
                    // Wait until data is available, then get it
                    data = (Datagram) worker.streamIn.readObject();
                    if(data == null)
                        continue;

                    if(data.type == Datagram.TYPE.PAYLOAD) {
                        // If it's payload, set it as the new value
                        value = (ArrayList<Integer>) data.data;
                    } else if(data.type == Datagram.TYPE.SHUTDOWN) {
                        // Server is notifying this client it intends to shutdown
                        Log.i("Server is disconnecting from client", ClientModel.class);
                        worker.outputHandler.serverNotifyDisconnect = true;
                        ClientModel.this.shutdown();
                    }
                } catch(ClassNotFoundException e) {
                    if(ClientModel.this.run)
                        Log.w("Failed to read in object from stream (Class not found: " + e.getMessage() + ")", ClientModel.class);
                    continue;
                } catch(IOException e) {
                    if(ClientModel.this.run) {
                        // Only error if we should be running right now
                        Log.e("Failed to read in object from stream, shutting down (IOException: " + e.getMessage() + ")", ClientModel.class);
                        ClientModel.this.shutdown();
                    }
                    continue;
                }
            }

            // Attempt to gracefully shut down input
            try {
                worker.streamIn.close();
            } catch (IOException e) {
                Log.w("Failed to gracefully close input socket (" + e.getMessage() + ")", ClientModel.class);
            }

            running = false;
        }
    }

    /**
     * ClientOutputHandler - Outputs datagrams to the server (e.g., channel count, shutdown message)
     */
    private class ClientOutputHandler implements Runnable {
        private boolean running;
        private boolean serverNotifyDisconnect = false;
        private ClientWorker worker;
        private ArrayList<Datagram> datagrams = new ArrayList<>();

        /**
         * ClientOutputHandler - Sends output to server
         * @param worker Parent worker (to link between input listener/input handler/output handler)
         */
        private ClientOutputHandler(ClientWorker worker) {
            this.worker = worker;
            this.running = false;
        }

        /**
         * sendDatagram - Adds datagram to queue to send to server
         * @param data Datagram to send to server
         */
        private synchronized void sendDatagram(Datagram data) {
            datagrams.add(data);
        }

        @Override
        public void run() {
            running = true;
            serverNotifyDisconnect = false;

            while(ClientModel.this.run) {
                // If we have any data to send
                if(datagrams.size() > 0) {
                    Datagram data = datagrams.get(0);
                    try {
                        worker.streamOut.writeObject(data);
                        worker.streamOut.flush();
                        datagrams.remove(0);
                    } catch (IOException e) {
                        Log.w("Failed send server data from client", ClientModel.class);
                    }
                }

                try {
                    Thread.sleep(20L);
                } catch (InterruptedException e) {
                    Log.w("Failed to sleep in client output (" + e.getMessage() + ")", ClientModel.class);
                }
            }

            // Gracefully disconnect from server (if the client isn't the one disconnecting)
            if(!serverNotifyDisconnect) {
                try {
                    worker.streamOut.writeObject(new Datagram(Datagram.TYPE.SHUTDOWN, null));
                    worker.streamOut.flush();
                    worker.streamOut.close();
                } catch (IOException e) {
                    Log.w("Failed to notify graceful disconnect with server (" + e.getMessage() + ")", ClientModel.class);
                }
            }

            running = false;
        }
    }

    /**
     * ClientInputHandler - Handles input at the client's set frequency
     */
    private class ClientInputHandler implements Runnable {
        private int tick;
        private boolean running;
        private ClientWorker worker;

        /**
         * ClientInputHandler - Handles input from input listener, at the client's frequency
         * @param worker Parent worker (to link between input listener/input handler/output handler)
         */
        private ClientInputHandler(ClientWorker worker) {
            this.worker = worker;
            this.running = false;
        }

        @Override
        public void run() {
            tick = 0;
            running = true;

            while(ClientModel.this.run) {
                // Get the most recent input at our own client's frequency
                ArrayList<Integer> values = worker.inputListener.getValue(false);

                if(values != null) {
                    if(values.size() != CHANNEL_COUNT) {
                        // If the values sent aren't the correct number of channels, notify server of correct count
                        worker.outputHandler.sendDatagram(new Datagram(Datagram.TYPE.SETTING, CHANNEL_COUNT));
                    } else {
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
                            ClientModel.this.valueList.add(value);
                            valueAvg = 0;
                            for (int val : valueList)
                                valueAvg += val;
                            valueAvg /= valueList.size();

                            // Minimum
                            if (value < ClientModel.this.valueMin)
                                ClientModel.this.valueMin = value;

                            // Maximum
                            if (value > ClientModel.this.valueMax)
                                ClientModel.this.valueMax = value;

                            // Notify listeners of the new value
                            ClientModel.this.notifyValuesChanged();
                        }
                    }
                }

                // Sleep to meet Client's frequency
                try {
                    Thread.sleep(1000 / ClientModel.this.FREQUENCY);
                } catch (InterruptedException e) {
                    Log.w("Interrupted exception thrown while waiting for client frequency", ClientModel.class);
                }

                tick++;
            }

            running = false;
        }
    }

    /**
     * ClientChannel - Represents the data values in a channel
     */
    public static class ClientChannel {
        private final ArrayList<ClientValueTuple> values;
        public final int id;

        /**
         * ClientChannel - A wrapper for a list of values, with an ID for the channel
         * @param id The channel identifier
         */
        private ClientChannel(int id) {
            this.id = id;
            values = new ArrayList<>();
        }

        /**
         * add - Add a value input to the channel
         * @param value The input value
         * @param tick The tick (time) it happened
         */
        private void add(Integer value, Integer tick) {
            values.add(new ClientValueTuple(value, tick));
        }

        /**
         * clear - Remove all values from the channel
         */
        private void clear() {
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
         * gettValues - Returns all values in the channel
         * @return Unmodifiable list of values (which can be null)
         */
        public List<ClientValueTuple> getValues() {
            return Collections.unmodifiableList(values);
        }
    }

    /**
     * ClientValueTuple - Pairs a value with a tick (time)
     */
    public static class ClientValueTuple {
        public final Integer value;
        public final Integer tick;

        /**
         * ClientValueTuple, pairs a value with a tick (time)
         * @param value Value from input
         * @param tick Tick that the value occurred from
         */
        private ClientValueTuple(Integer value, Integer tick) {
            this.value = value;
            this.tick = tick;
        }
    }
}
