package edu.asu.ser516.projecttwo.team04;

import com.sun.deploy.util.SessionState;
import edu.asu.ser516.projecttwo.team04.listeners.ClientListener;
import edu.asu.ser516.projecttwo.team04.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

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

    private ArrayList<ClientListener> listeners = new ArrayList<>();
    private Socket socket;
    private boolean run = false;

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
    }

    /**
     * start - Called to start the client model and connect
     */
    public void start() {
        if(run)
            throw new IllegalArgumentException("Server is already running");

        try {
            socket = new Socket(HOST, PORT);
            new Thread(new ClientWorker(socket)).start();
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

    private void notifyClientInputChanged(Integer newest, Integer min, Integer max, Integer avg) {
        for(ClientListener listener : listeners) {
            listener.inputChanged(newest, min, max, avg);
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

    private class ClientWorker implements Runnable {
        private Scanner scannerIn;

        private ClientWorker(Socket socket) {
            try {
                scannerIn = new Scanner(socket.getInputStream());
            } catch (IOException e) {
                Log.w("Client failed to read input stream (" + e.getMessage() + ")", ClientModel.class);
            }
        }

        @Override
        public void run() {
            while(ClientModel.this.run && scannerIn.hasNext()) {
                int value = scannerIn.nextInt();

                // First value edge case
                if(valueList.size() == 0) {
                    valueMin = value;
                    valueMax = value;
                    valueAvg = value;
                }

                // Average
                ClientModel.this.valueList.add( value );
                valueAvg = 0;
                for(int val : valueList)
                    valueAvg += val;
                valueAvg /= valueList.size();

                // Minimum
                if(value < ClientModel.this.valueMin)
                    ClientModel.this.valueMin = value;

                // Maximum
                if(value > ClientModel.this.valueMax)
                    ClientModel.this.valueMax = value;

                // Notify listeners of the new value
                ClientModel.this.notifyClientInputChanged(value, valueMin, valueMax, valueAvg);
            }

            // We're no longer running, send null values
            ClientModel.this.notifyClientInputChanged(null, null, null, null);
        }
    }
}
