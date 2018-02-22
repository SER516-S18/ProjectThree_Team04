package edu.asu.ser516.projecttwo.team04;

import edu.asu.ser516.projecttwo.team04.listeners.ServerListener;
import edu.asu.ser516.projecttwo.team04.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

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
    private ExecutorService executors = Executors.newCachedThreadPool();
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
                        new ServerWorker(clientSocket, ++clientID);
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

    /**
     * getRandom
     * @return A random value between the server's min and max, inclusive
     */
    private int getRandom() {
        return ThreadLocalRandom.current().nextInt(ServerModel.this.VALUE_MIN, ServerModel.this.VALUE_MAX + 1);
    }

    /**
     * ServerWorker - A worker that handles output and input to an individual client
     */
    public class ServerWorker {
        private final int id;
        private boolean run;
        private int channels = 1;
        private ObjectOutputStream streamOut;
        private ObjectInputStream streamIn;
        private boolean clientNotifyDisconnect = false;

        /**
         * ServerWorker The worker that handles input and output
         * @param socket The socket the client is connecting from
         * @param clientID The id of the client
         */
        public ServerWorker(Socket socket, int clientID) {
            id = clientID;
            run = true;

            try {
                streamOut = new ObjectOutputStream(socket.getOutputStream());
                streamIn = new ObjectInputStream(socket.getInputStream());

                // Output to the client
                Runnable output = () -> {
                    clientNotifyDisconnect = false;

                    while(ServerWorker.this.run && ServerModel.this.run) {
                        // Create a 'packet' with a value for each channel the client has
                        ArrayList<Integer> values = new ArrayList<>();
                        for(int i = 0; i < channels; i++) {
                            values.add(ServerModel.this.getRandom());
                        }
                        Datagram data = new Datagram(Datagram.TYPE.PAYLOAD, values);

                        // Send the 'packet' to the client
                        try {
                            streamOut.writeObject(data);
                            streamOut.flush();
                        } catch(IOException e) {
                            Log.w("Failed to send data to client #" + id + " (" + e.getMessage() + ")", ServerModel.class);
                        }

                        // Sleep to match the server's set frequency to send data
                        try {
                            Thread.sleep(1000 / ServerModel.this.FREQUENCY);
                        } catch (InterruptedException e) {
                            Log.w("Interrupted exception thrown while waiting in client #" + id, ServerModel.class);
                        }
                    }

                    // Gracefully disconnect client (if the server isn't the one shutting down)
                    if(!clientNotifyDisconnect) {
                        try {
                            streamOut.writeObject(new Datagram(Datagram.TYPE.SHUTDOWN, null));
                            streamOut.flush();
                            streamOut.close();
                        } catch (IOException e) {
                            Log.w("Failed to notify graceful disconnect with client (" + e.getMessage() + ")", ServerModel.class);
                        }
                    }
                };

                // Handle input from a client
                Runnable input = () -> {
                    while(ServerWorker.this.run && ServerModel.this.run) {
                        Datagram data;
                        try {
                            // Wait until data arrives
                            data = (Datagram) streamIn.readObject();
                            if(data == null)
                                continue;

                            if(data.type == Datagram.TYPE.SETTING) {
                                // If the data is to set the channel count, handle it
                                Integer count = (Integer) data.data;
                                if(count != null && count != channels) {
                                    channels = (Integer) data.data;
                                    Log.i("Client #" + id + " changed channel count to " + channels, ServerModel.class);
                                }
                            } else if(data.type == Datagram.TYPE.SHUTDOWN) {
                                // If the data is to notify the client intends to shutdown, disconnect
                                clientNotifyDisconnect = true;
                                ServerWorker.this.disconnect();
                                Log.i("Client #" + id + " disconnected successfully", ServerModel.class);
                            }
                        } catch(ClassNotFoundException e) {
                            Log.w("Failed to read in object from stream, disconnecting (Class not found: " + e.getMessage() + ")", ServerModel.class);
                            this.disconnect();
                            continue;
                        } catch(IOException e) {
                            Log.w("Failed to read in object from stream, disconnecting (IOException: " + e.getMessage() + ")", ServerModel.class);
                            this.disconnect();
                            continue;
                        }
                    }
                };

                executors.submit(output);
                executors.submit(input);
            } catch(IOException e1) {
                Log.e("Failed to initialize data stream with client #" + id + " (" + e1.getMessage() + ")", ServerModel.class);
                try {
                    socket.close();
                } catch(IOException e2) {
                    Log.e("Failed to close connection with client #" + id + " (" + e2.getMessage() + ")", ServerModel.class);
                }
            }
        }

        /**
         * disconnect - Disconnects this individual connection from the client
         */
        public void disconnect() {
            ServerWorker.this.run = false;

            try {
                streamOut.close();
            } catch(IOException e) {
                Log.w("Failed to gracefully close output stream to client #" + id, ServerWorker.class);
            }

            try {
                streamIn.close();
            } catch(IOException e) {
                Log.w("Failed to gracefully close input stream to client #" + id, ServerWorker.class);
            }
        }
    }
}
