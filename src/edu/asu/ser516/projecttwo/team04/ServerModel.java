package edu.asu.ser516.projecttwo.team04;

import edu.asu.ser516.projecttwo.team04.listeners.ServerListener;
import edu.asu.ser516.projecttwo.team04.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * ServerModel - The center model for the server
 */
@SuppressWarnings("unused")
public class ServerModel {
    private static ServerModel _instance = null;

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
    private ExecutorService executors = Executors.newCachedThreadPool();
    private int clientID = 0;
    private boolean run = false;

    private ServerModel() {
        this(0, 1024, 5, 1516);
    }

    private ServerModel(int min, int max, int frequency, int port) {
        this.setValueMin(min);
        this.setValueMax(max);
        this.setFrequency(frequency);
        this.setPort(port);
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
                        Socket clientSocket = serverSocket.accept();
                        executors.execute(new ServerWorker(clientSocket, ++clientID));
                        Log.v("Client #" + clientID + " connected successfully", ServerModel.class);
                    } catch(IOException e) {
                        Log.e("Failed to accept a socket connection to a client on port " + PORT, ServerModel.class);
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

    public void addListener(ServerListener listener) {
        if(listener == null)
            throw new IllegalArgumentException("Listener must not be null");
        else
            listeners.add(listener);
    }

    private void notifyServerShutdown() {
        Log.i("Server shutdown successfully", ServerModel.class);
        for(ServerListener listener : listeners) {
            listener.shutdown();
        }
    }

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
     * @param port
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

    public class ServerWorker implements Runnable {
        private final Socket socket;
        private final int id;
        private PrintWriter writeOut;
        private Scanner scannerIn;

        public ServerWorker(Socket c, int clientID) {
            socket = c;
            this.id = clientID;

            try {
                writeOut = new PrintWriter(socket.getOutputStream(), true);
                scannerIn = new Scanner(socket.getInputStream());
            } catch(IOException e1) {
                Log.e("Failed to initialize data stream with client #" + id + " (" + e1.getMessage() + ")", ServerModel.class);
                try {
                    socket.close();
                } catch(IOException e2) {
                    Log.e("Failed to close connection with client #" + id + " (" + e2.getMessage() + ")", ServerModel.class);
                }
            }
        }

        @Override
        public void run() {
            while(ServerModel.this.run && socket.isConnected()) {
                int val = ThreadLocalRandom.current().nextInt(ServerModel.this.VALUE_MIN, ServerModel.this.VALUE_MAX + 1);
                writeOut.println(val);

                try {
                    Thread.sleep(1000 / ServerModel.this.FREQUENCY);
                } catch (InterruptedException e) {
                    Log.w("Interrupted exception thrown while waiting in client #" + id, ServerModel.class);
                }
            }
        }
    }
}
