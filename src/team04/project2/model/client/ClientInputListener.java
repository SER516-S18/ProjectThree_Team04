package team04.project2.model.client;

import team04.project2.model.Datagram;
import team04.project2.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Necessary because server may send values at a different frequency
 */
public class ClientInputListener implements Runnable {
    private boolean isRunning;
    private ClientWorker worker;
    private ObjectInputStream streamIn;
    private ArrayList<Integer> value;

    /**
     * Accepts values from server at server's frequency
     * @param worker Parent worker (to link between input listener/input handler/output handler)
     * @param inputStream Input stream to listen from the server
     */
    public ClientInputListener(ClientWorker worker, ObjectInputStream inputStream) {
        this.worker = worker;
        this.streamIn = inputStream;
        this.isRunning = false;
    }

    /**
     * Gets the most recently received value
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

    /**
     * Execution to run on the thread, listening for input at Server's frequency
     */
    @Override
    public void run() {
        isRunning = true;

        while (ClientModel.get().isRunning()) {
            Datagram data;
            try {
                // Wait until data is available, then get it
                data = (Datagram) streamIn.readObject();
                if(data == null)
                    continue;

                if(data.type == Datagram.TYPE.PAYLOAD) {
                    // If it's payload, set it as the new value
                    try {
                        value = (ArrayList<Integer>) data.data;
                    } catch(ClassCastException e) {
                        Log.w("Got invalid payload from the server (" + e.getMessage() + ")", ClientInputListener.class);
                    }
                } else if(data.type == Datagram.TYPE.SHUTDOWN) {
                    // Server is notifying this client it intends to shutdown
                    Log.i("Server is disconnecting from client", ClientInputListener.class);
                    worker.getOutputHandler().notifyServerDisconnecting();
                    ClientModel.get().shutdown();
                }
            } catch(ClassNotFoundException e) {
                if(ClientModel.get().isRunning())
                    Log.w("Failed to read in object from stream (Class not found: " + e.getMessage() + ")", ClientInputListener.class);
                continue;
            } catch(IOException e) {
                if(ClientModel.get().isRunning()) {
                    // Only error if we should be running right now
                    Log.e("Failed to read in object from stream, shutting down (IOException: " + e.getMessage() + ")", ClientInputListener.class);
                    ClientModel.get().shutdown();
                }
                continue;
            }
        }

        // Attempt to gracefully shut down input
        try {
            streamIn.close();
        } catch (IOException e) {
            Log.w("Failed to gracefully close input socket (" + e.getMessage() + ")", ClientInputListener.class);
        }

        isRunning = false;
    }

    /**
     * Get if the input listener is running
     * @return boolean whether the input listener is running
     */
    public boolean isRunning() {
        return isRunning;
    }
}

