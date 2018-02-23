package team04.project2.model.client;

import team04.project2.model.Datagram;
import team04.project2.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * ClientOutputHandler - Outputs datagrams to the server (e.g., channel count, shutdown message)
 */
public class ClientOutputHandler implements Runnable {
    private boolean running;
    private boolean serverNotifyDisconnect = false;
    private ClientWorker worker;
    private ObjectOutputStream streamOut;
    private ArrayList<Datagram> datagrams = new ArrayList<>();

    /**
     * ClientOutputHandler - Sends output to server
     * @param worker Parent worker (to link between input listener/input handler/output handler)
     */
    public ClientOutputHandler(ClientWorker worker, ObjectOutputStream outputStream) {
        this.worker = worker;
        this.streamOut = outputStream;
        this.running = false;
    }

    /**
     * sendDatagram - Adds datagram to queue to send to server
     * @param data Datagram to send to server
     */
    public synchronized void sendDatagram(Datagram data) {
        datagrams.add(data);
    }

    /**
     * Execution to run on the thread, to handle output to Server
     */
    @Override
    public void run() {
        running = true;
        serverNotifyDisconnect = false;

        while(ClientModel.get().isRunning()) {
            // If we have any data to send
            if(datagrams.size() > 0) {
                Datagram data = datagrams.get(0);
                try {
                    streamOut.writeObject(data);
                    streamOut.flush();
                    datagrams.remove(0);
                } catch (IOException e) {
                    Log.w("Failed send server data from client", ClientOutputHandler.class);
                }
            }

            try {
                Thread.sleep(20L);
            } catch (InterruptedException e) {
                Log.w("Failed to sleep in client output (" + e.getMessage() + ")", ClientOutputHandler.class);
            }
        }

        // Gracefully disconnect from server (if the client isn't the one disconnecting)
        if(!serverNotifyDisconnect) {
            try {
                streamOut.writeObject(new Datagram(Datagram.TYPE.SHUTDOWN, null));
                streamOut.flush();
                streamOut.close();
            } catch (IOException e) {
                Log.w("Failed to notify graceful disconnect with server (" + e.getMessage() + ")", ClientOutputHandler.class);
            }
        }

        running = false;
    }

    /**
     * isRunning
     * @return boolean whether the output handler is running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * notifyServerDisconnecting - Used to notify the server is disconnecting, so don't error when it disconnects
     */
    public void notifyServerDisconnecting() {
        serverNotifyDisconnect = true;
    }
}
