package team04.project3.model.client;

import team04.project3.model.Datagram;
import team04.project3.util.Log;

import java.util.ArrayList;

/**
 * Handles input at the client's set frequency
 */
public class ClientInputHandler implements Runnable {
    private int tick;
    private boolean isRunning;
    private ClientWorker worker;

    /**
     * Handles input from input listener, at the client's frequency
     * @param worker Parent worker (to link between input listener/input handler/output handler)
     */
    public ClientInputHandler(ClientWorker worker) {
        this.worker = worker;
        this.isRunning = false;
    }

    /**
     * Execution to run on the thread, handling input at Client's frequency
     */
    @Override
    public void run() {
        tick = 0;
        isRunning = true;

        while(ClientModel.get().isRunning()) {
            // Get the most recent input at our own client's frequency
            ArrayList<Integer> values = worker.getInputListener().getValue(false);

            if(values != null) {
                if(values.size() != ClientModel.get().getChannelCount()) {
                    // If the values sent aren't the correct number of channels, notify server of correct count
                    worker.getOutputHandler().sendDatagram(new Datagram(Datagram.TYPE.SETTING, ClientModel.get().getChannelCount()));
                } else {
                    ClientModel.get().handleNewInputValues(values, tick);
                }
            }

            // Sleep to meet Client's frequency
            try {
                Thread.sleep(1000 / ClientModel.get().getFrequency());
            } catch (InterruptedException e) {
                Log.w("Interrupted exception thrown while waiting for client frequency", ClientInputHandler.class);
            }

            tick++;
        }

        isRunning = false;
    }

    /**
     * Get if the input handler is running
     * @return boolean whether the input handler is running
     */
    public boolean isRunning() {
        return isRunning;
    }
}