package edu.asu.ser516.projecttwo.team04;

import edu.asu.ser516.projecttwo.team04.listeners.ClientListener;
import edu.asu.ser516.projecttwo.team04.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * ClientModel, the main model for client
 * @author  David Henderson (dchende2@asu.edu)
 */

public class ClientModel {
    private static InetAddress LOCALHOST;
    private static ClientModel _instance;

    static {
        try {
            LOCALHOST = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            LOCALHOST = null;
            Log.e("Failed to get the default localhost (" + e.getMessage() + ")", ClientModel.class);
        }
    }

    public static ClientModel get() {
        if(_instance == null)
            _instance = new ClientModel();

        return _instance;
    }

    private int PORT;
    private InetAddress HOST;
    private int FREQUENCY;
    private int CHANNEL_COUNT;

    private ArrayList<ClientListener> listeners = new ArrayList<>();
    private Socket socket;
    private boolean run = false;

    private ArrayList<ClientChannel> channels = new ArrayList<>();
    private ArrayList<Integer> valueList = new ArrayList<>();
    private Integer valueMin = null;
    private Integer valueMax = null;
    private Integer valueAvg = null;

    private ClientModel() {
        this(1516, LOCALHOST);
    }

    private ClientModel(int port, InetAddress host) {
        this.setHost(host);
        this.setPort(port);
        this.setFrequency(5);
        this.setChannelCount(1);
    }

    /**
     * start - Called to start the client model and connect
     */
    public void start() {
        if(run)
            throw new IllegalArgumentException("Server is already running");

        try {
            socket = new Socket(HOST, PORT);

            // Start an endpoint for the client to listen on
            ClientEndpoint endpoint = new ClientEndpoint(socket);
            new Thread(endpoint).start();

            // Start a worker to deal with the input gathered from the endpoint
            new Thread(new ClientWorker(endpoint)).start();

            // Notify the client is now running
            run = true;
            this.notifyClientStarted();
        } catch (IOException e) {
            Log.e("Failed to connect to server at " + HOST.getCanonicalHostName() + " port " + PORT + " (" + e.getMessage() + ")", ClientModel.class);
        }
    }

    /**
     * shutdown - Called to disconnect the client from the server
     */
    public void shutdown() {
        if(!run)
            throw new IllegalArgumentException("Server is already stopped");

        try {
            if(socket != null)
                socket.close();
        } catch (IOException e) {
            Log.w("Failed to close socket to server (" + e.getMessage() + ")", ServerModel.class);
        }

        run = false;
        socket = null;

        valueList.clear();
        valueMin = null;
        valueMax = null;
        valueAvg = null;

        this.notifyClientShutdown();
    }

    /**
     * Gets the maximum, if any
     * @return Maximum or null, if there are no values
     */
    public Integer getMaximum() {
        return valueMax;
    }

    /**
     * Gets the minimum, if any
     * @return Minimum or null, if there are no values
     */
    public Integer getMinimum() {
        return valueMin;
    }

    /**
     * Gets the average, if any
     * @return Average or null, if there are no values
     */
    public Integer getAverage() {
        return valueAvg;
    }

    public void addListener(ClientListener listener) {
        if(listener == null)
            throw new IllegalArgumentException("Listener must not be null");
        else
            listeners.add(listener);
    }

    private void notifyClientShutdown() {
        Log.i("Client shutdown successfully", ClientModel.class);
        for(ClientListener listener : listeners) {
            listener.shutdown();
        }
    }

    private void notifyClientStarted() {
        Log.i("Client started and connected to server at " + HOST.getCanonicalHostName() + " port " + PORT, ClientModel.class);
        for(ClientListener listener : listeners) {
            listener.started();
        }
    }

    private void notifyClientInputChanged(Integer min, Integer max, Integer avg) {
        for(ClientListener listener : listeners) {
            listener.inputChanged(min, max, avg);
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
        else {
            if(count > CHANNEL_COUNT) {
                // Adding channels
                for(int i = 0; i < count - CHANNEL_COUNT; i++) {
                    channels.add(new ClientChannel());
                }
            } else if(count < CHANNEL_COUNT) {
                // Removing channels
                for(int i = CHANNEL_COUNT; i > count; i--) {
                    channels.remove(i);
                }
            }

            CHANNEL_COUNT = count;
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
     * ClientEndpoint - Class that simply continues to get input from the server at the server's frequency
     */
    private class ClientEndpoint implements Runnable {
        private Scanner scannerIn;
        private Integer value;

        private ClientEndpoint(Socket socket) {
            try {
                scannerIn = new Scanner(socket.getInputStream());
            } catch (IOException e) {
                Log.w("Client failed to read input stream (" + e.getMessage() + ")", ClientModel.class);
            }
        }

        @Override
        public void run() {
            while (ClientModel.this.run && scannerIn.hasNext()) {
                value = scannerIn.nextInt();
            }
        }

        /**
         * getValue - Gets the most recently received value
         * @param clear - Whether to clear the value to null after getting
         * @return value - Most recent value (or or null if not available)
         */
        public Integer getValue(boolean clear) {
            if(clear) {
                Integer temp = value;
                value = null;
                return temp;
            } else {
                return value;
            }
        }
    }

    private class ClientChannel {
        ArrayList<Integer> values = new ArrayList<>();
    }

    private class ClientWorker implements Runnable {
        private ClientEndpoint input;

        private ClientWorker(ClientEndpoint endpoint) {
            input = endpoint;
        }

        @Override
        public void run() {
            while(ClientModel.this.run) {
                Integer value = input.getValue(false);

                if(value != null) {
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
                    ClientModel.this.notifyClientInputChanged(valueMin, valueMax, valueAvg);
                }

                // Sleep to meet Client's frequency
                try {
                    Thread.sleep(1000 / ClientModel.this.FREQUENCY);
                } catch (InterruptedException e) {
                    Log.w("Interrupted exception thrown while waiting for client frequency", ClientModel.class);
                }
            }

            // We're no longer running, send null values
            ClientModel.this.notifyClientInputChanged(null, null, null);
        }
    }
}
