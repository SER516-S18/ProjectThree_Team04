package edu.asu.ser516.projecttwo.team04.model.server;

import edu.asu.ser516.projecttwo.team04.listeners.ServerListener;
import edu.asu.ser516.projecttwo.team04.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * ServerModel - The center model for the server
 * @author  David Henderson (dchende2@asu.edu)
 */

@SuppressWarnings("unused")
public class ServerModel {
    private static ServerModel _instance = null;

    /**
     * get - ServerModal singleton instance getter
     * @return ServerModel instance
     */
    public static ServerModel get() {
        if(_instance == null)
            _instance = new ServerModel();

        return _instance;
    }

    private int VALUE_MIN;
    private int VALUE_MAX;
    private int FREQUENCY;
    private int PORT;

    private ArrayList<ServerListener> listeners = new ArrayList<>();
    private ServerSocket serverSocket;
    private int clientID = 0;
    private boolean run = false;

    /**
     * Default constructor (with min 0, max 1024, frequency 5, and port 1516)
     */
    private ServerModel() {
        this(0, 1024, 5, 1516);
    }

    /**
     * ServerModel full constructor
     * @param min The minimum value to send
     * @param max The maximum value to send
     * @param frequency The frequency the server sends at
     * @param port The port the server sends to
     */
    private ServerModel(int min, int max, int frequency, int port) {
        this.setValueMin(min);
        this.setValueMax(max);
        this.setFrequency(frequency);
        this.setPort(port);

        // Shutdown server on program shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(ServerModel.this.isRunning())
                ServerModel.this.shutdown();
        }));
    }

    /**
     * start - A method called to start the server
     */
    public void start() {
        if(run)
            throw new IllegalArgumentException("Server is already running");

        run = true;
        try {
            serverSocket = new ServerSocket(PORT);
            new Thread(() -> {
                while (run) {
                    try {
                        // Accept new connections
                        Socket clientSocket = serverSocket.accept();
                        new ServerWorker(this, clientSocket, ++clientID);
                        Log.i("Client #" + clientID + " connected successfully", ServerModel.class);
                    } catch(IOException e) {
                        if(run) {
                            Log.e("Failed to accept a socket connection to a client on port " + PORT, ServerModel.class);
                        }
                    }
                }
            }).start();

            this.notifyServerStarted();
        } catch(IOException e) {
            Log.e("Failed to open a socket for the server on port " + PORT, ServerModel.class);
        }
    }

    /**
     * shutdown - A method called to shut down the server
     */
    public void shutdown() {
        if(!run)
            throw new IllegalArgumentException("Server is already stopped");

        try {
            if(serverSocket != null)
                serverSocket.close();
        } catch (IOException e) {
            Log.w("Failed to close server socket (" + e.getMessage() + ")", ServerModel.class);
        }

        run = false;
        serverSocket = null;
        this.notifyServerShutdown();
    }

    /**
     * Getter to check if server is running
     * @return run - If server is running
     */
    public boolean isRunning() {
        return run;
    }

    /**
     * Adds a ServerListener to notify
     * @param listener The listener to notify
     */
    public void addListener(ServerListener listener) {
        if(listener == null)
            throw new IllegalArgumentException("Listener must not be null");
        else
            listeners.add(listener);
    }

    /**
     * notifyServerShutdown - Notifies that the server is shutting down
     */
    private void notifyServerShutdown() {
        Log.i("Server shutdown successfully", ServerModel.class);
        for(ServerListener listener : listeners) {
            listener.shutdown();
        }
    }

    /**
     * notifyServerStarted - Notifies the server is starting
     */
    private void notifyServerStarted() {
        Log.i("Server started on port " + PORT, ServerModel.class);
        for(ServerListener listener : listeners) {
            listener.started();
        }
    }

    /**
     * Setter for server's frequency
     * @param freq Server's frequency
     */
    public void setFrequency(int freq) {
        if(freq < 1)
            throw new IllegalArgumentException("Frequency must be greater than zero");

        FREQUENCY = freq;
    }

    /**
     * Getter for server's frequency
     * @return frequency
     */
    public int getFrequency() {
        return FREQUENCY;
    }

    /**
     * Setter for the server's minimum value to send
     * @param min Minimum value the server will send
     */
    public void setValueMin(int min) {
        if(min > VALUE_MAX)
            throw new IllegalArgumentException("Minimum must be less than or equal to maximum");

        VALUE_MIN = min;
    }

    /**
     * Getter for the server's minimum value to send
     * @return minimum
     */
    public int getValueMin() {
        return VALUE_MIN;
    }

    /**
     * Setter for the server's maximum value to send
     * @param max Maximum value the server will send
     */
    public void setValueMax(int max) {
        if(max < VALUE_MIN)
            throw new IllegalArgumentException("Maximum must be greater than or equal to minimum");
        VALUE_MAX = max;
    }

    /**
     * Getter for the server's maximum value to send
     * @return maximum
     */
    public int getValueMax() {
        return VALUE_MAX;
    }

    /**
     * Setter for server's port
     * @param port Port to connect to
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
}
