package team04.project2.model.server;

import team04.project2.model.Datagram;
import team04.project2.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A worker that handles output and input to an individual client
 */
public class ServerWorker {
    private static final ExecutorService executors = Executors.newCachedThreadPool();

    private ServerModel model;
    private final int id;
    private boolean run;
    private int channels = 1;
    private ObjectOutputStream streamOut;
    private ObjectInputStream streamIn;
    private boolean clientNotifyDisconnect = false;

    /**
     * The worker that handles input and output
     * @param model The parent server model
     * @param socket The socket the client is connecting from
     * @param clientID The id of the client
     */
    public ServerWorker(ServerModel model, Socket socket, int clientID) {
        this.model = model;
        this.id = clientID;
        this.run = true;

        try {
            streamOut = new ObjectOutputStream(socket.getOutputStream());
            streamIn = new ObjectInputStream(socket.getInputStream());

            // Output to the client
            Runnable output = () -> {
                clientNotifyDisconnect = false;

                while(ServerWorker.this.run && model.isRunning()) {
                    // Create a 'packet' with a value for each channel the client has
                    ArrayList<Integer> values = new ArrayList<>();
                    for(int i = 0; i < channels; i++) {
                        values.add(this.getRandom());
                    }
                    Datagram data = new Datagram(Datagram.TYPE.PAYLOAD, values);

                    // Send the 'packet' to the client
                    try {
                        streamOut.writeObject(data);
                        streamOut.flush();
                    } catch(IOException e) {
                        Log.w("Failed to send data to client #" + id + " (" + e.getMessage() + ")", ServerWorker.class);
                    }

                    // Sleep to match the server's set frequency to send data
                    try {
                        Thread.sleep(1000 / model.getFrequency());
                    } catch (InterruptedException e) {
                        Log.w("Interrupted exception thrown while waiting in client #" + id, ServerWorker.class);
                    }
                }

                // Gracefully disconnect client (if the server isn't the one shutting down)
                if(!clientNotifyDisconnect) {
                    try {
                        streamOut.writeObject(new Datagram(Datagram.TYPE.SHUTDOWN, null));
                        streamOut.flush();
                        streamOut.close();
                    } catch (IOException e) {
                        Log.w("Failed to notify graceful disconnect with client (" + e.getMessage() + ")", ServerWorker.class);
                    }
                }
            };

            // Handle input from a client
            Runnable input = () -> {
                while(ServerWorker.this.run && model.isRunning()) {
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
                                Log.i("Client #" + id + " changed channel count to " + channels, ServerWorker.class);
                            }
                        } else if(data.type == Datagram.TYPE.SHUTDOWN) {
                            // If the data is to notify the client intends to shutdown, disconnect
                            clientNotifyDisconnect = true;
                            ServerWorker.this.disconnect();
                            Log.i("Client #" + id + " disconnected successfully", ServerWorker.class);
                        }
                    } catch(ClassNotFoundException e) {
                        if(model.isRunning()) {
                            Log.w("Failed to read in object from stream, disconnecting (Class not found: " + e.getMessage() + ")", ServerWorker.class);
                            this.disconnect();
                            continue;
                        }
                    } catch(IOException e) {
                        if(model.isRunning()) {
                            Log.w("Failed to read in object from stream, disconnecting (IOException: " + e.getMessage() + ")", ServerWorker.class);
                            this.disconnect();
                            continue;
                        }
                    }
                }
            };

            executors.submit(output);
            executors.submit(input);
        } catch(IOException e1) {
            Log.e("Failed to initialize data stream with client #" + id + " (" + e1.getMessage() + ")", ServerWorker.class);
            try {
                socket.close();
            } catch(IOException e2) {
                Log.e("Failed to close connection with client #" + id + " (" + e2.getMessage() + ")", ServerWorker.class);
            }
        }
    }

    /**
     * Disconnects this individual connection from the client
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

    /**
     * Gets a random value between server's min and max
     * @return A random value between the server's min and max, inclusive
     */
    private int getRandom() {
        return ThreadLocalRandom.current().nextInt(this.model.getValueMin(), this.model.getValueMax() + 1);
    }
}
