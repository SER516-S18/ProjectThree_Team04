package edu.asu.ser516.projecttwo.team04.model.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author  Jeb Johnson (jajohn64@asu.edu)
 * @version 1.0
 * @since   2018-02-18
 */

public class ClientSocket {


    private final int PORT = 1516;

    private OutputStreamWriter outputStreamWriter;
    private InputStreamReader inputStreamReader;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private OutputStream outputStream;
    private InputStream inputStream;
    private String returnMessage;
    private InetAddress host;
    private Socket socket;

    public  ClientSocket() throws Exception{

        setHost();
        initSocket();
        initOutputSteam();
        initInputStream();
    }

    private void initSocket() throws Exception{

        this.socket = new Socket(getHost(), getPORT());
    }

    private void initOutputSteam() throws Exception{

        this.outputStream = socket.getOutputStream();
        this.outputStreamWriter = new OutputStreamWriter(this.outputStream);
        this.bufferedWriter = new BufferedWriter(this.outputStreamWriter);
    }

    private void initInputStream() throws Exception {

        this.inputStream = this.socket.getInputStream();
        this.inputStreamReader = new InputStreamReader(this.inputStream);
        this.bufferedReader = new BufferedReader(this.inputStreamReader);
    }

    private void setHost() throws Exception{

        this.host = InetAddress.getLocalHost();
    }

    private InetAddress getHost() {

        return this.host;
    }

    private int getPORT() {

        return this.PORT;
    }


    private String setMessage(int data1, int data2, int data3, int data4) {

        return data1 + "," + data2 + "," + data3 + "," + data4 + "\n" ;
    }

    public String getReturnMessage() {

        return this.returnMessage;
    }

    /**
     *
     * @param  Takes the high value the low value and the number of channels to send to the server. The values
     *               get converted into a String and are then sent to the sever.
     */

    public void sendMessage(int highValue, int lowValue, int channels, int frequency) throws Exception{

        String outgoingMessage = setMessage(highValue, lowValue, channels, frequency);

        System.out.println("Sending Message: " + outgoingMessage);
        bufferedWriter.write(outgoingMessage);
        bufferedWriter.flush();
        System.out.println();

        this.returnMessage = bufferedReader.readLine();
        socket.close();
    }


}
