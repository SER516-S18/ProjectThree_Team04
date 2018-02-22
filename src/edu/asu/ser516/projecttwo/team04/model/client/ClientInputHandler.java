package edu.asu.ser516.projecttwo.team04.model.client;

import edu.asu.ser516.projecttwo.team04.Datagram;
import edu.asu.ser516.projecttwo.team04.util.Log;

import java.util.ArrayList;

/**
 * ClientInputHandler - Handles input at the client's set frequency
 */
public class ClientInputHandler implements Runnable {
    private int tick;
    private boolean running;
    private ClientWorker worker;

    /**
     * ClientInputHandler - Handles input from input listener, at the client's frequency
     * @param worker Parent worker (to link between input listener/input handler/output handler)
     */
    public ClientInputHandler(ClientWorker worker) {
        this.worker = worker;
        this.running = false;
    }

    @Override
    public void run() {
        tick = 0;
        running = true;

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

        running = false;
    }

    /**
     * isRunning
     * @return boolean whether the input handler is running
     */
    public boolean isRunning() {
        return running;
    }
}